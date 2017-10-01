package net.silentchaos512.gems.config;

import net.minecraftforge.common.config.Configuration;

public class GemsConfigHC {

  public static boolean TOOLS_BREAK = false;

  public static final String CAT_HC = GemsConfig.CAT_MAIN + Configuration.CATEGORY_SPLITTER
      + "hardcore";

  public static void load(GemsConfig config) {

    TOOLS_BREAK = config.loadBoolean("Tools Break Permanently", CAT_HC, TOOLS_BREAK,
        "If enable, tools can break like in vanilla and be lost forever.");
  }
}
