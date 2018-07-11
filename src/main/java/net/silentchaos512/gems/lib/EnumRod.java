package net.silentchaos512.gems.lib;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public enum EnumRod {

  WOOD("Wood", new ItemStack(Items.STICK)),
  BONE("Bone", new ItemStack(Items.BONE)),
  GOLD("Gold", null);

  public final String name;
  public final ItemStack craftingStack;

  private EnumRod(String name, ItemStack craftingStack) {

    this.name = name;
    this.craftingStack = craftingStack;
  }
}
