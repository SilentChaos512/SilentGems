package net.silentchaos512.gems.api.energy;

import net.minecraft.item.ItemStack;

public interface IChaosStorage {

  int receiveCharge(ItemStack stack, int maxReceive, boolean simulate);

  int extractCharge(ItemStack stack, int maxExtract, boolean simulate);

  int getCharge(ItemStack stack);

  int getMaxCharge(ItemStack stack);
}
