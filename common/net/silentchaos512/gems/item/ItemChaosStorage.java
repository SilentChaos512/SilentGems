package net.silentchaos512.gems.item;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosStorage;
import net.silentchaos512.gems.util.NBTHelper;
import net.silentchaos512.lib.item.ItemSL;


public class ItemChaosStorage extends ItemSL implements IChaosStorage {

  public static final String NBT_CHARGE = "ChaosCharge";

  protected int maxCharge;

  public ItemChaosStorage(int subItemCount, String name, int maxCharge) {

    super(subItemCount, SilentGems.MODID, name);
    setMaxStackSize(1);
    setNoRepair();
    this.maxCharge = maxCharge;
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {

    int energy = getCharge(stack);
    int capacity = getMaxCharge(stack);
    if (capacity == 0) {
      return 1.0;
    }
    return (double) (capacity - energy) / (double) capacity;
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {

    return getCharge(stack) < getMaxCharge(stack);
  }

  protected void setCharge(ItemStack stack, int value) {

    NBTHelper.setTagInt(stack, NBT_CHARGE, value);
  }

  @Override
  public int getCharge(ItemStack stack) {

    return NBTHelper.getTagInt(stack, NBT_CHARGE);
  }

  @Override
  public int getMaxCharge(ItemStack stack) {

    return maxCharge;
  }

  @Override
  public int receiveCharge(ItemStack stack, int maxReceive, boolean simulate) {

    int charge = getCharge(stack);
    int capacity = getMaxCharge(stack);
    int received = Math.min(capacity - charge, maxReceive);

    if (!simulate) {
      NBTHelper.setTagInt(stack, NBT_CHARGE, charge + received);
    }

    return received;
  }

  @Override
  public int extractCharge(ItemStack stack, int maxExtract, boolean simulate) {

    int charge = getCharge(stack);
    int capacity = getMaxCharge(stack);
    int extracted = Math.min(charge, maxExtract);

    if (!simulate) {
      NBTHelper.setTagInt(stack, NBT_CHARGE, charge - extracted);
    }

    return extracted;
  }
}
