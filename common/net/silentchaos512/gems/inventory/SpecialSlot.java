package net.silentchaos512.gems.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SpecialSlot extends Slot {

  public SpecialSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {

    return false;
  }
}
