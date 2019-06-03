package net.silentchaos512.gems.api;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.item.tool.ItemGemSword;
import net.silentchaos512.gems.lib.EnumToolType;
import net.silentchaos512.gems.util.ToolHelper;

public interface ITool extends IGearItem {
    ItemStack constructTool(ItemStack rod, ItemStack... materials);

    default float getMeleeDamage(ItemStack tool) {
        return getMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool);
    }

    default float getMagicDamage(ItemStack tool) {
        return getMagicDamageModifier() + ToolHelper.getMagicDamage(tool);
    }

    float getMeleeDamageModifier();

    float getMagicDamageModifier();

    float getMeleeSpeedModifier();

    default float getHarvestSpeedMultiplier() {
        return 1.0f;
    }

    default float getDurabilityMultiplier() {
        return 1.0f;
    }

    default float getRepairMultiplier() {
        return 1.0f;
    }

    default boolean isDiggingTool() {
        return false;
    }

    @Deprecated
    default boolean isSuperTool() {
        return false;
    }

    @Deprecated
    default Material[] getExtraEffectiveMaterials() {
        return new Material[]{};
    }

    default Material[] getExtraEffectiveMaterials(ItemStack tool) {
        return new Material[]{};
    }

    @Override
    default EnumToolType getToolType() {
        if (this instanceof ItemSword)
            return EnumToolType.SWORD;
        if (this instanceof ItemTool)
            return EnumToolType.HARVEST;
        if (this instanceof ItemBow)
            return EnumToolType.BOW;

        return EnumToolType.NONE;
    }

    default boolean isDigger(ItemStack stack) {
        return this instanceof ItemTool;
    }

    default boolean isCaster(ItemStack stack) {
        return this instanceof ItemGemSword && ToolHelper.getToolTier(stack).ordinal() >= EnumMaterialTier.SUPER.ordinal();
    }
}
