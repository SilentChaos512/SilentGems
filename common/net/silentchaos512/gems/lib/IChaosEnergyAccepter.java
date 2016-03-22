package net.silentchaos512.gems.lib;

public interface IChaosEnergyAccepter extends IChaosEnergyStorage {
    /**
     *
     * @param amount the amount of energy we are trying to push into the tile
     * @return the actual amount of energy that was pushed into the tile
     */
    int receiveEnergy(int amount);

    /**
     * @return true if the tile can receive any energy, false otherwise
     */
    boolean canReceiveEnergy();
}
