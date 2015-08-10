package net.silentchaos512.gems.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.particle.EntityParticleFXTest;
import net.silentchaos512.gems.core.util.LogHelper;

public class TileChaosPylon extends TileEntity {

  public static final int SEARCH_RADIUS = 4; // TODO: Should this be a config?

  protected int energyStored;
  protected int lastAltarX;
  protected int lastAltarZ;

  @Override
  public void readFromNBT(NBTTagCompound tags) {

    super.readFromNBT(tags);
    energyStored = tags.getInteger("Energy");
    lastAltarX = tags.getInteger("AltarX");
    lastAltarZ = tags.getInteger("AltarZ");
  }

  @Override
  public void writeToNBT(NBTTagCompound tags) {

    super.writeToNBT(tags);
    tags.setInteger("Energy", energyStored);
    tags.setInteger("AltarX", lastAltarX);
    tags.setInteger("AltarZ", lastAltarZ);
  }

  public int getEnergyStored() {

    return this.energyStored;
  }

  public int getMaxEnergyStored() {

    return 10000; // TODO: Config!
  }

  public TileEntity getAltar() {

    // Get last known altar, if it exists.
    TileEntity tile = this.worldObj.getTileEntity(this.lastAltarX, this.yCoord, this.lastAltarZ);
    if (tile != null && tile instanceof TileChaosAltar) {
      return tile;
    }

    // Last known altar coords are no good, try to find a new altar.
    LogHelper.debug("Pylon at (" + xCoord + ", " + yCoord + ", " + zCoord
        + ") searching for new altar...");
    for (int x = this.xCoord - SEARCH_RADIUS; x < this.xCoord + SEARCH_RADIUS + 1; ++x) {
      for (int z = this.zCoord - SEARCH_RADIUS; z < this.zCoord + SEARCH_RADIUS + 1; ++z) {
        tile = this.worldObj.getTileEntity(x, this.yCoord, z);
        if (tile != null && tile instanceof TileChaosAltar) {
          LogHelper.debug("Pylon found new altar at (" + x + ", " + yCoord + ", " + z + ")!");
          this.lastAltarX = x;
          this.lastAltarZ = z;
          return tile;
        }
      }
    }

    // None found
    return null;
  }

  @Override
  public void updateEntity() {

    if (this.worldObj.getTotalWorldTime() % 20 == 0) {
      this.spawnParticlesToAltar();
    }

    if (!this.worldObj.isRemote) {
      this.energyStored += 5; // TODO: Config!
      if (this.energyStored > this.getMaxEnergyStored()) {
        this.energyStored = this.getMaxEnergyStored();
      }

      this.sendEnergyToAltar();
      // if (this.worldObj.getTotalWorldTime() % 20 == 0) {
      // this.sendEnergyToAltar();
      // }
    }
  }

  private void sendEnergyToAltar() {

    TileEntity tile = this.getAltar();
    if (tile != null) {
      TileChaosAltar altar = (TileChaosAltar) tile;
      this.energyStored -= altar.receiveEnergy(this.energyStored);
    }
  }

  private void spawnParticlesToAltar() {

    TileEntity altar = this.getAltar();
    if (altar == null) {
      return;
    }

    double diffX = altar.xCoord - this.xCoord;
    double diffZ = altar.zCoord - this.zCoord;
    double stepX = diffX / 16.0;
    double stepZ = diffZ / 16.0;
    double x = this.xCoord + 0.5;
    double z = this.zCoord + 0.5;

    for (int i = 0; i < 16; ++i) {
      double motionX = SilentGems.instance.random.nextGaussian() * 0.0004;
      double motionY = SilentGems.instance.random.nextGaussian() * 0.0004;
      double motionZ = SilentGems.instance.random.nextGaussian() * 0.0004;
      EntityFX particleFX = new EntityParticleFXTest(this.worldObj, x, this.yCoord + 0.75, z, motionX, motionY, motionZ);
      SilentGems.proxy.spawnParticles(particleFX);
//      this.worldObj.spawnParticle("cloud", x, this.yCoord + 0.5, z, motionX, motionY, motionZ);
      x += stepX;
      z += stepZ;
    }
  }
}
