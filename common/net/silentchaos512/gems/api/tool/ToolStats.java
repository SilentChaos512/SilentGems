package net.silentchaos512.gems.api.tool;

import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.ArmorPartPosition;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.gems.util.ToolHelper;

public final class ToolStats {

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

  public ToolStats(ItemStack tool, ToolPart[] parts, EnumMaterialGrade[] grades) {

    this.tool = tool;
    this.parts = parts;
    this.grades = grades;
  }

  public ToolStats calculate() {

    if (parts.length == 0)
      return this;

    Set<ToolPart> uniqueParts = Sets.newConcurrentHashSet();

    // Head (main) parts
    for (int i = 0; i < parts.length; ++i) {
      ToolPart part = parts[i];
      EnumMaterialGrade grade = grades[i];
      float multi = (100 + grade.bonusPercent) / 100f;

      durability += part.getDurability() * multi;
      harvestSpeed += part.getHarvestSpeed() * multi;
      meleeDamage += part.getMeleeDamage() * multi;
      magicDamage += part.getMagicDamage() * multi;
      meleeSpeed += part.getMeleeSpeed() * multi;
      enchantability += part.getEnchantability() * multi;
      chargeSpeed += part.getChargeSpeed() * multi;
      protection += part.getProtection() * multi;
      harvestLevel = Math.max(harvestLevel, part.getHarvestLevel());
      uniqueParts.add(part);
    }

    // Variety bonus
    int variety = MathHelper.clamp(uniqueParts.size(), 1, GemsConfig.VARIETY_CAP);
    float bonus = 1.0f + GemsConfig.VARIETY_BONUS * (variety - 1);

    // Average head parts
    durability = bonus * durability / parts.length;
    harvestSpeed = bonus * harvestSpeed / parts.length;
    meleeDamage = bonus * meleeDamage / parts.length;
    magicDamage = bonus * magicDamage / parts.length;
    meleeSpeed = bonus * meleeSpeed / parts.length;
    chargeSpeed = bonus * chargeSpeed / parts.length;
    enchantability = bonus * enchantability / parts.length;
    protection = bonus * protection / parts.length;

    // Tool class multipliers
    if (tool.getItem() instanceof ITool) {
      ITool itool = (ITool) tool.getItem();
      durability *= itool.getDurabilityMultiplier();
      harvestSpeed *= itool.getHarvestSpeedMultiplier();
    }

    // Rod, tip, grip, frame
    ToolPart partRod = ToolHelper.getConstructionRod(tool);
    ToolPart partTip = ToolHelper.getConstructionTip(tool);
    ToolPart partGrip = ToolHelper.getPart(tool, ToolPartPosition.ROD_GRIP);
    ToolPart partFrame = ArmorHelper.getPart(tool, ArmorPartPosition.FRAME);

    for (ToolPart part : Lists.newArrayList(partRod, partTip, partGrip, partFrame)) {
      if (part != null) {
        part.applyStats(this);
      }
    }

    return this;
  }
}
