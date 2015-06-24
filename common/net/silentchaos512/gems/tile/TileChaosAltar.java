package net.silentchaos512.gems.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.silentchaos512.gems.energy.IChaosHandler;

public class TileChaosAltar extends TileEntity implements IChaosHandler {

  protected int energyStored;
  
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    
    super.readFromNBT(tags);
    energyStored = tags.getInteger("Energy");
  }
  
  @Override
  public void writeToNBT(NBTTagCompound tags) {
    
    super.writeToNBT(tags);
    tags.setInteger("Energy", energyStored);
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getEnergyStored() {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxEnergyStored() {

    // TODO Auto-generated method stub
    return 0;
  }
}
