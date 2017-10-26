package net.silentchaos512.gems.api.tool.part;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.tool.ToolStats;

public class ToolPartRod extends ToolPart {

  protected float durabilityMulti = 1.0f;
  protected float harvestSpeedMulti = 1.0f;
  protected float meleeDamageMulti = 1.0f;
  protected float magicDamageMulti = 1.0f;
  protected float enchantabilityMulti = 1.0f;

  public ToolPartRod(String key, ItemStack craftingStack) {

    super(key, craftingStack);
  }

  public ToolPartRod(String key, ItemStack craftingStack, String oreName) {

    super(key, craftingStack, oreName);
  }

  public ToolPartRod(String key, ItemStack craftingStack, float durabilityMulti,
      float harvestSpeedMutli, float meleeDamageMulti, float magicDamageMulti, float enchantabilityMulti) {

    this(key, craftingStack);
    this.durabilityMulti = durabilityMulti;
    this.harvestSpeedMulti = harvestSpeedMutli;
    this.meleeDamageMulti = meleeDamageMulti;
    this.magicDamageMulti = magicDamageMulti;
    this.enchantabilityMulti = enchantabilityMulti;
  }

  public ToolPartRod(String key, ItemStack craftingStack, String oreName, float durabilityMulti,
      float harvestSpeedMutli, float meleeDamageMulti, float magicDamageMulti, float enchantabilityMulti) {

    this(key, craftingStack, oreName);
    this.durabilityMulti = durabilityMulti;
    this.harvestSpeedMulti = harvestSpeedMutli;
    this.meleeDamageMulti = meleeDamageMulti;
    this.magicDamageMulti = magicDamageMulti;
    this.enchantabilityMulti = enchantabilityMulti;
  }

  @Override
  public void applyStats(ToolStats stats) {

    // Override if rod needs more multipliers or different logic
    stats.durability *= durabilityMulti;
    stats.harvestSpeed *= harvestSpeedMulti;
    stats.meleeDamage *= meleeDamageMulti;
    stats.magicDamage *= magicDamageMulti;
    stats.enchantability *= enchantabilityMulti;
  }

  @Override
  public int getDurability() {

    return 0;
  }

  @Override
  public float getHarvestSpeed() {

    return 0;
  }

  @Override
  public int getHarvestLevel() {

    return 0;
  }

  @Override
  public float getMeleeDamage() {

    return 0;
  }

  @Override
  public float getMagicDamage() {

    return 0;
  }

  @Override
  public int getEnchantability() {

    return 0;
  }

  @Override
  public float getMeleeSpeed() {

    return 1.0f;
  }

  @Override
  public float getChargeSpeed() {

    return 0;
  }

  @Override
  public final float getProtection() {

    return 0;
  }

  @Override
  public boolean validForPosition(ToolPartPosition pos) {

    return pos == ToolPartPosition.ROD;
  }

  @Override
  public boolean validForToolOfTier(EnumMaterialTier toolTier) {

    switch (toolTier) {
      case MUNDANE:
      case REGULAR:
        return tier == EnumMaterialTier.MUNDANE || tier == EnumMaterialTier.REGULAR;
      case SUPER:
        return tier == EnumMaterialTier.REGULAR || tier == EnumMaterialTier.SUPER;
//      case HYPER:
//        return tier == EnumMaterialTier.SUPER || tier == EnumMaterialTier.HYPER;
      default:
        return true;
    }
  }

  public boolean supportsDecoration() {

    return tier.ordinal() >= EnumMaterialTier.SUPER.ordinal();
  }
}
