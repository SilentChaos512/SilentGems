package net.silentchaos512.gems.lib;

import net.minecraft.util.text.TextFormatting;
import net.silentchaos512.gems.SilentGems;

public class TooltipHelper {

  public static final String FORMAT_INT = "%d";
  public static final String FORMAT_INT_LARGE = "%,d";
  public static final String FORMAT_FLOAT = "%.2f";

  public static String getAsColoredPercentage(String key, float value) {

    return getAsColoredPercentage(key, value, 100, false);
  }

  public static String getAsColoredPercentage(String key, float value, int whitePercentage,
      boolean addPlus) {

    int percent = Math.round(value * 100);
    TextFormatting color = percent > whitePercentage ? TextFormatting.GREEN
        : percent < whitePercentage ? TextFormatting.RED : TextFormatting.WHITE;
    return SilentGems.localizationHelper.getMiscText("Tooltip." + key,
        color + (addPlus && percent > 0 ? "+" : "") + percent + "%");
  }

  public static String numberToPercent(float value, int whitePercentage, boolean addPlus) {

    int percent = Math.round(value * 100);
    TextFormatting color = percent > whitePercentage ? TextFormatting.GREEN
        : percent < whitePercentage ? TextFormatting.RED : TextFormatting.WHITE;
    return color + (addPlus && percent > 0 ? "+" : "") + percent + "%";
  }

  public static String get(String key, int value) {

    return get(key, value, false);
  }

  public static String get(String key, int value, boolean addSpaces) {

    String number = String.format(value > 9999 ? FORMAT_INT_LARGE : FORMAT_INT, value);
    return (addSpaces ? "  " : "")
        + SilentGems.localizationHelper.getMiscText("Tooltip." + key, number);
  }

  public static String get(String key, float value) {

    return get(key, value, false);
  }

  public static String get(String key, float value, boolean addSpaces) {

    if (value == (int) value)
      return get(key, (int) value, addSpaces);

    String number = String.format(FORMAT_FLOAT, value).replaceFirst("0+$", "");
    return (addSpaces ? "  " : "")
        + SilentGems.localizationHelper.getMiscText("Tooltip." + key, number);
  }

  public static String get(String key, String value, boolean addSpaces) {

    return (addSpaces ? "  " : "")
        + SilentGems.localizationHelper.getMiscText("Tooltip." + key, value);
  }
}
