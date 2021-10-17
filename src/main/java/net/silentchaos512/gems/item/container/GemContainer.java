package net.silentchaos512.gems.item.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class GemContainer extends AbstractContainerMenu {
    private final ItemStack item;
    private final IItemHandler itemHandler;
    private final Class<? extends IContainerItem> containerItemClass;
    private int blocked = -1;

    public <T extends Item & IContainerItem> GemContainer(int id, Inventory playerInventory, MenuType<?> containerType, Class<T> containerItemClass) {
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
                public boolean mayPlace(@Nonnull ItemStack stack) {
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
                public boolean mayPickup(Player playerIn) {
                    return index != blocked;
                }
            });

            if (x == playerInventory.selected && ItemStack.matches(playerInventory.getSelected(), this.item)) {
                blocked = slot.index;
            }
        }
    }

    private static ItemStack getHeldContainerItem(Player player, Class<? extends Item> itemClass) {
        if (itemClass.isInstance(player.getMainHandItem().getItem()))
            return player.getMainHandItem();
        if (itemClass.isInstance(player.getOffhandItem().getItem()))
            return player.getOffhandItem();
        return ItemStack.EMPTY;
    }

    private boolean isContainerItem(ItemStack stack) {
        return this.containerItemClass.isInstance(stack.getItem());
    }

    public int getInventoryRows() {
        return this.itemHandler.getSlots() / 9;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot slot = this.getSlot(index);

        if (!slot.mayPickup(playerIn))
            return slot.getItem();

        if (index == blocked || !slot.hasItem())
            return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack newStack = stack.copy();

        int containerSlots = itemHandler.getSlots();
        if (index < containerSlots) {
            if (!this.moveItemStackTo(stack, containerSlots, this.slots.size(), true))
                return ItemStack.EMPTY;
            slot.setChanged();
        } else if (!this.moveItemStackTo(stack, 0, containerSlots, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty())
            slot.set(ItemStack.EMPTY);
        else
            slot.setChanged();

        slot.onTake(playerIn, newStack);
        return newStack;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId < 0 || slotId > slots.size()) {
            super.clicked(slotId, dragType, clickTypeIn, player);
            return;
        }

        Slot slot = slots.get(slotId);
        if (!canTake(slotId, slot, dragType, player, clickTypeIn)) {
            return;
        }

        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        ((IContainerItem) item.getItem()).saveInventory(item, itemHandler, playerIn);
    }

    public boolean canTake(int slotId, Slot slot, int button, Player player, ClickType clickType) {
        if (slotId == blocked)
            return false;

        // Hotbar swapping via number keys
        if (clickType == ClickType.SWAP) {
            int hotbarId = itemHandler.getSlots() + 27 + button;
            // Block swapping with container
            if (blocked == hotbarId)
                return false;

            Slot hotbarSlot = getSlot(hotbarId);
            if (slotId <= itemHandler.getSlots() - 1)
                return !isContainerItem(slot.getItem()) && !isContainerItem(hotbarSlot.getItem());
        }

        return true;
    }
}
