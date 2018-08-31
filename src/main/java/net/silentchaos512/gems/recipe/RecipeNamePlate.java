package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.lib.recipe.RecipeBaseSL;

public class RecipeNamePlate extends RecipeBaseSL {
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        int countNamePlate = 0;
        int countOther = 0;

        for (ItemStack stack : getNonEmptyStacks(inv)) {
            // Name Plate
            if (stack.isItemEqual(CraftingItems.NAME_PLATE.getStack()) && stack.hasDisplayName())
                ++countNamePlate;
                // Other
            else
                ++countOther;
        }

        return countNamePlate == 1 && countOther == 1;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack stackNamePlate = ItemStack.EMPTY;
        ItemStack stackOther = ItemStack.EMPTY;

        for (ItemStack stack : getNonEmptyStacks(inv)) {
            // Name plate?
            if (stack.isItemEqual(CraftingItems.NAME_PLATE.getStack())) {
                stackNamePlate = stack;
            }
            // Other item?
            else {
                stackOther = stack;
            }
        }

        if (stackNamePlate.isEmpty() || stackOther.isEmpty() || !stackNamePlate.hasDisplayName()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = stackOther.copy();
        result.setStackDisplayName(stackNamePlate.getDisplayName());
        return result;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
}
