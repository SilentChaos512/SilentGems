package net.silentchaos512.gems.lib;

import net.minecraft.item.ItemStack;

public interface IGemItem {

  public int getGemId(ItemStack stack);
  public boolean isSupercharged(ItemStack stack);
  public EnumMaterialClass getGemMaterialClass(ItemStack stack);
}
