package net.silentchaos512.gems.energy;

import net.minecraft.item.ItemStack;

public interface IChaosStorage {

  int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate);

  int extractEnergy(ItemStack stack, int maxExtract, boolean simulate);

  int getEnergyStored(ItemStack stack);

  int getMaxEnergyStored(ItemStack stack);

}
