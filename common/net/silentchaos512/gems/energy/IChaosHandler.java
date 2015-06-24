package net.silentchaos512.gems.energy;


public interface IChaosHandler {

  int receiveEnergy(int maxReceive, boolean simulate);
  
  int extractEnergy(int maxExtract, boolean simulate);
  
  int getEnergyStored();
  
  int getMaxEnergyStored();
}
