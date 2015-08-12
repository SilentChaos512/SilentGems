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
  public static final int SEARCH_HEIGHT = 1;
  public static final int TRANSFER_DELAY = 100;
  public static final int PARTICLE_COUNT = 16;

  protected int energyStored;
  protected int lastAltarX;
  protected int lastAltarY;
  protected int lastAltarZ;

  @Override
  public void readFromNBT(NBTTagCompound tags) {

    super.readFromNBT(tags);
    energyStored = tags.getInteger("Energy");
    lastAltarX = tags.getInteger("AltarX");
    lastAltarY = tags.getInteger("AltarY");
    lastAltarZ = tags.getInteger("AltarZ");
  }

  @Override
  public void writeToNBT(NBTTagCompound tags) {

    super.writeToNBT(tags);
    tags.setInteger("Energy", energyStored);
    tags.setInteger("AltarX", lastAltarX);
    tags.setInteger("AltarY", lastAltarY);
    tags.setInteger("AltarZ", lastAltarZ);
  }

  public int getEnergyStored() {

    return this.energyStored;
  }

  public int getMaxEnergyStored() {

    return 10000; // TODO: Config?
  }

  public TileEntity getAltar() {

    // Get last known altar, if it exists.
    TileEntity tile = this.worldObj.getTileEntity(this.lastAltarX, this.lastAltarY,
        this.lastAltarZ);
    if (tile != null && tile instanceof TileChaosAltar) {
      return tile;
    }

    // Last known altar coords are no good, try to find a new altar.
    for (int y = this.yCoord - SEARCH_HEIGHT; y < this.yCoord + SEARCH_HEIGHT + 1; ++y) {
      for (int x = this.xCoord - SEARCH_RADIUS; x < this.xCoord + SEARCH_RADIUS + 1; ++x) {
        for (int z = this.zCoord - SEARCH_RADIUS; z < this.zCoord + SEARCH_RADIUS + 1; ++z) {
          tile = this.worldObj.getTileEntity(x, y, z);
          if (tile != null && tile instanceof TileChaosAltar) {
            // LogHelper.debug("Pylon found new altar at (" + x + ", " + y + ", " + z + ")!");
            this.lastAltarX = x;
            this.lastAltarY = y;
            this.lastAltarZ = z;
            return tile;
          }
        }
      }
    }

    // None found
    return null;
  }

  @Override
  public void updateEntity() {

    if (!this.worldObj.isRemote) {
      this.energyStored += 5; // TODO: Config?
      if (this.energyStored > this.getMaxEnergyStored()) {
        this.energyStored = this.getMaxEnergyStored();
      }

      if (this.worldObj.getTotalWorldTime() % TRANSFER_DELAY == 0) {
        this.sendEnergyToAltar();
        this.spawnParticlesToAltar();
      }
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
    double diffY = altar.yCoord - this.yCoord;
    double diffZ = altar.zCoord - this.zCoord;
    double stepX = diffX / PARTICLE_COUNT;
    double stepY = diffY / PARTICLE_COUNT;
    double stepZ = diffZ / PARTICLE_COUNT;
    double x = this.xCoord + 0.5;
    double y = this.yCoord + 0.75;
    double z = this.zCoord + 0.5;

    for (int i = 0; i < 16; ++i) {
      double motionX = SilentGems.instance.random.nextGaussian() * 0.0004;
      double motionY = SilentGems.instance.random.nextGaussian() * 0.0004;
      double motionZ = SilentGems.instance.random.nextGaussian() * 0.0004;
      EntityFX particleFX = new EntityParticleFXTest(this.worldObj, x, y, z, motionX, motionY,
          motionZ);
      SilentGems.proxy.spawnParticles(particleFX);
      x += stepX;
      y += stepY;
      z += stepZ;
    }
  }
}
