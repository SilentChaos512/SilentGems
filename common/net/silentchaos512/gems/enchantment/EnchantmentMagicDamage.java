package net.silentchaos512.gems.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.item.tool.ItemGemSword;
import net.silentchaos512.gems.util.ToolHelper;

public class EnchantmentMagicDamage extends Enchantment {

  public static final String NAME = "MagicDamage";

  public EnchantmentMagicDamage() {

    super(Rarity.UNCOMMON, EnumEnchantmentType.WEAPON,
        new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
    setName(NAME);
    setRegistryName(NAME);
  }

  public float calcDamage(int level) {

    return 1f + Math.max(0, level - 1) / 2f;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {

    return stack.getItem() instanceof ItemGemSword
        && ToolHelper.getToolTier(stack).ordinal() >= EnumMaterialTier.SUPER.ordinal()
        && super.canApplyAtEnchantingTable(stack);
  }

  @Override
  public boolean canApplyTogether(Enchantment ench) {

    return !(ench instanceof EnchantmentDamage || ench == this);
  }

  @Override
  public int getMinEnchantability(int level) {

    return 5 + (level - 1) * 10;
  }

  @Override
  public int getMaxEnchantability(int level) {

    return getMinEnchantability(level) + 20;
  }

  @Override
  public int getMaxLevel() {

    return 5;
  }

  @Override
  public String getName() {

    return "enchantment.silentgems:" + NAME;
  }
}
