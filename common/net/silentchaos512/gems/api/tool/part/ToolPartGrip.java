package net.silentchaos512.gems.api.tool.part;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;


public class ToolPartGrip extends ToolPart {

  public ToolPartGrip(String key, ItemStack craftingStack) {

    super(key, craftingStack);
  }

  @Override
  public int getDurability() {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float getHarvestSpeed() {

    return 0.0f;
  }

  @Override
  public int getHarvestLevel() {

    // TODO Auto-generated method stub
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

    return pos == EnumPartPosition.ROD_WOOL;
  }

  @Override
  public boolean validForToolOfTier(EnumMaterialTier toolTier) {

    return true;
  }
}
