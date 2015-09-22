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
import net.silentchaos512.gems.energy.IChaosStorage;
import net.silentchaos512.gems.item.ChaosGem;

public class TileChaosAltar extends TileEntity implements ISidedInventory {
  
  public static final int BLOCK_UPDATE_DELAY = 200;

  protected ItemStack[] inventory = new ItemStack[2];
  protected int[] slotsBottom = new int[] { 1 };
  protected int[] slotsTop = new int[] { 0 };
  protected int[] slotsSide = new int[] { 0 };
  protected int energyStored = 0;
  
  //ADDED BY M4THG33K
  protected int timer = 0; //a timer to help in the rendering animations

  public int getTimer()
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

      if (slot >= 0 && slot < this.inventory.length) {
        this.inventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
      }
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound tags) {

    super.writeToNBT(tags);
    tags.setInteger("Energy", energyStored);

    NBTTagList tagList = new NBTTagList();
    for (int i = 0; i < this.inventory.length; ++i) {
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
    if (this.energyStored + amount > this.getMaxEnergyStored()) {
      amountReceived = this.getMaxEnergyStored() - this.energyStored;
      this.energyStored = this.getMaxEnergyStored();
    } else {
      amountReceived = amount;
      this.energyStored += amount;
    }
    
    this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    
    return amountReceived;
  }

  @Override
  public Packet getDescriptionPacket() {

    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("Energy", this.getEnergyStored());
    return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tags);
  }

  @Override
  public void onDataPacket(NetworkManager network, S35PacketUpdateTileEntity packet) {

    this.energyStored = packet.func_148857_g().getInteger("Energy");
  }

  @Override
  public void updateEntity() {

    if (!this.worldObj.isRemote) {
      ItemStack stack = getStackInSlot(0);
      if (stack != null && stack.getItem() instanceof IChaosStorage) {
        // Charge storage items.
        IChaosStorage chaosStorage = (IChaosStorage) stack.getItem();
        int amount = chaosStorage.receiveEnergy(stack, Math.min(this.energyStored, 1000), false);
        this.energyStored -= amount;
        if (amount != 0) {
          this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
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
        worldObj.notifyBlockChange(xCoord, yCoord, zCoord, blockType);
      }
    }
    else{
    	//update the timer - client side only; if you want each pylon/altar to have a unique
    	//animation, we will have to move this somewhere else and rework the code a bit more
    	timer = (timer+1)%360;
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
  public ItemStack getStackInSlotOnClosing(int slot) {

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
  public String getInventoryName() {

    return "container.silentgems:ChaosAltar";
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

    return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false
        : player.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D,
            (double) this.zCoord + 0.5D) <= 64.0D;
  }

  @Override
  public void openInventory() {

  }

  @Override
  public void closeInventory() {

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

  @Override
  public int[] getAccessibleSlotsFromSide(int side) {

    switch (side) {
      case 0:
        return slotsBottom;
      case 1:
        return slotsTop;
      default:
        return slotsSide;
    }
  }

  @Override
  public boolean canInsertItem(int slot, ItemStack stack, int side) {

    return this.isItemValidForSlot(slot, stack);
  }

  @Override
  public boolean canExtractItem(int slot, ItemStack stack, int side) {

    return side == 0; // TODO: Is this right?
  }
}
