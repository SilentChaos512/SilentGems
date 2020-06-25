/*
 * Silent's Gems -- SoulUrnContainer
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

package net.silentchaos512.gems.block.urn;

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.silentchaos512.gems.init.GemsContainers;
import net.silentchaos512.lib.util.InventoryUtils;

public class SoulUrnContainer extends Container {
    final SoulUrnTileEntity tileEntity;
    private final int inventoryRows;

    public SoulUrnContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        this(id, playerInventory, new SoulUrnTileEntity());
    }

    @SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
    public SoulUrnContainer(int id, PlayerInventory playerInventory, SoulUrnTileEntity tileEntity) {
        super(GemsContainers.SOUL_URN.get(), id);
        this.tileEntity = tileEntity;
        this.inventoryRows = this.tileEntity.getSizeInventory() / 9;
        int playerSlotsOffset = (this.inventoryRows - 4) * 18;

        // Add urn slots
        for (int row = 0; row < this.inventoryRows; ++row) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new SlotSoulUrn(this.tileEntity, x + row * 9, 8 + x * 18, 18 + row * 18));
            }
        }

        // Add player/hotbar slots
        InventoryUtils.createPlayerSlots(playerInventory, 8, 103 + playerSlotsOffset).forEach(this::addSlot);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileEntity.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.inventoryRows * 9) {
                if (!this.mergeItemStack(itemstack1, this.inventoryRows * 9, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.inventoryRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public static class SlotSoulUrn extends Slot {
        public SlotSoulUrn(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            Block block = Block.getBlockFromItem(stack.getItem());
            return !(block instanceof ShulkerBoxBlock || block instanceof SoulUrnBlock);
        }
    }
}
