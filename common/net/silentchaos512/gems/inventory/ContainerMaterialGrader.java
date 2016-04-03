package net.silentchaos512.gems.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.tile.TileMaterialGrader;
import net.silentchaos512.lib.inventory.ContainerSL;

public class ContainerMaterialGrader extends ContainerSL {

  private int chaosStored;
  private int progress;

  public ContainerMaterialGrader(InventoryPlayer playerInventory, IInventory tileInventory) {

    super(playerInventory, tileInventory);
  }

  @Override
  protected void addTileInventorySlots(IInventory inv) {

    addSlotToContainer(new Slot(tileInventory, 0, 56, 35));
    addSlotToContainer(new Slot(tileInventory, 1, 111, 35));
  }

  @Override
  public void onCraftGuiOpened(ICrafting listener) {

    super.onCraftGuiOpened(listener);
    listener.updateCraftingInventory(this, getInventory());
  }

  @Override
  public void detectAndSendChanges() {

    super.detectAndSendChanges();

    for (ICrafting crafter : crafters) {
      if (chaosStored != tileInventory.getField(0)) {
        crafter.sendProgressBarUpdate(this, 0, tileInventory.getField(0));
      }
      if (progress != tileInventory.getField(1)) {
        crafter.sendProgressBarUpdate(this, 1, tileInventory.getField(1));
      }
    }

    chaosStored = tileInventory.getField(0);
    progress = tileInventory.getField(1);
  }

  @Override
  public void updateProgressBar(int id, int data) {

    tileInventory.setField(id, data);
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

    ItemStack stack = null;
    Slot slot = (Slot) inventorySlots.get(slotIndex);

    if (slot != null && slot.getHasStack()) {
      ItemStack stack1 = slot.getStack();
      stack = stack1.copy();
      final int size = tileInventory.getSizeInventory();
      final int startPlayer = size;
      final int endPlayer = size + 27;
      final int startHotbar = size + 27;
      final int endHotbar = size + 36;

      System.out.println(String.format("slotIndex = %d, startPlayer = %d, endPlayer = %d",
          slotIndex, startPlayer, endPlayer));

      if (slotIndex == TileMaterialGrader.SLOT_OUTPUT) {
        // Remove from output slot?
        if (!this.mergeItemStack(stack1, startPlayer, endHotbar, true)) {
          return null;
        }
      } else if (slotIndex >= size
          && tileInventory.isItemValidForSlot(TileMaterialGrader.SLOT_INPUT, stack1)) {
        // Move from player to input slot?
        if (!mergeItemStack(stack1, 0, 1, false)) {
          return null;
        }
      } else if (slotIndex >= startPlayer && slotIndex < endPlayer) {
        // Move player items to hotbar.
        if (!mergeItemStack(stack1, startHotbar, endHotbar, false)) {
          return null;
        }
      } else if (slotIndex >= startHotbar && slotIndex < endHotbar) {
        // Move player items from hotbar.
        if (!mergeItemStack(stack1, startPlayer, endPlayer, false)) {
          return null;
        }
      } else if (!mergeItemStack(stack1, startPlayer, endHotbar, false)) {
        return null;
      }

      // if (tileInventory.isItemValidForSlot(slotIndex, stack1)) {
      // if (!mergeItemStack(stack1, 0, size, false)) {
      // return null;
      // }
      // } else if (slotIndex >= startPlayer && slotIndex < endPlayer) { // 0, 27
      // if (!mergeItemStack(stack1, startHotbar, endHotbar, false)) { // 27, 36
      // return null;
      // }
      // } else if (slotIndex >= startHotbar && slotIndex < endHotbar
      // && !mergeItemStack(stack1, startPlayer, endPlayer, false)) { // 27, 36, 0, 27
      // return null;
      // }
      // } else if (!mergeItemStack(stack1, startPlayer, endHotbar, false)) { // 0, 36
      // return null;
      // }

      if (stack1.stackSize == 0) {
        slot.putStack((ItemStack) null);
      } else {
        slot.onSlotChanged();
      }

      if (stack1.stackSize == stack.stackSize) {
        return null;
      }

      slot.onPickupFromSlot(player, stack1);
    }

    return stack;
  }
}
