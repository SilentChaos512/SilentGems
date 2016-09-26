package net.silentchaos512.gems.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.chunk.Chunk;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.gems.api.energy.IChaosStorage;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.util.DimensionalPosition;

public class TileChaosAltar extends TileEntity implements ISidedInventory, ITickable, IChaosAccepter {

  public static final int MAX_CHAOS_STORED = 1000000;
  public static final int MAX_RECEIVE = 100000;

  public static final int[] SLOTS_BOTTOM = { 1 };
  public static final int[] SLOTS_TOP = { 0 };
  public static final int[] SLOTS_SIDE = { 0 };

  protected int chaosStored;
  protected ItemStack[] inventory = new ItemStack[2];

  @Override
  public void update() {

    if (worldObj.isRemote) {
      return;
    }

    ItemStack stack = getStackInSlot(0);
    if (stack != null && stack.getItem() instanceof IChaosStorage) {
      // Charge chaos storage items.
      IChaosStorage chaosStorage = (IChaosStorage) stack.getItem();
      int amount = chaosStorage.receiveCharge(stack, Math.min(chaosStored, 1000), false);
      chaosStored -= amount;

      // Send update?
      if (amount != 0) {
        markDirty();
      }

      // Move full items to second slot
      if (chaosStorage.getCharge(stack) == chaosStorage.getMaxCharge(stack)) {
        if (getStackInSlot(1) == null) {
          setInventorySlotContents(1, stack);
          setInventorySlotContents(0, null);
        }
      }
    }
  }

  public ItemStack getStackToRender() {

    for (ItemStack stack : inventory) {
      if (stack != null) {
        return stack;
      }
    }
    return null;
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {

    super.readFromNBT(tags);
    chaosStored = tags.getInteger("Energy");

    NBTTagList tagList = tags.getTagList("Items", 10);
    for (int i = 0; i < tagList.tagCount(); ++i) {
      NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
      byte slot = tagCompound.getByte("Slot");

      if (slot >= 0 && slot < inventory.length) {
        this.inventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
      }
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {

    super.writeToNBT(tags);
    tags.setInteger("Energy", chaosStored);

    NBTTagList tagList = new NBTTagList();
    for (int i = 0; i < inventory.length; ++i) {
      if (this.inventory[i] != null) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setByte("Slot", (byte) i);
        this.inventory[i].writeToNBT(tagCompound);
        tagList.appendTag(tagCompound);
      }
    }
    tags.setTag("Items", tagList);
    return tags;
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("Energy", getCharge());
    return new SPacketUpdateTileEntity(pos, 1, tags);
  }

  @Override
  public NBTTagCompound getUpdateTag() {

    NBTTagCompound tags = super.getUpdateTag();
    tags.setInteger("Energy", getCharge());
    return tags;
  }

  @Override
  public void onDataPacket(NetworkManager network, SPacketUpdateTileEntity packet) {

    chaosStored = packet.getNbtCompound().getInteger("Energy");
  }

  @Override
  public void markDirty() {

    super.markDirty();
    Chunk chunk = worldObj.getChunkFromBlockCoords(pos);
    IBlockState state = worldObj.getBlockState(pos);
    worldObj.markAndNotifyBlock(pos, chunk, state, state, 2);
  }

  @Override
  public int getSizeInventory() {

    return inventory.length;
  }

  @Override
  public ItemStack getStackInSlot(int index) {

    return index >= 0 && index < inventory.length ? inventory[index] : null;
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

  }

  @Override
  public void closeInventory(EntityPlayer player) {

  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {

    return true;
  }

  @Override
  public int getField(int id) {

    if (id == 0) return chaosStored;
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

  @Override
  public String getName() {

    return Names.CHAOS_ALTAR;
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

  @Override
  public int getCharge() {

    return chaosStored;
  }

  @Override
  public int getMaxCharge() {

    return MAX_CHAOS_STORED;
  }

  @Override
  public int receiveCharge(int maxReceive, boolean simulate) {

    int received = Math.min(getMaxCharge() - getCharge(), maxReceive);
    if (!simulate) {
      chaosStored += received;
      if (received != 0) {
        markDirty();
      }
    }
    return received;
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side) {

    switch (side) {
      case DOWN:
        return SLOTS_BOTTOM;
      case UP:
        return SLOTS_TOP;
      default:
        return SLOTS_SIDE;
    }
  }

  @Override
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {

    return isItemValidForSlot(index, itemStackIn);
  }

  @Override
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {

    return direction == EnumFacing.DOWN;
  }

}
