package net.silentchaos512.gems.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.ArmorPartPosition;
import net.silentchaos512.gems.api.lib.EnumDecoPos;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.lib.util.StackHelper;

public class ArmorHelper {

  public static final int PROTECTION_CAP = 39;

  /*
   * NBT keys
   */

  public static final String NBT_ROOT_CONSTRUCTION = "SGConstruction";
  public static final String NBT_ROOT_DECORATION = "SGDecoration";
  public static final String NBT_ROOT_PROPERTIES = "SGProperties";
  public static final String NBT_ROOT_STATISTICS = "SGStatistics";

  // Construction
  public static final String NBT_ARMOR_TIER = "ArmorTier";

  // Stats
  public static final String NBT_PROP_DURABILITY = "Durability";
  public static final String NBT_PROP_PROTECTION = "Protection";
  public static final String NBT_PROP_ENCHANTABILITY = "Enchantability";

  // Statistics
  public static final String NBT_STATS_ORIGINAL_OWNER = "OriginalOwner";
  public static final String NBT_STATS_REDECORATED = "Redecorated";
  public static final String NBT_STATS_DAMAGE_TAKEN = "DamageTaken";

  public static void recalculateStats(ItemStack armor) {

    ToolHelper.recalculateStats(armor);
  }

  /*
   * Armor Properties
   */

  public static float getProtection(ItemStack armor) {

    return ToolHelper.getProtection(armor);
  }

  public static int getItemEnchantability(ItemStack armor) {

    return ToolHelper.getItemEnchantability(armor);
  }

  public static int getMaxDamage(ItemStack armor) {

    return ToolHelper.getMaxDamage(armor);
  }

  public static boolean isBroken(ItemStack armor) {

    return ToolHelper.isBroken(armor);
  }

  // ==========================================================================
  // Armor construction and decoration
  // ==========================================================================

  public static EnumMaterialTier getArmorTier(ItemStack armor) {

    return ToolHelper.getToolTier(armor);
  }

  public static ToolPart[] getConstructionParts(ItemStack armor) {

    return ToolHelper.getConstructionParts(armor);
  }

  public static EnumMaterialGrade[] getConstructionGrades(ItemStack armor) {

    return ToolHelper.getConstructionGrades(armor);
  }

  public static ToolPart getPart(ItemStack armor, ArmorPartPosition pos) {

    String key = getPartId(armor, pos.getKey(0));
    if (key == null || key.isEmpty()) {
      return null;
    }
    return ToolPartRegistry.getPart(key);
  }

  public static ToolPart getRenderPart(ItemStack armor, ArmorPartPosition pos) {

    String key = getPartId(armor, pos.getDecoKey());
    if (key == null || key.isEmpty()) {
      return getPart(armor, pos);
    }
    return ToolPartRegistry.getPart(key);
  }

  public static int getRenderColor(ItemStack armor, ArmorPartPosition pos) {

    return ToolRenderHelper.getInstance().getColor(armor, pos);
  }

  public static int[] getRenderColorList(ItemStack armor) {

    int[] values = new int[EnumDecoPos.values().length];
    for (ArmorPartPosition pos : ArmorPartPosition.values()) {
      values[pos.ordinal()] = getRenderColor(armor, pos);
    }
    return values;
  }

  public static String getRenderColorString(ItemStack stack) {

    StringBuilder toReturn = new StringBuilder();
    int[] colors = getRenderColorList(stack);
    for (int color : colors) {
      toReturn.append(color);
    }
    return toReturn.toString();
  }

  public static String getPartId(ItemStack tool, String key) {

    if (!tool.hasTagCompound()) {
      return null;
    }

    return getRootTag(tool, NBT_ROOT_CONSTRUCTION).getString(key).split("#")[0];
  }

  public static EnumMaterialGrade getPartGrade(ItemStack tool, String key) {

    if (!tool.hasTagCompound()) {
      return null;
    }

    String[] array = getRootTag(tool, NBT_ROOT_CONSTRUCTION).getString(key).split("#");
    if (array.length < 2) {
      return EnumMaterialGrade.NONE;
    }
    return EnumMaterialGrade.fromString(array[1]);
  }

  public static ItemStack constructArmor(Item item, ItemStack frame, ItemStack... materials) {

    // Shortcut for JEI recipes.
    if (materials.length == 1) {
      ItemStack[] newMats = new ItemStack[4];
      for (int i = 0; i < newMats.length; ++i)
        newMats[i] = materials[0];
      materials = newMats;
    }

    ItemStack result = new ItemStack(item);
    result.setTagCompound(new NBTTagCompound());
    result.getTagCompound().setTag(ToolHelper.NBT_TEMP_PARTLIST, new NBTTagCompound());

    // Construction
    ToolPart part;
    EnumMaterialGrade grade;
    for (int i = 0; i < materials.length; ++i) {
      if (StackHelper.isEmpty(materials[i])) {
        String str = "ArmorHelper.constructArmor: empty part! ";
        for (ItemStack stack : materials)
          str += stack + ", ";
        throw new IllegalArgumentException(str);
      }

      part = ToolPartRegistry.fromStack(materials[i]);
      grade = EnumMaterialGrade.fromStack(materials[i]);
      setTagPart(result, "Part" + i, part, grade);

      // Write part list for client-side name generation.
      result.getTagCompound().getCompoundTag(ToolHelper.NBT_TEMP_PARTLIST).setTag("part" + i,
          materials[i].writeToNBT(new NBTTagCompound()));
    }
    // Frame
    part = ToolPartRegistry.fromStack(frame);
    if (part != null) {
      setTagPart(result, ArmorPartPosition.FRAME.getKey(0), part, EnumMaterialGrade.NONE);
    }

    // Create name
    String displayName = ToolHelper.createToolName(item, materials);
    result.setStackDisplayName(displayName);

    recalculateStats(result);

    return result;
  }

  /*
   * Decoration Methods
   */

  public static ItemStack decorateArmor(ItemStack armor, ItemStack west, ItemStack north,
      ItemStack east, ItemStack south) {

    if (StackHelper.isEmpty(armor)) {
      return StackHelper.empty();
    }

    ItemStack result = StackHelper.safeCopy(armor);
    result = decorate(result, west, ArmorPartPosition.WEST);
    result = decorate(result, north, ArmorPartPosition.NORTH);
    result = decorate(result, east, ArmorPartPosition.EAST);
    result = decorate(result, south, ArmorPartPosition.SOUTH);
    return result;
  }

  private static ItemStack decorate(ItemStack armor, ItemStack material, ArmorPartPosition pos) {

    if (StackHelper.isEmpty(armor))
      return StackHelper.empty();
    if (StackHelper.isEmpty(material))
      return armor;

    ToolPart part = ToolPartRegistry.fromDecoStack(material);
    if (part == null)
      return null;

    // Only main parts (like gems) work
    if (!(part instanceof ToolPartMain))
      return armor;

    ItemStack result = StackHelper.safeCopy(armor);
    setTagPart(result, pos.getDecoKey(), part, EnumMaterialGrade.fromStack(material));

    return result;
  }

  // ==========================================================================
  // NBT helper methods
  // ==========================================================================

  private static NBTTagCompound getRootTag(ItemStack tool, String key) {

    if (key != null && !key.isEmpty()) {
      if (!tool.getTagCompound().hasKey(key)) {
        tool.getTagCompound().setTag(key, new NBTTagCompound());
      }
      return tool.getTagCompound().getCompoundTag(key);
    }
    return tool.getTagCompound();
  }

  private static void initRootTag(ItemStack tool) {

    if (!tool.hasTagCompound())
      tool.setTagCompound(new NBTTagCompound());
  }

  private static int getTagInt(ItemStack tool, String root, String name) {

    if (!tool.hasTagCompound())
      return 0;
    return getRootTag(tool, root).getInteger(name);
  }

  private static int getTagInt(ItemStack tool, String root, String name, int defaultValue) {

    if (!tool.hasTagCompound() || !getRootTag(tool, root).hasKey(name))
      return defaultValue;
    return getRootTag(tool, root).getInteger(name);
  }

  private static void setTagInt(ItemStack tool, String root, String name, int value) {

    initRootTag(tool);
    getRootTag(tool, root).setInteger(name, value);
  }

  private static float getTagFloat(ItemStack tool, String root, String name) {

    if (!tool.hasTagCompound())
      return 0.0f;
    return getRootTag(tool, root).getFloat(name);
  }

  private static void setTagFloat(ItemStack tool, String root, String name, float value) {

    initRootTag(tool);
    getRootTag(tool, root).setFloat(name, value);
  }

  private static boolean getTagBoolean(ItemStack tool, String root, String name) {

    if (!tool.hasTagCompound())
      return false;
    return getRootTag(tool, root).getBoolean(name);
  }

  private static void setTagBoolean(ItemStack tool, String root, String name, boolean value) {

    initRootTag(tool);
    getRootTag(tool, root).setBoolean(name, value);
  }

  private static String getTagString(ItemStack tool, String root, String name) {

    if (!tool.hasTagCompound())
      return "";
    return getRootTag(tool, root).getString(name);
  }

  private static void setTagString(ItemStack tool, String root, String name, String value) {

    initRootTag(tool);
    getRootTag(tool, root).setString(name, value);
  }

  private static void setTagPart(ItemStack tool, String name, ToolPart part,
      EnumMaterialGrade grade) {

    initRootTag(tool);
    getRootTag(tool, NBT_ROOT_CONSTRUCTION).setString(name, part.getKey() + "#" + grade.name());
  }

  // --------------
  // Statistics NBT
  // --------------

  public static String getOriginalOwner(ItemStack tool) {

    return getTagString(tool, NBT_ROOT_STATISTICS, NBT_STATS_ORIGINAL_OWNER);
  }

  public static void setOriginalOwner(ItemStack tool, EntityPlayer player) {

    setOriginalOwner(tool, player.getName());
  }

  public static void setOriginalOwner(ItemStack tool, String name) {

    ToolHelper.setOriginalOwner(tool, name);
  }

  public static int getStatRedecorated(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_REDECORATED);
  }

  public static void incrementStatRedecorated(ItemStack tool, int amount) {

    setTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_REDECORATED, getStatRedecorated(tool) + amount);
  }

  public static float getStatDamageTaken(ItemStack tool) {

    return getTagFloat(tool, NBT_ROOT_STATISTICS, NBT_STATS_DAMAGE_TAKEN);
  }

  public static void incrementStatDamageTaken(ItemStack tool, float amount) {

    setTagFloat(tool, NBT_ROOT_STATISTICS, NBT_STATS_DAMAGE_TAKEN,
        getStatDamageTaken(tool) + amount);
  }
}
