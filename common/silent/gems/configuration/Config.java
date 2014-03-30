package silent.gems.configuration;

import java.io.File;

import net.minecraftforge.common.Configuration;
import silent.gems.core.util.LogHelper;
import silent.gems.enchantment.ModEnchantments;
import silent.gems.lib.Names;

public class Config {

    /*
     * Misc
     */
    
    public static ConfigOptionDouble CHAOS_GEM_FLIGHT_MAX_SPEED = new ConfigOptionDouble("ChaosGem.FlightMaxSpeed", 25.0);
    public static ConfigOptionInt CHAOS_GEM_MAX_BUFFS = new ConfigOptionInt("ChaosGem.MaxBuffsPerGem", 2);
    public static ConfigOptionInt CHAOS_GEM_MAX_CHARGE = new ConfigOptionInt("ChaosGem.MaxCharge", 1200);
    public static ConfigOptionInt FOOD_SUPPORT_DURATION = new ConfigOptionInt("Food.SupportDuration", 600);
    
    /*
     * Graphics
     */
    
    // Nothing... yet
    
    /*
     * Audio
     */
    
    // Nothing... yet
    
    /*
     * Sigil config settings
     */

    public static ConfigOptionInt SIGIL_USE_DURATION = new ConfigOptionInt("Sigil.BaseUseDuration", 30);
    public static ConfigOptionInt SIGIL_PROJECTILE_DAMAGE = new ConfigOptionInt("Sigil.BaseProjectileDamage", 8);
    public static ConfigOptionInt SIGIL_SUPPORT_DURATION = new ConfigOptionInt("Sigil.BaseSupportDuration", 2400);
    
    /*
     * World generation config settings
     */

    public static ConfigOptionInt WORLD_GEM_CLUSTER_COUNT = new ConfigOptionInt("World.Gem.ClusterCount", 5);
    public static ConfigOptionInt WORLD_GEM_CLUSTER_SIZE = new ConfigOptionInt("World.Gem.ClusterSize", 8);
    public static ConfigOptionInt WORLD_GEM_MAX_HEIGHT = new ConfigOptionInt("World.Gem.MaxHeight", 40);
    public static ConfigOptionInt WORLD_CHAOS_ORE_CLUSTER_COUNT = new ConfigOptionInt("World.ChaosOre.ClusterCount", 1);
    public static ConfigOptionInt WORLD_CHAOS_ORE_CLUSTER_SIZE = new ConfigOptionInt("World.ChaosOre.ClusterSize", 16);
    public static ConfigOptionInt WORLD_CHAOS_ORE_MAX_HEIGHT = new ConfigOptionInt("World.ChaosOre.MaxHeight", 20);
    public static ConfigOptionInt WORLD_FLOWERS_PER_CHUNK = new ConfigOptionInt("World.FlowersPerChunk", 2);
    
    /*
     * **************
     * Config Handler
     * **************
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
    public static final String CATEGORY_WORLD_GEN = CATEGORY_WORLD + Configuration.CATEGORY_SPLITTER + "generation";
    public static final String CATEGORY_WORLD_STRUCTURE = CATEGORY_WORLD + Configuration.CATEGORY_SPLITTER + "structure";
    public static final String CATEGORY_BLOCK_PROPERTIES = Configuration.CATEGORY_BLOCK + Configuration.CATEGORY_SPLITTER + "properties";
    public static final String CATEGORY_ITEM_PROPERTIES = Configuration.CATEGORY_ITEM + Configuration.CATEGORY_SPLITTER + "properties";
    public static final String CATEGORY_DURABILITY = Configuration.CATEGORY_ITEM + Configuration.CATEGORY_SPLITTER + "durability";
    
    public static void init(File file) {
        
        c = new Configuration(file);
        
        try {
            c.load();
            
            /*
             * Misc
             */
            CHAOS_GEM_FLIGHT_MAX_SPEED.loadValue(c, CATEGORY_ITEM_PROPERTIES);
            CHAOS_GEM_MAX_BUFFS.loadValue(c, CATEGORY_ITEM_PROPERTIES);
            CHAOS_GEM_MAX_CHARGE.loadValue(c, CATEGORY_ITEM_PROPERTIES);
            FOOD_SUPPORT_DURATION.loadValue(c, CATEGORY_ITEM_PROPERTIES);
            
            /*
             * Enchantment ids
             */
            ModEnchantments.MENDING_ID = getEnchantmentId(Names.MENDING, ModEnchantments.MENDING_ID_DEFAULT);
            
            /*
             * Sigil
             */
            SIGIL_USE_DURATION.loadValue(c, CATEGORY_ITEM_PROPERTIES);
            SIGIL_PROJECTILE_DAMAGE.loadValue(c, CATEGORY_ITEM_PROPERTIES);
            SIGIL_SUPPORT_DURATION.loadValue(c, CATEGORY_ITEM_PROPERTIES);
            
            /*
             * World gen
             */
            WORLD_GEM_CLUSTER_COUNT.loadValue(c, CATEGORY_WORLD_GEN);
            WORLD_GEM_CLUSTER_SIZE.loadValue(c, CATEGORY_WORLD_GEN);
            WORLD_GEM_MAX_HEIGHT.loadValue(c, CATEGORY_WORLD_GEN);
            WORLD_CHAOS_ORE_CLUSTER_COUNT.loadValue(c, CATEGORY_WORLD_GEN);
            WORLD_CHAOS_ORE_CLUSTER_SIZE.loadValue(c, CATEGORY_WORLD_GEN);
            WORLD_CHAOS_ORE_MAX_HEIGHT.loadValue(c, CATEGORY_WORLD_GEN);
            WORLD_FLOWERS_PER_CHUNK.loadValue(c, CATEGORY_WORLD_GEN);
        }
        catch (Exception e) {
            LogHelper.severe("Oh noes!!! Couldn't load configuration file properly!");
        }
        finally {
            c.save();
        }
    }
    
    public static int getBlockId(String name, int default_id) {

        return c.getBlock(name, default_id).getInt(default_id);
    }
    
    public static int getBlockId(String name, int default_id, String comment) {
        
        return c.getBlock(name, default_id, comment).getInt(default_id);
    }

    public static int getItemId(String name, int default_id) {

        return c.getItem(name, default_id).getInt(default_id);
    }

    public static int getItemId(String name, int default_id, String comment) {

        return c.getItem(name, default_id, comment).getInt(default_id);
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
