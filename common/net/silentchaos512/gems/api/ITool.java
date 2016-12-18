package net.silentchaos512.gems.api;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public interface ITool {

  public ItemStack constructTool(ItemStack rod, ItemStack... materials);
  public float getMeleeDamage(ItemStack tool);
  public float getMagicDamage(ItemStack tool);
  public float getBaseMeleeDamageModifier();
  public float getBaseMeleeSpeedModifier();

  public default boolean isDiggingTool() {

    return false;
  }

  @Deprecated
  public default Material[] getExtraEffectiveMaterials() {

    return new Material[] {};
  }

  public default Material[] getExtraEffectiveMaterials(ItemStack tool) {

    return new Material[] {};
  }
}
