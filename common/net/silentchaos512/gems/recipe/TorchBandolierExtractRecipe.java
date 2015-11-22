package net.silentchaos512.gems.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.TorchBandolier;

public class TorchBandolierExtractRecipe implements IRecipe {

  @Override
  public boolean matches(InventoryCrafting inv, World world) {

    int numBandolier = 0;
    ItemStack bandolier = null;

    ItemStack stack;
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        if (stack.getItem() instanceof TorchBandolier) {
          ++numBandolier;
          bandolier = stack;
        } else {
          return false;
        }
      }
    }

    return numBandolier == 1 && bandolier != null
        && bandolier.getItemDamage() < TorchBandolier.MAX_DAMAGE;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack bandolier = null;
    ItemStack stack;

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        if (stack.getItem() instanceof TorchBandolier) {
          bandolier = stack;
        } else {
          return null;
        }
      }
    }

    // Safety check.
    if (bandolier == null) {
      return null;
    }

    // How many torches to extract?
    int torchCount = ModItems.torchBandolier.getTorchCount(bandolier);
    torchCount = torchCount > 64 ? 64 : torchCount;
    ItemStack result = bandolier.copy();
    result.attemptDamageItem(torchCount, SilentGems.instance.random);

    return result;
  }

  @Override
  public int getRecipeSize() {

    // TODO Auto-generated method stub
    return 1;
  }

  @Override
  public ItemStack getRecipeOutput() {

    return new ItemStack(Blocks.torch, 64);
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
