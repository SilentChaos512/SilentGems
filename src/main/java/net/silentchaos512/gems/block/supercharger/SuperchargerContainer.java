/*
 * Silent's Gems -- SuperchargerContainer
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.silentchaos512.gems.block.supercharger;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.init.GemsContainers;
import net.silentchaos512.lib.inventory.SlotOutputOnly;
import net.silentchaos512.lib.util.InventoryUtils;

import javax.annotation.Nonnull;

public class SuperchargerContainer extends Container {
    final SuperchargerTileEntity tileEntity;

    public SuperchargerContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new SuperchargerTileEntity());
    }

    @SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
    public SuperchargerContainer(int id, PlayerInventory playerInventory, SuperchargerTileEntity tileEntity) {
        super(GemsContainers.SUPERCHARGER.type(), id);
        this.tileEntity = tileEntity;

        this.addSlot(new Slot(this.tileEntity, 0, 56, 25));
        this.addSlot(new Slot(this.tileEntity, 1, 56, 45));
        this.addSlot(new SlotOutputOnly(this.tileEntity, 2, 115, 35));

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);

        trackIntArray(this.tileEntity.fields);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileEntity.isUsableByPlayer(playerIn);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            final int playerStart = 3;
            final int playerEnd = playerStart + 36;

            if (slotIndex == 2) {
                // Transfer from output slot
                if (!this.mergeItemStack(stack1, playerStart, playerEnd, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack1, stack);
            } else if (slotIndex != 0 && slotIndex != 1) {
                // Transfer from player...
                if (this.tileEntity.isItemValidForSlot(0, stack1)) {
                    // to input slot
                    if (!this.mergeItemStack(stack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.tileEntity.isItemValidForSlot(1, stack1)) {
                    // to catalyst slot
                    if (!this.mergeItemStack(stack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else {
                    // Move from hotbar to backpack or vice versa
                    final int hotbarStart = playerStart + 27;
                    if (slotIndex < hotbarStart) {
                        if (!this.mergeItemStack(stack1, hotbarStart, playerEnd, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (slotIndex < playerEnd && !this.mergeItemStack(stack1, playerStart, hotbarStart, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(stack1, playerStart, playerEnd, false)) {
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
