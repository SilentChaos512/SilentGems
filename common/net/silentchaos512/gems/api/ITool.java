package net.silentchaos512.gems.api;

import java.util.Set;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.config.ConfigOptionToolClass;

public interface ITool {

  public ItemStack constructTool(ItemStack rod, ItemStack... materials);

  public float getMeleeDamage(ItemStack tool);

  public float getMagicDamage(ItemStack tool);

  public float getBaseMeleeDamageModifier();

  public float getBaseMeleeSpeedModifier();

  public default float getHarvestSpeedMultiplier() {

    return 1.0f;
  }

  public default float getDurabilityMultiplier() {

    return 1.0f;
  }

  public default float getRepairMultiplier() {

    return 1.0f;
  }

  public default boolean isDiggingTool() {

    return false;
  }

  public ConfigOptionToolClass getConfig();

  @Deprecated
  public default boolean isSuperTool() {

    return false;
  }

  public default Set<EnumMaterialTier> getValidTiers() {

    return getConfig().validTiers;
  }

  @Deprecated
  public default Material[] getExtraEffectiveMaterials() {

    return new Material[] {};
  }

  public default Material[] getExtraEffectiveMaterials(ItemStack tool) {

    return new Material[] {};
  }
}
