package net.silentchaos512.gems.api.tool.part;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;

public class ToolPartRegistry {

  private static Map<String, ToolPart> map = Maps.newHashMap();

  public static ToolPart getPart(String key) {

    return map.get(key);
  }

  public static void putPart(ToolPart part) {

    String key = part.key;
    if (map.containsKey(key)) {
      throw new IllegalArgumentException("Already have a part with key " + part.key);
    }
    map.put(key, part);
  }

  public static ToolPart fromStack(ItemStack stack) {

    for (ToolPart part : map.values()) {
      // Exact match for crafting stack?
      if (part.craftingStack != null && part.craftingStack.isItemEqual(stack)) {
        return part;
      }
      // Matches ore dictionary key?
      if (!part.craftingOreDictName.isEmpty()) {
        for (ItemStack stackOre : OreDictionary.getOres(part.craftingOreDictName)) {
          if (stackOre.isItemEqual(stack)) {
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
}
