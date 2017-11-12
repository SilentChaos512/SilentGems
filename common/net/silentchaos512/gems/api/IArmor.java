package net.silentchaos512.gems.api;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.lib.EnumToolType;

public interface IArmor {

  public ItemStack constructArmor(ItemStack frame, ItemStack... materials);

  public default EnumToolType getToolType() {

    return EnumToolType.ARMOR;
  }
}
