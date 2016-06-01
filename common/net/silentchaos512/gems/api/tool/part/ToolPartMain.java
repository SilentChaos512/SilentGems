package net.silentchaos512.gems.api.tool.part;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.util.ToolHelper;

public class ToolPartMain extends ToolPart {

  public ToolPartMain(String key, ItemStack craftingStack) {

    super(key, craftingStack);
  }

  public ToolPartMain(String key, ItemStack craftingStack, String oreName) {

    super(key, craftingStack, oreName);
  }

  @Override
  public int getRepairAmount(ItemStack tool) {

    int max = tool.getMaxDamage();
    float scale = 0.0f;

    EnumMaterialTier tier = getTier();

    switch (ToolHelper.getToolTier(tool)) {
      case MUNDANE:
        switch (tier) {
          case MUNDANE:
            scale = 0.5f;
            break;
          case REGULAR:
          case SUPER:
            scale = 1.0f;
        }
        break;
      case REGULAR:
        switch (tier) {
          case MUNDANE:
            scale = 0.0f;
            break;
          case REGULAR:
            scale = 0.5f;
            break;
          case SUPER:
            scale = 1.0f;
            break;
        }
        break;
      case SUPER:
        switch (tier) {
          case MUNDANE:
            scale = 0.0f;
            break;
          case REGULAR:
            scale = 0.25f;
            break;
          case SUPER:
            scale = 1.0f;
            break;
        }
        break;
    }

    SilentGems.instance.logHelper.debug(ToolHelper.getToolTier(tool), tier, scale, scale * max);
    return (int) (scale * max);
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
  public int getProtection() {

    return 0;
  }

  @Override
  public boolean validForPosition(EnumPartPosition pos) {

    switch (pos) {
      case HEAD_LEFT:
      case HEAD_MIDDLE:
      case HEAD_RIGHT:
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
