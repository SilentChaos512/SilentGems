package net.silentchaos512.gems.item.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class GemContainer extends Container {
    private final ItemStack item;
    private final IItemHandler itemHandler;
    private final Class<? extends IContainerItem> containerItemClass;
    private int blocked = -1;

    public <T extends Item & IContainerItem> GemContainer(int id, PlayerInventory playerInventory, ContainerType<?> containerType, Class<T> containerItemClass) {
        super(containerType, id);
        this.item = getHeldContainerItem(playerInventory.player, containerItemClass);
        this.itemHandler = containerItemClass.cast(this.item.getItem()).getInventory(this.item);
        this.containerItemClass = containerItemClass;

        Predicate<ItemStack> validItems = s -> containerItemClass.cast(this.item.getItem()).canStore(s);

        for (int i = 0; i < this.itemHandler.getSlots(); ++i) {
            int x = 8 + 18 * (i % 9);
            int y = 18 + 18 * (i / 9);
            addSlot(new SlotItemHandler(itemHandler, i, x, y) {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return validItems.test(stack);
                }
            });
        }

        final int rowCount = this.itemHandler.getSlots() / 9;
        final int yOffset = (rowCount - 4) * 18;

        // Player inventory
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 103 + y * 18 + yOffset));
            }
        }

        // Hotbar
        for (int x = 0; x < 9; ++x) {
            Slot slot = addSlot(new Slot(playerInventory, x, 8 + x * 18, 161 + yOffset) {
                @Override
                public boolean canTakeStack(PlayerEntity playerIn) {
                    return slotNumber != blocked;
                }
            });

            if (x == playerInventory.currentItem && ItemStack.areItemStacksEqual(playerInventory.getCurrentItem(), this.item)) {
                blocked = slot.slotNumber;
            }
        }
    }

    private static ItemStack getHeldContainerItem(PlayerEntity player, Class<? extends Item> itemClass) {
        if (itemClass.isInstance(player.getHeldItemMainhand().getItem()))
            return player.getHeldItemMainhand();
        if (itemClass.isInstance(player.getHeldItemOffhand().getItem()))
            return player.getHeldItemOffhand();
        return ItemStack.EMPTY;
    }

    private boolean isContainerItem(ItemStack stack) {
        return this.containerItemClass.isInstance(stack.getItem());
    }

    public int getInventoryRows() {
        return this.itemHandler.getSlots() / 9;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        Slot slot = this.getSlot(index);

        if (!slot.canTakeStack(playerIn))
            return slot.getStack();

        if (index == blocked || !slot.getHasStack())
            return ItemStack.EMPTY;

        ItemStack stack = slot.getStack();
        ItemStack newStack = stack.copy();

        int containerSlots = itemHandler.getSlots();
        if (index < containerSlots) {
            if (!this.mergeItemStack(stack, containerSlots, this.inventorySlots.size(), true))
                return ItemStack.EMPTY;
            slot.onSlotChanged();
        } else if (!this.mergeItemStack(stack, 0, containerSlots, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty())
            slot.putStack(ItemStack.EMPTY);
        else
            slot.onSlotChanged();

        return slot.onTake(playerIn, newStack);
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        if (slotId < 0 || slotId > inventorySlots.size())
            return super.slotClick(slotId, dragType, clickTypeIn, player);

        Slot slot = inventorySlots.get(slotId);
        if (!canTake(slotId, slot, dragType, player, clickTypeIn))
            return slot.getStack();

        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        ((IContainerItem) item.getItem()).saveInventory(item, itemHandler, playerIn);
    }

    public boolean canTake(int slotId, Slot slot, int button, PlayerEntity player, ClickType clickType) {
        if (slotId == blocked || slotId <= itemHandler.getSlots() - 1 && isContainerItem(player.inventory.getItemStack()))
            return false;

        // Hotbar swapping via number keys
        if (clickType == ClickType.SWAP) {
            int hotbarId = itemHandler.getSlots() + 27 + button;
            // Block swapping with container
            if (blocked == hotbarId)
                return false;

            Slot hotbarSlot = getSlot(hotbarId);
            if (slotId <= itemHandler.getSlots() - 1)
                return !isContainerItem(slot.getStack()) && !isContainerItem(hotbarSlot.getStack());
        }

        return true;
    }
}
