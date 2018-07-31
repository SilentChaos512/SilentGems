package net.silentchaos512.gems.compat;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.google.common.base.Predicate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.silentchaos512.lib.collection.ItemStackList;
import net.silentchaos512.lib.util.StackHelper;

public class BaublesCompat {

  public static final String MOD_ID = "baubles";

  public static boolean MOD_LOADED = false;

  public static ItemStackList getBaubles(EntityPlayer player, Predicate<ItemStack> predicate) {

    ItemStackList list = ItemStackList.create();

    if (!MOD_LOADED)
      return list;

    IBaublesItemHandler inv = BaublesApi.getBaublesHandler(player);

    for (int i = 0; i < inv.getSlots(); ++i) {
      ItemStack stack = inv.getStackInSlot(i);
      if (StackHelper.isValid(stack) && predicate.apply(stack)) {
        list.add(stack);
      }
    }

    return list;
  }
}
