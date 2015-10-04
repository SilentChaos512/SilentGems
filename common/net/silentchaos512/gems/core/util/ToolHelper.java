package net.silentchaos512.gems.core.util;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.item.Gem;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemHoe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.item.tool.GemSword;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.material.ModMaterials;

public class ToolHelper {

  /**
   * Gets the "gem ID", or base material ID for a tool. Note that the regular and supercharged gems have the same ID
   * (ie, regular ruby == supercharged ruby == 0). For gems, this number is in [0, 11]. Other IDs can be found in
   * ModMaterials.
   * 
   * @param tool
   * @return The material ID, or -1 if it can't be determined.
   */
  public static int getToolGemId(ItemStack tool) {

    if (tool == null) {
      return -1;
    }

    Item item = tool.getItem();
    if (item instanceof GemSword) {
      return ((GemSword) item).gemId;
    } else if (item instanceof GemPickaxe) {
      return ((GemPickaxe) item).gemId;
    } else if (item instanceof GemShovel) {
      return ((GemShovel) item).gemId;
    } else if (item instanceof GemAxe) {
      return ((GemAxe) item).gemId;
    } else if (item instanceof GemHoe) {
      return ((GemHoe) item).gemId;
    } else if (item instanceof GemSickle) {
      return ((GemSickle) item).gemId;
    } else {
      LogHelper.debug("Called ToolHelper.getToolGemId on a non-Gems tool!");
      return -1;
    }
  }

  /**
   * Determines whether the tool is "supercharged" or not.
   * 
   * @param tool
   * @return True for supercharged tools (ornate rod) or false for regular tools (wooden rod).
   */
  public static boolean getToolIsSupercharged(ItemStack tool) {

    if (tool == null) {
      return false;
    }

    Item item = tool.getItem();
    if (item instanceof GemSword) {
      return ((GemSword) item).supercharged;
    } else if (item instanceof GemPickaxe) {
      return ((GemPickaxe) item).supercharged;
    } else if (item instanceof GemShovel) {
      return ((GemShovel) item).supercharged;
    } else if (item instanceof GemAxe) {
      return ((GemAxe) item).supercharged;
    } else if (item instanceof GemHoe) {
      return ((GemHoe) item).supercharged;
    } else if (item instanceof GemSickle) {
      return ((GemSickle) item).supercharged;
    } else {
      LogHelper.debug("Called ToolHelper.getToolIsSupercharged on a non-Gems tool!");
      return false;
    }
  }

  /**
   * Gets the material ID for the given material, or -1 if it's not a recognized tool material.
   * 
   * @param material
   * @return
   */
  public static int getIdFromMaterial(ItemStack material) {

    Item item = material.getItem();
    if (item instanceof Gem) {
      return material.getItemDamage() & 0xF;
    } else if (item == Items.flint) {
      return ModMaterials.FLINT_GEM_ID;
    } else if (item == Items.fish) {
      return ModMaterials.FISH_GEM_ID;
    } else {
      return -1;
    }
  }

  // ==========================================================================
  // NBT helper methods
  // ==========================================================================

  public static int getToolHeadLeft(ItemStack tool) {

    if (!tool.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_LEFT)) {
      return -1;
    }
    return tool.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_LEFT);
  }

  public static void setToolHeadLeft(ItemStack tool, int id) {

    tool.stackTagCompound.setByte(Strings.TOOL_ICON_HEAD_LEFT, (byte) id);
  }

  public static int getToolHeadMiddle(ItemStack tool) {

    if (!tool.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_MIDDLE)) {
      return -1;
    }
    return tool.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_MIDDLE);
  }

  public static void setToolHeadMiddle(ItemStack tool, int id) {

    tool.stackTagCompound.setByte(Strings.TOOL_ICON_HEAD_MIDDLE, (byte) id);
  }

  public static int getToolHeadRight(ItemStack tool) {

    if (!tool.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_RIGHT)) {
      return -1;
    }
    return tool.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_RIGHT);
  }

  public static void setToolHeadRight(ItemStack tool, int id) {

    tool.stackTagCompound.setByte(Strings.TOOL_ICON_HEAD_RIGHT, (byte) id);
  }
  
  public static int getToolRodDeco(ItemStack tool) {
    
    if (!tool.stackTagCompound.hasKey(Strings.TOOL_ICON_DECO)) {
      return -1;
    }
    return tool.stackTagCompound.getByte(Strings.TOOL_ICON_DECO);
  }

  public static void setToolRodDeco(ItemStack tool, int id) {

    tool.stackTagCompound.setByte(Strings.TOOL_ICON_DECO, (byte) id);
  }
  
  public static int getToolRodWool(ItemStack tool) {
    
    if (!tool.stackTagCompound.hasKey(Strings.TOOL_ICON_ROD)) {
      return -1;
    }
    return tool.stackTagCompound.getByte(Strings.TOOL_ICON_ROD);
  }
  
  public static void setToolRodWool(ItemStack tool, int id) {
    
    tool.stackTagCompound.setByte(Strings.TOOL_ICON_ROD, (byte) id);
  }
}
