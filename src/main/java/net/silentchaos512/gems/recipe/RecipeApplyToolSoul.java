package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ItemToolSoul;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.lib.recipe.RecipeBaseSL;


public class RecipeApplyToolSoul extends RecipeBaseSL {
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        return !getCraftingResult(inventoryCrafting).isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack tool = ItemStack.EMPTY;
        ItemStack soul = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            // Found a tool or armor piece?
            if (stack.getItem() instanceof ITool || stack.getItem() instanceof IArmor) {
                if (!tool.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                tool = stack;
            }
            // Found a soul?
            else if (stack.getItem() instanceof ItemToolSoul) {
                if (!soul.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                soul = stack;
            }
        }

        if (tool.isEmpty() || soul.isEmpty()) {
            return ItemStack.EMPTY;
        }

        // Does tool already have a soul?
        if (SoulManager.getSoul(tool) != null) {
            return ItemStack.EMPTY;
        }

        // Is the soul valid?
        ToolSoul toolSoul = ModItems.toolSoul.getSoul(soul);
        if (toolSoul == null) {
            return ItemStack.EMPTY;
        }

        ItemStack result = tool.copy();
        // Have to change UUID to prevent soul duping.
//    result.getTagCompound().removeTag(ToolHelper.NBT_UUID);
        SoulManager.setSoul(result, toolSoul, true);

        // Apply name, if applicable.
        if (soul.hasDisplayName()) {
            String name = soul.getDisplayName();
            toolSoul.setName(name);
            result.setStackDisplayName(name);
        }

        // Recalculate stats and return.
        ToolHelper.recalculateStats(result);
        return result;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
}