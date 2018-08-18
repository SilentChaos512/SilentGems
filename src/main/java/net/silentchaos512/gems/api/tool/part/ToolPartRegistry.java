package net.silentchaos512.gems.api.tool.part;

import gnu.trove.map.hash.THashMap;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Used to register tool parts, and match parts to item stacks.
 *
 * @author SilentChaos512
 *
 */
public class ToolPartRegistry {

  private static Map<String, ToolPart> map = new THashMap<>();
  private static List<ToolPartMain> mains = new ArrayList<>();
  private static List<ToolPartRod> rods = new ArrayList<>();
  private static Map<ItemStack, ToolPart> STACK_TO_PART = new THashMap<>();

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
    else if (part instanceof ToolPartRod)
      rods.add((ToolPartRod) part);

    // Compatibility for pre-modifier stats
    if (part.stats == null) {
      part.setStatsFromOldMethods();
    }
  }

  /**
   * Gets the tool part that matches the ItemStack. Also checks the ore dictionary for parts that have an ore dictionary
   * key.
   *
   * @param stack
   * @return
   */
  public static @Nullable ToolPart fromStack(ItemStack stack) {

    if (stack.isEmpty())
      return null;

    if (STACK_TO_PART.containsKey(stack))
      return STACK_TO_PART.get(stack);

    for (ToolPart part : map.values()) {
      if (part.matchesForCrafting(stack, true)) {
        STACK_TO_PART.put(stack, part);
        return part;
      }
    }
    return null;
  }

  public static @Nullable ToolPart fromDecoStack(ItemStack stack) {

    if (stack.isEmpty())
      return null;

    for (ToolPart part : map.values()) {
      if (part.matchesForDecorating(stack, true)) {
        return part;
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

  /**
   * Gets a list of registered ToolPartRods in the order they are registered. DO NOT modify this.
   * @return
   */
  public static List<ToolPartRod> getRods() {

    return rods;
  }
}
