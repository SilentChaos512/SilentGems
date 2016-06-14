package net.silentchaos512.gems.api;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.util.ToolHelper;

public interface ITool {

  public ItemStack constructTool(ItemStack rod, ItemStack... materials);
  public float getMeleeDamage(ItemStack tool);
  public float getMagicDamage(ItemStack tool);
  public float getBaseMeleeDamageModifier();
  public float getBaseMeleeSpeedModifier();

  public default boolean isDiggingTool() {

    return false;
  }

  public default Material[] getExtraEffectiveMaterials() {

    return new Material[] {};
  }
}
