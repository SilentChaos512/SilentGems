package net.silentchaos512.gems.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;


public class ItemGemShard extends Item implements IAddRecipes, ICustomModel {
    public ItemGemShard() {
        setHasSubtypes(true);
    }

    @Override
    public void addOreDict() {
        for (EnumGem gem : EnumGem.values()) {
            OreDictionary.registerOre(gem.getShardOreName(), gem.getShard());
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (EnumGem gem : EnumGem.values()) {
            items.add(gem.getShard());
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack) + stack.getItemDamage();
    }

    @Override
    public void registerModels() {
        for (EnumGem gem : EnumGem.values()) {
            SilentGems.registry.setModel(this, gem.getMetadata(), Names.GEM_SHARD + gem.getMetadata());
        }
    }
}
