package silent.gems.core.registry;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import silent.gems.block.BlockSG;
import silent.gems.core.util.LogHelper;
import silent.gems.item.ItemSG;
import silent.gems.item.block.ItemBlockSG;
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
     */
    public static void registerBlock(Class<? extends Block> blockClass, String key) {

        registerBlock(blockClass, key, ItemBlockSG.class);
    }

    /**
     * Add a Block to the hash map and registers it in the GameRegistry.
     * 
     * @param blockClass
     *            The Block class to register.
     * @param key
     *            The name of the Block.
     * @param itemBlockClass
     *            The ItemBlock to use.
     * @param constructorParams
     *            The list of parameters for the constructor (minus the ID).
     */
    public static void registerBlock(Class<? extends Block> blockClass, String key,
            Class<? extends ItemBlock> itemBlockClass, Object... constructorParams) {

        int i;

        try {
            // Create an array of the classes in the constructor parameters and the int for id.
            Class[] paramClasses = getParameterClasses(constructorParams);

            // Get the constructor for this Block.
            Constructor<?> c = blockClass.getDeclaredConstructor(paramClasses);

            // Instantiate and add to hash map.
            Block block = (Block) c.newInstance(constructorParams);
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
     * @param constructorParams
     *            The list of parameters for the constructor (minus the ID).
     */
    public static void registerItem(Class<? extends Item> itemClass, String key, Object... constructorParams) {

        int i;

        try {
            // Create an array of the classes in the constructor parameters and the int for id.
            Class[] paramClasses = getParameterClasses(constructorParams);

            // Get the constructor for this Item.
            Constructor<?> c = itemClass.getDeclaredConstructor(paramClasses);

            // Instantiate and add to hash map.
            Item item = (Item) c.newInstance(constructorParams);
            items.put(key, item);
            GameRegistry.registerItem(item, key);
        }
        catch (Exception e) {
            LogHelper.severe("Failed to register item " + key);
            e.printStackTrace();
        }
    }

    private static Class[] getParameterClasses(Object[] params) {

        Class[] result = new Class[params.length];
        for (int i = 0; i < params.length; ++i) {
            result[i] = params[i].getClass();
            if (result[i] == Integer.class) {
                result[i] = int.class;
            }
            else if (result[i] == Float.class) {
                result[i] = float.class;
            }
            else if (result[i] == Boolean.class) {
                result[i] = boolean.class;
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
            if (block instanceof IAddRecipe) {
                ((IAddRecipe) block).addRecipes();
                ((IAddRecipe) block).addOreDict();
            }
        }
        for (Item item : items.values()) {
            if (item instanceof IAddRecipe) {
                ((IAddRecipe) item).addRecipes();
                ((IAddRecipe) item).addOreDict();
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
