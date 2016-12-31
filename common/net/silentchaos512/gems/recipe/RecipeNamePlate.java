package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.recipe.RecipeBase;

public class RecipeNamePlate extends RecipeBase {

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack stackNamePlate = ItemStack.EMPTY;
    ItemStack stackOther = ItemStack.EMPTY;
    ItemStack stack;
    ItemStack namePlateForMatching = ModItems.craftingMaterial.namePlate;

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (!stack.isEmpty()) {
        // Name plate?
        if (stack.isItemEqual(namePlateForMatching)) {
          if (!stackNamePlate.isEmpty()) {
            return ItemStack.EMPTY;
          }
          stackNamePlate = stack;
        }
        // Other item?
        else {
          if (!stackOther.isEmpty()) {
            return ItemStack.EMPTY;
          }
          stackOther = stack;
        }
      }
    }

    if (stackNamePlate.isEmpty() || stackOther.isEmpty() || !stackNamePlate.hasDisplayName()) {
      return ItemStack.EMPTY;
    }

    ItemStack result = stackOther.copy();
    result.setStackDisplayName(stackNamePlate.getDisplayName());
    return result;
  }
}
