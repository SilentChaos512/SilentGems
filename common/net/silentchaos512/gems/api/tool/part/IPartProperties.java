package net.silentchaos512.gems.api.tool.part;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;

public interface IPartProperties {

  public String getName();

  public String getNamePrefix();

  public int getColor();

  public EnumMaterialTier getTier();

  public int getDurability();

  public float getMiningSpeed(); // Harvest speed

  public int getHarvestLevel();

  public float getMeleeDamage();

  public float getMagicDamage();

  public float getMeleeSpeed();

  public int getEnchantability();

  public float getChargeSpeed();

  public float getProtection();

  public ItemStack getCraftingStack();

  public String getCraftingOreName();
}
