package net.silentchaos512.gems.api.tool.part;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.gems.util.ToolHelper;

public class ToolPartMain extends ToolPart {

  public ToolPartMain(String key, ItemStack craftingStack) {

    super(key, craftingStack);
  }

  public ToolPartMain(String key, ItemStack craftingStack, String oreName) {

    super(key, craftingStack, oreName);
  }

  @Override
  public int getRepairAmount(ItemStack stack) {

    int max = stack.getMaxDamage();
    float scale = 0.0f;

    EnumMaterialTier partTier = getTier();
    EnumMaterialTier stackTier = stack.getItem() instanceof ITool ? ToolHelper.getToolTier(stack)
        : (stack.getItem() instanceof IArmor ? ArmorHelper.getArmorTier(stack) : null);

    if (stackTier == null)
      return 0;

    switch (ToolHelper.getToolTier(stack)) {
      case MUNDANE:
        switch (partTier) {
          case MUNDANE:
            scale = 0.5f;
            break;
          case REGULAR:
          case SUPER:
            scale = 1.0f;
        }
        break;
      case REGULAR:
        switch (partTier) {
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
        switch (partTier) {
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

    SilentGems.logHelper.debug(stackTier, partTier, scale, scale * max);
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
  public float getProtection() {

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
