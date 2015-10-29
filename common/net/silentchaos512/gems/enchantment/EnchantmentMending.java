package net.silentchaos512.gems.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.silentchaos512.gems.core.util.InventoryHelper;
import net.silentchaos512.gems.item.armor.ArmorSG;
import net.silentchaos512.gems.lib.Names;

public class EnchantmentMending extends Enchantment {

  // The chance of the nth enchantment level repairing the tool each second, at y=256.
  private final static int[] rates = { 64, 48, 32, 24, 16, 12, 8, 4, 2, 1 };

  protected EnchantmentMending(int par1, int par2, EnumEnchantmentType par3EnumEnchantmentType) {

    super(par1, par2, par3EnumEnchantmentType);
    setName(Names.MENDING);
  }

  @Override
  public boolean canApply(ItemStack stack) {

    // TODO: Fix this so mending books can be applied to non-gem tools?
    if (stack.getItem().isDamageable() && (InventoryHelper.isGemTool(stack)
        || stack.getItem() instanceof ArmorSG || stack.getItem() instanceof ItemBook)) {
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

  // I'm using a quadratic equation for the elevation bonus multiplier.
  private static final float A = -1f / (256f * 256f);
  private static final float B = 1f / 128f;

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

    // Calculate repair chance with elevation bonus.
    float x = (float) player.posY;
    x = MathHelper.clamp_float(x, 1f, 256f);
    float elevationMulti = x * (A * x + B);
    int chance = (int) (rates[lvl - 1] / elevationMulti);
    // LogHelper.debug(elevationMulti + ", " + chance);

    if (player.worldObj.rand.nextInt(chance) == 0) {
      itemStack.setItemDamage(itemStack.getItemDamage() - 1);
    }
  }
}
