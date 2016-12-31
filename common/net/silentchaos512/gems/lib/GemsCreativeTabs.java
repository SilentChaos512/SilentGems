package net.silentchaos512.gems.lib;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.item.ModItems;

public class GemsCreativeTabs {

  public static final CreativeTabs blocks = makeTab("blocks", new ItemStack(ModBlocks.gemOre));
  public static final CreativeTabs materials = makeTab("materials", new ItemStack(ModItems.gem));
  public static final CreativeTabs tools = makeTab("tools", new ItemStack(ModItems.torchBandolier));
  public static final CreativeTabs utility = makeTab("utility", new ItemStack(ModItems.drawingCompass));

  static CreativeTabs makeTab(final String name, final ItemStack icon) {

    return new CreativeTabs(SilentGems.MODID + ":" + name) {

      @Override
      public ItemStack getTabIconItem() {

        return icon;
      }
    };
  }
}
