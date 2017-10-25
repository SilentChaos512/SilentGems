package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.util.StackHelper;

public class RecipeNamePlate extends RecipeBaseSL {

  @Override
  public boolean matches(InventoryCrafting inv, World world) {

    int countNamePlate = 0;
    int countOther = 0;

    for (ItemStack stack : getNonEmptyStacks(inv)) {
      // Name Plate
      if (stack.isItemEqual(ModItems.craftingMaterial.namePlate) && stack.hasDisplayName())
        ++countNamePlate;
      // Other
      else
        ++countOther;
    }

    return countNamePlate == 1 && countOther == 1;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack stackNamePlate = StackHelper.empty();
    ItemStack stackOther = StackHelper.empty();

    for (ItemStack stack : getNonEmptyStacks(inv)) {
      // Name plate?
      if (stack.isItemEqual(ModItems.craftingMaterial.namePlate)) {
        stackNamePlate = stack;
      }
      // Other item?
      else {
        stackOther = stack;
      }
    }

    if (StackHelper.isEmpty(stackNamePlate) || StackHelper.isEmpty(stackOther)
        || !stackNamePlate.hasDisplayName()) {
      return StackHelper.empty();
    }

    ItemStack result = StackHelper.safeCopy(stackOther);
    result.setStackDisplayName(stackNamePlate.getDisplayName());
    return result;
  }
}
