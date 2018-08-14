package net.silentchaos512.gems.item.tool;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.StackHelper;

import java.util.Set;

public class ItemGemPaxel extends ItemGemPickaxe {

    public static final Material[] EFFECTIVE_MATERIALS = constructMaterialArray();

    public ItemGemPaxel() {

        setTranslationKey(SilentGems.RESOURCE_PREFIX + Names.PAXEL);
        materialLength = 6;
    }

    private static Material[] constructMaterialArray() {

        Set<Material> set = Sets.newConcurrentHashSet();
        set.addAll(ItemGemPickaxe.BASE_EFFECTIVE_MATERIALS);
        set.addAll(ItemGemShovel.BASE_EFFECTIVE_MATERIALS);
        set.addAll(ItemGemAxe.BASE_EFFECTIVE_MATERIALS);
        for (Material mat : ItemGemPickaxe.EXTRA_EFFECTIVE_MATERIALS)
            set.add(mat);
        for (Material mat : ItemGemAxe.EXTRA_EFFECTIVE_MATERIALS)
            set.add(mat);
        return set.toArray(new Material[set.size()]);
    }

    public ConfigOptionToolClass getConfig() {

        return GemsConfig.paxel;
    }

    @Override
    public ItemStack constructTool(boolean supercharged, ItemStack material) {

        return constructTool(supercharged, material, material, material, material, material);
    }

    @Override
    public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

        if (getConfig().isDisabled)
            return StackHelper.empty();
        return ToolHelper.constructTool(this, rod, materials);
    }

    @Override
    public float getMeleeDamageModifier() {

        return 2.0f;
    }

    @Override
    public float getMeleeSpeedModifier() {

        return -3.2f;
    }

    @Override
    public float getHarvestSpeedMultiplier() {

        return 0.75f;
    }

    @Override
    public float getDurabilityMultiplier() {

        return 1.25f;
    }

    @Override
    public float getRepairMultiplier() {

        return 0.70f;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSuperTool() {

        return true;
    }

    @Override
    public Material[] getExtraEffectiveMaterials(ItemStack tool) {

        return ToolHelper.isBroken(tool) ? new Material[]{} : EFFECTIVE_MATERIALS;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {

        return ToolHelper.getDigSpeed(stack, state, EFFECTIVE_MATERIALS);
    }

    @Override
    protected boolean canHarvestBlock(IBlockState state, int toolLevel) {

        // Wrong harvest level?
        if (state.getBlock().getHarvestLevel(state) > toolLevel)
            return false;
        // Material in list?
        for (Material mat : EFFECTIVE_MATERIALS)
            if (mat.equals(state.getMaterial()))
                return true;

        return super.canHarvestBlock(state, toolLevel);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {

        return ToolHelper.isBroken(stack) ? ImmutableSet.of()
                : ImmutableSet.of("pickaxe", "shovel", "axe");
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {

        if (!getConfig().isDisabled)
            ToolHelper.addExampleRecipe(this, "hhh", "hrh", "hr ");
    }

    @Override
    public String getName() {

        return Names.PAXEL;
    }
}
