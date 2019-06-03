package net.silentchaos512.gems.api.tool.part;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;

public interface IPartProperties {
    String getName();

    String getNamePrefix();

    int getColor();

    EnumMaterialTier getTier();

    int getDurability();

    float getMiningSpeed(); // Harvest speed

    int getHarvestLevel();

    float getMeleeDamage();

    float getMagicDamage();

    float getMeleeSpeed();

    int getEnchantability();

    float getChargeSpeed();

    float getProtection();

    ItemStack getCraftingStack();

    String getCraftingOreName();
}
