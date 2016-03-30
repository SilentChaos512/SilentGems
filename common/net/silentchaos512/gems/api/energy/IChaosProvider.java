package net.silentchaos512.gems.api.energy;

public interface IChaosProvider extends IChaosHandler {

  int extractEnergy(int maxExtract, boolean simulate);
}
