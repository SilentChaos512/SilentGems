package net.silentchaos512.gems.lib;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.lib.creativetab.CreativeTabSL;

public class GemsCreativeTabs {

  public static final CreativeTabSL blocks = makeTab("blocks", new ItemStack(ModBlocks.gemOre));
  public static final CreativeTabSL materials = makeTab("materials", new ItemStack(ModItems.gem));
  public static final CreativeTabSL tools = makeTab("tools", new ItemStack(ModItems.torchBandolier));
  public static final CreativeTabSL utility = makeTab("utility", new ItemStack(ModItems.drawingCompass));

  static CreativeTabSL makeTab(final String name, final ItemStack icon) {

    return new CreativeTabSL(SilentGems.MODID + ":" + name) {

      @Override
      public ItemStack getStack() {

        return icon;
      }
    };
  }
}
