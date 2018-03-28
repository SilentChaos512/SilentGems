package net.silentchaos512.gems.item.quiver;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.lib.util.StackHelper;

public interface IQuiver {

  public static final int MAX_STACKS = 4;
  public static final String NBT_INVENTORY = "inventory";

  public default IItemHandler getInventory(@Nonnull ItemStack stack) {

    if (StackHelper.isEmpty(stack)) {
      return null;
    }
    if (!stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound());
    }
    if (!stack.getTagCompound().hasKey(NBT_INVENTORY)) {
      stack.getTagCompound().setTag(NBT_INVENTORY, new NBTTagList());
    }

    ItemStackHandler stackHandler = new ItemStackHandler(MAX_STACKS);
    NBTTagList tagList = stack.getTagCompound().getTagList(NBT_INVENTORY, 10);
    for (int i = 0; i < tagList.tagCount(); ++i) {
      stackHandler.setStackInSlot(i, new ItemStack(tagList.getCompoundTagAt(i)));
    }

    return stackHandler;
  }

  public default ItemStack updateQuiver(@Nonnull ItemStack stack, IItemHandler itemHandler,
      EntityPlayer player) {

    if (!stack.hasTagCompound())
      stack.setTagCompound(new NBTTagCompound());

    boolean containsArrows = false;

    if (itemHandler != null) {
      NBTTagList invTag = new NBTTagList();
      for (int i = 0; i < itemHandler.getSlots(); i++) {
        ItemStack arrowStack = itemHandler.getStackInSlot(i);
        invTag.appendTag(arrowStack.serializeNBT());
        containsArrows |= StackHelper.isValid(arrowStack);
      }

      stack.getTagCompound().setTag(NBT_INVENTORY, invTag);

      // Update to empty/non-empty quiver item as needed.
      Item intendedItem = containsArrows ? ModItems.quiver : ModItems.quiverEmpty;
      if (stack.getItem() != intendedItem) {
        // Need to remove original quiver, replace with new one.
        ItemStack newQuiver = new ItemStack(intendedItem, 1);
        newQuiver.setTagCompound(stack.getTagCompound());
        int oldIndex = player.inventory.getSlotFor(stack);
        if (oldIndex >= 0) {
          //player.inventory.removeStackFromSlot(oldIndex);
          player.inventory.setInventorySlotContents(oldIndex, newQuiver);
        }
      }
    }

    return stack;
  }
}
