package net.silentchaos512.gems.api;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

  public default boolean isSuperTool() {

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
