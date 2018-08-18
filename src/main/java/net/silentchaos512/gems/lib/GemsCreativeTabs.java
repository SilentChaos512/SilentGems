package net.silentchaos512.gems.lib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;

public class GemsCreativeTabs {
    public static final CreativeTabs BLOCKS = SilentGems.registry.makeCreativeTab(SilentGems.MODID + ".blocks",
            () -> new ItemStack(ModBlocks.gemOre, 1, SilentGems.random.nextInt(16)));
    public static final CreativeTabs MATERIALS = SilentGems.registry.makeCreativeTab(SilentGems.MODID + ".materials",
            () -> EnumGem.getRandom().getItem());
    public static final CreativeTabs TOOLS = SilentGems.registry.makeCreativeTab(SilentGems.MODID + ".tools",
            () -> new ItemStack(ModItems.torchBandolier));
    public static final CreativeTabs UTILITY = SilentGems.registry.makeCreativeTab(SilentGems.MODID + ".utility",
            () -> new ItemStack(ModItems.drawingCompass));
}
