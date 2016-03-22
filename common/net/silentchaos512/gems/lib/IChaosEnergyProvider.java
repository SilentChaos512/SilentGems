package net.silentchaos512.gems.lib;

import net.minecraft.tileentity.TileEntity;

public interface IChaosEnergyProvider extends IChaosEnergyStorage {

    /**
     *
     * @return should return a tile entity that extends IChaosEnergyAcceptor - the proper checks are left to the method
     */
    TileEntity getEnergyAccepter();

    /**
     * use this to send energy from the provider to an acceptor
     * @return true if energy was sent, false otherwise
     */
    boolean sendEnergyToAccepter();
}
