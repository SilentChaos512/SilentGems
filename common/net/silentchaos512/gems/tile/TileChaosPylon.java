package net.silentchaos512.gems.tile;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.BlockChaosPylon;
import net.silentchaos512.gems.client.particle.EntityParticleFXChaosTransfer;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.LogHelper;

public class TileChaosPylon extends TileEntity implements IInventory {

  public static final int SEARCH_RADIUS = 4;
  public static final int SEARCH_HEIGHT = 1;
  public static final int TRANSFER_DELAY = 10;
  public static final int ALTAR_SEARCH_DELAY = 200;
  public static final int PARTICLE_DELAY = 100;
  public static final int PARTICLE_COUNT = 16;

  protected int energyStored;
  protected int burnTimeRemaining;
  protected int currentItemBurnTime;
  protected int lastAltarX;
  protected int lastAltarY;
  protected int lastAltarZ;

  protected ItemStack[] inventory = new ItemStack[1];

  @Override
  public void readFromNBT(NBTTagCompound tags) {

    super.readFromNBT(tags);
    energyStored = tags.getInteger("Energy");
    burnTimeRemaining = tags.getInteger("BurnTime");
    lastAltarX = tags.getInteger("AltarX");
    lastAltarY = tags.getInteger("AltarY");
    lastAltarZ = tags.getInteger("AltarZ");

    NBTTagList tagList = tags.getTagList("Items", 10);
    for (int i = 0; i < tagList.tagCount(); ++i) {
      NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
      byte slot = tagCompound.getByte("Slot");

      if (slot >= 0 && slot < inventory.length) {
        inventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
      }
    }

    currentItemBurnTime = TileEntityFurnace.getItemBurnTime(inventory[0]);
  }

  @Override
  public void writeToNBT(NBTTagCompound tags) {

    super.writeToNBT(tags);
    tags.setInteger("Energy", energyStored);
    tags.setInteger("BurnTime", burnTimeRemaining);
    tags.setInteger("AltarX", lastAltarX);
    tags.setInteger("AltarY", lastAltarY);
    tags.setInteger("AltarZ", lastAltarZ);

    NBTTagList tagList = new NBTTagList();
    for (int i = 0; i < inventory.length; ++i) {
      if (inventory[i] != null) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setByte("Slot", (byte) i);
        inventory[i].writeToNBT(tagCompound);
        tagList.appendTag(tagCompound);
      }
    }
    tags.setTag("Items", tagList);
  }

  public int getEnergyStored() {

    return energyStored;
  }

  public int getMaxEnergyStored() {

    return 10000; // TODO: Config?
  }

  public TileEntity getAltar() {
    
    if (worldObj.isRemote) {
      return null;
    }

    // Get last known altar, if it exists.
    TileEntity tile = this.worldObj.getTileEntity(lastAltarX, lastAltarY, lastAltarZ);
    if (tile != null && tile instanceof TileChaosAltar) {
      return tile;
    }

    // Last known altar coords are no good, try to find a new altar.
    if (worldObj.getTotalWorldTime() % ALTAR_SEARCH_DELAY == 0) {
      LogHelper.debug(
          "Pylon at (" + xCoord + ", " + yCoord + ", " + zCoord + ") searching for new altar...");
      for (int y = yCoord - SEARCH_HEIGHT; y < yCoord + SEARCH_HEIGHT + 1; ++y) {
        for (int x = xCoord - SEARCH_RADIUS; x < xCoord + SEARCH_RADIUS + 1; ++x) {
          for (int z = zCoord - SEARCH_RADIUS; z < zCoord + SEARCH_RADIUS + 1; ++z) {
            tile = worldObj.getTileEntity(x, y, z);
            if (tile != null && tile instanceof TileChaosAltar) {
              LogHelper.debug("Pylon found new altar at (" + x + ", " + y + ", " + z + ")!");
              lastAltarX = x;
              lastAltarY = y;
              lastAltarZ = z;
              return tile;
            }
          }
        }
      }
    }

    // None found
    return null;
  }

  @Override
  public Packet getDescriptionPacket() {

    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("BurnTime", burnTimeRemaining);
    tags.setInteger("CurrentItemBurnTime", currentItemBurnTime);
    return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tags);
  }

  @Override
  public void onDataPacket(NetworkManager network, S35PacketUpdateTileEntity packet) {

    burnTimeRemaining = packet.func_148857_g().getInteger("BurnTime");
    currentItemBurnTime = packet.func_148857_g().getInteger("CurrentItemBurnTime");
  }

  @Override
  public void updateEntity() {

    if (!worldObj.isRemote) {
      // Produce energy
      energyStored += getEnergyProduced();
      if (energyStored > getMaxEnergyStored()) {
        energyStored = getMaxEnergyStored();
      }

      // Burner pylon fuel
      if (getPylonType() == BlockChaosPylon.Type.BURNER) {
        burnFuel();
      }

      // Transfer energy
      if (worldObj.getTotalWorldTime() % TRANSFER_DELAY == 0) {
        sendEnergyToAltar();
        // if (sendEnergyToAltar()) {
        // spawnParticlesToAltar();
        // }
      }
    }

    // FIXME: Particles - don't spawn when no energy is transfered?
    if (worldObj.getTotalWorldTime() % PARTICLE_DELAY == 0) {
      spawnParticlesToAltar();
    }
  }

  public int getEnergyProduced() {

    BlockChaosPylon.Type type = getPylonType();
    if (type != null) {
      if (type == BlockChaosPylon.Type.PASSIVE) {
        return Config.pylonPassiveGenerationRate;
      } else if (type == BlockChaosPylon.Type.BURNER) {
        if (burnTimeRemaining > 0) {
          return Config.pylonBurnerGenerationRate;
        }
      } else {
        LogHelper.warning("TileChaosPylon.getEnergyProduced: Unknown pylon type!");
      }
    }

    return 0;
  }

  private void burnFuel() {

    boolean markForUpdate = false;

    if (burnTimeRemaining > 0) {
      --burnTimeRemaining;
      markForUpdate = true;
    }

    if (burnTimeRemaining <= 0 && getEnergyStored() < getMaxEnergyStored()) {
      if (inventory[0] != null) {
        int fuelBurnTime = TileEntityFurnace.getItemBurnTime(inventory[0]);
        if (fuelBurnTime > 0) {
          currentItemBurnTime = burnTimeRemaining = fuelBurnTime;
          --inventory[0].stackSize;
          if (inventory[0].stackSize == 0) {
            inventory[0] = null;
          }
          markForUpdate = true;
        }
      }
    }

    if (markForUpdate) {
      worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
  }

  public boolean isBurningFuel() {

    return burnTimeRemaining > 0;
  }

  public int getBurnTimeRemaining() {

    return burnTimeRemaining;
  }

  public int getCurrentItemBurnTime() {

    return currentItemBurnTime;
  }

  public int getBurnTimeRemainingScaled(int k) {

    if (currentItemBurnTime == 0) {
      currentItemBurnTime = 200;
    }

    return burnTimeRemaining * k / currentItemBurnTime;
  }

  private boolean sendEnergyToAltar() {

    TileEntity tile = getAltar();
    if (tile != null) {
      TileChaosAltar altar = (TileChaosAltar) tile;
      int sent = altar.receiveEnergy(energyStored);
      energyStored -= sent;
      return sent > 0;
    }
    return false;
  }

  private void spawnParticlesToAltar() {

    TileEntity altar = getAltar();
    if (altar == null) {
      return;
    }

    double diffX = altar.xCoord - xCoord;
    double diffY = altar.yCoord - yCoord;
    double diffZ = altar.zCoord - zCoord;
    double stepX = diffX / PARTICLE_COUNT;
    double stepY = diffY / PARTICLE_COUNT;
    double stepZ = diffZ / PARTICLE_COUNT;
    double x = xCoord + 0.5;
    double y = yCoord + 0.75;
    double z = zCoord + 0.5;

    for (int i = 0; i < 16; ++i) {
      double motionX = SilentGems.instance.random.nextGaussian() * 0.0004;
      double motionY = SilentGems.instance.random.nextGaussian() * 0.0004;
      double motionZ = SilentGems.instance.random.nextGaussian() * 0.0004;
      SilentGems.proxy.spawnParticles("chaosTransfer", worldObj, x, y, z, motionX, motionY,
          motionZ);
      x += stepX;
      y += stepY;
      z += stepZ;
    }
  }

  public BlockChaosPylon.Type getPylonType() {

    int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    if (meta >= 0 && meta < BlockChaosPylon.Type.values().length) {
      return BlockChaosPylon.Type.values()[meta];
    }
    return null;
  }

  @Override
  public int getSizeInventory() {

    if (getPylonType() == BlockChaosPylon.Type.BURNER) {
      return 1;
    }
    return 0;
  }

  @Override
  public ItemStack getStackInSlot(int slot) {

    if (slot >= 0 && slot < getSizeInventory()) {
      return inventory[slot];
    }
    return null;
  }

  @Override
  public ItemStack decrStackSize(int slot, int count) {

    if (inventory[slot] != null) {
      ItemStack stack;

      if (inventory[slot].stackSize <= count) {
        stack = inventory[slot];
        inventory[slot] = null;
        return stack;
      } else {
        stack = inventory[slot].splitStack(count);

        if (inventory[slot].stackSize == 0) {
          inventory[slot] = null;
        }

        return stack;
      }
    } else {
      return null;
    }
  }

  @Override
  public ItemStack getStackInSlotOnClosing(int slot) {

    if (inventory[slot] != null) {
      ItemStack stack = inventory[slot];
      inventory[slot] = null;
      return stack;
    } else {
      return null;
    }
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack stack) {

    this.inventory[slot] = stack;

    if (stack != null && stack.stackSize > getInventoryStackLimit()) {
      stack.stackSize = getInventoryStackLimit();
    }
  }

  @Override
  public String getInventoryName() {

    return "container.silentgems:BurnerPylon";
  }

  @Override
  public boolean hasCustomInventoryName() {

    return false;
  }

  @Override
  public int getInventoryStackLimit() {

    return 64;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer player) {

    return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false
        : player.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D,
            (double) zCoord + 0.5D) <= 64.0D;
  }

  @Override
  public void openInventory() {

  }

  @Override
  public void closeInventory() {

  }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack stack) {

    return true;
  }
}
