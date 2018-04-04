package net.silentchaos512.gems.api.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.ArmorPartPosition;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.stats.CommonItemStats;
import net.silentchaos512.gems.api.stats.ItemStat;
import net.silentchaos512.gems.api.stats.ItemStatModifier;
import net.silentchaos512.gems.api.stats.ItemStatModifier.Operation;
import net.silentchaos512.gems.api.tool.part.ArmorPartFrame;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartGrip;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRod;
import net.silentchaos512.gems.api.tool.part.ToolPartTip;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.gems.util.ToolHelper;

public final class ToolStats {

  public static final String ID_VARIETY_BONUS = "variety_bonus";

  private final ToolPart[] parts;
  private final EnumMaterialGrade[] grades;

  public final ItemStack tool;
  public float durability = 0f;
  public float harvestSpeed = 0f;
  public float meleeDamage = 0f;
  public float magicDamage = 0f;
  public float meleeSpeed = 0f;
  public float chargeSpeed = 0f;
  public float enchantability = 0f;
  public float protection = 0f;
  public int harvestLevel = 0;

  public ToolStats(ItemStack tool) {

    this.tool = tool;
    this.parts = new ToolPart[0];
    this.grades = new EnumMaterialGrade[0];
  }

  public ToolStats(ItemStack tool, ToolPart[] parts, EnumMaterialGrade[] grades) {

    this.tool = tool;
    this.parts = parts;
    this.grades = grades;
  }

  public ToolStats(ItemStack tool, Map<ToolPart, EnumMaterialGrade> parts) {

    this.tool = tool;
    this.parts = parts.keySet().toArray(new ToolPart[0]);
    this.grades = parts.values().toArray(new EnumMaterialGrade[0]);
  }

  public ToolStats calculate() {

    if (parts.length == 0)
      return this;

    Map<ItemStat, List<ItemStatModifier>> mods = new HashMap<>();
    for (ItemStat stat : ItemStat.ALL_STATS) {
      mods.put(stat, new ArrayList<>());
    }
    Set<ToolPart> uniqueParts = Sets.newConcurrentHashSet();

    ToolPart partRod = null, partTip = null, partGrip = null, partFrame = null;

    for (int i = 0; i < parts.length; ++i) {
      ToolPart part = parts[i];
      EnumMaterialGrade grade = grades[i];
      // Head (main) parts influence stats now, others are later.
      if (part instanceof ToolPartMain) {
        for (ItemStat stat : ItemStat.ALL_STATS) {
          ItemStatModifier statModifier = part.getStatModifier(stat, grade);
          if (statModifier != null) {
            mods.get(stat).add(statModifier);
          }
        }
        uniqueParts.add(part);
      } else if (part instanceof ToolPartRod) {
        partRod = part;
      } else if (part instanceof ToolPartTip) {
        partTip = part;
      } else if (part instanceof ArmorPartFrame) {
        partFrame = part;
      }

      // float multi = (100 + grade.bonusPercent) / 100f;
      // durability += part.getDurability() * multi;
      // harvestSpeed += part.getHarvestSpeed() * multi;
      // meleeDamage += part.getMeleeDamage() * multi;
      // magicDamage += part.getMagicDamage() * multi;
      // meleeSpeed += part.getMeleeSpeed() * multi;
      // enchantability += part.getEnchantability() * multi;
      // chargeSpeed += part.getChargeSpeed() * multi;
      // protection += part.getProtection() * multi;
      // harvestLevel = Math.max(harvestLevel, part.getHarvestLevel());
    }

    // Variety bonus
    int variety = MathHelper.clamp(uniqueParts.size(), 1, GemsConfig.VARIETY_CAP);
    float bonus = 1.0f + GemsConfig.VARIETY_BONUS * (variety - 1);
    mods.get(CommonItemStats.DURABILITY)
        .add(new ItemStatModifier(ID_VARIETY_BONUS, bonus, Operation.MULTIPLY));
    mods.get(CommonItemStats.HARVEST_SPEED)
        .add(new ItemStatModifier(ID_VARIETY_BONUS, bonus, Operation.MULTIPLY));
    mods.get(CommonItemStats.MELEE_DAMAGE)
        .add(new ItemStatModifier(ID_VARIETY_BONUS, bonus, Operation.MULTIPLY));
    mods.get(CommonItemStats.MAGIC_DAMAGE)
        .add(new ItemStatModifier(ID_VARIETY_BONUS, bonus, Operation.MULTIPLY));
    mods.get(CommonItemStats.ATTACK_SPEED)
        .add(new ItemStatModifier(ID_VARIETY_BONUS, bonus, Operation.MULTIPLY));
    mods.get(CommonItemStats.CHARGE_SPEED)
        .add(new ItemStatModifier(ID_VARIETY_BONUS, bonus, Operation.MULTIPLY));
    mods.get(CommonItemStats.ENCHANTABILITY)
        .add(new ItemStatModifier(ID_VARIETY_BONUS, bonus, Operation.MULTIPLY));
    mods.get(CommonItemStats.ARMOR)
        .add(new ItemStatModifier(ID_VARIETY_BONUS, bonus, Operation.MULTIPLY));
    mods.get(CommonItemStats.MAGIC_ARMOR)
        .add(new ItemStatModifier(ID_VARIETY_BONUS, bonus, Operation.MULTIPLY));

    // Tool class multipliers
    if (tool.getItem() instanceof ITool) {
      ITool itool = (ITool) tool.getItem();
      durability *= itool.getDurabilityMultiplier();
      harvestSpeed *= itool.getHarvestSpeedMultiplier();
    }

    // Rod, tip, grip, frame
    if (partRod == null) {
      partRod = ToolHelper.getConstructionRod(tool);
    }
    if (partTip == null) {
      partTip = ToolHelper.getConstructionTip(tool);
    }
    if (partGrip == null) {
      partGrip = ToolHelper.getPart(tool, ToolPartPosition.ROD_GRIP);
    }
    if (partFrame == null) {
      partFrame = ArmorHelper.getPart(tool, ArmorPartPosition.FRAME);
    }

    for (ToolPart part : Lists.newArrayList(partRod, partTip, partGrip, partFrame)) {
      if (part != null) {
        for (ItemStat stat : ItemStat.ALL_STATS) {
          ItemStatModifier statModifier = part.getStatModifier(stat, EnumMaterialGrade.NONE);
          if (statModifier != null) {
            mods.get(stat).add(statModifier);
          }
        }
      }
    }

    durability = calcStat(CommonItemStats.DURABILITY, mods);
    harvestSpeed = calcStat(CommonItemStats.HARVEST_SPEED, mods);
    meleeDamage = calcStat(CommonItemStats.MELEE_DAMAGE, mods);
    magicDamage = calcStat(CommonItemStats.MAGIC_DAMAGE, mods);
    meleeSpeed = calcStat(CommonItemStats.ATTACK_SPEED, mods);
    chargeSpeed = calcStat(CommonItemStats.CHARGE_SPEED, mods);
    enchantability = calcStat(CommonItemStats.ENCHANTABILITY, mods);
    protection = calcStat(CommonItemStats.ARMOR, mods);
    harvestLevel = (int) calcStat(CommonItemStats.HARVEST_LEVEL, mods);

    return this;
  }

  public float calcStat(ItemStat stat, Map<ItemStat, List<ItemStatModifier>> mods) {

    // if (stat == CommonItemStats.CHARGE_SPEED) {
    // String str = "";
    // for (ItemStatModifier mod : mods.get(stat)) {
    // str += "{" + mod.getId() + " " + mod.getOperation() + " " + mod.getAmount() + "}, ";
    // }
    // SilentGems.logHelper.debug(str);
    // }
    return stat.compute(0f, mods.get(stat));
  }
}
