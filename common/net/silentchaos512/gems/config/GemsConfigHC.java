package net.silentchaos512.gems.config;

import net.minecraftforge.common.config.Configuration;

public class GemsConfigHC {

  public static boolean HARD_SUPER_GEMS;
  final static boolean HARD_SUPER_GEMS_DEFAULT = false;
  public static boolean TOOLS_BREAK;
  final static boolean TOOLS_BREAK_DEFAULT = false;

  public static final String CAT_HC = GemsConfig.CAT_MAIN + Configuration.CATEGORY_SPLITTER
      + "hardcore";
  public static final String CAT_RECIPE = CAT_HC + Configuration.CATEGORY_SPLITTER + "recipes";
  public static final String CAT_VANILLA_TOOLS = CAT_HC + Configuration.CATEGORY_SPLITTER + "vanilla_tools";

  public static void load(GemsConfig config) {

    config.getConfiguration().setCategoryComment(CAT_RECIPE,
        "Hardcore recipes. Require a Minecraft restart for changes to take effect.");
    config.getConfiguration().setCategoryRequiresMcRestart(CAT_RECIPE, true);

    HARD_SUPER_GEMS = config.loadBoolean("Hard Supercharged Gems", CAT_RECIPE,
        HARD_SUPER_GEMS_DEFAULT,
        "If enabled, supercharged gems will require ender essence instead of glowstone.");
    TOOLS_BREAK = config.loadBoolean("Tools Break Permanently", CAT_HC, TOOLS_BREAK_DEFAULT,
        "If enable, tools can break like in vanilla and be lost forever.");
  }
}
