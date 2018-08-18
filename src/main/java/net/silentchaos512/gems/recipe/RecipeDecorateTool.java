package net.silentchaos512.gems.recipe;

import com.google.common.collect.Lists;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IAmmoTool;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.tool.part.*;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.util.ItemHelper;

import java.util.List;

public class RecipeDecorateTool extends RecipeBaseSL {
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        return !getCraftingResult(inventoryCrafting).isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        int i;
        int toolRow = 0;
        int toolCol = 0;
        ItemStack stack;
        ItemStack tool = ItemStack.EMPTY;
        int repairValue = 0;
        int ammoValue = 0;

        // Find tool position
        for (int row = 0; row < inv.getWidth(); ++row) {
            for (int col = 0; col < inv.getHeight(); ++col) {
                stack = inv.getStackInRowAndColumn(row, col);
                if (!stack.isEmpty() && stack.getItem() instanceof ITool) {
                    tool = stack;
                    toolRow = row;
                    toolCol = col;
                }
            }
        }

        // Found a tool?
        if (tool.isEmpty()) {
            return ItemStack.EMPTY;
        }

        // Check adjacent materials
        ItemStack west = inv.getStackInRowAndColumn(toolRow - 1, toolCol);
        ItemStack north = inv.getStackInRowAndColumn(toolRow, toolCol - 1);
        ItemStack east = inv.getStackInRowAndColumn(toolRow + 1, toolCol);
        ItemStack south = inv.getStackInRowAndColumn(toolRow, toolCol + 1);

        if (!checkIsDecorationMaterial(west) || !checkIsDecorationMaterial(north)
                || !checkIsDecorationMaterial(east) || !checkIsDecorationMaterial(south)) {
            return ItemStack.EMPTY;
        }

        // Check other materials and get all repair values.
        List<ItemStack> otherMats = Lists.newArrayList();
        EnumMaterialTier toolTier = ToolHelper.getToolTier(tool);
        for (i = 0; i < inv.getSizeInventory(); ++i) {
            stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && !(stack.getItem() instanceof ITool)) {
                ToolPart part = ToolPartRegistry.fromDecoStack(stack);
                // Invalid part or not a part?
                if (part == null) {
                    return ItemStack.EMPTY;
                }
                // Valid for tool tier?
                if (!part.validForToolOfTier(toolTier) && !(part instanceof ToolPartMain)) {
                    return ItemStack.EMPTY;
                }
                int repairAmount = part.getRepairAmount(tool, stack);
                if (repairAmount > 0) {
                    repairValue += repairAmount;
                    ++ammoValue;
                }
                otherMats.add(stack);
            }
        }

        if (otherMats.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = tool.copy();

        result = ToolHelper.decorateTool(tool, west, north, east, south);

        boolean lockedStats = result.getTagCompound().getBoolean(ToolHelper.NBT_LOCK_STATS);

        // Other materials
        for (ItemStack other : otherMats) {
            ToolPart part = ToolPartRegistry.fromDecoStack(other);
            EnumMaterialGrade grade = EnumMaterialGrade.fromStack(other);
            if (!result.isEmpty() && part instanceof ToolPartRod) {
                ToolHelper.setRenderPart(result, part, grade, ToolPartPosition.ROD);
            } else if (part instanceof ToolPartTip) {
                if (lockedStats) {
                    // Tips change stats, so using them with locked tools is not allowed.
                    return ItemStack.EMPTY;
                }
                ToolHelper.setConstructionTip(result, part);
            }
        }

        if (repairValue > 0) {
            // Makes odd repair values line up better (2 polished stone on pickaxe makes a full repair, etc.)
            repairValue += 1;
        }

        // Tool repair multiplier
        repairValue *= ((ITool) tool.getItem()).getRepairMultiplier();

        // Repair.
        ItemHelper.attemptDamageItem(result, -repairValue, SilentGems.random);
        // Restore ammo.
        if (result.getItem() instanceof IAmmoTool && ammoValue > 0) {
            IAmmoTool ammoTool = (IAmmoTool) result.getItem();
            ammoTool.addAmmo(result, ammoValue * GemsConfig.TOMAHAWK_AMMO_PER_MAT);
        }

        // Recalculate stats.
        ToolHelper.recalculateStats(result);
        ToolHelper.incrementStatRedecorated(result, 1);

        // Change the UUID so that rendering cache updates immediately for recipe output.
        result.getTagCompound().removeTag(ToolHelper.NBT_UUID + "Most");
        result.getTagCompound().removeTag(ToolHelper.NBT_UUID + "Least");
        ToolHelper.getUUID(result);

        return result;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    private boolean checkIsDecorationMaterial(ItemStack stack) {
        if (stack.isEmpty()) {
            return true;
        }

        ToolPart part = ToolPartRegistry.fromDecoStack(stack);
        return part != null;
    }
}
