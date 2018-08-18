package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.util.ItemHelper;

public class RecipeDecorateArmor extends RecipeBaseSL {
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

        // Find armor position
        for (int row = 0; row < inv.getWidth(); ++row) {
            for (int col = 0; col < inv.getHeight(); ++col) {
                stack = inv.getStackInRowAndColumn(row, col);
                if (!stack.isEmpty() && stack.getItem() instanceof IArmor) {
                    tool = stack;
                    toolRow = row;
                    toolCol = col;
                }
            }
        }

        // Found armor piece?
        if (tool.isEmpty()) {
            return ItemStack.EMPTY;
        }

        // Check adjacent materials
        ItemStack west = inv.getStackInRowAndColumn(toolRow - 1, toolCol);
        ItemStack north = inv.getStackInRowAndColumn(toolRow, toolCol - 1);
        ItemStack east = inv.getStackInRowAndColumn(toolRow + 1, toolCol);
        ItemStack south = inv.getStackInRowAndColumn(toolRow, toolCol + 1);
        int validPartCount = (west.isEmpty() ? 0 : 1) + (north.isEmpty() ? 0 : 1) + (east.isEmpty() ? 0 : 1) + (south.isEmpty() ? 0 : 1);

        if (!checkIsDecorationMaterial(west) || !checkIsDecorationMaterial(north)
                || !checkIsDecorationMaterial(east) || !checkIsDecorationMaterial(south)) {
            return ItemStack.EMPTY;
        }

        // Check for other pieces (invalid for armor) and get all repair values.
        EnumMaterialTier toolTier = ArmorHelper.getArmorTier(tool);
        ToolPart part;
        int partsFound = 0;
        for (i = 0; i < inv.getSizeInventory(); ++i) {
            stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && !(stack.getItem() instanceof IArmor)) {
                part = ToolPartRegistry.fromDecoStack(stack);
                // Invalid part or not a part?
                if (part == null || !(part instanceof ToolPartMain)) {
                    return ItemStack.EMPTY;
                }
                // Found a part.
                ++partsFound;
                // Add to total repair value.
                repairValue += part.getRepairAmount(tool, stack);
            }
        }

        // Make sure we got no other parts.
        if (partsFound != validPartCount) {
            return ItemStack.EMPTY;
        }

        ItemStack result = ArmorHelper.decorateArmor(tool, west, north, east, south);

        // Repair.
        ItemHelper.attemptDamageItem(result, -repairValue, SilentGems.random);

        // Recalculate stats.
        ArmorHelper.recalculateStats(result);
        ArmorHelper.incrementStatRedecorated(result, 1);

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
        return part != null && part instanceof ToolPartMain;
    }
}
