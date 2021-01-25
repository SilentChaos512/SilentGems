package net.silentchaos512.gems.item.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public interface IContainerItem {
    int getInventorySize(ItemStack stack);

    boolean canStore(ItemStack stack);

    default IItemHandler getInventory(ItemStack stack) {
        ItemStackHandler stackHandler = new ItemStackHandler((getInventorySize(stack)));
        stackHandler.deserializeNBT(stack.getOrCreateTag().getCompound("Inventory"));
        return stackHandler;
    }

    default void saveInventory(ItemStack stack, IItemHandler itemHandler, PlayerEntity player) {
        if (itemHandler != null && itemHandler instanceof ItemStackHandler) {
            stack.getOrCreateTag().put("Inventory", ((ItemStackHandler) itemHandler).serializeNBT());
        }
    }
}
