package net.silentchaos512.gems.item.tool;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.RecipeMaker;

public class ItemGemKatana extends ItemGemSword {
    @Override
    public ConfigOptionToolClass getConfig() {
        return GemsConfig.katana;
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
    public float getMeleeDamageModifier() {
        return 2.0f;
    }

    @Override
    public float getMagicDamageModifier() {
        return 3.0f;
    }

    @Override
    public float getMeleeSpeedModifier() {
        return -2.2f;
    }

    @Override
    public float getDurabilityMultiplier() {
        return 0.75f;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSuperTool() {
        return true;
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        if (!getConfig().isDisabled)
            ToolHelper.addExampleRecipe(this, "hh", "h ", "r ");
    }
}
