package net.silentchaos512.gems.config;

import net.minecraftforge.common.config.Configuration;

public class GemsConfigHC {
    public static boolean HARD_SUPER_GEMS;
    final static boolean HARD_SUPER_GEMS_DEFAULT = false;
    public static EnumRepairLogic REPAIR_LOGIC;
    final static EnumRepairLogic REPAIR_LOGIC_DEFAULT = EnumRepairLogic.MATERIAL_BASED;
    public static boolean TOOLS_BREAK;
    final static boolean TOOLS_BREAK_DEFAULT = false;

    public static final String CAT_HC = GemsConfig.CAT_MAIN + Configuration.CATEGORY_SPLITTER
            + "hardcore";
    public static final String CAT_RECIPE = CAT_HC + Configuration.CATEGORY_SPLITTER + "recipes";
    public static final String CAT_TOOLS = CAT_HC + Configuration.CATEGORY_SPLITTER + "tools";
    public static final String CAT_VANILLA_TOOLS = CAT_HC + Configuration.CATEGORY_SPLITTER
            + "vanilla_tools";

    public static void load(GemsConfig config) {
        config.getConfiguration().setCategoryComment(CAT_RECIPE,
                "Hardcore recipes. Require a Minecraft restart for changes to take effect.");
        config.getConfiguration().setCategoryRequiresMcRestart(CAT_RECIPE, true);

        HARD_SUPER_GEMS = config.loadBoolean("Hard Supercharged Gems", CAT_RECIPE,
                HARD_SUPER_GEMS_DEFAULT,
                "If enabled, supercharged gems will require ender essence instead of glowstone.");
        REPAIR_LOGIC = EnumRepairLogic.loadConfig(config);
        TOOLS_BREAK = config.loadBoolean("Tools Break Permanently", CAT_TOOLS, TOOLS_BREAK_DEFAULT,
                "If enable, tools can break like in vanilla and be lost forever.");
    }

    public enum EnumRepairLogic {
        CLASSIC, MATERIAL_BASED, HARD_MATERIAL_BASED, NOT_ALLOWED;

        static EnumRepairLogic loadConfig(GemsConfig config) {

            String[] validValues = new String[values().length];
            for (int i = 0; i < values().length; ++i) {
                validValues[i] = values()[i].name();
            }

            String str = config.getConfiguration().getString("Repair Logic", CAT_TOOLS,
                    GemsConfigHC.REPAIR_LOGIC_DEFAULT.name(),
                    "Determines how tools/armor are repaired via decorating.\n"
                            + "  CLASSIC - Old logic, based solely on the tier of the tool/armor and the material.\n"
                            + "  MATERIAL_BASED - Based on the durability of the repair material.\n"
                            + "  HARD_MATERIAL_BASED - Material based, but with lower repair values.\n"
                            + "  NOT_ALLOWED - Decorating does not repair tools or armor.",
                    validValues);

            for (EnumRepairLogic mode : values()) {
                if (mode.name().equalsIgnoreCase(str)) {
                    return mode;
                }
            }
            return GemsConfigHC.REPAIR_LOGIC_DEFAULT;
        }
    }
}
