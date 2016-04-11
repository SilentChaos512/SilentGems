package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.recipe.RecipeBase;

public class RecipeNamePlate extends RecipeBase {

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack stackNamePlate = null;
    ItemStack stackOther = null;
    ItemStack stack;
    ItemStack namePlateForMatching = ModItems.craftingMaterial.namePlate;

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        // Name plate?
        if (stack.isItemEqual(namePlateForMatching)) {
          if (stackNamePlate != null) {
            return null;
          }
          stackNamePlate = stack;
        }
        // Other item?
        else {
          if (stackOther != null) {
            return null;
          }
          stackOther = stack;
        }
      }
    }

    if (stackNamePlate == null || stackOther == null || !stackNamePlate.hasDisplayName()) {
      return null;
    }

    ItemStack result = stackOther.copy();
    result.setStackDisplayName(stackNamePlate.getDisplayName());
    return result;
  }
}
