package silent.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import silent.gems.item.Gem;
import silent.gems.item.TorchBandolier;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Strings;

public class TorchBandolierRecipe implements IRecipe {

  @Override
  public boolean matches(InventoryCrafting inv, World world) {

    int numBandolier = 0;
    int numGem = 0;

    ItemStack stack;

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        if (inv.getStackInSlot(i).getItem() instanceof TorchBandolier) {
          ++numBandolier;
        } else if (inv.getStackInSlot(i).getItem() instanceof Gem) {
          ++numGem;
        }
      }
    }

    return numBandolier == 1 && numGem == 1;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack bandolier = null, gem = null, result, stack;

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        if (stack.getItem() instanceof TorchBandolier) {
          bandolier = stack;
        } else if (stack.getItem() instanceof Gem && stack.getItemDamage() < EnumGem.count()) {
          gem = stack;
        }
      }
    }

    if (bandolier == null || gem == null) {
      return null;
    }

    result = bandolier.copy();
    if (result.getTagCompound() == null) {
      result.setTagCompound(new NBTTagCompound());
    }
    int k = gem.getItemDamage();
    result.getTagCompound().setByte(Strings.TORCH_BANDOLIER_GEM, (byte) k);

    return result;
  }

  @Override
  public int getRecipeSize() {

    return 9;
  }

  @Override
  public ItemStack getRecipeOutput() {

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
