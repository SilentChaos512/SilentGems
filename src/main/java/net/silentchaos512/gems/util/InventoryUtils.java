package net.silentchaos512.gems.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public final class InventoryUtils {
    private InventoryUtils() {throw new IllegalAccessError("Utility class");}

    /**
     * Gets the total number of matching items in all slots in the inventory.
     *
     * @param inventory  The inventory
     * @param ingredient The items to match ({@link net.minecraft.item.crafting.Ingredient}, etc.)
     * @return The number of items in all matching item stacks
     */
    public static int getTotalCount(IInventory inventory, Predicate<ItemStack> ingredient) {
        int total = 0;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    /**
     * Consumes (removes) items from the inventory. This is useful for machines, which may have
     * multiple input slots and recipes that consume multiple of one item.
     *
     * @param inventory The inventory
     * @param ingredient The items to match ({@link net.minecraft.item.crafting.Ingredient}, etc.)
     * @param amount The total number of items to remove
     */
    public static void consumeItems(IInventory inventory, Predicate<ItemStack> ingredient, int amount) {
        int amountLeft = amount;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                int toRemove = Math.min(amountLeft, stack.getCount());

                stack.shrink(toRemove);
                if (stack.isEmpty()) {
                    inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }

                amountLeft -= toRemove;
                if (amountLeft == 0) {
                    return;
                }
            }
        }
    }
}

