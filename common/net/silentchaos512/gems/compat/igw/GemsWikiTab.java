package net.silentchaos512.gems.compat.igw;

import igwmod.gui.GuiWiki;
import igwmod.gui.tabs.BaseWikiTab;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.item.ModItems;

public class GemsWikiTab extends BaseWikiTab {

  public GemsWikiTab() {

//    pageEntries.add("RootTest");
//    pageEntries.add("block/BlockTest");
//    pageEntries.add("item/ItemTest");
    pageEntries.add("GettingStarted");
    pageEntries.add("Decorating");
    pageEntries.add("block/ChaosOre");
  }

  @Override
  public String getName() {

    return SilentGems.MOD_NAME;
  }

  @Override
  public ItemStack renderTabIcon(GuiWiki arg0) {

    return new ItemStack(ModItems.gem);
  }

  @Override
  protected String getPageLocation(String arg0) {

    return "silentgems:" + arg0;
  }

  @Override
  protected String getPageName(String arg0) {

    return LocalizationHelper.getWikiPageName(arg0);
  }

}
