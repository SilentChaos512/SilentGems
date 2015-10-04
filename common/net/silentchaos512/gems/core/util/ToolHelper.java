package net.silentchaos512.gems.core.util;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.Gem;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemHoe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.item.tool.GemSword;
import net.silentchaos512.gems.material.ModMaterials;

public class ToolHelper {

  public static final String NBT_ROOT = SilentGems.MOD_ID + "Tool";
  public static final String NBT_HEAD_L = "HeadL";
  public static final String NBT_HEAD_M = "HeadM";
  public static final String NBT_HEAD_R = "HeadR";
  public static final String NBT_ROD = "Rod";
  public static final String NBT_ROD_DECO = "RodDeco";
  public static final String NBT_ROD_WOOL = "RodWool";
  public static final String NBT_TIP = "Tip";

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

  private static int getTag(String name, ItemStack tool) {

    // Create tag compound, if needed.
    if (tool.stackTagCompound == null) {
      tool.setTagCompound(new NBTTagCompound());
    }

    // Create root tag, if needed.
    if (!tool.stackTagCompound.hasKey(NBT_ROOT)) {
      tool.stackTagCompound.setTag(NBT_ROOT, new NBTTagCompound());
    }

    // Get the requested value.
    NBTTagCompound tags = (NBTTagCompound) tool.stackTagCompound.getTag(NBT_ROOT);
    if (!tags.hasKey(name)) {
      return -1;
    }
    return tags.getByte(name);
  }

  private static void setTag(String name, int value, ItemStack tool) {

    // Create tag compound, if needed.
    if (tool.stackTagCompound == null) {
      tool.setTagCompound(new NBTTagCompound());
    }

    // Create root tag, if needed.
    if (!tool.stackTagCompound.hasKey(NBT_ROOT)) {
      tool.stackTagCompound.setTag(NBT_ROOT, new NBTTagCompound());
    }

    // Set the tag.
    NBTTagCompound tags = (NBTTagCompound) tool.stackTagCompound.getTag(NBT_ROOT);
    tags.setByte(name, (byte) value);
  }

  public static int getToolHeadLeft(ItemStack tool) {

    return getTag(NBT_HEAD_L, tool);
  }

  public static void setToolHeadLeft(ItemStack tool, int id) {

    setTag(NBT_HEAD_L, id, tool);
  }

  public static int getToolHeadMiddle(ItemStack tool) {

    return getTag(NBT_HEAD_M, tool);
  }

  public static void setToolHeadMiddle(ItemStack tool, int id) {

    setTag(NBT_HEAD_M, id, tool);
  }

  public static int getToolHeadRight(ItemStack tool) {

    return getTag(NBT_HEAD_R, tool);
  }

  public static void setToolHeadRight(ItemStack tool, int id) {

    setTag(NBT_HEAD_R, id, tool);
  }

  public static int getToolRodDeco(ItemStack tool) {

    return getTag(NBT_ROD_DECO, tool);
  }

  public static void setToolRodDeco(ItemStack tool, int id) {

    setTag(NBT_ROD_DECO, id, tool);
  }

  public static int getToolRodWool(ItemStack tool) {

    return getTag(NBT_ROD_WOOL, tool);
  }

  public static void setToolRodWool(ItemStack tool, int id) {

    setTag(NBT_ROD_WOOL, id, tool);
  }

  public static int getToolRod(ItemStack tool, int id) {

    return getTag(NBT_ROD, tool);
  }

  public static void setToolRod(ItemStack tool, int id) {

    setTag(NBT_ROD, id, tool);
  }

  public static int getToolHeadTip(ItemStack tool) {

    return getTag(NBT_TIP, tool);
  }

  public static void setToolHeadTip(ItemStack tool, int id) {

    setTag(NBT_TIP, id, tool);
  }
}
