package net.silentchaos512.gems.compat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.function.Predicate;

public class BaublesCompat {
    public static final String MOD_ID = "baubles";

    public static boolean MOD_LOADED = false;

    public static NonNullList<ItemStack> getBaubles(PlayerEntity player, Predicate<ItemStack> predicate) {
        NonNullList<ItemStack> list = NonNullList.create();

        if (!MOD_LOADED)
            return list;

        // FIXME
//        IBaublesItemHandler inv = BaublesApi.getBaublesHandler(player);
//
//        for (int i = 0; i < inv.getSlots(); ++i) {
//            ItemStack stack = inv.getStackInSlot(i);
//            if (!stack.isEmpty() && predicate.test(stack)) {
//                list.add(stack);
//            }
//        }

        return list;
    }
}
