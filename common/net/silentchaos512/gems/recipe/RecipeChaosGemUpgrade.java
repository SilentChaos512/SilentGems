package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.item.ItemChaosGem;
import net.silentchaos512.gems.item.ItemChaosRune;
import net.silentchaos512.lib.recipe.RecipeBase;

public class RecipeChaosGemUpgrade extends RecipeBase {

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack chaosGem = ItemStack.EMPTY;
    ItemStack stack;

    // Find the Chaos Gem.
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (!stack.isEmpty() && stack.getItem() instanceof ItemChaosGem) {
        // Must have only one Chaos Gem!
        if (!chaosGem.isEmpty())
          return ItemStack.EMPTY;
        chaosGem = stack.copy();
      }
    }

    // Found Chaos Gem?
    if (chaosGem.isEmpty()) {
      return ItemStack.EMPTY;
    }

    ItemChaosGem itemGem = (ItemChaosGem) chaosGem.getItem();

    // Find runes and apply them.
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (!stack.isEmpty() && stack.getItem() instanceof ItemChaosRune) {
        ItemChaosRune itemRune = (ItemChaosRune) stack.getItem();
        if (!itemGem.addBuff(chaosGem, itemRune.getBuff(stack)))
          return ItemStack.EMPTY;
      }
    }

    return chaosGem;
  }

  @Override
  public int getRecipeSize() {

    return 10;
  }
}
