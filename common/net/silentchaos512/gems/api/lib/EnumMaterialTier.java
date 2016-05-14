package net.silentchaos512.gems.api.lib;

import net.silentchaos512.gems.SilentGems;

public enum EnumMaterialTier {

  MUNDANE, REGULAR, SUPER;

  public String getLocalizedName() {

    return SilentGems.instance.localizationHelper.getMiscText("ToolTier." + name().toLowerCase());
  }
}
