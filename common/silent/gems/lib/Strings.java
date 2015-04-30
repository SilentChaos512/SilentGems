package silent.gems.lib;

public class Strings {

  public final static String EMPTY = "";

  /*
   * Localization prefix
   */
  public static final String RESOURCE_PREFIX = Reference.MOD_ID.toLowerCase() + ":";
  public static final String BUFF_RESOURCE_PREFIX = "buff." + RESOURCE_PREFIX;

  /*
   * Messages
   */
  public final static String PRESS_CTRL = "PressCtrl";
  public final static String PRESS_SHIFT = "PressShift";

  /*
   * Item information
   */
  // public final static String GEM_MATERIAL_DAMAGE = "Gem.Damage";
  // public final static String GEM_MATERIAL_EFFICIENCY = "Gem.Efficiency";
  // public final static String GEM_MATERIAL_MAX_USES = "Gem.MaxUses";
  // public final static String GEM_MATERIAL_TOOL_PROPERTIES = "Gem.ToolProperties";

  /*
   * NBT related constants
   */
  public static final String CHAOS_GEM_BUFF_ID = "id";
  public static final String CHAOS_GEM_BUFF_LIST = "buff";
  public static final String CHAOS_GEM_BUFF_LEVEL = "lvl";
  public static final String CHAOS_GEM_CHARGE = "charge";
  public static final String CHAOS_GEM_CHEATY = "cheaty";
  public static final String CHAOS_GEM_ENABLED = "enabled";
  public static final String TELEPORTER_LINKER_STATE = "state";
  public static final String TOOL_ICON_DECO = "Deco";
  public static final String TOOL_ICON_HANDLE = "Handle";
  public static final String TOOL_ICON_HEAD_LEFT = "HeadL";
  public static final String TOOL_ICON_HEAD_MIDDLE = "HeadM";
  public static final String TOOL_ICON_HEAD_RIGHT = "HeadR";
  public static final String TOOL_ICON_ROD = "Rod"; // This is actually the wool, it was added before rods :(
  public static final String TORCH_BANDOLIER_AUTO_FILL = "AutoFill";
  public static final String TORCH_BANDOLIER_GEM = "Gem";
  public static final String NBT_TE_STATE_KEY = "teState";
  public static final String NBT_CUSTOM_NAME = "CustomName";
  public static final String NBT_TE_DIRECTION_KEY = "teDirection";

  /*
   * Tool constants
   */
  public final static String TOOL_ARMOR = "Tool.Armor";
  public final static String TOOL_AXE = "Tool.Axe";
  public final static String TOOL_BOW = "Tool.Bow";
  public final static String TOOL_HOE = "Tool.Hoe";
  public final static String TOOL_PICKAXE = "Tool.Pickaxe";
  public final static String TOOL_SHOVEL = "Tool.Shovel";
  public final static String TOOL_SICKLE = "Tool.Sickle";
  public final static String TOOL_SWORD = "Tool.Sword";

  /*
   * Ore dictionary keys
   */
  public final static String ORE_DICT_GEM_BASIC = "gemSilentBasic";
  public final static String ORE_DICT_GEM_SHARD = "gemSilentShard";
  public final static String ORE_DICT_STICK_FANCY = "stickSilentFancy";
}
