package net.silentchaos512.gems.guide.page;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.config.ConfigOptionOreGen;
import net.silentchaos512.lib.guidebook.GuideBook;
import net.silentchaos512.lib.guidebook.internal.GuiGuideBase;
import net.silentchaos512.lib.guidebook.page.GuidePage;
import net.silentchaos512.lib.guidebook.page.PageTextOnly;

public class PageOreSpawn extends PageTextOnly {

  public final ConfigOptionOreGen config;

  public PageOreSpawn(GuideBook book, int key, ConfigOptionOreGen config) {

    super(book, key);
    this.config = config;
  }

  public PageOreSpawn(GuideBook book, int key, int priority, ConfigOptionOreGen config) {

    super(book, key, priority);
    this.config = config;
  }

  @Override
  public String getInfoText() {

    String str = book.loc.getLocalizedString("guide", "oreConfigPage", config.name, config.veinCount,
        config.veinSize, config.minY, config.maxY);
    str = doTextReplacements(str);
    str += super.getInfoText();
    return str;
  }
}
