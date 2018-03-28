package net.silentchaos512.gems.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.silentchaos512.gems.item.quiver.IQuiver;

public class SlotQuiver extends SlotItemHandler {

  public SlotQuiver(IItemHandler itemHandler, int index, int xPosition, int yPosition) {

    super(itemHandler, index, xPosition, yPosition);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {

    if (!super.isItemValid(stack)) {
      return false;
    }
    return stack.getItem() instanceof ItemArrow && !(stack.getItem() instanceof IQuiver);
  }
}
