package net.silentchaos512.gems.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.ToolStats;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.guide.GuideBookGems;
import net.silentchaos512.gems.util.ToolHelper;

public class ToolData {

  /*
   * NBT keys
   */

  // Root keys
  public static final String NBT_ROOT_CONSTRUCTION = "SGConstruction";
  public static final String NBT_ROOT_PROPERTIES = "SGProperties";
  public static final String NBT_ROOT_TOOL_SOUL = "SGToolSoul";
  public static final String NBT_ROOT_STATISTICS = "SGStatistics";

  // Settings
  public static final String NBT_SETTINGS_SUPER_SKILL = "SuperSkillEnabled";

  // Construction
  public static final String NBT_PART_ROOT = "Part";

  public static final String NBT_PART_HEAD = "Head";
  public static final String NBT_PART_ROD = "Rod";
  public static final String NBT_PART_ROD_DECO = "RodDeco";
  public static final String NBT_PART_HEAD_TIP = "HeadTip";

  // Stats
  public static final String NBT_PROP_DURABILITY = "Durability";
  public static final String NBT_PROP_HARVEST_LEVEL = "HarvestLevel";
  public static final String NBT_PROP_HARVEST_SPEED = "HarvestSpeed";
  public static final String NBT_PROP_MELEE_DAMAGE = "MeleeDamage";
  public static final String NBT_PROP_MAGIC_DAMAGE = "MagicDamage";
  public static final String NBT_PROP_ENCHANTABILITY = "Enchantability";
  public static final String NBT_PROP_MELEE_SPEED = "MeleeSpeed";
  public static final String NBT_PROP_BLOCKING_POWER = "BlockingPower";

  // NBT for statistics
  public static final String NBT_STATS_ORIGINAL_OWNER = "OriginalOwner";
  public static final String NBT_STATS_BLOCKS_MINED = "BlocksMined";
  public static final String NBT_STATS_BLOCKS_PLACED = "BlocksPlaced";
  public static final String NBT_STATS_BLOCKS_TILLED = "BlocksTilled";
  public static final String NBT_STATS_HITS = "HitsLanded";
  public static final String NBT_STATS_KILL_COUNT = "KillCount";
  public static final String NBT_STATS_PATHS_MADE = "PathsMade";
  public static final String NBT_STATS_SHOTS_FIRED = "ShotsFired";
  public static final String NBT_STATS_SHOTS_LANDED = "ShotsLanded";
  public static final String NBT_STATS_THROWN = "ThrownCount";

  // Compatibility
  public static final String NBT_COMPAT_SG2 = "SG2_Tool";

  static Map<UUID, ToolData> MAP = new HashMap<>();

  // Construction
  public ToolPart partHead, partRod, partTip;
  public boolean isSG2Tool = false;

  // Soul
//  public @Nullable ToolSoul soul;

  // Properties
  public int maxDurability;
  public int harvestLevel;
  public float harvestSpeed;
  public float meleeDamage;
  public float magicDamage;
  public int enchantability;
  public float meleeSpeed;
  public float protection;

  // Settings
  public boolean superSkillEnabled = false;

  // Statistics
  String originalOwner;
  public int blocksMined;
  public int blocksPlaced;
  public int blocksTilled;
  public int hitsLanded;
  public int killCount;
  public int pathsMade;
  public int shotsFired;
  public int shotsLanded;
  public int thrownCount;

  public static @Nullable ToolData get(ItemStack tool, boolean putIfNotInMap) {

    UUID uuid = ToolHelper.getUUID(tool);
    if (uuid == null) {
      return null;
    }

    ToolData data = MAP.get(uuid);
    if (data == null) {
      data = readFromNBT(tool.getTagCompound());
      if (putIfNotInMap) {
        MAP.put(uuid, data);
      }
    }

    return data;
  }

  static ToolData readFromNBT(NBTTagCompound tags) {

    ToolData data = new ToolData();

    if (tags == null) {
      return data;
    }

    // Construction
    NBTTagCompound construction = getRootTag(tags, NBT_ROOT_CONSTRUCTION);
    data.partHead = getPart(construction, NBT_PART_HEAD);
    data.partRod = getPart(construction, NBT_PART_ROD);
    data.partTip = getPart(construction, NBT_PART_HEAD_TIP);

    // Soul
//    data.soul = ToolSoul.readFromNBT(getRootTag(tags, NBT_ROOT_TOOL_SOUL));

    // Properties
    NBTTagCompound props = getRootTag(tags, NBT_ROOT_PROPERTIES);
    data.maxDurability = props.getInteger(NBT_PROP_DURABILITY);
    data.harvestLevel = props.getInteger(NBT_PROP_HARVEST_LEVEL);
    data.harvestSpeed = props.getFloat(NBT_PROP_HARVEST_SPEED);
    data.meleeDamage = props.getFloat(NBT_PROP_MELEE_DAMAGE);
    data.magicDamage = props.getFloat(NBT_PROP_MAGIC_DAMAGE);
    data.enchantability = props.getInteger(NBT_PROP_ENCHANTABILITY);
    data.meleeSpeed = props.getFloat(NBT_PROP_MELEE_SPEED);
    data.protection = props.getFloat(NBT_PROP_BLOCKING_POWER);

    // Settings
    data.superSkillEnabled = props.getBoolean(NBT_SETTINGS_SUPER_SKILL);

    // Statistics
    NBTTagCompound stats = getRootTag(tags, NBT_ROOT_STATISTICS);
    data.originalOwner = stats.getString(NBT_STATS_ORIGINAL_OWNER);
    data.blocksMined = stats.getInteger(NBT_STATS_BLOCKS_MINED);
    data.blocksPlaced = stats.getInteger(NBT_STATS_BLOCKS_PLACED);
    data.blocksTilled = stats.getInteger(NBT_STATS_BLOCKS_TILLED);
    data.hitsLanded = stats.getInteger(NBT_STATS_HITS);
    data.killCount = stats.getInteger(NBT_STATS_KILL_COUNT);
    data.pathsMade = stats.getInteger(NBT_STATS_PATHS_MADE);
    data.shotsFired = stats.getInteger(NBT_STATS_SHOTS_FIRED);
    data.shotsLanded = stats.getInteger(NBT_STATS_SHOTS_LANDED);
    data.thrownCount = stats.getInteger(NBT_STATS_THROWN);

    // TODO

    return data;
  }

  static void writeToNBT(NBTTagCompound tags) {

    // TODO
  }

  static NBTTagCompound getRootTag(NBTTagCompound tags, String key) {

    if (key != null && !key.isEmpty()) {
      if (!tags.hasKey(key)) {
        tags.setTag(key, new NBTTagCompound());
      }
      return tags.getCompoundTag(key);
    }
    return null;
  }

  public void setToolStats(ToolStats stats) {

    maxDurability = (int) stats.durability;
    harvestLevel = stats.harvestLevel;
    harvestSpeed = stats.harvestSpeed;
    meleeDamage = stats.meleeDamage;
    magicDamage = stats.magicDamage;
    meleeSpeed = stats.meleeSpeed;
    protection = stats.protection;
    enchantability = (int) stats.enchantability;
  }

  static ToolPart getPart(NBTTagCompound tags, String key) {

    return ToolPartRegistry.getPart(tags.getString(key).split("#")[0]);
  }

  public @Nullable ToolPart getPart(EnumPartPosition pos) {

    switch (pos) {
      case HEAD:
        return partHead;
      case ROD:
        return partRod;
      case TIP:
        return partTip;
      default:
        SilentGems.logHelper.warning("ToolData#getPart: Unknown part position " + pos);
        return null;
    }
  }

  public void setPart(ToolPart part, EnumPartPosition pos) {

    switch (pos) {
      case HEAD:
        partHead = part;
        break;
      case ROD:
        partRod = part;
      case TIP:
        partTip = part;
      default:
        SilentGems.logHelper.warning("ToolData#setPart: Unknown part position " + pos);
    }
  }

  public String getOriginalOwner() {

    return originalOwner;
  }

  public void setOriginalOwner(EntityPlayer player) {

    setOriginalOwner(player.getName());
  }

  public void setOriginalOwner(String name) {

    if (originalOwner.isEmpty()) {
      if (name.equals(Names.SILENT_CHAOS_512))
        name = TextFormatting.RED + name;
      else if (name.equals(Names.M4THG33K))
        name = TextFormatting.GREEN + name;
      else if (name.equals(Names.CHAOTIC_PLAYZ))
        name = TextFormatting.BLUE + name;
      else if (name.equals(GuideBookGems.TOOL_OWNER_NAME))
        name = TextFormatting.AQUA + name;

      originalOwner = name;
    }
  }
}
