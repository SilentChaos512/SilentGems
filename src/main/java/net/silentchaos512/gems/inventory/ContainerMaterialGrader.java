package net.silentchaos512.gems.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.tile.TileMaterialGrader;
import net.silentchaos512.lib.inventory.ContainerSL;
import net.silentchaos512.lib.util.StackHelper;

public class ContainerMaterialGrader extends ContainerSL {

  public ContainerMaterialGrader(InventoryPlayer playerInventory, IInventory tileInventory) {

    super(playerInventory, tileInventory);
  }

  @Override
  protected void addTileInventorySlots(IInventory inv) {

    addSlotToContainer(new Slot(tileInventory, 0, 26, 35));
    addSlotToContainer(new SpecialSlot(tileInventory, 1, 80, 35));
    addSlotToContainer(new SpecialSlot(tileInventory, 2, 98, 35));
    addSlotToContainer(new SpecialSlot(tileInventory, 3, 116, 35));
    addSlotToContainer(new SpecialSlot(tileInventory, 4, 134, 35));
  }

  @Override
  public void addListener(IContainerListener listener) {

    super.addListener(listener);
    listener.sendAllContents(this, getInventory());
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

    ItemStack stack = StackHelper.empty();
    Slot slot = (Slot) inventorySlots.get(slotIndex);

    if (slot != null && slot.getHasStack()) {
      ItemStack stack1 = slot.getStack();
      stack = stack1.copy();
      final int size = tileInventory.getSizeInventory();
      final int startPlayer = size;
      final int endPlayer = size + 27;
      final int startHotbar = size + 27;
      final int endHotbar = size + 36;

      if (slotIndex >= TileMaterialGrader.SLOT_OUTPUT_START && slotIndex < TileMaterialGrader.INVENTORY_SIZE) {
        // Remove from output slot?
        if (!this.mergeItemStack(stack1, startPlayer, endHotbar, true)) {
          return StackHelper.empty();
        }
      } else if (slotIndex >= size
          && tileInventory.isItemValidForSlot(TileMaterialGrader.SLOT_INPUT, stack1)) {
        // Move from player to input slot?
        if (!mergeItemStack(stack1, 0, 1, false)) {
          return StackHelper.empty();
        }
      } else if (slotIndex >= startPlayer && slotIndex < endPlayer) {
        // Move player items to hotbar.
        if (!mergeItemStack(stack1, startHotbar, endHotbar, false)) {
          return StackHelper.empty();
        }
      } else if (slotIndex >= startHotbar && slotIndex < endHotbar) {
        // Move player items from hotbar.
        if (!mergeItemStack(stack1, startPlayer, endPlayer, false)) {
          return StackHelper.empty();
        }
      } else if (!mergeItemStack(stack1, startPlayer, endHotbar, false)) {
        return StackHelper.empty();
      }

      if (StackHelper.getCount(stack1) == 0) {
        slot.putStack(StackHelper.empty());
      } else {
        slot.onSlotChanged();
      }

      if (StackHelper.getCount(stack1) == StackHelper.getCount(stack)) {
        return StackHelper.empty();
      }

      ContainerSL.onTakeFromSlot(slot, player, stack1);
    }

    return stack;
  }
}
