package net.silentchaos512.gems.block.purifier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.silentchaos512.gems.init.GemsContainers;
import net.silentchaos512.lib.util.InventoryUtils;

import javax.annotation.Nonnull;

public class PurifierContainer extends Container {
    private final IIntArray fields;

    public PurifierContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new PurifierTileEntity(), new IntArray(2));
    }

    public PurifierContainer(int id, PlayerInventory playerInventory, PurifierTileEntity tileEntity, IIntArray fields) {
        super(GemsContainers.PURIFIER.type(), id);
        this.fields = fields;

        trackIntArray(this.fields);

        this.addSlot(new Slot(tileEntity, 0, 80, 35) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return PurifierTileEntity.isPurifyingCatalyst(stack);
            }
        });

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);
    }

    public int getRemainingBurnTime() {
        return fields.get(0);
    }

    public int getTotalBurnTime() {
        return fields.get(1);
    }

    public int getChaosDissipated() {
        return getRemainingBurnTime() > 0 ? 100 : 0;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if (slotIndex != 0) {
                if (PurifierTileEntity.isPurifyingCatalyst(stack1)) {
                    if (!this.mergeItemStack(stack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 1 && slotIndex < 28) {
                    if (!this.mergeItemStack(stack1, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 28 && slotIndex < 37
                        && !this.mergeItemStack(stack1, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack1, 1, 37, false)) {
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
