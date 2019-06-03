package net.silentchaos512.gems.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.IItemHandler;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.quiver.IQuiver;
import net.silentchaos512.gems.item.quiver.ItemQuiver;

import javax.annotation.Nonnull;

/**
 * Lots of code borrowed/adapted from Iron Backpacks: {@link} <a href=
 * "https://github.com/gr8pefish/IronBackpacks/blob/dev-1.12/src/main/java/gr8pefish/ironbackpacks/container/ContainerBackpack.java">Iron
 * Backpacks' ContainerBackpack.java</a>
 *
 * @author Silent
 */
public class ContainerQuiver extends Container {
    @Nonnull
    private final ItemStack quiver;
    @Nonnull
    private final IItemHandler itemHandler;

    private int blocked = -1;

    public ContainerQuiver(@Nonnull ItemStack quiver, @Nonnull InventoryPlayer inventoryPlayer, @Nonnull EnumHand hand) {
        this.quiver = quiver;
        this.itemHandler = ModItems.quiver.getInventory(quiver);

        setupSlots(inventoryPlayer, itemHandler, hand);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        Slot slot = this.getSlot(slotIndex);

        if (!slot.canTakeStack(player))
            return slot.getStack();

        if (slotIndex == blocked)
            return ItemStack.EMPTY;

        if (!slot.getHasStack())
            return ItemStack.EMPTY;

        ItemStack stack = slot.getStack();
        ItemStack newStack = stack.copy();

        if (slotIndex < ItemQuiver.MAX_STACKS) {
            if (!this.mergeItemStack(stack, ItemQuiver.MAX_STACKS, this.inventorySlots.size(), true))
                return ItemStack.EMPTY;
            slot.onSlotChanged();
        } else if (!this.mergeItemStack(stack, 0, ItemQuiver.MAX_STACKS, false))
            return ItemStack.EMPTY;

        if (stack.isEmpty())
            slot.putStack(ItemStack.EMPTY);
        else
            slot.onSlotChanged();

        return slot.onTake(player, newStack);
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int button, ClickType flag, EntityPlayer player) {
        if (slotId < 0 || slotId > inventorySlots.size())
            return super.slotClick(slotId, button, flag, player);

        Slot slot = inventorySlots.get(slotId);
        if (!canTake(slotId, slot, button, player, flag))
            return slot.getStack();

        return super.slotClick(slotId, button, flag, player);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (!(quiver.getItem() instanceof IQuiver)) {
            SilentGems.logHelper.warn("Item is not a quiver? " + quiver.getItem());
            return;
        }
        ((IQuiver) quiver.getItem()).updateQuiver(quiver, itemHandler, playerIn);
    }

    public boolean canTake(int slotId, Slot slot, int button, EntityPlayer player, ClickType clickType) {
        if (slotId == blocked)
            return false;

        if (slotId <= ItemQuiver.MAX_STACKS - 1) {
            if (player.inventory.getItemStack().getItem() instanceof IQuiver)
                return false;
        }

        // Hotbar swapping via number keys
        if (clickType == ClickType.SWAP) {
            int hotbarId = ItemQuiver.MAX_STACKS + 27 + button;
            // Block swapping with quiver
            if (blocked == hotbarId)
                return false;

            Slot hotbarSlot = getSlot(hotbarId);
            if (slotId <= ItemQuiver.MAX_STACKS - 1) {
                if (slot.getStack().getItem() instanceof IQuiver
                        || hotbarSlot.getStack().getItem() instanceof IQuiver)
                    return false;
            }
        }

        return true;
    }

    private void setupSlots(InventoryPlayer inventoryPlayer, IItemHandler itemHandler, EnumHand hand) {
        setupQuiverSlots(itemHandler);
        setupPlayerSlots(inventoryPlayer, hand);
    }

    private void setupQuiverSlots(IItemHandler itemHandler) {
        int xOffset = 1 + getContainerInvXOffset();
        int yOffset = 1 + getBorderTop();
        for (int x = 0; x < ItemQuiver.MAX_STACKS; ++x) {
            addSlotToContainer(new SlotQuiver(itemHandler, x, xOffset + x * 18, yOffset));
        }
    }

    private void setupPlayerSlots(InventoryPlayer inventoryPlayer, EnumHand hand) {
        int xOffset = 1 + getPlayerInvXOffset();
        int yOffset = 1 + getBorderTop() + getContainerInvHeight() + getBufferInventory();

        // Inventory
        for (int y = 0; y < 3; y++, yOffset += 18)
            for (int x = 0; x < 9; x++)
                addSlotToContainer(new Slot(inventoryPlayer, x + y * 9 + 9, xOffset + x * 18, yOffset));

        // Hotbar
        yOffset += getBufferHotbar();
        for (int x = 0; x < 9; x++) {
            Slot slot = addSlotToContainer(new Slot(inventoryPlayer, x, xOffset + x * 18, yOffset) {

                @Override
                public boolean canTakeStack(final EntityPlayer playerIn) {

                    return slotNumber != blocked;
                }
            });
            if (x == inventoryPlayer.currentItem && hand == EnumHand.MAIN_HAND)
                blocked = slot.slotNumber;
        }
    }

    // GUI/slot setup helpers

    /**
     * Returns the size of the top border in pixels.
     */
    public int getBorderTop() {
        return 34;
    }

    /**
     * Returns the size of the side border in pixels.
     */
    public int getBorderSide() {
        return 7;
    }

    /**
     * Returns the size of the bottom border in pixels.
     */
    public int getBorderBottom() {
        return 7;
    }

    /**
     * Returns the space between container and player inventory in pixels.
     */
    public int getBufferInventory() {
        return 31;
    }

    /**
     * Returns the space between player inventory and hotbar in pixels.
     */
    public int getBufferHotbar() {
        return 4;
    }

    /**
     * Returns the total width of the container in pixels.
     */
    public int getWidth() {
        return ItemQuiver.MAX_STACKS * 18 + getBorderSide() * 2;
    }

    /**
     * Returns the total height of the container in pixels.
     */
    public int getHeight() {
        return getBorderTop() + (1 * 18) + getBufferInventory() + (4 * 18) + getBufferHotbar()
                + getBorderBottom();
    }

    /**
     * Returns the size of the container's width, only the inventory/slots, not the border, in
     * pixels.
     */
    public int getContainerInvWidth() {
        return ItemQuiver.MAX_STACKS * 18;
    }

    /**
     * Returns the size of the container's height, only the inventory/slots, not the border, in
     * pixels.
     */
    public int getContainerInvHeight() {
        return 1 * 18;
    }

    /**
     * Returns the size of the x offset for the backpack container in pixels.
     */
    public int getContainerInvXOffset() {
        return getBorderSide() + Math.max(0, (getPlayerInvWidth() - getContainerInvWidth()) / 2);
    }

    /**
     * Returns the size of the x offset for the player's inventory in pixels.
     */
    public int getPlayerInvXOffset() {
        return getBorderSide() + Math.max(0, (getContainerInvWidth() - getPlayerInvWidth()) / 2);
    }

    /**
     * Returns the size of the player's inventory width, not including the borders, in pixels.
     */
    public int getPlayerInvWidth() {
        return 9 * 18;
    }

    /**
     * Returns the size of the player's inventory height, including the hotbar, in pixels.
     */
    public int getPlayerInvHeight() {
        return 4 * 18 + getBufferHotbar();
    }
}
