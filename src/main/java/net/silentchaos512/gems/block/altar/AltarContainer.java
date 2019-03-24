package net.silentchaos512.gems.block.altar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.silentchaos512.lib.inventory.SlotOutputOnly;

import javax.annotation.Nonnull;

public class AltarContainer extends Container {
    private final AltarTileEntity altar;

    public AltarContainer(InventoryPlayer inventoryPlayer, AltarTileEntity altar) {
        this.altar = altar;
        this.setupSlots(inventoryPlayer);
    }

    private void setupSlots(InventoryPlayer inventoryPlayer) {
        this.addSlot(new Slot(this.altar, 0, 56, 35));
        this.addSlot(new Slot(this.altar, 1, 83, 54));
        this.addSlot(new SlotOutputOnly(this.altar, 2, 111, 35));

        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.altar.isUsableByPlayer(playerIn);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if (slotIndex == 1) {
                if (!this.mergeItemStack(stack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(stack1, stack);
            } else if (slotIndex != 0) {
                if (this.altar.isItemValidForSlot(0, stack1)) {
                    if (!this.mergeItemStack(stack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 2 && slotIndex < 29) {
                    if (!this.mergeItemStack(stack1, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 29 && slotIndex < 38
                        && !this.mergeItemStack(stack1, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack1, 2, 38, false)) {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack1);
        }

        return stack;
    }
}
