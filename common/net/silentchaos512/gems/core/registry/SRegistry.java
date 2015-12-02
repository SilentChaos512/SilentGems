package net.silentchaos512.gems.core.registry;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.block.BlockSG;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.item.ItemSG;
import net.silentchaos512.gems.item.block.ItemBlockSG;

public class SRegistry {

  private static final HashMap<String, Block> blocks = new HashMap<String, Block>();
  private static final HashMap<String, Item> items = new HashMap<String, Item>();

  public static final ArrayList<ModelResourceLocation> toolBaseModels = new ArrayList<ModelResourceLocation>();

  /**
   * Add a Block to the hash map and registers it in the GameRegistry.
   * 
   * @param blockClass
   *          The Block class to register.
   * @param key
   *          The name of the Block.
   */
  public static Block registerBlock(Class<? extends Block> blockClass, String key) {

    return registerBlock(blockClass, key, ItemBlockSG.class);
  }

  /**
   * Add a Block to the hash map and registers it in the GameRegistry.
   * 
   * @param blockClass
   *          The Block class to register.
   * @param key
   *          The name of the Block.
   * @param itemBlockClass
   *          The ItemBlock to use.
   * @param constructorParams
   *          The list of parameters for the constructor (minus the ID).
   */
  public static Block registerBlock(Class<? extends Block> blockClass, String key,
      Class<? extends ItemBlock> itemBlockClass, Object... constructorParams) {

    try {
      // Create an array of the classes in the constructor parameters and the int for id.
      Class[] paramClasses = getParameterClasses(constructorParams);

      // Get the constructor for this Block.
      Constructor<?> c = blockClass.getDeclaredConstructor(paramClasses);

      // Instantiate and add to hash map.
      Block block = (Block) c.newInstance(constructorParams);
      blocks.put(key, block);
      GameRegistry.registerBlock(block, itemBlockClass, key);
      return block;
    } catch (Exception e) {
      LogHelper.severe("Failed to register block " + key);
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Creates a new Item instance and add it to the hash map.
   * 
   * @param itemClass
   *          The Item to add.
   * @param key
   *          The name of the Item.
   * @param constructorParams
   *          The list of parameters for the constructor (minus the ID).
   */
  public static Item registerItem(Class<? extends Item> itemClass, String key,
      Object... constructorParams) {

    try {
      // Create an array of the classes in the constructor parameters and the int for id.
      Class[] paramClasses = getParameterClasses(constructorParams);

      // Get the constructor for this Item.
      Constructor<?> c = itemClass.getDeclaredConstructor(paramClasses);

      // Instantiate and add to hash map.
      Item item = (Item) c.newInstance(constructorParams);
      items.put(key, item);
      GameRegistry.registerItem(item, key);
      return item;
    } catch (Exception e) {
      LogHelper.severe("Failed to register item " + key);
      e.printStackTrace();
      return null;
    }
  }

  private static Class[] getParameterClasses(Object[] params) {

    Class[] result = new Class[params.length];
    for (int i = 0; i < params.length; ++i) {
      result[i] = params[i].getClass();
      if (result[i] == Integer.class) {
        result[i] = int.class;
      } else if (result[i] == Float.class) {
        result[i] = float.class;
      } else if (result[i] == Boolean.class) {
        result[i] = boolean.class;
      }
    }
    return result;
  }

  /**
   * Calls the addRecipes and addOreDict methods of all Blocks and Items that can be cast to IAddRecipe. Should be
   * called after registering all Blocks and Items.
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
   * Calls the addThaumcraftStuff method of all Blocks and Items that can be cast to IAddThaumcraftStuff. TC4 API says
   * to register aspects after TC, so does this need to go in post-init?
   */
  public static void addThaumcraftStuff() {

    for (Block block : blocks.values()) {
      if (block instanceof IAddThaumcraftStuff) {
        ((IAddThaumcraftStuff) block).addThaumcraftStuff();
      }
    }
    for (Item item : items.values()) {
      if (item instanceof IAddThaumcraftStuff) {
        ((IAddThaumcraftStuff) item).addThaumcraftStuff();
      }
    }
  }

  public static String[] removeNullElements(String[] names) {

    ArrayList<String> list = new ArrayList<String>();
    for (String name : names) {
      if (name != null) {
        list.add(name);
      }
    }
    return list.toArray(new String[] {});
  }

  /**
   * Registers model variant names.
   */
  public static void clientPreInit() {

    // Blocks (IHasVariants)
    for (Block block : blocks.values()) {
      if (block instanceof IHasVariants) {
        IHasVariants var = (IHasVariants) block;
        String[] names = removeNullElements(var.getVariantNames());
        ModelBakery.addVariantName(Item.getItemFromBlock(block), names);
      }
    }

    // Items (IHasVariants)
    for (Item item : items.values()) {
      if (item instanceof IHasVariants) {
        IHasVariants var = (IHasVariants) item;
        String[] names = removeNullElements(var.getVariantNames());
        ModelBakery.addVariantName(item, names);
      }
    }
  }

  /**
   * Register models for all variants.
   */
  public static void clientInit() {

    // Blocks (IRegisterModels)
    for (Block block : blocks.values()) {
      if (block instanceof IRegisterModels) {
        ((IRegisterModels) block).registerModels();
      }
    }

    // Blocks (IHasVariants)
    for (Block block : blocks.values()) {
      Item item = Item.getItemFromBlock(block);
      if (block instanceof IHasVariants && !(block instanceof IRegisterModels)) {
        IHasVariants var = (IHasVariants) block;
        String[] variants = var.getVariantNames();
        int count = variants.length;
        for (int i = 0; i < count; ++i) {
          if (variants[i] != null) {
            ModelResourceLocation model = new ModelResourceLocation(variants[i], "inventory");
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, i, model);
          }
        }
      }
    }

    // Items (IRegisterModels)
    for (Item item : items.values()) {
      if (item instanceof IRegisterModels) {
        ((IRegisterModels) item).registerModels();
      }
    }

    // Items (IHasVariants)
    for (Item item : items.values()) {
      if (item instanceof IHasVariants && !(item instanceof IRegisterModels)) {
        IHasVariants var = (IHasVariants) item;
        String[] variants = var.getVariantNames();
        int count = variants.length;
        for (int i = 0; i < count; ++i) {
          if (variants[i] != null) {
            ModelResourceLocation model = new ModelResourceLocation(variants[i], "inventory");
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, i, model);
            // Tool model?
            // if (InventoryHelper.isGemTool(new ItemStack(item))) {
            // toolBaseModels.add(model);
            // }
          }
        }
      }
    }
  }

  public static Item[] getAllItemsOfType(Class<? extends Item> clazz) {

    ArrayList<Item> result = new ArrayList<Item>();
    for (Item item : items.values()) {
      if (item.getClass() == clazz) {
        result.add(item);
      }
    }
    return result.toArray(new Item[result.size()]);
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
    } else {
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
    } else {
      return null;
    }
  }
}
