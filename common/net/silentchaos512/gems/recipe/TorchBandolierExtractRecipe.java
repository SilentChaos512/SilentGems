package net.silentchaos512.gems.recipe;

import java.lang.reflect.Field;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.util.PlayerHelper;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.TorchBandolier;

import com.google.common.base.Throwables;

import cpw.mods.fml.relauncher.ReflectionHelper;

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

    return new ItemStack(Blocks.torch, torchCount);
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
}
