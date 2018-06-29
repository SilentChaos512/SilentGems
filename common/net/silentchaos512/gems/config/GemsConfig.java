package net.silentchaos512.gems.config;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.Configuration;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.enchantment.*;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.module.*;
import net.silentchaos512.gems.potion.PotionFreezing;
import net.silentchaos512.gems.potion.PotionShocking;
import net.silentchaos512.gems.util.WeightedRandomItemSG;
import net.silentchaos512.lib.config.AdaptiveConfig;

import java.util.*;

public class GemsConfig extends AdaptiveConfig {

  /*
   * Debug
   */

  public static boolean DEBUG_MODE = false;
  public static boolean DEBUG_LOG_POTS_AND_LIGHTS = false;
  public static int DEBUG_LOT_POTS_AND_LIGHTS_DELAY = 1200;

  /*
   * Blocks
   */

  public static int GLOW_ROSE_LIGHT_LEVEL = 10;

  public static int TELEPORTER_FREE_RANGE = 64;
  public static int TELEPORTER_COST_PER_BLOCK = 100;
  public static int TELEPORTER_COST_CROSS_DIMENSION = 100000;
  public static int TELEPORTER_MAX_CHARGE = 1000000;
  public static float TELEPORTER_REDSTONE_SEARCH_RADIUS = 2.0f;
  public static boolean TELEPORTER_ALLOW_DUMB = true;

  /*
   * Items
   */

  public static int BURN_TIME_CHAOS_COAL = 6400;
  public static boolean SPAWN_PLAYER_WITH_GUIDE_BOOK = true;

  public static int FOOD_SUPPORT_DURATION = 600;
  public static float FOOD_SECRET_DONUT_CHANCE = 0.33f;
  public static float FOOD_SECRET_DONUT_TEXT_CHANCE = 0.6f;

  public static int RETURN_HOME_USE_TIME = 16;
  public static int RETURN_HOME_USE_COST = 10000;
  public static int RETURN_HOME_MAX_CHARGE = 100000;

  /*
   * Tools
   */

  public static float VARIETY_BONUS = 0.075f;
  public static int VARIETY_CAP = 3;
  public static int TOMAHAWK_MAX_AMMO = 4;
  public static int TOMAHAWK_AMMO_PER_MAT = 1;
  public static boolean SWITCH_AXE_SUPER = false;

  public static ConfigOptionToolClass sword = new ConfigOptionToolClass(ModItems.sword, "sword");
  public static ConfigOptionToolClass dagger = new ConfigOptionToolClass(ModItems.dagger, "dagger");
  public static ConfigOptionToolClass katana = new ConfigOptionToolClass(ModItems.katana, "katana");
  public static ConfigOptionToolClass machete = new ConfigOptionToolClass(ModItems.machete,
      "machete");
  public static ConfigOptionToolClass scepter = new ConfigOptionToolClass(ModItems.scepter,
      "scepter");
  public static ConfigOptionToolClass tomahawk = new ConfigOptionToolClass(ModItems.tomahawk,
      "tomahawk");
  public static ConfigOptionToolClass pickaxe = new ConfigOptionToolClass(ModItems.pickaxe,
      "pickaxe");
  public static ConfigOptionToolClass shovel = new ConfigOptionToolClass(ModItems.shovel, "shovel");
  public static ConfigOptionToolClass axe = new ConfigOptionToolClass(ModItems.axe, "axe");
  public static ConfigOptionToolClass paxel = new ConfigOptionToolClass(ModItems.paxel, "paxel");
  public static ConfigOptionToolClass hoe = new ConfigOptionToolClass(ModItems.hoe, "hoe");
  public static ConfigOptionToolClass sickle = new ConfigOptionToolClass(ModItems.sickle, "sickle");
  public static ConfigOptionToolClass bow = new ConfigOptionToolClass(ModItems.bow, "hoe");
  public static ConfigOptionToolClass shield = new ConfigOptionToolClass(ModItems.shield, "shield");

  /*
   * Tool Souls
   */

  public static boolean SOULS_GAIN_XP_FROM_FAKE_PLAYERS;

  /*
   * Tool Parts
   */

  public static boolean PART_DISABLE_ALL_MUNDANE = false;
  public static boolean PART_DISABLE_ALL_REGULAR = false;
  public static boolean PART_DISABLE_ALL_SUPER = false;
  public static List<String> PART_BLACKLIST = Lists.newArrayList();

  /*
   * Nodes
   */

  public static boolean CHAOS_NODE_SALT_DELAY = true;
  public static int CHAOS_NODE_PARTICLE_OVERRIDE = -1;
  public static Set<Item> NODE_REPAIR_WHITELIST = new HashSet<>();
  public static Set<Item> NODE_REPAIR_BLACKLIST = new HashSet<>();

  /*
   * Entities
   */

  public static int ENDER_SLIME_SPAWN_WEIGHT = 3;
  public static int ENDER_SLIME_SPAWN_MIN = 1;
  public static int ENDER_SLIME_SPAWN_MAX = 2;

  /*
   * GUI
   */

  public static boolean SHOW_BONUS_ARMOR_BAR = true;
  public static boolean CHAOS_BAR_SHOW_ALWAYS = false;
  public static boolean SHOW_ARROW_COUNT = true;
  public static int CHAOS_GEM_BAR_POS_X = 5;
  public static int CHAOS_GEM_BAR_POS_Y = 5;
  public static int CHAOS_BAR_OFFSET_X = 0;
  public static int CHAOS_BAR_OFFSET_Y = 0;
  public static boolean CHAOS_BAR_BUMP_HEIGHT = true;

  /*
   * Recipes
   */

  public static boolean RECIPE_TELEPORTER_DISABLE = false;
  public static boolean RECIPE_TELEPORTER_ANCHOR_DISABLE = false;
  public static boolean RECIPE_TELEPORTER_REDSTONE_DISABLE = false;
  public static boolean RECIPE_TOKEN_FROST_WALKER_DISABLE = false;
  public static boolean RECIPE_TOKEN_MENDING_DISABLE = false;

  /*
   * Misc
   */

  public static boolean HIDE_FLAVOR_TEXT_ALWAYS = false;
  public static boolean HIDE_FLAVOR_TEXT_UNTIL_SHIFT = true;
  public static boolean RIGHT_CLICK_TO_PLACE_ENABLED = true;
  public static boolean RIGHT_CLICK_TO_PLACE_ON_SNEAK_ONLY = false;

  /*
   * Compat
   */

  public static boolean ENABLE_TIC_SUPPORT = true;

  /*
   * World generation
   */

  public static ConfigOptionOreGen WORLD_GEN_GEMS;
  public static ConfigOptionOreGen WORLD_GEN_GEMS_DARK;
  public static ConfigOptionOreGen WORLD_GEN_GEMS_LIGHT;
  public static ConfigOptionOreGen WORLD_GEN_CHAOS;
  public static ConfigOptionOreGen WORLD_GEN_ENDER;
  public static List<WeightedRandomItemSG> GEM_WEIGHTS = new ArrayList<>();
  public static List<WeightedRandomItemSG> GEM_WEIGHTS_DARK = new ArrayList<>();
  public static List<WeightedRandomItemSG> GEM_WEIGHTS_LIGHT = new ArrayList<>();

  // Regions
  public static boolean GEM_REGIONS_ENABLED;
  public static int GEM_REGIONS_SIZE;
  public static float GEM_REGIONS_SECOND_GEM_CHANCE;

  // Geodes
  public static float GEODE_DARK_FREQUENCY;
  public static float GEODE_LIGHT_FREQUENCY;
  public static int GEODE_MIN_Y;
  public static int GEODE_MAX_Y;
  public static float GEODE_FILL_RATIO;
  public static float GEODE_GEM_DENSITY;
  public static boolean GEODE_SEAL_BREAKS;

  public static int GLOW_ROSE_PER_CHUNK = 2;
  public static final Set<Integer> GLOW_ROSE_DIMENSION_BLACKLIST = new HashSet<>();
  public static float CHAOS_NODES_PER_CHUNK = 0.006f;
  public static final Set<Integer> CHAOS_NODE_DIMENSION_BLACKLIST = new HashSet<>();

  /*
   * Categories
   */

  static final String split = Configuration.CATEGORY_SPLITTER;
  public static final String CAT_MAIN = "main";
  public static final String CAT_DEBUG = CAT_MAIN + split + "debug";
  public static final String CAT_BLOCK = CAT_MAIN + split + "blocks";
  public static final String CAT_CHAOS = CAT_MAIN + split + "chaos";
  public static final String CAT_COMPAT = CAT_MAIN + split + "compat";
  public static final String CAT_CONTROLS = CAT_MAIN + split + "controls";
  public static final String CAT_ENCHANTMENT = CAT_MAIN + split + "enchantment";
  public static final String CAT_ENTITY = CAT_MAIN + split + "entity";
  public static final String CAT_GUI = CAT_MAIN + split + "gui";
  public static final String CAT_ITEM = CAT_MAIN + split + "items";
  public static final String CAT_MISC = CAT_MAIN + split + "misc";
  public static final String CAT_NODES = CAT_MAIN + split + "chaos nodes";
  public static final String CAT_RECIPE = CAT_MAIN + split + "recipes";
  public static final String CAT_TOOL_PARTS = CAT_ITEM + split + "tool_parts";
  public static final String CAT_TOOL_SOULS = CAT_ITEM + split + "tool_souls";
  public static final String CAT_TOOLS = CAT_ITEM + split + "tools";
  public static final String CAT_TOOLTIPS = CAT_MAIN + split + "tooltips";
  public static final String CAT_WORLD_GEN = CAT_MAIN + split + "world generation";
  public static final String CAT_WORLD_GEN_GEM_WEIGHT = CAT_WORLD_GEN + split + "gem weights";
  public static final String CAT_WORLD_GEN_GEODES = CAT_WORLD_GEN + split + "gem geodes";
  public static final String CAT_WORLD_GEN_REGIONS = CAT_WORLD_GEN + split + "gem regions";

  public static final GemsConfig INSTANCE = new GemsConfig();

  public GemsConfig() {

    super(SilentGems.MODID, true, SilentGems.BUILD_NUM);
  }

  @Override
  public void load() {

    try {
      //@formatter:off

      String comment;

      /*
       * Debug
       */

      config.setCategoryComment(CAT_DEBUG, "Options for debugging. Generally, you should leave these "
          + "alone unless I tell you to change them. Enabling debug options will likely result in "
          + "log spam, but may help me track down issues.");

      DEBUG_MODE = config.getBoolean("Debug Mode", CAT_DEBUG,
          DEBUG_MODE,
          "Generic debug mode option. May add text to GUIs and WIT HUD.");

      DEBUG_LOG_POTS_AND_LIGHTS = config.getBoolean("Log Pots and Lights", CAT_DEBUG,
          DEBUG_LOG_POTS_AND_LIGHTS,
          "Logs the existence of Chaos Flower Pots and Phantom Lights. Their tile entities, to "
              + "be more precise. Also lists the position of each one.");
      DEBUG_LOT_POTS_AND_LIGHTS_DELAY = config.getInt("Log Pots and Lights - Delay", CAT_DEBUG,
          DEBUG_LOT_POTS_AND_LIGHTS_DELAY, 600, 72000,
          "Pot and Light logging will occur every this many ticks. 1200 ticks = 1 minute.");

      /*
       * Blocks
       */

      final String catGlowRose = CAT_BLOCK + split + "Glow Rose";
      GLOW_ROSE_LIGHT_LEVEL = config.getInt("Light Level", catGlowRose,
          GLOW_ROSE_LIGHT_LEVEL, 0, 15,
          "The light level glow roses emit.");

      final String catTeleporter = CAT_BLOCK + split + "Teleporter";
      TELEPORTER_ALLOW_DUMB = config.getBoolean("Allow Dumb Teleporters", catTeleporter,
          TELEPORTER_ALLOW_DUMB,
          "Allows teleports to happen even if the destination teleporter has been removed.");
      TELEPORTER_COST_CROSS_DIMENSION = config.getInt("Chaos Cost Cross Dimension", catTeleporter,
          TELEPORTER_COST_CROSS_DIMENSION, 0, Integer.MAX_VALUE,
          "The amount of Chaos charged for traveling between dimensions.");
      TELEPORTER_COST_PER_BLOCK = config.getInt("Chaos Cost Per Block", catTeleporter,
          TELEPORTER_COST_PER_BLOCK, 0, Integer.MAX_VALUE,
          "The amount of Chaos charged per block traveled.");
      TELEPORTER_FREE_RANGE = config.getInt("Free Range", catTeleporter,
          TELEPORTER_FREE_RANGE, 0, Integer.MAX_VALUE,
          "The distance that can be teleported for free.");
      TELEPORTER_MAX_CHARGE = config.getInt("Max Chaos", catTeleporter,
          TELEPORTER_MAX_CHARGE, 0, Integer.MAX_VALUE,
          "The maximum amount of Chaos a teleporter can store.");

      /*
       * Items
       */

      BURN_TIME_CHAOS_COAL = loadInt("Chaos Coal Burn Time", CAT_ITEM,
          BURN_TIME_CHAOS_COAL, 0, Integer.MAX_VALUE,
          "The burn time of Chaos Coal. Regular coal is 1600 ticks.");
      SPAWN_PLAYER_WITH_GUIDE_BOOK = loadBoolean("Spawn with Guide Book", CAT_ITEM,
          SPAWN_PLAYER_WITH_GUIDE_BOOK,
          "Should players be given a copy of the guide book when they first spawn?");
      if (ModItems.guideBook != null)
        ModItems.guideBook.giveBookOnFirstLogin = SPAWN_PLAYER_WITH_GUIDE_BOOK;

      // Food
      final String catFood = CAT_ITEM + split + "Food";
      FOOD_SUPPORT_DURATION = loadInt("Support Duration", catFood,
          FOOD_SUPPORT_DURATION, 0, 72000,
          "The base duration of potion effects from food. The actual duration will vary by effect.");
      FOOD_SECRET_DONUT_CHANCE = config.getFloat("Secret Donut Effect Chance", catFood,
          FOOD_SECRET_DONUT_CHANCE, 0.0f, 1.0f,
          "The chance of secret donuts giving potion effects.");
      FOOD_SECRET_DONUT_TEXT_CHANCE = config.getFloat("Secret Donut Text Chance", catFood,
          FOOD_SECRET_DONUT_TEXT_CHANCE, 0.0f, 1.0f,
          "The chance of secrets donuts putting weird text in your chat.");

      // Return Home Charm
      final String catReturnHome = CAT_ITEM + split + "Return Home Charm";
      RETURN_HOME_USE_TIME = loadInt("Use Time", catReturnHome,
          RETURN_HOME_USE_TIME, 0, Integer.MAX_VALUE,
          "The number of ticks the Return Home Charm must be 'charged' to use. Set to 0 for instant use.");
      RETURN_HOME_USE_COST = loadInt("Use Cost", catReturnHome,
          RETURN_HOME_USE_COST, 0, Integer.MAX_VALUE,
          "The amount of Chaos required to teleport.");
      RETURN_HOME_MAX_CHARGE = loadInt("Max Charge", catReturnHome,
          RETURN_HOME_MAX_CHARGE, 0, Integer.MAX_VALUE,
          "The maximum amount of Chaos a charm can hold.");

      /*
       * Tools
       */

      //addAdaptorMapping(0, "Variety Bonus", 0.75);
      VARIETY_BONUS = config.getFloat("Variety Bonus", CAT_TOOLS,
          VARIETY_BONUS, 0f, 1f,
          "The \"variety bonus\" for mixed-material tools and armor. Default is a 7.5% bonus for each"
          + " additional unique part.");
      VARIETY_CAP = config.getInt("Variety Cap", CAT_TOOLS,
          VARIETY_CAP, 1, 9,
          "The maximum number of unique parts that can affect the variety bonus");

      TOMAHAWK_MAX_AMMO = loadInt("Tomahawk Max Ammo", CAT_TOOLS,
          TOMAHAWK_MAX_AMMO,
          0, Byte.MAX_VALUE, "The maximum \"ammo\" for tomahawks. This is the number that can be thrown before retrieval/repairs"
          + " are required. Setting this to 0 will make it so tomahawks cannot be thrown.");

      TOMAHAWK_AMMO_PER_MAT = loadInt("Tomahawk Ammo per Material", CAT_TOOLS,
          TOMAHAWK_AMMO_PER_MAT, 0, Byte.MAX_VALUE,
          "The \"ammo\" restored by each material (gem, etc.) when decorating a tomahawk.");
      SWITCH_AXE_SUPER = loadBoolean("Switch Axe Super Skill to Area Miner", CAT_TOOLS,
          SWITCH_AXE_SUPER,
          "Change the super skill for axes to Area Miner. Useful if playing with Veinminer installed.");

      sword.loadValue(config);
      dagger.loadValue(config);
      katana.loadValue(config);
      machete.loadValue(config);
      scepter.loadValue(config);
      tomahawk.loadValue(config);
      pickaxe.loadValue(config);
      shovel.loadValue(config);
      axe.loadValue(config);
      paxel.loadValue(config);
      hoe.loadValue(config);
      sickle.loadValue(config);
      bow.loadValue(config);
      shield.loadValue(config);

      /*
       * Tool Souls
       */

      SOULS_GAIN_XP_FROM_FAKE_PLAYERS = loadBoolean("Gain XP From Fake Players", CAT_TOOL_SOULS, false,
          "If true, tools with souls can gain XP when used by fake players.");

      /*
       * Tool Parts
       */

      String catPartDisable = CAT_TOOL_PARTS + split + "disable";
      config.setCategoryRequiresMcRestart(catPartDisable, true);
      config.setCategoryComment(catPartDisable, "Disable specific tool parts or entire tiers. As a reminder:"
          + " Mundane includes flint, Regular is ordinary gems, and Super is supercharged gems. Hover"
          + " over the item for the tool part and hold Ctrl to see the tier.");
      PART_DISABLE_ALL_MUNDANE = config.get(catPartDisable, "All Mundane Tier", false).getBoolean();
      PART_DISABLE_ALL_REGULAR = config.get(catPartDisable, "All Regular Tier", false).getBoolean();
      PART_DISABLE_ALL_SUPER = config.get(catPartDisable, "All Super Tier", false).getBoolean();
      String[] partBlacklistTemp = config.getStringList("Blacklist", catPartDisable, new String[0],
          "You can disable individual parts by listing the part keys here, if disabling groups with"
          + " the other settings doesn't cut it. Hover over the item in-game and hold Ctrl+Shift to"
          + " reveal the part key.");
      for (String str : partBlacklistTemp)
        PART_BLACKLIST.add(str);

      /*
       * Enchantments
       */

      String enchEnabledComment = "Allow this enchantment to be applied at the enchanting table."
          + " Enchantment tokens will still work.";
      EnchantmentGravity.ENABLED = loadBoolean("Gravity - Enabled", CAT_ENCHANTMENT, true,
          enchEnabledComment);
      EnchantmentIceAspect.ENABLED = loadBoolean("Ice Aspect - Enabled", CAT_ENCHANTMENT, true,
          enchEnabledComment);
      EnchantmentLifeSteal.ENABLED = loadBoolean("Life Steal - Enabled", CAT_ENCHANTMENT, true,
          enchEnabledComment);
      EnchantmentLightningAspect.ENABLED = loadBoolean("Lightning Aspect - Enabled",
          CAT_ENCHANTMENT, true, enchEnabledComment);
      EnchantmentMagicDamage.ENABLED = loadBoolean("Concentration - Enabled", CAT_ENCHANTMENT, true,
          enchEnabledComment);
      // DoT and other effects
      PotionFreezing.CONTINUOUS_DAMAGE_ENABLED = loadBoolean("Ice Aspect - DoT Enabled", CAT_ENCHANTMENT, true,
          "Allow the damage over time (DoT) of Ice Aspect. Disabling will still apply the slowing effect.");
      PotionShocking.CONTINUOUS_DAMAGE_ENABLED = loadBoolean("Lightning Aspect - DoT Enabled", CAT_ENCHANTMENT, true,
          "Allow the damage over time (DoT) of Lightning Aspect. Disabling will still apply the slowing effect.");
      PotionShocking.CHAINING_ENABLED = loadBoolean("Lightning Aspect - Chaining Enabled", CAT_ENCHANTMENT, true,
          "Allow the chaining effect of Lightning Aspect, where the effect spreads to nearby entities.");

      /*
       * Nodes
       */

      CHAOS_NODE_SALT_DELAY = loadBoolean("Salted Delay", CAT_NODES, true,
          "Adds a extra value to the delays on node activity. The value is not truly random but"
          + " based on the position of the node. If disabled, all nodes will try the same effect"
          + " at the same time.");
      CHAOS_NODE_PARTICLE_OVERRIDE = loadInt("Particle Setting Override", CAT_NODES,
          CHAOS_NODE_PARTICLE_OVERRIDE, -1, 2,
          "Override vanilla particle settings for chaos nodes. -1 will use vanilla settings, 0 is"
          + " All, 1 is Decreased, and 2 is Minimal");
      loadNodeItemList(true, config.getStringList("Repair Whitelist", CAT_NODES,
          new String[0],
          "Repair packets will try to repair these items, if possible. REMOVING ITEMS REQUIRES A RESTART."));
      loadNodeItemList(false, config.getStringList("Repair Blacklist", CAT_NODES,
          new String[0],
          "Repair packets will not try to repair these items. REMOVING ITEMS REQUIRES A RESTART."));

      /*
       * Entities
       */

      String catEnderSlime = CAT_ENTITY + split + "ender_slime";
      config.setCategoryRequiresMcRestart(catEnderSlime, true);
      ENDER_SLIME_SPAWN_WEIGHT = loadInt("Spawn Weight", catEnderSlime,
          ENDER_SLIME_SPAWN_WEIGHT, 0, Integer.MAX_VALUE,
          "Spawn weight (how common they are). Set to 0 to disable.");
      ENDER_SLIME_SPAWN_MIN = loadInt("Spawn Min", catEnderSlime,
          ENDER_SLIME_SPAWN_MIN, 1, 8,
          "Minimum group size. Should be less than or equal to max.");
      ENDER_SLIME_SPAWN_MAX = loadInt("Spawn Max", catEnderSlime,
          ENDER_SLIME_SPAWN_MAX, 1, 8,
          "Maximum group size. Should be greater than or equal to min.");
      if (ENDER_SLIME_SPAWN_MIN > ENDER_SLIME_SPAWN_MAX) {
        ENDER_SLIME_SPAWN_MIN = ENDER_SLIME_SPAWN_MAX;
      }

      /*
       * GUI
       */

      SHOW_BONUS_ARMOR_BAR = loadBoolean("Show Bonus Armor Bar", CAT_GUI,
          SHOW_BONUS_ARMOR_BAR,
          "Shows armor points beyond 20 on the bar as yellow armor pieces above the normal ones.");
      CHAOS_BAR_SHOW_ALWAYS = loadBoolean("Show Chaos Bar Always", CAT_GUI,
          CHAOS_BAR_SHOW_ALWAYS,
          "Show the chaos bar at all times. By default, it only shows when you gain/lose chaos.");
      SHOW_ARROW_COUNT = loadBoolean("Show Arrow Count", CAT_GUI,
          true,
          "Show how many arrows you have (and what kind will be fired) when you have a bow"
          + " equipped. Compatible with quivers!");
      CHAOS_GEM_BAR_POS_X = loadInt("Chaos Gem Charge Bar X", CAT_GUI,
          CHAOS_GEM_BAR_POS_X,
          "The X position for chaos gem charge bars. Negative numbers will anchor to the right of the screen.");
      CHAOS_GEM_BAR_POS_Y = loadInt("Chaos Gem Charge Bar Y", CAT_GUI,
          CHAOS_GEM_BAR_POS_Y,
          "The Y position for chaos gem charge bars. Negative numbers will anchor to the bottom of the screen.");
      CHAOS_BAR_OFFSET_X = loadInt("Chaos Bar Offset X", CAT_GUI,
          CHAOS_BAR_OFFSET_X,
          "X offset for the player's chaos bar, relative to its normal position.");
      CHAOS_BAR_OFFSET_Y = loadInt("Chaos Bar Offset Y", CAT_GUI,
          CHAOS_BAR_OFFSET_Y,
          "Y offset for the player's chaos bar, relative to its normal position.");
      CHAOS_BAR_BUMP_HEIGHT = loadBoolean("Chaos Bar Bump Height", CAT_GUI, true,
          "Set false if you are moving the chaos bar to a different position. If true, increases the \"GUI height\" variable when drawing the chaos bar."
          + " The variable affects the position of other bars drawn on the same side.");

      /*
       * Recipes
       */

      config.setCategoryRequiresMcRestart(CAT_RECIPE, true);

      RECIPE_TELEPORTER_DISABLE = loadBoolean("Disable Regular Teleporter Recipes", CAT_RECIPE,
          RECIPE_TELEPORTER_DISABLE,
          "Disable recipes for regular gem teleporters.");
      RECIPE_TELEPORTER_ANCHOR_DISABLE = loadBoolean("Disable Teleporter Anchor Recipes", CAT_RECIPE,
          RECIPE_TELEPORTER_ANCHOR_DISABLE,
          "Disable recipes for teleporter anchors.");
      RECIPE_TELEPORTER_REDSTONE_DISABLE = loadBoolean("Disable Redstone Teleporter Recipes", CAT_RECIPE,
          RECIPE_TELEPORTER_REDSTONE_DISABLE,
          "Disable recipes for redstone gem teleporters.");

      RECIPE_TOKEN_FROST_WALKER_DISABLE = loadBoolean("Disable Frost Walker Token Recipe", CAT_RECIPE,
          RECIPE_TOKEN_FROST_WALKER_DISABLE,
          "Disables recipes for Frost Walker enchantment tokens.");
      RECIPE_TOKEN_MENDING_DISABLE = loadBoolean("Disable Mending Token Recipe", CAT_RECIPE,
          RECIPE_TOKEN_MENDING_DISABLE,
          "Disables recipes for Mending enchantment tokens.");

      /*
       * Misc
       */

      HIDE_FLAVOR_TEXT_ALWAYS = loadBoolean("Hide Flavor Text - Always", CAT_TOOLTIPS,
          HIDE_FLAVOR_TEXT_ALWAYS,
          "Always hide the potentially funny, but useless item descriptions.");
      HIDE_FLAVOR_TEXT_UNTIL_SHIFT = loadBoolean("Hide Flavor Text - Until Shift", CAT_TOOLTIPS,
          HIDE_FLAVOR_TEXT_UNTIL_SHIFT,
          "Hide the flavor text until shift is pressed.");

      // Controls - right-click to place
      final String catRctp = CAT_CONTROLS + Configuration.CATEGORY_SPLITTER + "Right-click to place";
      config.setCategoryComment(catRctp, "Mining tools have the ability to place blocks in the slot "
          + "after them (or in slot 9 if that doesn't work) by right-clicking.");
      RIGHT_CLICK_TO_PLACE_ENABLED = loadBoolean("Enabled", catRctp,
          RIGHT_CLICK_TO_PLACE_ENABLED,
          "If set to false, the ability of mining tools to place blocks by right-clicking will be completely disabled.");
      RIGHT_CLICK_TO_PLACE_ON_SNEAK_ONLY = loadBoolean("Only When Sneaking", catRctp,
          RIGHT_CLICK_TO_PLACE_ON_SNEAK_ONLY,
          "If set to true and right-click to place is enabled, this ability will only activate "
              + "while sneaking (holding shift, normally).");

      /*
       * Compat
       */

      config.setCategoryRequiresMcRestart(CAT_COMPAT, true);
      ENABLE_TIC_SUPPORT = loadBoolean("Tinkers Construct Support", CAT_COMPAT,
          ENABLE_TIC_SUPPORT,
          "Set to false to disable registration of TiC parts.");

      /*
       * World Generation
       */

      GLOW_ROSE_PER_CHUNK = loadInt("Flowers per Chunk", CAT_WORLD_GEN,
          GLOW_ROSE_PER_CHUNK, 0, 100,
          "The number of glow roses to attempt to spawn per chunk.");
      CHAOS_NODES_PER_CHUNK = config.getFloat("Chaos Nodes per Chunk", CAT_WORLD_GEN,
          CHAOS_NODES_PER_CHUNK, 0.0f, 8.0f,
          "The number of chaos nodes to try to spawn per chunk. If less than 1 (recommended), you"
              + " can think of this as the chance to spawn in a chunk.");

      GLOW_ROSE_DIMENSION_BLACKLIST.clear();
      GLOW_ROSE_DIMENSION_BLACKLIST.addAll(tryParseDimensionList(config.getStringList("Flowers Dimension Blacklist", CAT_WORLD_GEN,
              new String[0], "The dimensions that glow roses may not spawn in.")));
      CHAOS_NODE_DIMENSION_BLACKLIST.clear();
      CHAOS_NODE_DIMENSION_BLACKLIST.addAll(tryParseDimensionList(config.getStringList("Chaos Node Dimension Blacklist", CAT_WORLD_GEN,
              new String[0], "The dimensions that chaos nodes may not spawn in.")));

      WORLD_GEN_GEMS = new ConfigOptionOreGen("Gems (Overworld)", 0, 10.0f, 8, 5, 45);
      WORLD_GEN_GEMS.loadValue(config, CAT_WORLD_GEN);
      WORLD_GEN_GEMS_DARK = new ConfigOptionOreGen("Dark Gems (Nether)", -1, 12.5f, 10, 30, 100);
      WORLD_GEN_GEMS_DARK.loadValue(config, CAT_WORLD_GEN);
      WORLD_GEN_GEMS_LIGHT = new ConfigOptionOreGen("Light Gems (The End)", 1, 12.5f, 8, 16, 64);
      WORLD_GEN_GEMS_LIGHT.loadValue(config, CAT_WORLD_GEN);
      WORLD_GEN_CHAOS = new ConfigOptionOreGen("Chaos Ore", 0, 1.75f, 16, 5, 20);
      WORLD_GEN_CHAOS.loadValue(config, CAT_WORLD_GEN);
      WORLD_GEN_ENDER = new ConfigOptionOreGen("Ender Essence Ore", 1, 1.0f, 32, 10, 70);
      WORLD_GEN_ENDER.loadValue(config, CAT_WORLD_GEN);

      // Gem weights
      final int weightMax = 1000;
      config.setCategoryComment(CAT_WORLD_GEN_GEM_WEIGHT,
          "The spawn weights of the gems. If two gems have different weights, the gem with the\n"
              + "higher weight is more likely to be selected when placing gems in the world.\n"
              + "Just increasing weights will NOT increase the number of gems that spawn!\n"
              + "Values must be between 1 and " + weightMax + ", inclusive.");
      for (EnumGem gem : EnumGem.values()) {
        int k = config.get(CAT_WORLD_GEN_GEM_WEIGHT, gem.getGemName(), 10).getInt();
        k = MathHelper.clamp(k, 1, weightMax);
        WeightedRandomItemSG item = new WeightedRandomItemSG(k, gem.ordinal() & 0xF);
        switch (gem.getSet()) {
          case CLASSIC:
            GEM_WEIGHTS.add(item);
            break;
          case DARK:
            GEM_WEIGHTS_DARK.add(item);
            break;
          case LIGHT:
            GEM_WEIGHTS_LIGHT.add(item);
            break;
        }
      }

      // Gem regions
      config.setCategoryComment(CAT_WORLD_GEN_REGIONS, "Settings for \"gem regions\". This will"
          + " randomly assign one or two gems to spawn in each region in the overworld. This does"
          + " not consider things like biomes, it's purely random. The usual world gen configs"
          + " still apply, this only changes which gem type is selected for each vein.");
      GEM_REGIONS_ENABLED = loadBoolean("Enable", CAT_WORLD_GEN_REGIONS, false,
          "Enables gem regions.");
      GEM_REGIONS_SIZE = loadInt("Region Size", CAT_WORLD_GEN_REGIONS,
          12, 1, 256,
          "Each region is a square of this many chunks on each side.");
      GEM_REGIONS_SECOND_GEM_CHANCE = config.getFloat("Second Gem Chance", CAT_WORLD_GEN_REGIONS,
          0.75f, 0f, 1f,
          "The chance of a second gem being selected to spawn in the region.");

      // Gem geodes
      config.setCategoryComment(CAT_WORLD_GEN_GEODES, "Geodes are deposits of Nether/End gems that"
          + " spawn rarely in the overworld and similar dimensions. They are large, but encased in"
          + " a tough shell.");
      GEODE_DARK_FREQUENCY = config.getFloat("Dark Geode Frequency", CAT_WORLD_GEN_GEODES,
          0.0125f, 0f, 1f,
          "The chance a geode of Nether (dark) gems will spawn in any given chunk.");
      GEODE_LIGHT_FREQUENCY = config.getFloat("Light Geode Frequency", CAT_WORLD_GEN_GEODES,
          0.0125f, 0f, 1f,
          "The chance a geode of End (light) gems will spawn in any given chunk.");
      GEODE_FILL_RATIO = config.getFloat("Fill Ratio", CAT_WORLD_GEN_GEODES,
          0.6f, 0f, 1f,
          "The ratio of geodes that will attempt to fill with gems.");
      GEODE_GEM_DENSITY = config.getFloat("Gem Density", CAT_WORLD_GEN_GEODES,
          0.75f, 0f, 1f,
          "The density of gems in the filled region of the geode.");
      GEODE_MIN_Y = loadInt("Min Y", CAT_WORLD_GEN_GEODES,
          20, 0, 255,
          "The minimum height geodes will attempt to spawn at.");
      GEODE_MAX_Y = loadInt("Max Y", CAT_WORLD_GEN_GEODES,
          40, 0, 255,
          "The maximum height geodes will attempt to spawn at.");
      GEODE_SEAL_BREAKS = loadBoolean("Seal Breaks", CAT_WORLD_GEN_GEODES, true,
          "If the geode is \"broken\" (intersects with a cave or something), the world generator"
          + " will seal the break.");

      GemsConfigHC.load(this);
      //@formatter:on
    } catch (Exception e) {
      System.out.println("Oh noes!!! Couldn't load configuration file properly!");
    }
  }

  public static void loadNodeItemList(boolean whitelist, String[] list) {

    for (int i = 0; i < list.length; ++i) {
      Item item = Item.getByNameOrId(list[i]);
      if (item != null) {
        if (whitelist) {
          NODE_REPAIR_WHITELIST.add(item);
        } else {
          NODE_REPAIR_BLACKLIST.add(item);
        }
      }
    }
  }

  public void loadModuleConfigs() {

    ModuleCoffee.loadConfig(config);
    ModuleEntityRandomEquipment.loadConfig(config);
    ModuleHolidayCheer.instance.loadConfig(config);
    ModuleAprilTricks.instance.loadConfig(config);
    ModuleHalloweenHijinks.instance.loadConfig(config);
  }

  private Collection<Integer> tryParseDimensionList(String[] dims) {
    List<Integer> list = new ArrayList<>();
    for (String str : dims) {
      try {
        list.add(Integer.parseInt(str));
      } catch (NumberFormatException ex) {
        SilentGems.logHelper.debug("Could not parse \"" + str + "\" as integer for dimension blacklist.");
      }
    }
    return list;
  }
}
