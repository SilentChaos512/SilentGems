package net.silentchaos512.gems.tile;

import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.BlockChaosPylon;
import net.silentchaos512.gems.client.particle.EntityParticleFXChaosTransfer;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.lib.IChaosEnergyAccepter;
import net.silentchaos512.gems.lib.IChaosEnergyProvider;

public class TileChaosPylon extends TileEntity implements IInventory, ITickable, IChaosEnergyProvider {

  public static final int SEARCH_RADIUS = 4;
  public static final int SEARCH_HEIGHT = 1;
  public static final int TRANSFER_DELAY = 10;
  public static final int ALTAR_SEARCH_DELAY = 100;
  public static final int PARTICLE_DELAY = 100;
  public static final int PARTICLE_COUNT = 1;

  protected int energyStored;
  protected int burnTimeRemaining;
  protected int currentItemBurnTime;
  protected int lastAltarX;
  protected int lastAltarY;
  protected int lastAltarZ;

  protected ItemStack[] inventory = new ItemStack[1];

  // ADDED BY M4THG33K

  protected float timer = 0;
  protected int pylonType = -1; // used for rendering; set by external sources. a value of -1 means "something is wrong"

  public float getTimer() {

    return timer;
  }

  public int getPylonTypeInteger() {

    return pylonType;
  }

  public void setTimer(int n) {

    timer = n;
  }

  public void setPylonTypeInteger(int myType) {

    pylonType = myType;
  }

  // END ADDED BY M4THG33K

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

    // read the pylonType info
    if (tags.hasKey("MyPylonType")) {
      pylonType = tags.getInteger("MyPylonType");
    } else {
      pylonType = -1; // default to error texture. Break/replace current blocks to fix
    }
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

    // save pylon type
    tags.setInteger("MyPylonType", pylonType);
  }

  @Override
  public int getEnergyStored() {

    return energyStored;
  }

  @Override
  public int getMaxEnergyStored() {

    return 10000; // TODO: Config?
  }

  @Override
  public TileEntity getEnergyAccepter() {

    // Get last known altar, if it exists.
    TileEntity tile = worldObj.getTileEntity(new BlockPos(lastAltarX, lastAltarY, lastAltarZ));
    if (tile != null && tile instanceof IChaosEnergyAccepter) {
      return tile;
    }

    boolean searching = false;
    // Last known altar coords are no good, try to find a new altar.
    if (worldObj.getTotalWorldTime() % ALTAR_SEARCH_DELAY == 0) {
      // Search
      final int xCoord = pos.getX();
      final int yCoord = pos.getY();
      final int zCoord = pos.getZ();
      for (int y = yCoord - SEARCH_HEIGHT; y < yCoord + SEARCH_HEIGHT + 1; ++y) {
        for (int x = xCoord - SEARCH_RADIUS; x < xCoord + SEARCH_RADIUS + 1; ++x) {
          for (int z = zCoord - SEARCH_RADIUS; z < zCoord + SEARCH_RADIUS + 1; ++z) {
            tile = worldObj.getTileEntity(new BlockPos(x, y, z));
            if (tile != null && tile instanceof IChaosEnergyAccepter) {
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
    tags.setInteger("AltarX", lastAltarX);
    tags.setInteger("AltarY", lastAltarY);
    tags.setInteger("AltarZ", lastAltarZ);
    return new S35PacketUpdateTileEntity(pos, 1, tags);
  }

  @Override
  public void onDataPacket(NetworkManager network, S35PacketUpdateTileEntity packet) {

    NBTTagCompound tags = packet.getNbtCompound();
    burnTimeRemaining = tags.getInteger("BurnTime");
    currentItemBurnTime = tags.getInteger("CurrentItemBurnTime");
    lastAltarX = tags.getInteger("AltarX");
    lastAltarY = tags.getInteger("AltarY");
    lastAltarZ = tags.getInteger("AltarZ");
  }

  @Override
  public void update() {

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
        sendEnergyToAccepter();
        // if (sendEnergyToAltar()) {
        // spawnParticlesToAltar();
        // }
      }
    } else {
      // update the timer - client side only; if you want each pylon/altar to have a unique
      // animation, we will have to move this somewhere else and rework the code a bit more
      timer = (timer + 1) % 360;
    }

    if (worldObj.getTotalWorldTime() % PARTICLE_DELAY == 0) {
      if (!spawnParticlesToAltar()) {
        spawnBadPlacementParticles();
      }
    }
  }

  public int getEnergyProduced() {

    BlockChaosPylon.Type type = getPylonType();
    if (type != null) {
      if (type == BlockChaosPylon.Type.PASSIVE) {
        return Config.PYLON_PASSIVE_GENERATION_RATE;
      } else if (type == BlockChaosPylon.Type.BURNER) {
        if (burnTimeRemaining > 0) {
          return Config.PYLON_BURNER_GENERATION_RATE;
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
            inventory[0] = inventory[0].getItem().getContainerItem(inventory[0]);
          }
          markForUpdate = true;
        }
      }
    }

    if (markForUpdate) {
      worldObj.markBlockForUpdate(pos);
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

  @Override
  public boolean sendEnergyToAccepter() {

    TileEntity tile = getEnergyAccepter();
    if (tile != null) {
      IChaosEnergyAccepter altar = (IChaosEnergyAccepter) tile;
      int sent = altar.receiveEnergy(energyStored);
      energyStored -= sent;
      return sent > 0;
    }
    return false;
  }

  private boolean spawnParticlesToAltar() {

    // Don't spawn particles on minimal.
    if (SilentGems.proxy.getParticleSettings() == 2) {
      return true;
    }

    TileEntity altar = getEnergyAccepter();
    if (altar == null) {
      return false;
    }

    double diffX = altar.getPos().getX() - pos.getX();
    double diffY = altar.getPos().getY() + 0.5 - pos.getY();
    double diffZ = altar.getPos().getZ() - pos.getZ();
    // double stepX = diffX / PARTICLE_COUNT;
    // double stepY = diffY / PARTICLE_COUNT;
    // double stepZ = diffZ / PARTICLE_COUNT;
    double x = pos.getX() + 0.5;
    double y = pos.getY() + 0.5;
    double z = pos.getZ() + 0.5;

    for (int i = 0; i < PARTICLE_COUNT; ++i) {
      double motionX = SilentGems.instance.random.nextGaussian() * 0.0008
          + diffX / (EntityParticleFXChaosTransfer.MAX_AGE - 1);
      double motionY = SilentGems.instance.random.nextGaussian() * 0.0008
          + diffY / (EntityParticleFXChaosTransfer.MAX_AGE - 1);
      double motionZ = SilentGems.instance.random.nextGaussian() * 0.0008
          + diffZ / (EntityParticleFXChaosTransfer.MAX_AGE - 1);
      SilentGems.proxy.spawnParticles("chaosTransfer", worldObj, x, y, z, motionX, motionY,
          motionZ);
      // x += stepX;
      // y += stepY;
      // z += stepZ;
    }

    return true;
  }

  private void spawnBadPlacementParticles() {

    // Don't spawn particles on minimal.
    if (SilentGems.proxy.getParticleSettings() == 2) {
      return;
    }

    double x = pos.getX() + 0.5;
    double y = pos.getY() + 0.75;
    double z = pos.getZ() + 0.5;

    for (int i = 0; i < PARTICLE_COUNT * 2; ++i) {
      double motionX = SilentGems.instance.random.nextGaussian() * 0.02;
      double motionY = Math.abs(SilentGems.instance.random.nextGaussian() * 0.02);
      double motionZ = SilentGems.instance.random.nextGaussian() * 0.02;
      SilentGems.proxy.spawnParticles("chaosNoAltar", worldObj, x, y, z, motionX, motionY, motionZ);
    }
  }

  public BlockChaosPylon.Type getPylonType() {

    IBlockState state = worldObj.getBlockState(pos);
    int meta = state.getBlock().getMetaFromState(state);
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
  public ItemStack removeStackFromSlot(int slot) {

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
  public int getInventoryStackLimit() {

    return 64;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer player) {

    return worldObj.getTileEntity(pos) != this ? false
        : player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
            (double) pos.getZ() + 0.5D) <= 64.0D;
  }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack stack) {

    return true;
  }

  @Override
  public String getName() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean hasCustomName() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public IChatComponent getDisplayName() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void openInventory(EntityPlayer player) {

    // TODO Auto-generated method stub

  }

  @Override
  public void closeInventory(EntityPlayer player) {

    // TODO Auto-generated method stub

  }

  @Override
  public int getField(int id) {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setField(int id, int value) {

    // TODO Auto-generated method stub

  }

  @Override
  public int getFieldCount() {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void clear() {

    // TODO Auto-generated method stub

  }
}
