package net.silentchaos512.gems.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class ContainerBurnerPylon extends Container {

  private final IInventory tilePylon;

  public ContainerBurnerPylon(InventoryPlayer playerInventory, IInventory pylonInventory) {

    this.tilePylon = pylonInventory;
    this.addSlotToContainer(new Slot(pylonInventory, 0, 80, 34));

    int i;
    for (i = 0; i < 3; ++i) {
      for (int j = 0; j < 9; ++j) {
        this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }

    for (i = 0; i < 9; ++i) {
      this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
    }
  }

  @Override
  public boolean canInteractWith(EntityPlayer player) {

    return tilePylon.isUseableByPlayer(player);
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

    ItemStack stack = null;
    Slot slot = (Slot) this.inventorySlots.get(slotIndex);

    if (slot != null && slot.getHasStack()) {
      ItemStack stack1 = slot.getStack();
      stack = stack1.copy();

      if (slotIndex != 0) {
        if (tilePylon.isItemValidForSlot(0, stack1)) {
          if (!this.mergeItemStack(stack1, 0, 1, false)) {
            return null;
          }
        } else if (slotIndex >= 1 && slotIndex < 28) {
          if (!this.mergeItemStack(stack1, 28, 37, false)) {
            return null;
          }
        } else if (slotIndex >= 28 && slotIndex < 37 && !this.mergeItemStack(stack1, 1, 28, false)) {
          return null;
        }
      } else if (!this.mergeItemStack(stack1, 1, 37, false)) {
        return null;
      }
      
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
