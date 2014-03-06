package silent.gems.core.registry;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import silent.gems.block.BlockSG;
import silent.gems.configuration.Config;
import silent.gems.core.util.LogHelper;
import silent.gems.item.ItemSG;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;

public class SRegistry {

    private final static HashMap<String, Block> blocks = new HashMap<String, Block>();
    private final static HashMap<String, Item> items = new HashMap<String, Item>();

    /**
     * Add a Block to the hash map and registers it in the GameRegistry.
     * 
     * @param blockClass
     *            The Block class to register.
     * @param key
     *            The name of the Block.
     * @param defaultId
     *            The default ID. The ID that is used is loaded from the config file.
     */
    public static void registerBlock(Class<? extends Block> blockClass, String key, int defaultId) {

        registerBlock(blockClass, key, defaultId, ItemBlock.class, "");
    }

    /**
     * Add a Block to the hash map and registers it in the GameRegistry.
     * 
     * @param blockClass
     *            The Block class to register.
     * @param key
     *            The name of the Block.
     * @param defaultId
     *            The default ID. The ID that is used is loaded from the config file.
     * @param comment
     *            A comment for the config file.
     */
    public static void registerBlock(Class<? extends Block> blockClass, String key, int defaultId, String comment) {

        registerBlock(blockClass, key, defaultId, ItemBlock.class, comment);
    }

    /**
     * Add a Block to the hash map and registers it in the GameRegistry.
     * 
     * @param blockClass
     *            The Block class to register.
     * @param key
     *            The name of the Block.
     * @param defaultId
     *            The default ID. The ID that is used is loaded from the config file.
     * @param itemBlockClass
     *            The ItemBlock to use.
     */
    public static void registerBlock(Class<? extends Block> blockClass, String key, int defaultId, Class<? extends ItemBlock> itemBlockClass) {

        registerBlock(blockClass, key, defaultId, itemBlockClass, "");
    }

    /**
     * Add a Block to the hash map and registers it in the GameRegistry.
     * 
     * @param blockClass
     *            The Block class to register.
     * @param key
     *            The name of the Block.
     * @param defaultId
     *            The default ID. The ID that is used is loaded from the config file.
     * @param itemBlockClass
     *            The ItemBlock to use.
     * @param comment
     *            A comment for the config file.
     * @param constructorParams
     *            The list of parameters for the constructor (minus the ID).
     */
    public static void registerBlock(Class<? extends Block> blockClass, String key, int defaultId,
            Class<? extends ItemBlock> itemBlockClass, String comment, Object... constructorParams) {

        int id;
        if (!comment.equals(Strings.EMPTY)) {
            id = Config.getBlockId(key, defaultId, comment);
        }
        else {
            id = Config.getBlockId(key, defaultId);
        }
        int i;

        try {
            // Create an array of the classes in the constructor parameters and the int for id.
            Class[] paramClasses = getParameterClasses(constructorParams);

            // Get the constructor for this Block.
            Constructor<?> c = blockClass.getDeclaredConstructor(paramClasses);

            // Create the parameters list for the constructor, including id.
            Object[] params = new Object[constructorParams.length + 1];
            params[0] = id;
            for (i = 0; i < constructorParams.length; ++i) {
                params[i + 1] = constructorParams[i];
            }

            // Instantiate and add to hash map.
            Block block = (Block) c.newInstance(params);
            blocks.put(key, block);
            GameRegistry.registerBlock(block, itemBlockClass, key);
        }
        catch (Exception e) {
            LogHelper.severe("Failed to register block " + key);
            e.printStackTrace();
        }
    }

    /**
     * Creates a new Item instance and add it to the hash map.
     * 
     * @param itemClass
     *            The Item to add.
     * @param key
     *            The name of the Item.
     * @param defaultId
     *            The default ID. The ID that is used is loaded from the config file.
     */
    public static void registerItem(Class<? extends Item> itemClass, String key, int defaultId) {

        registerItem(itemClass, key, defaultId, "");
    }

    /**
     * Creates a new Item instance and add it to the hash map.
     * 
     * @param itemClass
     *            The Item to add.
     * @param key
     *            The name of the Item.
     * @param defaultId
     *            The default ID. The ID that is used is loaded from the config file.
     * @param comment
     *            A comment for the config file.
     * @param constructorParams
     *            The list of parameters for the constructor (minus the ID).
     */
    public static void registerItem(Class<? extends Item> itemClass, String key, int defaultId, String comment, Object... constructorParams) {

        int id;
        if (!comment.equals(Strings.EMPTY)) {
            id = Config.getItemId(key, defaultId, comment);
        }
        else {
            id = Config.getItemId(key, defaultId);
        }
        int i;

        try {
            // Create an array of the classes in the constructor parameters and the int for id.
            Class[] paramClasses = getParameterClasses(constructorParams);

            // Get the constructor for this Item.
            Constructor<?> c = itemClass.getDeclaredConstructor(paramClasses);

            // Create the parameters list for the constructor, including id.
            Object[] params = new Object[constructorParams.length + 1];
            params[0] = id;
            for (i = 0; i < constructorParams.length; ++i) {
                params[i + 1] = constructorParams[i];
            }

            // Instantiate and add to hash map.
            Item item = (Item) c.newInstance(params);
            items.put(key, item);
            GameRegistry.registerItem(item, key);
        }
        catch (Exception e) {
            LogHelper.severe("Failed to register item " + key);
            e.printStackTrace();
        }
    }

    private static Class[] getParameterClasses(Object[] params) {

        Class[] result = new Class[params.length + 1];
        result[0] = int.class;
        for (int i = 0; i < params.length; ++i) {
            result[i + 1] = params[i].getClass();
            if (result[i + 1] == Integer.class) {
                result[i + 1] = int.class;
            }
            else if (result[i + 1] == Float.class) {
                result[i + 1] = float.class;
            }
            else if (result[i + 1] == Boolean.class) {
                result[i + 1] = boolean.class;
            }
        }
        return result;
    }

    /**
     * Calls the addRecipes and addOreDict methods of all Blocks and Items that can be cast to BlockSG and ItemSG.
     * Should be called after registering all Blocks and Items.
     */
    public static void addRecipesAndOreDictEntries() {

        for (Block block : blocks.values()) {
            if (block instanceof BlockSG) {
                ((BlockSG) block).addRecipes();
                ((BlockSG) block).addOreDict();
            }
        }
        for (Item item : items.values()) {
            if (item instanceof ItemSG) {
                ((ItemSG) item).addRecipes();
                ((ItemSG) item).addOreDict();
            }
        }
    }

    /**
     * Gets the Block registered with the given key.
     * 
     * @param key
     * @return
     */
    public static Block getBlock(String key) {

        if (!blocks.containsKey(key)) {
            LogHelper.severe("No block with key " + key + "! This is a bug!");
        }

        return blocks.get(key);
    }

    /**
     * Gets the Block registered with the given key and cast to BlockSG, if possible. Returns null otherwise.
     * 
     * @param key
     * @return
     */
    public static BlockSG getBlockSG(String key) {

        if (!blocks.containsKey(key)) {
            LogHelper.severe("No block with key " + key + "! This is a bug!");
        }

        if (blocks.get(key) instanceof BlockSG) {
            return (BlockSG) blocks.get(key);
        }
        else {
            return null;
        }
    }

    /**
     * Gets the Item registered with the given key.
     * 
     * @param key
     * @return
     */
    public static Item getItem(String key) {

        if (!items.containsKey(key)) {
            LogHelper.severe("No item with key " + key + "! This is a bug!");
        }

        return items.get(key);
    }

    /**
     * Gets the Item registered with the given key and cast to ItemSG, if possible. Returns null otherwise.
     * 
     * @param key
     * @return
     */
    public static ItemSG getItemSG(String key) {

        if (!items.containsKey(key)) {
            LogHelper.severe("No item with key " + key + "! This is a bug!");
        }

        if (items.get(key) instanceof ItemSG) {
            return (ItemSG) items.get(key);
        }
        else {
            return null;
        }
    }
}
