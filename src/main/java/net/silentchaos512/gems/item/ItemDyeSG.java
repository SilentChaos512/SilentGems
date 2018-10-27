package net.silentchaos512.gems.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDyeSG extends Item implements IAddRecipes, ICustomModel {
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(I18n.format("misc.silentgems.legacyItem"));
    }

    @Override
    public void addOreDict() {
        OreDictionary.registerOre("dyeBlack", new ItemStack(this, 1, 0));
        OreDictionary.registerOre("dyeBlue", new ItemStack(this, 1, 4));
    }

    @Override
    public void registerModels() {
        SilentGems.registry.setModel(this, 0, "dyeblack");
        SilentGems.registry.setModel(this, 4, "dyeblue");
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 4));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        String prefix = "item." + SilentGems.MODID + ".";
        if (stack.getItemDamage() == 0) return prefix + "dyeblack";
        else if (stack.getItemDamage() == 4) return prefix + "dyeblue";
        else return prefix + "dyeunknown";
    }
}
