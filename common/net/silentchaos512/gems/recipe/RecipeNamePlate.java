package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.util.StackHelper;

public class RecipeNamePlate extends RecipeBaseSL {

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack stackNamePlate = StackHelper.empty();
    ItemStack stackOther = StackHelper.empty();
    ItemStack stack;
    ItemStack namePlateForMatching = ModItems.craftingMaterial.namePlate;

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (StackHelper.isValid(stack)) {
        // Name plate?
        if (stack.isItemEqual(namePlateForMatching)) {
          if (StackHelper.isValid(stackNamePlate)) {
            return StackHelper.empty();
          }
          stackNamePlate = stack;
        }
        // Other item?
        else {
          if (StackHelper.isValid(stackOther)) {
            return StackHelper.empty();
          }
          stackOther = stack;
        }
      }
    }

    if (StackHelper.isEmpty(stackNamePlate) || StackHelper.isEmpty(stackOther) || !stackNamePlate.hasDisplayName()) {
      return StackHelper.empty();
    }

    ItemStack result = StackHelper.safeCopy(stackOther);
    result.setStackDisplayName(stackNamePlate.getDisplayName());
    return result;
  }
}
