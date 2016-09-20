package net.silentchaos512.gems.api;

import net.minecraft.item.ItemStack;

public interface IAmmoTool {

  public int getAmmo(ItemStack tool);
  public int getMaxAmmo(ItemStack tool);
  public void addAmmo(ItemStack tool, int amount);
}
