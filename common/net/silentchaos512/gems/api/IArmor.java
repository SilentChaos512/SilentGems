package net.silentchaos512.gems.api;

import net.minecraft.item.ItemStack;

public interface IArmor {

  public ItemStack constructArmor(ItemStack... materials);
}
