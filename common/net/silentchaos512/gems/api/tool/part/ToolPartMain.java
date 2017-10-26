package net.silentchaos512.gems.api.tool.part;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.tool.ToolStats;
import net.silentchaos512.gems.config.GemsConfigHC;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.gems.util.ToolHelper;

public class ToolPartMain extends ToolPart {

  static final float[][] REPAIR_VALUES = {//
      { 0.500f, 1.000f, 1.000f, 1.000f }, // mundane
      { 0.000f, 0.500f, 1.000f, 1.000f }, // regular
      { 0.000f, 0.250f, 1.000f, 1.000f }, // super
      { 0.000f, 0.125f, 0.500f, 1.000f }  // hyper
  };

  public ToolPartMain(String key, ItemStack craftingStack) {

    super(key, craftingStack);
  }

  public ToolPartMain(String key, ItemStack craftingStack, String oreName) {

    super(key, craftingStack, oreName);
  }

  @Override
  public int getRepairAmount(ItemStack toolOrArmor, ItemStack partRep) {

    if (isBlacklisted(partRep)) {
      return 0;
    }

    if (GemsConfigHC.REPAIR_LOGIC != GemsConfigHC.EnumRepairLogic.CLASSIC) {
      ToolPart repairPart = ToolPartRegistry.fromStack(partRep);
      EnumMaterialGrade repairGrade = EnumMaterialGrade.fromStack(partRep);
      float amount = repairPart.getDurability() * (100 + repairGrade.bonusPercent) / 100;

      // Since armor has much lower durability per item, we'll adjust down for armor.
      if (toolOrArmor.getItem() instanceof IArmor) {
        amount /= 2f;
      }

      switch (GemsConfigHC.REPAIR_LOGIC) {
        case HARD_MATERIAL_BASED:
          return (int) (amount / 4f);
        case MATERIAL_BASED:
          return (int) (amount / 2f);
        case NOT_ALLOWED:
          return 0;
        default:
          break;
      }
    }

    // Classic repair logic.
    int max = toolOrArmor.getMaxDamage();
    float scale = 0.0f;

    EnumMaterialTier partTier = getTier();
    EnumMaterialTier stackTier = toolOrArmor.getItem() instanceof ITool
        ? ToolHelper.getToolTier(toolOrArmor)
        : (toolOrArmor.getItem() instanceof IArmor ? ArmorHelper.getArmorTier(toolOrArmor) : null);

    if (stackTier == null)
      return 0;

    int toolTierIndex = ToolHelper.getToolTier(toolOrArmor).ordinal();
    int partTierIndex = partTier.ordinal();
    scale = REPAIR_VALUES[toolTierIndex][partTierIndex];

    return (int) (scale * max);
  }

  @Override
  public final void applyStats(ToolStats stats) {

    // Custom logic not allowed.
  }

  @Override
  public int getDurability() {

    return 0;
  }

  @Override
  public float getHarvestSpeed() {

    return 0;
  }

  @Override
  public int getHarvestLevel() {

    return 0;
  }

  @Override
  public float getMeleeDamage() {

    return 0;
  }

  @Override
  public float getMagicDamage() {

    return 0;
  }

  @Override
  public int getEnchantability() {

    return 0;
  }

  @Override
  public float getMeleeSpeed() {

    return 0;
  }

  @Override
  public float getChargeSpeed() {

    return 0;
  }

  @Override
  public float getProtection() {

    return 0;
  }

  @Override
  public boolean validForPosition(ToolPartPosition pos) {

    switch (pos) {
      case HEAD:
      case ROD_DECO:
        return true;
      default:
        return false;
    }
  }

  @Override
  public boolean validForToolOfTier(EnumMaterialTier toolTier) {

    return getTier() == toolTier;
  }
}
