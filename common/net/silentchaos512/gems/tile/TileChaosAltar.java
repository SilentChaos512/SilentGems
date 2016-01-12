package net.silentchaos512.gems.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.silentchaos512.gems.energy.IChaosStorage;

public class TileChaosAltar extends TileEntity implements ISidedInventory, ITickable {
  
  public static final int BLOCK_UPDATE_DELAY = 200;

  protected ItemStack[] inventory = new ItemStack[2];
  protected int[] slotsBottom = new int[] { 1 };
  protected int[] slotsTop = new int[] { 0 };
  protected int[] slotsSide = new int[] { 0 };
  protected int energyStored = 0;
  
  //ADDED BY M4THG33K
  protected float timer = 0; //a timer to help in the rendering animations

  public float getTimer()
  {
    return timer;
  }

  public void setTimer(int n)
  {
    timer = n;
  }
  //END ADDED BY M4THG33K

  @Override
  public void readFromNBT(NBTTagCompound tags) {

    super.readFromNBT(tags);
    energyStored = tags.getInteger("Energy");

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
  public void writeToNBT(NBTTagCompound tags) {

    super.writeToNBT(tags);
    tags.setInteger("Energy", energyStored);

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
  }

  public int getEnergyStored() {

    return this.energyStored;
  }

  public int getMaxEnergyStored() {

    return 1000000; // TODO: Config?
  }

  public int receiveEnergy(int amount) {

    int amountReceived = 0;
    if (energyStored + amount > getMaxEnergyStored()) {
      amountReceived = getMaxEnergyStored() - energyStored;
      energyStored = getMaxEnergyStored();
    } else {
      amountReceived = amount;
      energyStored += amount;
    }
    
    this.worldObj.markBlockForUpdate(pos);
    
    return amountReceived;
  }

  @Override
  public Packet getDescriptionPacket() {

    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("Energy", getEnergyStored());
    return new S35PacketUpdateTileEntity(pos, 1, tags);
  }

  @Override
  public void onDataPacket(NetworkManager network, S35PacketUpdateTileEntity packet) {

    this.energyStored = packet.getNbtCompound().getInteger("Energy");
  }

  @Override
  public void update() {

    if (!this.worldObj.isRemote) {
      ItemStack stack = getStackInSlot(0);
      if (stack != null && stack.getItem() instanceof IChaosStorage) {
        // Charge storage items.
        IChaosStorage chaosStorage = (IChaosStorage) stack.getItem();
        int amount = chaosStorage.receiveEnergy(stack, Math.min(energyStored, 1000), false);
        energyStored -= amount;
        if (amount != 0) {
          this.worldObj.markBlockForUpdate(pos);
        }

        // Move full item to second slot
        if (chaosStorage.getEnergyStored(stack) == chaosStorage.getMaxEnergyStored(stack)) {
          if (this.getStackInSlot(1) == null) {
            this.setInventorySlotContents(1, stack);
            this.setInventorySlotContents(0, null);
          }
        }
      }
      
      if (worldObj.getTotalWorldTime() % BLOCK_UPDATE_DELAY == 0) {
        worldObj.notifyBlockOfStateChange(pos, blockType);
      }
    }
    else{
    	//update the timer - client side only; if you want each pylon/altar to have a unique
    	//animation, we will have to move this somewhere else and rework the code a bit more
    	timer = (timer + 1) % 360;
    }
  }

  @Override
  public int getSizeInventory() {

    return inventory.length;
  }

  @Override
  public ItemStack getStackInSlot(int slot) {

    return inventory[slot];
  }

  @Override
  public ItemStack decrStackSize(int slot, int count) {

    if (this.inventory[slot] != null) {
      ItemStack stack;

      if (this.inventory[slot].stackSize <= count) {
        stack = this.inventory[slot];
        this.inventory[slot] = null;
        return stack;
      } else {
        stack = this.inventory[slot].splitStack(count);

        if (this.inventory[slot].stackSize == 0) {
          this.inventory[slot] = null;
        }

        return stack;
      }
    } else {
      return null;
    }
  }

  @Override
  public ItemStack removeStackFromSlot(int slot) {

    if (this.inventory[slot] != null) {
      ItemStack stack = this.inventory[slot];
      this.inventory[slot] = null;
      return stack;
    } else {
      return null;
    }
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack stack) {

    this.inventory[slot] = stack;

    if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
      stack.stackSize = this.getInventoryStackLimit();
    }
  }

  @Override
  public int getInventoryStackLimit() {

    return 64;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer player) {

    return this.worldObj.getTileEntity(pos) != this ? false
        : player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
            (double) pos.getZ() + 0.5D) <= 64.0D;
  }
  
  @Override
  public boolean isItemValidForSlot(int slot, ItemStack stack) {

    if (slot == 0 && stack != null) {
      if (stack.getItem() instanceof IChaosStorage) {
        return true;
      }
    }

    return false;
  }

//  @Override
//  public int[] getAccessibleSlotsFromSide(EnumFacing side) {
//
//    switch (side) {
//      case DOWN:
//        return slotsBottom;
//      case UP:
//        return slotsTop;
//      default:
//        return slotsSide;
//    }
//  }

  @Override
  public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {

    return this.isItemValidForSlot(slot, stack);
  }

  @Override
  public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {

    return side == EnumFacing.DOWN;
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
  public int[] getSlotsForFace(EnumFacing side) {

    // TODO Auto-generated method stub
    return null;
  }
}
