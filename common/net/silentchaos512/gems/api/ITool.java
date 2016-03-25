package net.silentchaos512.gems.api;

import net.minecraft.item.ItemStack;

public interface ITool {

  public ItemStack constructTool(ItemStack rod, ItemStack... materials);
  public float getMeleeDamage(ItemStack tool);
  public float getMagicDamage(ItemStack tool);
  public float getBaseMeleeDamageModifier();
  public float getBaseMeleeSpeedModifier();
}
