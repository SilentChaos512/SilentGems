package silent.gems.configuration;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import silent.gems.core.util.LogHelper;
import silent.gems.enchantment.ModEnchantments;
import silent.gems.lib.Names;

public class Config {

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
  public static ConfigOptionInt FOOD_SUPPORT_DURATION = new ConfigOptionInt("Food.SupportDuration",
      600);
  public static ConfigOptionInt GLOW_ROSE_LIGHT_LEVEL = new ConfigOptionInt("GlowRose.LightLevel",
      10);

  /*
   * World generation config settings
   */

  public static ConfigOptionInt WORLD_GEM_CLUSTER_COUNT = new ConfigOptionInt(
      "World.Gem.ClusterCount", 5);
  public static ConfigOptionInt WORLD_GEM_CLUSTER_SIZE = new ConfigOptionInt(
      "World.Gem.ClusterSize", 8);
  public static ConfigOptionInt WORLD_GEM_MAX_HEIGHT = new ConfigOptionInt("World.Gem.MaxHeight",
      40);
  public static ConfigOptionInt WORLD_CHAOS_ORE_CLUSTER_COUNT = new ConfigOptionInt(
      "World.ChaosOre.ClusterCount", 1);
  public static ConfigOptionInt WORLD_CHAOS_ORE_CLUSTER_SIZE = new ConfigOptionInt(
      "World.ChaosOre.ClusterSize", 18);
  public static ConfigOptionInt WORLD_CHAOS_ORE_MAX_HEIGHT = new ConfigOptionInt(
      "World.ChaosOre.MaxHeight", 20);
  public static ConfigOptionInt WORLD_CHAOS_ORE_RARITY = new ConfigOptionInt(
      "World.ChaosOre.Rarity", 2);
  public static ConfigOptionInt WORLD_FLOWERS_PER_CHUNK = new ConfigOptionInt(
      "World.FlowersPerChunk", 1);

  /*
   * Config Handler
   */

  private static Configuration c;

  /*
   * Config categories
   */
  public static final String CATEGORY_ENCHANTMENT = "enchantment";
  public static final String CATEGORY_WORLD = "world";
  public static final String CATEGORY_WORLD_GEN = CATEGORY_WORLD + Configuration.CATEGORY_SPLITTER
      + "generation";
  public static final String CATEGORY_BLOCK_PROPERTIES = "block" + Configuration.CATEGORY_SPLITTER
      + "properties";
  public static final String CATEGORY_ITEM_PROPERTIES = "item" + Configuration.CATEGORY_SPLITTER
      + "properties";

  public static void init(File file) {

    c = new Configuration(file);

    try {
      c.load();

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
      FOOD_SUPPORT_DURATION.loadValue(c, CATEGORY_ITEM_PROPERTIES,
          "The base duration for special effects from food");
      GLOW_ROSE_LIGHT_LEVEL.loadValue(c, CATEGORY_BLOCK_PROPERTIES,
          "The light level glow roses emit, must be between 0 and 15 inclusive.").validate();

      /*
       * Enchantment ids
       */
      ModEnchantments.MENDING_ID = getEnchantmentId(Names.MENDING,
          ModEnchantments.MENDING_ID_DEFAULT);

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
