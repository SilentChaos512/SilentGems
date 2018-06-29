package net.silentchaos512.gems.guide.page;

import net.silentchaos512.gems.config.ConfigOptionOreGen;
import net.silentchaos512.lib.guidebook.GuideBook;
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

    String str = book.loc.getLocalizedString("guide", "oreConfigPage", config.getName(), config.getVeinCount(),
            config.getVeinSize(), config.getMinY(), config.getMaxY());
    str = doTextReplacements(str);
    str += super.getInfoText();
    return str;
  }
}
