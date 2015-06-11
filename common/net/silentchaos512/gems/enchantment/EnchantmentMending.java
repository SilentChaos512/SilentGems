package net.silentchaos512.gems.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.silentchaos512.gems.core.util.InventoryHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.lib.Names;

public class EnchantmentMending extends Enchantment {

  // The chance of the nth enchantment level repairing the tool each tick.
  private final static int[] rates = { 12, 10, 8, 6, 4, 2, 1, 1, 1, 1 };

  protected EnchantmentMending(int par1, int par2, EnumEnchantmentType par3EnumEnchantmentType) {

    super(par1, par2, par3EnumEnchantmentType);
    setName(Names.MENDING);
  }

  @Override
  public boolean canApply(ItemStack stack) {

    // TODO: Fix this so mending books can be applied to non-gem tools?
    if (InventoryHelper.isGemTool(stack) || stack.getItem() instanceof ItemBook) {
      return stack.isItemStackDamageable() ? true : super.canApply(stack);
    }

    return false;
  }

  @Override
  public int getMinEnchantability(int par1) {

    return 5 + (par1 - 1) * 8;
  }

  @Override
  public int getMaxEnchantability(int par1) {

    return super.getMinEnchantability(par1) + 50;
  }

  @Override
  public int getMaxLevel() {

    return 5;
  }

  @Override
  public String getTranslatedName(int par1) {

    return StatCollector.translateToLocal("enchantment." + Names.MENDING) + " "
        + StatCollector.translateToLocal("enchantment.level." + par1);
  }

  public void tryActivate(EntityPlayer player, ItemStack itemStack) {

    if (itemStack.getItemDamage() == 0 || !itemStack.isItemStackDamageable()) {
      return;
    }

    // Get enchantment level. Only evaluate if level is between 1 and 10, inclusive.
    int lvl = EnchantmentHelper.getEnchantmentLevel(this.effectId, itemStack);
    if (lvl < 1) {
      return;
    }
    if (lvl > 10) {
      lvl = 10;
    }

    if (player.worldObj.rand.nextInt(rates[lvl - 1]) == 0) {
      itemStack.setItemDamage(itemStack.getItemDamage() - 1);
    }
  }
}
