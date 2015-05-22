package net.silentchaos512.gems.configuration;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.lib.Names;

public class Config {

  /*
   * Tools
   */
  public static int miningLevelRegular = 2;
  public static int miningLevelSuper = 4;
  public static int miningLevelFish = 3;

  /*
   * Misc
   */

  public static ConfigOptionInt CHAOS_ESSENCE_PER_ORE = new ConfigOptionInt("ChaosEssence.PerOre",
      1);
  public static ConfigOptionDouble CHAOS_GEM_CAPACITY_UPGRADE_INCREASE = new ConfigOptionDouble(
      "ChaosGem.CapacityUpgradeIncrease", 0.25);
  public static ConfigOptionDouble CHAOS_GEM_FLIGHT_MAX_SPEED = new ConfigOptionDouble(
      "ChaosGem.FlightMaxSpeed", 25.0);
  public static ConfigOptionDouble CHAOS_GEM_FLIGHT_THRUST = new ConfigOptionDouble(
      "ChaosGem.FlightThrust", 0.1);
  public static ConfigOptionInt CHAOS_GEM_RECHARGE_RATE = new ConfigOptionInt(
      "ChaosGem.RechargeRate", 40);
  public static ConfigOptionInt CHAOS_GEM_MAX_BUFFS = new ConfigOptionInt(
      "ChaosGem.MaxBuffsPerGem", 3);
  public static ConfigOptionInt CHAOS_GEM_MAX_CHARGE = new ConfigOptionInt("ChaosGem.MaxCharge",
      10000);
  public static ConfigOptionBoolean ENCHANTMENT_TOKENS_ON_ANY_TOOL = new ConfigOptionBoolean(
      "EnchantmentToken.CanApplyToAnyTool", true);
  public static ConfigOptionInt FOOD_SUPPORT_DURATION = new ConfigOptionInt("Food.SupportDuration",
      600);
  public static ConfigOptionInt GLOW_ROSE_LIGHT_LEVEL = new ConfigOptionInt("GlowRose.LightLevel",
      10);
  public static ConfigOptionInt HOLDING_GEM_MAX_ITEMS = new ConfigOptionInt("HoldingGem.MaxItems",
      4096);

  /*
   * World generation config settings
   */

  public static ConfigOptionInt WORLD_GEM_CLUSTER_COUNT = new ConfigOptionInt(
      "World.Gem.ClusterCount", 6);
  public static ConfigOptionInt WORLD_GEM_CLUSTER_SIZE = new ConfigOptionInt(
      "World.Gem.ClusterSize", 8);
  public static ConfigOptionInt WORLD_GEM_MAX_HEIGHT = new ConfigOptionInt("World.Gem.MaxHeight",
      40);
  public static ConfigOptionInt WORLD_CHAOS_ORE_CLUSTER_COUNT = new ConfigOptionInt(
      "World.ChaosOre.ClusterCount", 1);
  public static ConfigOptionInt WORLD_CHAOS_ORE_CLUSTER_SIZE = new ConfigOptionInt(
      "World.ChaosOre.ClusterSize", 20);
  public static ConfigOptionInt WORLD_CHAOS_ORE_MAX_HEIGHT = new ConfigOptionInt(
      "World.ChaosOre.MaxHeight", 20);
  public static ConfigOptionInt WORLD_CHAOS_ORE_RARITY = new ConfigOptionInt(
      "World.ChaosOre.Rarity", 1);
  public static ConfigOptionInt WORLD_FLOWERS_PER_CHUNK = new ConfigOptionInt(
      "World.FlowersPerChunk", 1);

  /*
   * Config Handler
   */

  private static Configuration c;

  /*
   * Config categories
   */
  public static final String CATEGORY_KEYBIND = "keybindings";
  public static final String CATEGORY_GRAPHICS = "graphics";
  public static final String CATEGORY_AUDIO = "audio";
  public static final String CATEGORY_ENCHANTMENT = "enchantment";
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
       * Tools
       */
      miningLevelRegular = c.getInt("Tool.MiningLevel.Regular", CATEGORY_ITEM_PROPERTIES,
          miningLevelRegular, 0, 100, "The mining level of regular gem tools.");
      miningLevelSuper = c.getInt("Tool.MiningLevel.Super", CATEGORY_ITEM_PROPERTIES,
          miningLevelSuper, 0, 100, "The mining level of supercharged gem tools.");
      miningLevelFish = c.getInt("Tool.MiningLevel.Fish", CATEGORY_ITEM_PROPERTIES,
          miningLevelFish, 0, 100, "The mining level of the gag fish tools.");

      /*
       * Misc
       */
      CHAOS_ESSENCE_PER_ORE.loadValue(c, CATEGORY_ITEM_PROPERTIES,
          "The number of Chaos Essence you get for smelting one Chaos Ore").validate();
      CHAOS_GEM_CAPACITY_UPGRADE_INCREASE
          .loadValue(c, CATEGORY_ITEM_PROPERTIES,
              "The capacity increase (as a fraction) for each level of the Capacity upgrade on a Chaos Gem");
      CHAOS_GEM_FLIGHT_MAX_SPEED.loadValue(c, CATEGORY_ITEM_PROPERTIES).validate();
      CHAOS_GEM_FLIGHT_THRUST.loadValue(c, CATEGORY_ITEM_PROPERTIES).validate();
      CHAOS_GEM_MAX_BUFFS.loadValue(c, CATEGORY_ITEM_PROPERTIES,
          "The number of unique upgrades you can put on a Chaos Gem").validate();
      CHAOS_GEM_MAX_CHARGE.loadValue(c, CATEGORY_ITEM_PROPERTIES,
          "The base maximum charge level for Chaos Gems").validate();
      CHAOS_GEM_RECHARGE_RATE.loadValue(c, CATEGORY_ITEM_PROPERTIES,
          "The amount of charge a Chaos Gem gains for every second deactivated.");
      ENCHANTMENT_TOKENS_ON_ANY_TOOL.loadValue(c, CATEGORY_ITEM_PROPERTIES,
          "Allows Enchantment Tokens to be used on appropriate tools from other mods.");
      FOOD_SUPPORT_DURATION.loadValue(c, CATEGORY_ITEM_PROPERTIES,
          "The base duration for special effects from food");
      GLOW_ROSE_LIGHT_LEVEL.loadValue(c, CATEGORY_BLOCK_PROPERTIES,
          "The light level glow roses emit, must be between 0 and 15 inclusive.").validate();
      HOLDING_GEM_MAX_ITEMS.loadValue(c, CATEGORY_ITEM_PROPERTIES,
          "The number of blocks the Holding Gem can store.");

      /*
       * Enchantment ids
       */
      ModEnchantments.MENDING_ID = getEnchantmentId(Names.MENDING,
          ModEnchantments.MENDING_ID_DEFAULT);
      ModEnchantments.AOE_ID = getEnchantmentId(Names.AOE, ModEnchantments.AOE_ID_DEFAULT);

      /*
       * World gen
       */
      WORLD_GEM_CLUSTER_COUNT.loadValue(c, CATEGORY_WORLD_GEN);
      WORLD_GEM_CLUSTER_SIZE.loadValue(c, CATEGORY_WORLD_GEN);
      WORLD_GEM_MAX_HEIGHT.loadValue(c, CATEGORY_WORLD_GEN);
      WORLD_CHAOS_ORE_CLUSTER_COUNT.loadValue(c, CATEGORY_WORLD_GEN);
      WORLD_CHAOS_ORE_CLUSTER_SIZE.loadValue(c, CATEGORY_WORLD_GEN);
      WORLD_CHAOS_ORE_MAX_HEIGHT.loadValue(c, CATEGORY_WORLD_GEN);
      WORLD_CHAOS_ORE_RARITY.loadValue(c, CATEGORY_WORLD_GEN).validate();
      WORLD_FLOWERS_PER_CHUNK.loadValue(c, CATEGORY_WORLD_GEN);
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
