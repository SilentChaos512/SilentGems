package net.silentchaos512.gems.configuration;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.util.WeightedRandom;
import net.minecraftforge.common.config.Configuration;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.WeightedRandomItemSG;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

public class Config {

  /*
   * Blocks
   */
  public static int CHAOS_ESSENCE_PER_ORE = 1;
  public static int GLOW_ROSE_LIGHT_LEVEL = 10;
  public static int PYLON_BURNER_GENERATION_RATE = 10;
  public static int PYLON_PASSIVE_GENERATION_RATE = 1;
  public static float REDSTONE_TELEPORTER_SEARCH_RANGE = 2.0f;

  /*
   * Items
   */
  public static boolean ENCHANTMENT_TOKENS_ON_ANY_TOOL = true;
  public static int CHAOS_COAL_BURN_TIME = 6400;
  public static int RETURN_HOME_USE_TIME = 24;
  public static int RETURN_HOME_MAX_USES = 64;
  public static int CHAOS_GEM_MAX_MINI_PYLON = 5;
  public static int CHAOS_GEM_MAX_BUFFS = 3;
  public static int FOOD_SUPPORT_DURATION = 600;

  /*
   * Tools
   */
  public static int MINING_LEVEL_REGULAR = 2;
  public static int MINING_LEVEL_SUPER = 4;
  public static int MINING_LEVEL_FISH = 0;
  public static int MINING_LEVEL_IRON_TIP = 2;
  public static int MINING_LEVEL_DIAMOND_TIP = 3;
  public static int DURABILITY_BOOST_IRON_TIP = 128;
  public static int DURABILITY_BOOST_DIAMOND_TIP = 512;

  /*
   * Recipes
   */
  public static boolean RECIPE_TELEPORTER_DISABLED = false;
  public static boolean RECIPE_REDSTONE_TELEPORTER_DISABLED = false;
  public static boolean RECIPE_TELEPORTER_ANCHOR_DISABLED = false;

  /*
   * Misc
   */
  public static boolean HIDE_FLAVOR_TEXT_ALWAYS = false;
  public static boolean HIDE_FLAVOR_TEXT_UNTIL_SHIFT = true;

  /*
   * World generation config settings
   */

  public static int WORLD_GEM_CLUSTER_COUNT = 6;
  public static int WORLD_GEM_CLUSTER_SIZE = 8;
  public static int WORLD_GEM_MAX_HEIGHT = 40;
  public static int WORLD_CHAOS_ORE_CLUSTER_COUNT = 1;
  public static int WORLD_CHAOS_ORE_CLUSTER_SIZE = 20;
  public static int WORLD_CHAOS_ORE_MAX_HEIGHT = 20;
  public static int WORLD_CHAOS_ORE_RARITY = 1;
  public static int WORLD_FLOWERS_PER_CHUNK = 2;

  public static ArrayList<WeightedRandomItemSG> GEM_WEIGHTS = new ArrayList<WeightedRandomItemSG>();

  /*
   * Config Handler
   */

  private static Configuration c;

  /*
   * Config categories
   */
  public static final String CATEGORY_KEYBIND = "keybindings";
  public static final String CATEGORY_AUDIO = "audio";
  public static final String CATEGORY_ENCHANTMENT = "enchantment";
  public static final String CATEGORY_RECIPE = "recipes";
  public static final String CATEGORY_WORLD = "world";
  public static final String CATEGORY_WORLD_GEN = CATEGORY_WORLD + Configuration.CATEGORY_SPLITTER
      + "generation";
  public static final String CATEGORY_WORLD_STRUCTURE = CATEGORY_WORLD
      + Configuration.CATEGORY_SPLITTER + "structure";
  public static final String CATEGORY_BLOCK_PROPERTIES = "block" + Configuration.CATEGORY_SPLITTER
      + "properties";
  public static final String CATEGORY_ITEM_PROPERTIES = "item" + Configuration.CATEGORY_SPLITTER
      + "properties";
  public static final String CATEGORY_DURABILITY = "item" + Configuration.CATEGORY_SPLITTER
      + "durability";

  public static void init(File file) {

    c = new Configuration(file);

    try {
      c.load();

      /*
       * Blocks
       */
      GLOW_ROSE_LIGHT_LEVEL = c.getInt("GlowRose.LightLevel", CATEGORY_BLOCK_PROPERTIES,
          GLOW_ROSE_LIGHT_LEVEL, 0, 15, "The light level glow roses emit.");
      PYLON_BURNER_GENERATION_RATE = c.getInt("ChaosPylon.Burner.GenerationRate",
          CATEGORY_BLOCK_PROPERTIES, PYLON_BURNER_GENERATION_RATE, 1, 100,
          "The energy generation rate and the amount of energy produced per unit of burn time of the fuel.");
      PYLON_PASSIVE_GENERATION_RATE = c.getInt("ChaosPylon.Passive.GenerationRate",
          CATEGORY_BLOCK_PROPERTIES, PYLON_PASSIVE_GENERATION_RATE, 0, Integer.MAX_VALUE,
          "The energy generation rate of passive pylons, in chaos per tick.");
      REDSTONE_TELEPORTER_SEARCH_RANGE = c.getFloat("RedstoneTeleporter.SearchRadius",
          CATEGORY_BLOCK_PROPERTIES, REDSTONE_TELEPORTER_SEARCH_RANGE, 0.0f, 32.0f,
          "The radius (in blocks) in which redstone-powered teleporters will select entities to teleport.");

      /*
       * Items
       */
      CHAOS_ESSENCE_PER_ORE = c.getInt("ChaosEssence.PerOre", CATEGORY_ITEM_PROPERTIES,
          CHAOS_ESSENCE_PER_ORE, 1, 8,
          "The number of Chaos Essence you get for smelting one Chaos Ore");
      FOOD_SUPPORT_DURATION = c.getInt("Food.SupportDuration", CATEGORY_ITEM_PROPERTIES,
          FOOD_SUPPORT_DURATION, 0, 6000, "The base duration for special effects from food");
      ENCHANTMENT_TOKENS_ON_ANY_TOOL = c.getBoolean("EnchantmentToken.CanApplyToAnyTool",
          CATEGORY_ITEM_PROPERTIES, ENCHANTMENT_TOKENS_ON_ANY_TOOL,
          "Allows Enchantment Tokens to be used on appropriate tools from other mods and vanilla.");
      CHAOS_COAL_BURN_TIME = c.getInt("ChaosCoal.BurnTime", CATEGORY_ITEM_PROPERTIES,
          CHAOS_COAL_BURN_TIME, 0, Integer.MAX_VALUE,
          "The burn time of Chaos Coal. Vanilla coal is 1600.");
      RETURN_HOME_USE_TIME = c.getInt("ReturnHome.UseTime", CATEGORY_ITEM_PROPERTIES,
          RETURN_HOME_USE_TIME, 0, 200,
          "The number of ticks a player must hold right-click to activate the Return Home.");
      RETURN_HOME_MAX_USES = c.getInt("ReturnHome.MaxUses", CATEGORY_ITEM_PROPERTIES,
          RETURN_HOME_MAX_USES, 0, Integer.MAX_VALUE,
          "The number of times a Return Home Charm can be used before breaking. Set to 0 for infinite.");
      CHAOS_GEM_MAX_BUFFS = c.getInt("ChaosGem.MaxBuffsPerGem", CATEGORY_ITEM_PROPERTIES,
          CHAOS_GEM_MAX_BUFFS, 1, 8,
          "The maximum number of unique effects that can be put on a Chaos Gem");
      CHAOS_GEM_MAX_MINI_PYLON = c.getInt("ChaosGem.MaxMiniPylons", CATEGORY_ITEM_PROPERTIES,
          CHAOS_GEM_MAX_MINI_PYLON, 0, Integer.MAX_VALUE,
          "The most mini pylons that can be added to a chaos gem.");

      /*
       * Recipes
       */
      RECIPE_TELEPORTER_DISABLED = c.getBoolean("Teleporter.Disabled", CATEGORY_RECIPE,
          RECIPE_TELEPORTER_DISABLED, "Set to true to disable teleporter recipes");
      RECIPE_REDSTONE_TELEPORTER_DISABLED = c.getBoolean("RedstoneTeleporter.Disabled",
          CATEGORY_RECIPE, RECIPE_REDSTONE_TELEPORTER_DISABLED,
          "Set to true to disable redstone teleporter recipes.");
      RECIPE_TELEPORTER_ANCHOR_DISABLED = c.getBoolean("TeleporterAnchor.Disabled", CATEGORY_RECIPE,
          RECIPE_TELEPORTER_ANCHOR_DISABLED, "Set to true to disable teleporter anchor recipes.");

      /*
       * Tools
       */
      MINING_LEVEL_REGULAR = c.getInt("Tool.MiningLevel.Regular", CATEGORY_ITEM_PROPERTIES,
          MINING_LEVEL_REGULAR, 0, 100, "The mining level of regular gem tools.");
      MINING_LEVEL_SUPER = c.getInt("Tool.MiningLevel.Super", CATEGORY_ITEM_PROPERTIES,
          MINING_LEVEL_SUPER, 0, 100, "The mining level of supercharged gem tools.");
      MINING_LEVEL_FISH = c.getInt("Tool.MiningLevel.Fish", CATEGORY_ITEM_PROPERTIES,
          MINING_LEVEL_FISH, 0, 100, "The mining level of the gag fish tools.");
      MINING_LEVEL_IRON_TIP = c.getInt("Tool.MiningLevel.IronTip", CATEGORY_ITEM_PROPERTIES,
          MINING_LEVEL_IRON_TIP, 0, 100,
          "The mining level of tools with the iron-tipped upgrade (if it's not already higher).");
      MINING_LEVEL_DIAMOND_TIP = c.getInt("Tool.MiningLevel.DiamondTip", CATEGORY_ITEM_PROPERTIES,
          MINING_LEVEL_DIAMOND_TIP, 0, 100,
          "The mining level of tools with the diamond-tipped upgrade (if it's not already higher).");
      DURABILITY_BOOST_IRON_TIP = c.getInt("Tool.DurabilityBoost.Iron", CATEGORY_ITEM_PROPERTIES,
          DURABILITY_BOOST_IRON_TIP, 0, Short.MAX_VALUE,
          "The value added to the durability (max damage) of tools with the 'iron-tipped' upgrade.");
      DURABILITY_BOOST_DIAMOND_TIP = c.getInt("Tool.DurabilityBoost.Diamond",
          CATEGORY_ITEM_PROPERTIES, DURABILITY_BOOST_DIAMOND_TIP, 0, Short.MAX_VALUE,
          "The value added to the durability (max damage) of tools with the 'diamond-tipped' upgrade.");

      /*
       * Misc
       */
      HIDE_FLAVOR_TEXT_ALWAYS = c.getBoolean("Tooltips.HideFlavorText.Always",
          CATEGORY_ITEM_PROPERTIES, HIDE_FLAVOR_TEXT_ALWAYS,
          "Always hide the potentially funny, but useless item descriptions.");
      HIDE_FLAVOR_TEXT_UNTIL_SHIFT = c.getBoolean("Tooltips.HideFlavorText.UntilShift",
          CATEGORY_ITEM_PROPERTIES, HIDE_FLAVOR_TEXT_UNTIL_SHIFT,
          "Hide the flavor text until shift is pressed.");

      /*
       * Enchantment ids
       */
      ModEnchantments.MENDING_ID = getEnchantmentId(Names.MENDING,
          ModEnchantments.MENDING_ID_DEFAULT);
      ModEnchantments.AOE_ID = getEnchantmentId(Names.AOE, ModEnchantments.AOE_ID_DEFAULT);

      /*
       * World gen
       */
      WORLD_GEM_CLUSTER_COUNT = c.getInt("World.Gem.ClusterCount", CATEGORY_WORLD_GEN,
          WORLD_GEM_CLUSTER_COUNT, 0, 1000, CATEGORY_WORLD_GEN);
      WORLD_GEM_CLUSTER_SIZE = c.getInt("World.Gem.ClusterSize", CATEGORY_WORLD_GEN,
          WORLD_GEM_CLUSTER_SIZE, 0, 1000, CATEGORY_WORLD_GEN);
      WORLD_GEM_MAX_HEIGHT = c.getInt("World.Gem.MaxHeight", CATEGORY_WORLD_GEN,
          WORLD_GEM_MAX_HEIGHT, 0, 255, CATEGORY_WORLD_GEN);
      WORLD_CHAOS_ORE_CLUSTER_COUNT = c.getInt("World.ChaosOre.ClusterCount", CATEGORY_WORLD_GEN,
          WORLD_CHAOS_ORE_CLUSTER_COUNT, 0, 1000, CATEGORY_WORLD_GEN);
      WORLD_CHAOS_ORE_CLUSTER_SIZE = c.getInt("World.ChaosOre.ClusterSize", CATEGORY_WORLD_GEN,
          WORLD_CHAOS_ORE_CLUSTER_SIZE, 0, 1000, CATEGORY_WORLD_GEN);
      WORLD_CHAOS_ORE_MAX_HEIGHT = c.getInt("World.ChaosOre.MaxHeight", CATEGORY_WORLD_GEN,
          WORLD_CHAOS_ORE_MAX_HEIGHT, 0, 255, CATEGORY_WORLD_GEN);
      WORLD_CHAOS_ORE_RARITY = c.getInt("World.ChaosOre.Rarity", CATEGORY_WORLD_GEN,
          WORLD_CHAOS_ORE_RARITY, 1, 1000, CATEGORY_WORLD_GEN);
      WORLD_FLOWERS_PER_CHUNK = c.getInt("World.FlowersPerChunk", CATEGORY_WORLD_GEN,
          WORLD_FLOWERS_PER_CHUNK, 0, 1000, CATEGORY_WORLD_GEN);

      // Gem weights
      for (EnumGem gem : EnumGem.values()) {
        int k = c.getInt("World.Gem.Weight" + gem.name, CATEGORY_WORLD_GEN, 10, 1, 1000,
            "How like this gem is to be selected when spawning a vein of gem ore");
        GEM_WEIGHTS.add(new WeightedRandomItemSG(k, gem.id));
      }
    } catch (Exception e) {
      LogHelper.severe("Oh noes!!! Couldn't load configuration file properly!");
    } finally {
      c.save();
    }
  }

  public static int getEnchantmentId(String name, int default_id) {

    return c.get(CATEGORY_ENCHANTMENT, name, default_id).getInt(default_id);
  }

  public static int getGeneralInt(String category, String name, int default_value, String comment) {

    return c.get(category, name, default_value, comment).getInt(default_value);
  }

  public static void save() {

    c.save();
  }
}
