package net.silentchaos512.gems.compat;

import com.google.common.base.Predicate;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BaublesCompat {

  public static NonNullList<ItemStack> getBaubles(EntityPlayer player,
      Predicate<ItemStack> predicate) {

    NonNullList<ItemStack> list = NonNullList.create();
    IBaublesItemHandler inv = BaublesApi.getBaublesHandler(player);

    for (int i = 0; i < inv.getSlots(); ++i) {
      ItemStack stack = inv.getStackInSlot(i);
      if (!stack.isEmpty() && predicate.apply(stack)) {
        list.add(stack);
      }
    }

    return list;
  }
}
