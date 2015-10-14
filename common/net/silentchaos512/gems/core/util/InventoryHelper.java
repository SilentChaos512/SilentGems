package net.silentchaos512.gems.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemBow;
import net.silentchaos512.gems.item.tool.GemHoe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.item.tool.GemSword;

public class InventoryHelper {

  /**
   * Convenience method for detecting gem tools.
   * 
   * @param stack
   * @return
   */
  public static boolean isGemTool(ItemStack stack) {

    if (stack != null) {
      Item item = stack.getItem();
      return (item instanceof GemSword) || item instanceof GemPickaxe || item instanceof GemShovel
          || item instanceof GemAxe || item instanceof GemHoe || item instanceof GemSickle
          || item instanceof GemBow;
    }
    return false;
  }

  /**
   * Convenience method for detecting tools, vanilla and modded (with the exception of sickles)
   * 
   * @param stack
   * @return
   */
  public static boolean isTool(ItemStack stack) {

    if (stack != null) {
      Item item = stack.getItem();
      return (item instanceof ItemSword) || item instanceof ItemPickaxe || item instanceof ItemSpade
          || item instanceof ItemAxe || item instanceof ItemHoe || item instanceof GemSickle
          || item instanceof ItemBow;
    }
    return false;
  }

  public static boolean isItemBlock(Item item, Block block) {

    return Block.getIdFromBlock(block) == Item.getIdFromItem(item);
  }

  public static boolean isStackBlock(ItemStack stack, Block block) {

    return Block.getIdFromBlock(block) == Item.getIdFromItem(stack.getItem());
  }

  /**
   * Convenience method for matching an ore dictionary entry.
   * 
   * @param stack
   * @param oreName
   * @return
   */
  public static boolean matchesOreDict(ItemStack stack, String oreName) {

    int[] ids = OreDictionary.getOreIDs(stack);
    for (int i = 0; i < ids.length; ++i) {
      if (OreDictionary.getOreName(ids[i]).equals(oreName)) {
        return true;
      }
    }
    return false;
  }
}
