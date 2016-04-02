package net.silentchaos512.gems.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileBasicInventory extends TileEntity implements IInventory {

  protected ItemStack[] inventory;
  protected String nameInventory;

  public TileBasicInventory(int inventorySize, String nameInventory) {

    this.inventory = new ItemStack[inventorySize];
    this.nameInventory = "container.silentgems." + nameInventory + ".name";
  }

  @Override
  public String getName() {

    return nameInventory;
  }

  @Override
  public boolean hasCustomName() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ITextComponent getDisplayName() {

    return new TextComponentString(nameInventory);
  }

  @Override
  public int getSizeInventory() {

    return inventory.length;
  }

  @Override
  public ItemStack getStackInSlot(int index) {

    return index >= 0 && index < getSizeInventory() ? inventory[index] : null;
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

    for (int i = 0; i < inventory.length; ++i) {
      inventory[i] = null;
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    NBTTagList tagList = compound.getTagList("Items", 10);
    inventory = new ItemStack[this.getSizeInventory()];

    for (int i = 0; i < tagList.tagCount(); ++i) {
      NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
      byte b0 = tagCompound.getByte("Slot");

      if (b0 >= 0 && b0 <= inventory.length) {
        inventory[b0] = ItemStack.loadItemStackFromNBT(tagCompound);
      }
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    NBTTagList tagList = new NBTTagList();

    for (int i = 0; i < inventory.length; ++i) {
      if (inventory[i] != null) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setByte("Slot", (byte) i);
        inventory[i].writeToNBT(tagCompound);
        tagList.appendTag(tagCompound);
      }
    }

    compound.setTag("Items", tagList);
  }
}
