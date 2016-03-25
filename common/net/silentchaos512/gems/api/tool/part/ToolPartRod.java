package net.silentchaos512.gems.api.tool.part;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;

public class ToolPartRod extends ToolPart {

  public ToolPartRod(String key, ItemStack stack) {

    super(key, stack);
  }

  public ToolPartRod(String key, String oreName) {

    super(key, oreName);
  }

  @Override
  public int getDurability() {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float getHarvestSpeed() {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getHarvestLevel() {

    return 0;
  }

  @Override
  public float getMeleeDamage() {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float getMagicDamage() {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getEnchantability() {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float getMeleeSpeed() {

    return 1.0f;
  }

  @Override
  public float getChargeSpeed() {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean validForPosition(EnumPartPosition pos) {

    return pos == EnumPartPosition.ROD;
  }

  @Override
  public boolean validForToolOfTier(EnumMaterialTier toolTier) {

    switch (toolTier) {
      case MUNDANE:
      case REGULAR:
        return tier == EnumMaterialTier.MUNDANE || tier == EnumMaterialTier.REGULAR;
      case SUPER:
        return tier == EnumMaterialTier.REGULAR || tier == EnumMaterialTier.SUPER;
      default:
        return true;
    }
  }

  public boolean supportsDecoration() {

    return getTier() == EnumMaterialTier.SUPER;
  }
}
