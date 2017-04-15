package net.silentchaos512.gems.config;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.module.ModuleAprilTricks;
import net.silentchaos512.gems.lib.module.ModuleCoffee;
import net.silentchaos512.gems.lib.module.ModuleEntityRandomEquipment;
import net.silentchaos512.gems.lib.module.ModuleHolidayCheer;
import net.silentchaos512.gems.util.WeightedRandomItemSG;
import net.silentchaos512.lib.config.AdaptiveConfig;

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
  public static int TOMAHAWK_MAX_AMMO = 4;
  public static int TOMAHAWK_AMMO_PER_MAT = 1;
  public static boolean SWITCH_AXE_SUPER = false;

  public static boolean TOOL_DISABLE_SWORD;
  public static boolean TOOL_DISABLE_KATANA;
  public static boolean TOOL_DISABLE_SCEPTER;
  public static boolean TOOL_DISABLE_TOMAHAWK;
  public static boolean TOOL_DISABLE_PICKAXE;
  public static boolean TOOL_DISABLE_SHOVEL;
  public static boolean TOOL_DISABLE_AXE;
  public static boolean TOOL_DISABLE_PAXEL;
  public static boolean TOOL_DISABLE_HOE;
  public static boolean TOOL_DISABLE_SICKLE;
  public static boolean TOOL_DISABLE_BOW;
  public static boolean TOOL_DISABLE_SHIELD;

  /*
   * Tool Parts
   */

  public static boolean PART_DISABLE_ALL_MUNDANE = false;
  public static boolean PART_DISABLE_ALL_REGULAR = false;
  public static boolean PART_DISABLE_ALL_SUPER = false;
  public static List<String> PART_BLACKLIST = Lists.newArrayList();

  /*
   * Chaos
   */

  public static boolean CHAOS_DIRECT_TRANSFER = false;

  /*
   * GUI
   */

  public static boolean SHOW_BONUS_ARMOR_BAR = true;

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
   * World generation
   */

  public static ConfigOptionOreGen WORLD_GEN_GEMS;
  public static ConfigOptionOreGen WORLD_GEN_GEMS_DARK;
  public static ConfigOptionOreGen WORLD_GEN_CHAOS;
  public static ConfigOptionOreGen WORLD_GEN_ENDER;
  public static List<WeightedRandomItemSG> GEM_WEIGHTS = Lists.newArrayList();
  public static List<WeightedRandomItemSG> GEM_WEIGHTS_DARK = Lists.newArrayList();
  public static int GLOW_ROSE_PER_CHUNK = 2;
  public static float CHAOS_NODES_PER_CHUNK = 0.006f;

  /*
   * Categories
   */

  static final String split = Configuration.CATEGORY_SPLITTER;
  public static final String CAT_MAIN = "main";
  public static final String CAT_DEBUG = CAT_MAIN + split + "debug";
  public static final String CAT_BLOCK = CAT_MAIN + split + "blocks";
  public static final String CAT_CHAOS = CAT_MAIN + split + "chaos";
  public static final String CAT_CONTROLS = CAT_MAIN + split + "controls";
  public static final String CAT_ENCHANTMENT = CAT_MAIN + split + "enchantment";
  public static final String CAT_GUI = CAT_MAIN + split + "gui";
  public static final String CAT_ITEM = CAT_MAIN + split + "items";
  public static final String CAT_MISC = CAT_MAIN + split + "misc";
  public static final String CAT_RECIPE = CAT_MAIN + split + "recipes";
  public static final String CAT_TOOL_PARTS = CAT_ITEM + split + "tool_parts";
  public static final String CAT_TOOLS = CAT_ITEM + split + "tools";
  public static final String CAT_TOOLTIPS = CAT_MAIN + split + "tooltips";
  public static final String CAT_WORLD_GEN = CAT_MAIN + split + "world generation";
  public static final String CAT_WORLD_GEN_GEM_WEIGHT = CAT_WORLD_GEN + split + "gem weights";

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

      final String catToolDisable = CAT_TOOLS + split + "disable";
      config.setCategoryRequiresMcRestart(catToolDisable, true);
      config.setCategoryComment(catToolDisable, "Disable the crafting of specific tool classes by"
          + " toggling the desired option to \"true\". Doing so will prevent ANY tool of that type"
          + " from being crafted (all tiers, mixed and non-mixed, and both base mod and add-on parts).");
      TOOL_DISABLE_SWORD = config.get(catToolDisable, "Sword", false).getBoolean();
      TOOL_DISABLE_KATANA = config.get(catToolDisable, "Katana", false).getBoolean();
      TOOL_DISABLE_SCEPTER = config.get(catToolDisable, "Scepter", false).getBoolean();
      TOOL_DISABLE_TOMAHAWK = config.get(catToolDisable, "Tomahawk", false).getBoolean();
      TOOL_DISABLE_PICKAXE = config.get(catToolDisable, "Pickaxe", false).getBoolean();
      TOOL_DISABLE_SHOVEL = config.get(catToolDisable, "Shovel", false).getBoolean();
      TOOL_DISABLE_AXE = config.get(catToolDisable, "Axe", false).getBoolean();
      TOOL_DISABLE_PAXEL = config.get(catToolDisable, "Paxel", false).getBoolean();
      TOOL_DISABLE_HOE = config.get(catToolDisable, "Hoe", false).getBoolean();
      TOOL_DISABLE_SICKLE = config.get(catToolDisable, "Sickle", false).getBoolean();
      TOOL_DISABLE_BOW = config.get(catToolDisable, "Bow", false).getBoolean();
      TOOL_DISABLE_SHIELD= config.get(catToolDisable, "Shield", false).getBoolean();

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
       * Chaos
       */

      config.setCategoryComment(CAT_CHAOS, "Options for the Chaos energy system");
      CHAOS_DIRECT_TRANSFER = loadBoolean("Direct Transfer", CAT_CHAOS, CHAOS_DIRECT_TRANSFER,
          "If true, Chaos transfer entities will not be spawned, the energy is just sent directly"
          + " to the target. This might help if your server is struggling with large numbers of"
          + " nodes and pylons.");

      /*
       * GUI
       */

      SHOW_BONUS_ARMOR_BAR = loadBoolean("Show Bonus Armor Bar", CAT_GUI,
          SHOW_BONUS_ARMOR_BAR,
          "Shows armor points beyond 20 on the bar as yellow armor pieces above the normal ones.");

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
       * World Generation
       */

      GLOW_ROSE_PER_CHUNK = loadInt("Flowers per Chunk", CAT_WORLD_GEN,
          GLOW_ROSE_PER_CHUNK, 0, 100,
          "The number of glow roses to attempt to spawn per chunk.");
      CHAOS_NODES_PER_CHUNK = config.getFloat("Chaos Nodes per Chunk", CAT_WORLD_GEN,
          CHAOS_NODES_PER_CHUNK, 0.0f, 8.0f,
          "The number of chaos nodes to try to spawn per chunk. If less than 1 (recommended), you"
              + " can think of this as the chance to spawn in a chunk.");

      WORLD_GEN_GEMS = new ConfigOptionOreGen("Gems (Overworld)", 0, 10.0f, 8, 5, 45);
      WORLD_GEN_GEMS.loadValue(config, CAT_WORLD_GEN);
      WORLD_GEN_GEMS_DARK = new ConfigOptionOreGen("Dark Gems (Nether)", -1, 12.5f, 10, 30, 100);
      WORLD_GEN_GEMS_DARK.loadValue(config, CAT_WORLD_GEN);
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
        if (gem.ordinal() < EnumGem.CARNELIAN.ordinal()) {
          GEM_WEIGHTS.add(item);
        } else {
          GEM_WEIGHTS_DARK.add(item);
        }
      }

      //@formatter:on
    } catch (Exception e) {
      System.out.println("Oh noes!!! Couldn't load configuration file properly!");
    }
  }

  public void loadModuleConfigs() {

    ModuleCoffee.loadConfig(config);
    ModuleEntityRandomEquipment.loadConfig(config);
    ModuleHolidayCheer.instance.loadConfig(config);
    ModuleAprilTricks.instance.loadConfig(config);
  }
}
