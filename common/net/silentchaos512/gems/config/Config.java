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
import net.silentchaos512.gems.util.WeightedRandomItemSG;

public class Config {

  /*
   * Blocks
   */

  public static int GLOW_ROSE_LIGHT_LEVEL = 10;

  /*
   * Items
   */

  public static int FOOD_SUPPORT_DURATION = 600;
  public static float FOOD_SECRET_DONUT_CHANCE = 0.33f;
  public static float FOOD_SECRET_DONUT_TEXT_CHANCE = 0.6f;

  /*
   * Tools
   */

  // TODO

  /*
   * Recipes
   */

  // TODO

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
  public static final String CAT_MAIN = "Main";
  public static final String CAT_BLOCK = CAT_MAIN + split + "Blocks";
  public static final String CAT_CONTROLS = CAT_MAIN + split + "Controls";
  public static final String CAT_ITEM = CAT_MAIN + split + "Items";
  public static final String CAT_TOOLTIPS = CAT_MAIN + split + "Tooltips";
  public static final String CAT_WORLD_GEN = CAT_MAIN + split + "World Generation";
  public static final String CAT_WORLD_GEN_GEM_WEIGHT = CAT_WORLD_GEN + split + "Gem Weights";

  private static File configFile;
  private static Configuration c;

  public static void init(File file) {

    configFile = file;
    c = new Configuration(file, true);
    load();
  }

  public static void load() {

    try {
      //@formatter:off

      /*
       * Blocks
       */

      final String catGlowRose = CAT_BLOCK + split + "Glow Rose";
      GLOW_ROSE_LIGHT_LEVEL = c.getInt("Light Level", catGlowRose,
          GLOW_ROSE_LIGHT_LEVEL, 0, 15,
          "The light level glow roses emit.");

      /*
       * Items
       */

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
      WORLD_GEN_ENDER = new ConfigOptionOreGen("Ender Essence Ore", 1, 4.0f, 32, 10, 70);
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
