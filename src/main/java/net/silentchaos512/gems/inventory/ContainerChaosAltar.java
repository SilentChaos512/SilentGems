package net.silentchaos512.gems.inventory;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.silentchaos512.lib.inventory.ContainerSL;
import net.silentchaos512.lib.util.StackHelper;

public class ContainerChaosAltar extends Container {

  private final IInventory tileAltar;

  public ContainerChaosAltar(InventoryPlayer playerInventory, IInventory altarInventory) {

    this.tileAltar = altarInventory;
    this.addSlotToContainer(new Slot(altarInventory, 0, 56, 35));
    this.addSlotToContainer(new SpecialSlot(altarInventory, 1, 111, 35)); // I added this small class to keep people
                                                                          // from putting stuff in the output slot - you
                                                                          // can remove if you want. -M4th
    this.addSlotToContainer(new Slot(altarInventory, 2, 83, 54));

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

    return tileAltar.isUsableByPlayer(player);
  }

  @Override
  public @Nonnull ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

    ItemStack stack = StackHelper.empty();
    Slot slot = (Slot) this.inventorySlots.get(slotIndex);

    if (slot != null && slot.getHasStack()) {
      ItemStack stack1 = slot.getStack();
      stack = StackHelper.safeCopy(stack1);

      if (slotIndex == 1) {
        if (!this.mergeItemStack(stack1, 2, 38, true)) {
          return StackHelper.empty();
        }

        slot.onSlotChange(stack1, stack);
      } else if (slotIndex != 0) {
        if (tileAltar.isItemValidForSlot(0, stack1)) {
          if (!this.mergeItemStack(stack1, 0, 1, false)) {
            return StackHelper.empty();
          }
        } else if (slotIndex >= 2 && slotIndex < 29) {
          if (!this.mergeItemStack(stack1, 29, 38, false)) {
            return StackHelper.empty();
          }
        } else if (slotIndex >= 29 && slotIndex < 38
            && !this.mergeItemStack(stack1, 2, 29, false)) {
          return StackHelper.empty();
        }
      } else if (!this.mergeItemStack(stack1, 2, 38, false)) {
        return StackHelper.empty();
      }

      if (StackHelper.isEmpty(stack1)) {
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
