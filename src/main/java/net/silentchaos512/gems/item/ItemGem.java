package net.silentchaos512.gems.item;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.init.ModEnchantments;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;

import java.util.List;

public class ItemGem extends Item implements IAddRecipes, ICustomModel {
    public ItemGem() {
        setHasSubtypes(true);
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        EnumGem gem = EnumGem.getFromStack(stack);
        boolean controlDown = KeyTracker.isControlDown();

        if (controlDown && (gem == EnumGem.RUBY || gem == EnumGem.BERYL || gem == EnumGem.SAPPHIRE || gem == EnumGem.TOPAZ)) {
            list.add(SilentGems.i18n.subText(this, "original4"));
        }
    }

    @Override
    public void addOreDict() {
        for (EnumGem gem : EnumGem.values()) {
            for (String key : gem.getItemOreNames()) {
                OreDictionary.registerOre(key, gem.getItem());
            }
            OreDictionary.registerOre(gem.getItemSuperOreName(), gem.getItemSuper());
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;

        for (EnumGem gem : EnumGem.values()) {
            items.add(gem.getItem());

            // If Silent Gear is present, add new supercharged gems
            // TODO: Remove me? Supercharged gem sub-items might be a bit much...
            if (Loader.isModLoaded("silentgear") && SilentGems.instance.isDevBuild()) {
                for (int level = 1; level <= ModEnchantments.supercharged.getMaxLevel(); ++level) {
                    ItemStack stack = gem.getItem();
                    EnchantmentHelper.setEnchantments(ImmutableMap.of(ModEnchantments.supercharged, level), stack);
                    items.add(stack);
                }
            }
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack) + stack.getItemDamage();
    }

    @Override
    public void registerModels() {
        for (EnumGem gem : EnumGem.values()) {
            SilentGems.registry.setModel(this, gem.getMetadata(), "gem" + gem.getMetadata());
        }
    }
}
