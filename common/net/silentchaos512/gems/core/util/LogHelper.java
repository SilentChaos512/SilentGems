package net.silentchaos512.gems.core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.silentchaos512.gems.SilentGems;

public class LogHelper {

  public static void severe(Object object) {

    SilentGems.logger.error(object);
  }

  public static void debug(Object object) {

    SilentGems.logger.debug(object);
    System.out.println(object);
  }

  public static void warning(Object object) {

    SilentGems.logger.warn(object);
  }

  public static void info(Object object) {

    SilentGems.logger.info(object);
  }

  /**
   * Prints a derp message to the console.
   */
  public static void derp() {

    debug("Derp!");
  }

  public static void derp(String message) {

    debug("Derp! " + message);
  }

  public static void derpRand() {

    String s = "";
    for (int i = 0; i < SilentGems.instance.random.nextInt(6); ++i) {
      s += " ";
    }
    debug(s + "Derp!");
  }

  public static void yay() {

    debug("Yay!");
  }

  // Prints XYZ coordinates in a nice format.
  public static String coord(int x, int y, int z) {

    return "(" + x + ", " + y + ", " + z + ")";
  }

  public static String coordFromNBT(NBTTagCompound tags) {

    if (!NBTHelper.hasValidXYZD(tags)) {
      return "(invalid coords)";
    }

    return coord(tags.getInteger("X"), tags.getInteger("Y"), tags.getInteger("Z"));
  }

  public static void list(Object... objects) {

    String s = "";
    for (int i = 0; i < objects.length; ++i) {
      if (i != 0) {
        s += ", ";
      }
      s += objects[i];
    }
    debug(s);
  }
}