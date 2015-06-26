package net.silentchaos512.gems.core.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.silentchaos512.gems.SilentGems;
import cpw.mods.fml.common.FMLLog;

public class LogHelper {

  private static Logger logger = Logger.getLogger(SilentGems.MOD_ID);

  public static void init() {

    logger.setParent((Logger) FMLLog.getLogger());
  }

  public static void log(Level level, Object object) {

    logger.log(level, object.toString());
  }

  public static void severe(Object object) {

    log(Level.SEVERE, object.toString());
  }

  public static void debug(Object object) {

    log(Level.INFO, object.toString());
  }

  public static void warning(Object object) {

    log(Level.WARNING, object.toString());
  }

  public static void info(Object object) {

    log(Level.INFO, object.toString());
  }

  public static void config(Object object) {

    log(Level.CONFIG, object.toString());
  }

  public static void fine(Object object) {

    log(Level.FINE, object.toString());
  }

  public static void finer(Object object) {

    log(Level.FINER, object.toString());
  }

  public static void finest(Object object) {

    log(Level.FINEST, object.toString());
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
