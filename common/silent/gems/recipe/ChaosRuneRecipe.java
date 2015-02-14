package silent.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import silent.gems.item.ChaosGem;
import silent.gems.item.ChaosRune;
import silent.gems.lib.buff.ChaosBuff;

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

    return numGems == 1 && numRunes == 1
        && ChaosGem.canAddBuff(gem, ChaosBuff.all.get(rune.getItemDamage()));
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

    ChaosBuff buff = ChaosBuff.all.get(rune.getItemDamage());
    if (ChaosGem.canAddBuff(result, buff)) {
      ChaosGem.addBuff(result, buff);
    }

    return result;
  }

  @Override
  public int getRecipeSize() {

    // What's this?
    return 2;
  }

  @Override
  public ItemStack getRecipeOutput() {

    // What's this?
    return null;
  }

  @Override
  public ItemStack[] getRemainingItems(InventoryCrafting inv) {

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      ItemStack stack = inv.getStackInSlot(i);
      if (stack != null) {
        --stack.stackSize;
        if (stack.stackSize <= 0) {
          stack = null;
        }
        inv.setInventorySlotContents(i, stack);
      }
    }
    return new ItemStack[] {};
  }
}
