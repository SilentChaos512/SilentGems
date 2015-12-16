package net.silentchaos512.gems.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.StatCollector;
import net.silentchaos512.gems.lib.Names;

public class EnchantmentLifeSteal extends Enchantment {

  protected EnchantmentLifeSteal(int effectId, int weight, EnumEnchantmentType type) {

    super(effectId, weight, type);
    setName(Names.LIFE_STEAL);
  }

  @Override
  public boolean canApply(ItemStack stack) {

    Item item = stack.getItem();
    if (item.isDamageable() && (item instanceof ItemSword || item instanceof ItemBook)) {
      return stack.isItemStackDamageable() ? true : super.canApply(stack);
    }

    return false;
  }

  @Override
  public int getMinEnchantability(int p_77321_1_) {

    return 15 + (p_77321_1_ - 1) * 9;
  }

  @Override
  public int getMaxEnchantability(int p_77317_1_) {

    return super.getMinEnchantability(p_77317_1_) + 50;
  }

  @Override
  public int getMaxLevel() {

    return 2;
  }
  
  @Override
  public String getTranslatedName(int par1) {

    return StatCollector.translateToLocal("enchantment." + Names.LIFE_STEAL) + " "
        + StatCollector.translateToLocal("enchantment.level." + par1);
  }
}
