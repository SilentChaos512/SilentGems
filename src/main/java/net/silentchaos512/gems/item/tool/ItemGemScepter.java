package net.silentchaos512.gems.item.tool;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.RecipeMaker;

public class ItemGemScepter extends ItemGemSword {
    @Override
    public ConfigOptionToolClass getConfig() {
        return GemsConfig.scepter;
    }

    @Override
    public ItemStack constructTool(ItemStack rod, ItemStack... materials) {
        if (getConfig().isDisabled) return ItemStack.EMPTY;

        if (materials.length >= 2) {
            ItemStack temp = materials[0];
            materials[0] = materials[1];
            materials[1] = temp;
        }
        return ToolHelper.constructTool(this, rod, materials);
    }

    @Override
    public float getMeleeDamageModifier() {
        return 1.0f;
    }

    @Override
    public float getMagicDamageModifier() {
        return 5.0f;
    }

    @Override
    public float getMeleeSpeedModifier() {
        return -3.2f;
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
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;
        list.addAll(ToolHelper.getSubItems(this, 5));
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        if (!getConfig().isDisabled)
            ToolHelper.addExampleRecipe(this, " h ", "hrh", "hrh");
    }
}
