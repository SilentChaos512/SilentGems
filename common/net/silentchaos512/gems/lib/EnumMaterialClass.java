package net.silentchaos512.gems.lib;

import net.silentchaos512.gems.core.util.LogHelper;

public enum EnumMaterialClass {

  //@formatter:off
  /**
   * Simple materials like flint, fluffy fabric, etc.
   */
  MUNDANE,
  /**
   * Regular (non-upgraded) gems, as found in the world.
   */
  REGULAR,
  /**
   * Supercharged (artificially upgraded) gems the player creates.
   */
  SUPERCHARGED,
  /**
   * Crystallized chaos essence.
   */
  CHAOS;
  //@formatter:on

  public static float getRepairValue(EnumMaterialClass tool, EnumMaterialClass repair) {

    if (repair == EnumMaterialClass.CHAOS) {
      return 1f;
    }

    //@formatter:off
    switch (tool) {
      case CHAOS:
      case SUPERCHARGED:
        switch (repair) {
          case SUPERCHARGED: return 0.5f;
          case REGULAR: return 0.125f;
          default: return 0f;
        }
      case REGULAR:
        switch (repair) {
          case SUPERCHARGED: return 1f;
          case REGULAR: return 0.25f;
          default: return 0f;
        }
      case MUNDANE:
        return repair == MUNDANE ? 0.5f : 1f;
      default:
        LogHelper.warning("EnumMaterialClass.getRepairValue: unknown tool material (" + tool + ")!");
        return 0f;
    }
    //@formatter:on
  }
}
