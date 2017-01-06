package net.silentchaos512.gems.api.tool.part;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Used to register tool parts, and match parts to item stacks.
 * 
 * @author SilentChaos512
 *
 */
public class ToolPartRegistry {

  private static Map<String, ToolPart> map = Maps.newHashMap();
  private static List<ToolPartMain> mains = Lists.newArrayList();
  private static Map<ItemStack, ToolPart> STACK_TO_PART = Maps.newHashMap();

  /**
   * @param key
   *          The key the part was registered with.
   * @return The part for the given key.
   */
  public static ToolPart getPart(String key) {

    return map.get(key.toLowerCase());
  }

  /**
   * Registers a tool part.
   * 
   * @param part
   */
  public static void putPart(ToolPart part) {

    String key = part.key;
    if (map.containsKey(key))
      throw new IllegalArgumentException("Already have a part with key " + part.key);
    map.put(key, part);

    if (part instanceof ToolPartMain)
      mains.add((ToolPartMain) part);
  }

  /**
   * Gets the tool part that matches the ItemStack. Also checks the ore dictionary for parts that have an ore dictionary
   * key.
   * 
   * @param stack
   * @return
   */
  public static ToolPart fromStack(ItemStack stack) {

    if (stack.isEmpty())
      return null;

    if (STACK_TO_PART.containsKey(stack))
      return STACK_TO_PART.get(stack);

    for (ToolPart part : map.values()) {
      // Exact match for crafting stack?
      if (part.craftingStack != null && part.craftingStack.isItemEqual(stack)) {
        STACK_TO_PART.put(stack, part);
        return part;
      }
      // Matches ore dictionary key?
      if (!part.craftingOreDictName.isEmpty()) {
        for (ItemStack stackOre : OreDictionary.getOres(part.craftingOreDictName)) {
          if (stackOre.isItemEqual(stack)) {
            STACK_TO_PART.put(stack, part);
            return part;
          }
        }
      }
    }
    return null;
  }

  public static Set<String> getKeySet() {

    return map.keySet();
  }

  public static Collection<ToolPart> getValues() {

    return map.values();
  }

  /**
   * Gets a list of registered ToolPartMains in the order they are registered (used for sub-item display). DO NOT modify this.
   */
  public static List<ToolPartMain> getMains() {

    return mains;
  }
}
