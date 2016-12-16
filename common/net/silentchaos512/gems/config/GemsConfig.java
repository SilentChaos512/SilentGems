package net.silentchaos512.gems.config;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.module.ModuleCoffee;
import net.silentchaos512.gems.lib.module.ModuleEntityRandomEquipment;
import net.silentchaos512.gems.lib.module.ModuleHolidayCheer;
import net.silentchaos512.gems.util.WeightedRandomItemSG;

public class GemsConfig {

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

  public static boolean TOOL_DISABLE_SWORD;
  public static boolean TOOL_DISABLE_KATANA;
  public static boolean TOOL_DISABLE_SCEPTER;
  public static boolean TOOL_DISABLE_TOMAHAWK;
  public static boolean TOOL_DISABLE_PICKAXE;
  public static boolean TOOL_DISABLE_SHOVEL;
  public static boolean TOOL_DISABLE_AXE;
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

  private static Configuration c;

  public static void init(File file) {

    c = new Configuration(file);
    load();
  }

  public static void load() {

    try {
      //@formatter:off

      /*
       * Debug
       */

      c.setCategoryComment(CAT_DEBUG, "Options for debugging. Generally, you should leave these "
          + "alone unless I tell you to change them. Enabling debug options will likely result in "
          + "log spam, but may help me track down issues.");

      DEBUG_MODE = c.getBoolean("Debug Mode", CAT_DEBUG,
          DEBUG_MODE,
          "Generic debug mode option. May add text to GUIs and WIT HUD.");

      DEBUG_LOG_POTS_AND_LIGHTS = c.getBoolean("Log Pots and Lights", CAT_DEBUG,
          DEBUG_LOG_POTS_AND_LIGHTS,
          "Logs the existence of Chaos Flower Pots and Phantom Lights. Their tile entities, to " 
              + "be more precise. Also lists the position of each one.");
      DEBUG_LOT_POTS_AND_LIGHTS_DELAY = c.getInt("Log Pots and Lights - Delay", CAT_DEBUG,
          DEBUG_LOT_POTS_AND_LIGHTS_DELAY, 600, 72000,
          "Pot and Light logging will occur every this many ticks. 1200 ticks = 1 minute.");

      /*
       * Blocks
       */

      final String catGlowRose = CAT_BLOCK + split + "Glow Rose";
      GLOW_ROSE_LIGHT_LEVEL = c.getInt("Light Level", catGlowRose,
          GLOW_ROSE_LIGHT_LEVEL, 0, 15,
          "The light level glow roses emit.");

      final String catTeleporter = CAT_BLOCK + split + "Teleporter";
      TELEPORTER_ALLOW_DUMB = c.getBoolean("Allow Dumb Teleporters", catTeleporter,
          TELEPORTER_ALLOW_DUMB,
          "Allows teleports to happen even if the destination teleporter has been removed.");
      TELEPORTER_COST_CROSS_DIMENSION = c.getInt("Chaos Cost Cross Dimension", catTeleporter,
          TELEPORTER_COST_CROSS_DIMENSION, 0, Integer.MAX_VALUE,
          "The amount of Chaos charged for traveling between dimensions.");
      TELEPORTER_COST_PER_BLOCK = c.getInt("Chaos Cost Per Block", catTeleporter,
          TELEPORTER_COST_PER_BLOCK, 0, Integer.MAX_VALUE,
          "The amount of Chaos charged per block traveled.");
      TELEPORTER_FREE_RANGE = c.getInt("Free Range", catTeleporter,
          TELEPORTER_FREE_RANGE, 0, Integer.MAX_VALUE,
          "The distance that can be teleported for free.");
      TELEPORTER_MAX_CHARGE = c.getInt("Max Chaos", catTeleporter,
          TELEPORTER_MAX_CHARGE, 0, Integer.MAX_VALUE,
          "The maximum amount of Chaos a teleporter can store.");

      /*
       * Items
       */

      BURN_TIME_CHAOS_COAL = c.getInt("Chaos Coal Burn Time", CAT_ITEM,
          BURN_TIME_CHAOS_COAL, 0, Integer.MAX_VALUE,
          "The burn time of Chaos Coal. Regular coal is 1600 ticks.");

      // Food
      final String catFood = CAT_ITEM + split + "Food";
      FOOD_SUPPORT_DURATION = c.getInt("Support Duration", catFood,
          FOOD_SUPPORT_DURATION, 0, 72000,
          "The base duration of potion effects from food. The actual duration will vary by effect.");
      FOOD_SECRET_DONUT_CHANCE = c.getFloat("Secret Donut Effect Chance", catFood,
          FOOD_SECRET_DONUT_CHANCE, 0.0f, 1.0f,
          "The chance of secret donuts giving potion effects.");
      FOOD_SECRET_DONUT_TEXT_CHANCE = c.getFloat("Secret Donut Text Chance", catFood,
          FOOD_SECRET_DONUT_TEXT_CHANCE, 0.0f, 1.0f,
          "The chance of secrets donuts putting weird text in your chat.");

      // Return Home Charm
      final String catReturnHome = CAT_ITEM + split + "Return Home Charm";
      RETURN_HOME_USE_TIME = c.getInt("Use Time", catReturnHome,
          RETURN_HOME_USE_TIME, 0, Integer.MAX_VALUE,
          "The number of ticks the Return Home Charm must be 'charged' to use. Set to 0 for instant use.");
      RETURN_HOME_USE_COST = c.getInt("Use Cost", catReturnHome,
          RETURN_HOME_USE_COST, 0, Integer.MAX_VALUE,
          "The amount of Chaos required to teleport.");
      RETURN_HOME_MAX_CHARGE = c.getInt("Max Charge", catReturnHome,
          RETURN_HOME_MAX_CHARGE, 0, Integer.MAX_VALUE,
          "The maximum amount of Chaos a charm can hold.");
      
      /*
       * Tools
       */
      
      VARIETY_BONUS = c.getFloat("Variety Bonus", CAT_TOOLS,
          VARIETY_BONUS, 0f, 1f,
          "The \"variety bonus\" for mixed-material tools and armor. Default is a 7.5% bonus for each"
          + " additional unique part.");
      TOMAHAWK_MAX_AMMO = c.getInt("Tomahawk Max Ammo", CAT_TOOLS,
          TOMAHAWK_MAX_AMMO, 0, Byte.MAX_VALUE,
          "The maximum \"ammo\" for tomahawks. This is the number that can be thrown before retrieval/repairs"
          + " are required. Setting this to 0 will make it so tomahawks cannot be thrown.");
      TOMAHAWK_AMMO_PER_MAT = c.getInt("Tomahawk Ammo per Material", CAT_TOOLS,
          TOMAHAWK_AMMO_PER_MAT, 0, Byte.MAX_VALUE,
          "The \"ammo\" restored by each material (gem, etc.) when decorating a tomahawk.");

      final String catToolDisable = CAT_TOOLS + split + "disable";
      c.setCategoryRequiresMcRestart(catToolDisable, true);
      c.setCategoryComment(catToolDisable, "Disable the crafting of specific tool classes by"
          + " toggling the desired option to \"true\". Doing so will prevent ANY tool of that type"
          + " from being crafted (all tiers, mixed and non-mixed, and both base mod and add-on parts).");
      TOOL_DISABLE_SWORD = c.get(catToolDisable, "Sword", false).getBoolean();
      TOOL_DISABLE_KATANA = c.get(catToolDisable, "Katana", false).getBoolean();
      TOOL_DISABLE_SCEPTER = c.get(catToolDisable, "Scepter", false).getBoolean();
      TOOL_DISABLE_TOMAHAWK = c.get(catToolDisable, "Tomahawk", false).getBoolean();
      TOOL_DISABLE_PICKAXE = c.get(catToolDisable, "Pickaxe", false).getBoolean();
      TOOL_DISABLE_SHOVEL = c.get(catToolDisable, "Shovel", false).getBoolean();
      TOOL_DISABLE_AXE = c.get(catToolDisable, "Axe", false).getBoolean();
      TOOL_DISABLE_HOE = c.get(catToolDisable, "Hoe", false).getBoolean();
      TOOL_DISABLE_SICKLE = c.get(catToolDisable, "Sickle", false).getBoolean();
      TOOL_DISABLE_BOW = c.get(catToolDisable, "Bow", false).getBoolean();
      TOOL_DISABLE_SHIELD= c.get(catToolDisable, "Shield", false).getBoolean();

      /*
       * Tool Parts
       */

      String catPartDisable = CAT_TOOL_PARTS + split + "disable";
      c.setCategoryRequiresMcRestart(catPartDisable, true);
      c.setCategoryComment(catPartDisable, "Disable specific tool parts or entire tiers. As a reminder:"
          + " Mundane includes flint, Regular is ordinary gems, and Super is supercharged gems. Hover"
          + " over the item for the tool part and hold Ctrl to see the tier.");
      PART_DISABLE_ALL_MUNDANE = c.get(catPartDisable, "All Mundane Tier", false).getBoolean();
      PART_DISABLE_ALL_REGULAR = c.get(catPartDisable, "All Regular Tier", false).getBoolean();
      PART_DISABLE_ALL_SUPER = c.get(catPartDisable, "All Super Tier", false).getBoolean();
      String[] partBlacklistTemp = c.getStringList("Blacklist", catPartDisable, new String[0],
          "You can disable individual parts by listing the part keys here, if disabling groups with"
          + " the other settings doesn't cut it. Hover over the item in-game and hold Ctrl+Shift to"
          + " reveal the part key.");
      for (String str : partBlacklistTemp)
        PART_BLACKLIST.add(str);

      /*
       * GUI
       */

      SHOW_BONUS_ARMOR_BAR = c.getBoolean("Show Bonus Armor Bar", CAT_GUI,
          SHOW_BONUS_ARMOR_BAR,
          "Shows armor points beyond 20 on the bar as yellow armor pieces above the normal ones.");

      /*
       * Recipes
       */

      c.setCategoryRequiresMcRestart(CAT_RECIPE, true);

      RECIPE_TELEPORTER_DISABLE = c.getBoolean("Disable Regular Teleporter Recipes", CAT_RECIPE,
          RECIPE_TELEPORTER_DISABLE,
          "Disable recipes for regular gem teleporters.");
      RECIPE_TELEPORTER_ANCHOR_DISABLE = c.getBoolean("Disable Teleporter Anchor Recipes", CAT_RECIPE,
          RECIPE_TELEPORTER_ANCHOR_DISABLE,
          "Disable recipes for teleporter anchors.");
      RECIPE_TELEPORTER_REDSTONE_DISABLE = c.getBoolean("Disable Redstone Teleporter Recipes", CAT_RECIPE,
          RECIPE_TELEPORTER_REDSTONE_DISABLE,
          "Disable recipes for redstone gem teleporters.");

      RECIPE_TOKEN_FROST_WALKER_DISABLE = c.getBoolean("Disable Frost Walker Token Recipe", CAT_RECIPE,
          RECIPE_TOKEN_FROST_WALKER_DISABLE,
          "Disables recipes for Frost Walker enchantment tokens.");
      RECIPE_TOKEN_MENDING_DISABLE = c.getBoolean("Disable Mending Token Recipe", CAT_RECIPE,
          RECIPE_TOKEN_MENDING_DISABLE,
          "Disables recipes for Mending enchantment tokens.");

      /*
       * Misc
       */

      HIDE_FLAVOR_TEXT_ALWAYS = c.getBoolean("Hide Flavor Text - Always", CAT_TOOLTIPS,
          HIDE_FLAVOR_TEXT_ALWAYS,
          "Always hide the potentially funny, but useless item descriptions.");
      HIDE_FLAVOR_TEXT_UNTIL_SHIFT = c.getBoolean("Hide Flavor Text - Until Shift", CAT_TOOLTIPS,
          HIDE_FLAVOR_TEXT_UNTIL_SHIFT,
          "Hide the flavor text until shift is pressed.");

      // Controls - right-click to place
      final String catRctp = CAT_CONTROLS + Configuration.CATEGORY_SPLITTER + "Right-click to place";
      c.setCategoryComment(catRctp, "Mining tools have the ability to place blocks in the slot "
          + "after them (or in slot 9 if that doesn't work) by right-clicking.");
      RIGHT_CLICK_TO_PLACE_ENABLED = c.getBoolean("Enabled", catRctp,
          RIGHT_CLICK_TO_PLACE_ENABLED,
          "If set to false, the ability of mining tools to place blocks by right-clicking will be completely disabled.");
      RIGHT_CLICK_TO_PLACE_ON_SNEAK_ONLY = c.getBoolean("Only When Sneaking", catRctp,
          RIGHT_CLICK_TO_PLACE_ON_SNEAK_ONLY,
          "If set to true and right-click to place is enabled, this ability will only activate "
              + "while sneaking (holding shift, normally).");

      /*
       * World Generation
       */

      GLOW_ROSE_PER_CHUNK = c.getInt("Flowers per Chunk", CAT_WORLD_GEN,
          GLOW_ROSE_PER_CHUNK, 0, 100,
          "The number of glow roses to attempt to spawn per chunk.");
      CHAOS_NODES_PER_CHUNK = c.getFloat("Chaos Nodes per Chunk", CAT_WORLD_GEN,
          CHAOS_NODES_PER_CHUNK, 0.0f, 8.0f,
          "The number of chaos nodes to try to spawn per chunk. If less than 1 (recommended), you"
              + " can think of this as the chance to spawn in a chunk.");

      WORLD_GEN_GEMS = new ConfigOptionOreGen("Gems (Overworld)", 0, 10.0f, 8, 5, 45);
      WORLD_GEN_GEMS.loadValue(c, CAT_WORLD_GEN);
      WORLD_GEN_GEMS_DARK = new ConfigOptionOreGen("Dark Gems (Nether)", -1, 12.5f, 10, 30, 100);
      WORLD_GEN_GEMS_DARK.loadValue(c, CAT_WORLD_GEN);
      WORLD_GEN_CHAOS = new ConfigOptionOreGen("Chaos Ore", 0, 1.75f, 16, 5, 20);
      WORLD_GEN_CHAOS.loadValue(c, CAT_WORLD_GEN);
      WORLD_GEN_ENDER = new ConfigOptionOreGen("Ender Essence Ore", 1, 1.0f, 32, 10, 70);
      WORLD_GEN_ENDER.loadValue(c, CAT_WORLD_GEN);

      // Gem weights
      final int weightMax = 1000;
      c.setCategoryComment(CAT_WORLD_GEN_GEM_WEIGHT,
          "The spawn weights of the gems. If two gems have different weights, the gem with the\n"
              + "higher weight is more likely to be selected when placing gems in the world.\n"
              + "Just increasing weights will NOT increase the number of gems that spawn!\n"
              + "Values must be between 1 and " + weightMax + ", inclusive.");
      for (EnumGem gem : EnumGem.values()) {
        int k = c.get(CAT_WORLD_GEN_GEM_WEIGHT, gem.getGemName(), 10).getInt();
        k = MathHelper.clamp_int(k, 1, weightMax);
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

  public static void loadModuleConfigs() {

    ModuleCoffee.loadConfig(c);
    ModuleEntityRandomEquipment.loadConfig(c);
    ModuleHolidayCheer.instance.loadConfig(c);
  }

  public static void save() {

    if (c.hasChanged()) {
      c.save();
    }
  }

  public static ConfigCategory getCategory(String str) {

    return c.getCategory(str);
  }

  public static Configuration getConfiguration() {

    return c;
  }

  public static List<IConfigElement> getConfigElements() {

    return new ConfigElement(getCategory(CAT_MAIN)).getChildElements();
  }
}
