package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.item.ChaosGem;
import net.silentchaos512.gems.item.ChaosRune;
import net.silentchaos512.gems.lib.buff.ChaosBuff;

public class ChaosRuneRecipe implements IRecipe {

  @Override
  public boolean matches(InventoryCrafting inventorycrafting, World world) {

    int numGems = 0;
    int numRunes = 0;

    ItemStack stack, gem = null, rune = null;

    // Count valid ingredients and look for invalid
    for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
      stack = inventorycrafting.getStackInSlot(i);
      if (stack != null) {
        if (stack.getItem() instanceof ChaosGem) {
          ++numGems;
          gem = stack;
        } else if (stack.getItem() instanceof ChaosRune) {
          ++numRunes;
          rune = stack;
        } else {
          // Invalid item
          return false;
        }
      }
    }

    if (gem == null || rune == null) {
      return false;
    }

    ChaosGem chaosGem = (ChaosGem) gem.getItem();
    int id = rune.getItemDamage();
    ChaosBuff buff = null;
    if (id >= 0 && id <= ChaosBuff.values().length) {
      buff = ChaosBuff.values()[id];
    }
    return numGems == 1 && numRunes == 1 && buff != null && chaosGem.canAddBuff(gem, buff);
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {

    ItemStack gem = null, rune = null, stack = null;

    // Find ingredients
    for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
      stack = inventorycrafting.getStackInSlot(i);
      if (stack != null) {
        if (stack.getItem() instanceof ChaosGem) {
          gem = stack;
        } else if (stack.getItem() instanceof ChaosRune) {
          rune = stack;
        }
      }
    }

    if (gem == null || rune == null) {
      return null;
    }

    ItemStack result = gem.copy();

    ChaosBuff buff = ChaosBuff.values()[rune.getItemDamage()];
    ChaosGem chaosGem = (ChaosGem) gem.getItem();
    if (chaosGem.canAddBuff(result, buff)) {
      chaosGem.addBuff(result, buff);
    }

    return result;
  }

  @Override
  public int getRecipeSize() {

    // TODO What's this?
    return 2;
  }

  @Override
  public ItemStack getRecipeOutput() {

    // TODO What's this?
    return null;
  }

}
