package net.silentchaos512.gems.lib;

public interface IChaosEnergyStorage {

    /**
     * @return the current amount of stored energy
     */
    int getEnergyStored();

    /**
     * @return the maximum amount of energy that can be stored
     */
    int getMaxEnergyStored();
}
