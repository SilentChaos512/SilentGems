package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.item.ItemChaosGem;
import net.silentchaos512.gems.item.ItemChaosRune;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.util.StackHelper;

public class RecipeChaosGemUpgrade extends RecipeBaseSL {

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack chaosGem = StackHelper.empty();
    ItemStack stack;

    // Find the Chaos Gem.
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (StackHelper.isValid(stack) && stack.getItem() instanceof ItemChaosGem) {
        // Must have only one Chaos Gem!
        if (StackHelper.isValid(chaosGem))
          return StackHelper.empty();
        chaosGem = StackHelper.safeCopy(stack);
      }
    }

    // Found Chaos Gem?
    if (StackHelper.isEmpty(chaosGem)) {
      return StackHelper.empty();
    }

    ItemChaosGem itemGem = (ItemChaosGem) chaosGem.getItem();

    // Find runes and apply them.
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (StackHelper.isValid(stack) && stack.getItem() instanceof ItemChaosRune) {
        ItemChaosRune itemRune = (ItemChaosRune) stack.getItem();
        if (!itemGem.addBuff(chaosGem, itemRune.getBuff(stack)))
          return StackHelper.empty();
      }
    }

    return chaosGem;
  }
}
