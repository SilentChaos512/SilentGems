package net.silentchaos512.gems.tile;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.gems.api.energy.IChaosProvider;
import net.silentchaos512.gems.entity.packet.EntityPacketChaos;
import net.silentchaos512.gems.lib.EnumPylonType;
import net.silentchaos512.gems.util.ChaosUtil;

public class TileChaosPylon extends TileEntity implements IInventory, ITickable, IChaosProvider {

  public static final double SEARCH_RADIUS_PLAYER_SQUARED = 16 * 16;
  public static final int SEARCH_RADIUS_BLOCK = 6;
  public static final int SEND_CHAOS_DELAY = 100;
  public static final int MAX_CHAOS_STORED = 10000;
  public static final int MAX_CHAOS_TRANSFERED = 10000;
  public static final int CHAOS_GENERATION_RATE_PASSIVE = 10;
  public static final int CHAOS_GENERATION_RATE_BURNER = 100;

  protected int chaosStored;
  protected int burnTimeRemaining;
  protected int currentItemBurnTime;
  protected ItemStack[] inventory = new ItemStack[1];

  protected EnumPylonType pylonType = EnumPylonType.NONE;

  public EnumPylonType getPylonType() {

    return pylonType;
  }

  public void setPylonType(EnumPylonType type) {

    pylonType = type;
  }

  @Override
  public void update() {

    if (!worldObj.isRemote) {
      // Generate chaos.
      chaosStored = Math.min(getCharge() + getEnergyProduced(), getMaxCharge());

      // Burner pylon fuel
      if (pylonType == EnumPylonType.BURNER) {
        burnFuel();
      }

      // SilentGems.instance.logHelper.debug(getPylonType());

      // Transfer energy to blocks, then players.
      if (worldObj.getTotalWorldTime() % SEND_CHAOS_DELAY == 0) {
        List<IChaosAccepter> accepters = ChaosUtil.getNearbyAccepters(worldObj, pos,
            SEARCH_RADIUS_BLOCK, SEARCH_RADIUS_BLOCK);
        List<EntityPlayer> players = worldObj.getEntities(EntityPlayer.class,
            e -> e.getDistanceSq(pos) < SEARCH_RADIUS_PLAYER_SQUARED);

        if (!players.isEmpty() || !accepters.isEmpty()) {
          final int amountForEach = Math.min(MAX_CHAOS_TRANSFERED,
              getCharge() / (accepters.size() + players.size()));

          sendEnergyToAccepters(accepters, amountForEach);
          sendEnergyToPlayers(players, amountForEach);
        }
      }
    }
  }

  protected void sendEnergyToAccepters(List<IChaosAccepter> list, int amountForEach) {

    if (getCharge() <= 0)
      return;

    int amount;

    for (IChaosAccepter accepter : list) {
      if (getCharge() <= 0) {
        return;
      }

      amount = Math.min(getCharge(), amountForEach);
      amount = accepter.receiveCharge(amount, true);
      if (amount > 0) {
        extractEnergy(amount, false);
        EntityPacketChaos packet = ChaosUtil.spawnPacketToBlock(worldObj, pos,
            ((TileEntity) accepter).getPos(), amount);
        Vec3d vel = new Vec3d(0.1, 0.25, 0.0);
        vel = vel.rotateYaw(2 * (float) Math.PI * SilentGems.random.nextFloat());
        packet.setVelocity(vel);
      }
    }
  }

  protected void sendEnergyToPlayers(List<EntityPlayer> list, int amountForEach) {

    if (getCharge() <= 0) {
      return;
    }

    int amount;

    for (EntityPlayer player : list) {
      if (getCharge() <= 0) {
        return;
      }

      amount = Math.min(getCharge(), amountForEach);
      int amountPlayerCanAccept = ChaosUtil.getAmountPlayerCanAccept(player, amount);
      if (amountPlayerCanAccept > 0) {
        amount = Math.min(amount, amountPlayerCanAccept);
        ChaosUtil.spawnPacketToEntity(worldObj, pos, player, amount);
        extractEnergy(amount, false);
      }
    }
  }

  private void burnFuel() {

    boolean markForUpdate = false;

    if (burnTimeRemaining > 0) {
      --burnTimeRemaining;
      markForUpdate = true;
    }

    if (burnTimeRemaining <= 0 && getCharge() < getMaxCharge()) {
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
      markDirty();
      // worldObj.markBlockForUpdate(pos);
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
  public SPacketUpdateTileEntity getUpdatePacket() {

    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("Energy", chaosStored);
    tags.setInteger("BurnTime", burnTimeRemaining);
    tags.setInteger("CurrentItemBurnTime", currentItemBurnTime);
    return new SPacketUpdateTileEntity(pos, 1, tags);
  }

  @Override
  public NBTTagCompound getUpdateTag() {

    NBTTagCompound tags = super.getUpdateTag();
    tags.setInteger("Energy", chaosStored);
    tags.setInteger("BurnTime", burnTimeRemaining);
    tags.setInteger("CurrentItemBurnTime", currentItemBurnTime);
    return tags;
  }

  @Override
  public void onDataPacket(NetworkManager network, SPacketUpdateTileEntity packet) {

    NBTTagCompound tags = packet.getNbtCompound();
    chaosStored = tags.getInteger("Energy");
    burnTimeRemaining = tags.getInteger("BurnTime");
    currentItemBurnTime = tags.getInteger("CurrentItemBurnTime");
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {

    super.readFromNBT(tags);
    chaosStored = tags.getInteger("Energy");
    burnTimeRemaining = tags.getInteger("BurnTime");

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
      pylonType = EnumPylonType.getByMeta(tags.getInteger("MyPylonType"));
    } else {
      pylonType = EnumPylonType.NONE; // default to error texture. Break/replace current blocks to fix
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {

    super.writeToNBT(tags);
    tags.setInteger("Energy", chaosStored);
    tags.setInteger("BurnTime", burnTimeRemaining);

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
    tags.setInteger("MyPylonType", pylonType.getMeta());
    return tags;
  }

  @Override
  public String getName() {

    return "ChaosPylon"; // TODO
  }

  @Override
  public boolean hasCustomName() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ITextComponent getDisplayName() {

    // TODO Auto-generated method stub
    return null;
  }

  public int getEnergyProduced() {

    if (pylonType == EnumPylonType.BURNER && burnTimeRemaining <= 0) {
      return 0;
    }
    return pylonType.getChaosGenerationRate();

  }

  @Override
  public int getCharge() {

    return chaosStored;
  }

  @Override
  public int getMaxCharge() {

    return MAX_CHAOS_STORED;
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {

    int amount = Math.min(getCharge(), maxExtract);
    if (!simulate) {
      chaosStored -= amount;
    }
    return amount;
  }

  @Override
  public int getSizeInventory() {

    return getPylonType() == EnumPylonType.BURNER ? 1 : 0;
  }

  @Override
  public ItemStack getStackInSlot(int index) {

    if (index >= 0 && index < getSizeInventory()) {
      return inventory[index];
    }
    return null;
  }

  @Override
  public ItemStack decrStackSize(int index, int count) {

    if (inventory[index] != null) {
      ItemStack stack;

      if (inventory[index].stackSize <= count) {
        stack = inventory[index];
        inventory[index] = null;
        return stack;
      } else {
        stack = inventory[index].splitStack(count);

        if (inventory[index].stackSize == 0) {
          inventory[index] = null;
        }

        return stack;
      }
    } else {
      return null;
    }
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {

    if (inventory[index] != null) {
      ItemStack stack = inventory[index];
      inventory[index] = null;
      return stack;
    }
    return null;
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {

    inventory[index] = stack;
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

    return worldObj.getTileEntity(pos) != this ? false : player.getDistanceSq(pos) <= 64.0;
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
  public boolean isItemValidForSlot(int index, ItemStack stack) {

    return true;
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
