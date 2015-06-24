package net.silentchaos512.gems.energy;

import net.minecraft.nbt.NBTTagCompound;

public class ChaosStorage implements IChaosStorage {

  protected int energy;
  protected int capacity;
  protected int maxReceive;
  protected int maxExtract;

  public ChaosStorage(int capacity) {

    this(capacity, capacity, capacity);
  }

  public ChaosStorage(int capacity, int maxTransfer) {

    this(capacity, maxTransfer, maxTransfer);
  }

  public ChaosStorage(int capacity, int maxReceive, int maxExtract) {

    this.capacity = capacity;
    this.maxReceive = maxReceive;
    this.maxExtract = maxExtract;
  }

  public ChaosStorage readFromNBT(NBTTagCompound nbt) {

    this.energy = nbt.getInteger("ChaosEnergy");

    if (energy > capacity) {
      energy = capacity;
    }
    return this;
  }

  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

    if (energy < 0) {
      energy = 0;
    }
    nbt.setInteger("ChaosEnergy", energy);
    return nbt;
  }

  public void setMaxTransfer(int maxTransfer) {

    setMaxReceive(maxTransfer);
    setMaxExtract(maxTransfer);
  }

  public void setMaxReceive(int maxReceive) {

    this.maxReceive = maxReceive;
  }

  public void setMaxExtract(int maxExtract) {

    this.maxExtract = maxExtract;
  }

  public int getMaxReceive() {

    return maxReceive;
  }

  public int getMaxExtract() {

    return maxExtract;
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
