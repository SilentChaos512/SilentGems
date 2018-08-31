package net.silentchaos512.gems.item.tool;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.RecipeMaker;

public class ItemGemDagger extends ItemGemSword {
    public ConfigOptionToolClass getConfig() {
        return GemsConfig.dagger;
    }

    @Override
    public ItemStack constructTool(ItemStack rod, ItemStack... materials) {
        if (getConfig().isDisabled) return ItemStack.EMPTY;
        return ToolHelper.constructTool(this, rod, materials);
    }

    @Override
    public ItemStack constructTool(boolean supercharged, ItemStack... materials) {
        if (getConfig().isDisabled) return ItemStack.EMPTY;
        ItemStack rod = supercharged ? CraftingItems.ORNATE_GOLD_ROD.getStack() : new ItemStack(Items.STICK);
        return ToolHelper.constructTool(this, rod, materials);
    }

    @Override
    public float getMeleeDamage(ItemStack tool) {
        return (getMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool)) / 2;
    }

    @Override
    public float getMagicDamage(ItemStack tool) {
        return 1.0f + ToolHelper.getMagicDamage(tool);
    }

    @Override
    public float getMeleeDamageModifier() {
        return 1.0f;
    }

    @Override
    public float getMagicDamageModifier() {
        return 1.0f;
    }

    @Override
    public float getMeleeSpeedModifier() {
        return -1.4f;
    }

    @Override
    public float getDurabilityMultiplier() {
        return 1.0f;
    }

    @Override
    public float getRepairMultiplier() {
        return 2.0f;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.hurtResistantTime *= 0.67f; // Make target vulnerable sooner.
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        if (!getConfig().isDisabled)
            ToolHelper.addExampleRecipe(this, "h", "r");
    }
}
