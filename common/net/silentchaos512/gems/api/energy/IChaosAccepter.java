package net.silentchaos512.gems.api.energy;

public interface IChaosAccepter extends IChaosHandler {

  int receiveCharge(int maxReceive, boolean simulate);
}
