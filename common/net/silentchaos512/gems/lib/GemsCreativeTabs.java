package net.silentchaos512.gems.lib;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.lib.creativetab.CreativeTabSL;

public class GemsCreativeTabs {

  //@formatter:off
  public static final CreativeTabSL blocks = new CreativeTabSL(
      SilentGems.RESOURCE_PREFIX + "blocks", ModBlocks.gemOre, SilentGems.random.nextInt(16));
  public static final CreativeTabSL materials = new CreativeTabSL(
      SilentGems.RESOURCE_PREFIX + "materials", ModItems.gem, SilentGems.random.nextInt(32));
  public static final CreativeTabSL tools = new CreativeTabSL(
      SilentGems.RESOURCE_PREFIX + "tools", ModItems.torchBandolier, 0);
  public static final CreativeTabSL utility = new CreativeTabSL(
      SilentGems.RESOURCE_PREFIX + "utility", ModItems.drawingCompass, 0);
  //@formatter:on
}
