package silent.gems.core.util;

import java.util.Date;
import java.util.logging.Logger;

import net.minecraft.nbt.NBTTagCompound;
import silent.gems.SilentGems;
import silent.gems.lib.Reference;
import cpw.mods.fml.common.FMLLog;

public class LogHelper {

    private final static String CONFIG = "[CONFIG]";
    private final static String DEBUG = "[DEBUG]";
    private final static String FINE = "[FINE]";
    private final static String FINER = "[FINER]";
    private final static String FINEST = "[FINEST]";
    private final static String INFO = "[INFO]";
    private final static String SEVERE = "[SEVERE]";
    private final static String WARNING = "[WARNING]";

    private static Logger logger = Logger.getLogger(Reference.MOD_ID);

    public static void init() {

        logger.setParent((Logger) FMLLog.getLogger());
    }

    public static void log(String logLevel, Object object) {

        // logger.log(logLevel, object.toString());
        System.out.println((new Date()).toString() + " [" + Reference.MOD_ID + "] " + logLevel + " " + object.toString());
    }

    public static void severe(Object object) {

        log(SEVERE, object.toString());
    }

    public static void debug(Object object) {

        log(DEBUG, object.toString());
    }

    public static void warning(Object object) {

        log(WARNING, object.toString());
    }

    public static void info(Object object) {

        log(INFO, object.toString());
    }

    public static void config(Object object) {

        log(CONFIG, object.toString());
    }

    public static void fine(Object object) {

        log(FINE, object.toString());
    }

    public static void finer(Object object) {

        log(FINER, object.toString());
    }

    public static void finest(Object object) {

        log(FINEST, object.toString());
    }

    /**
     * Prints a derp message to the console.
     */
    public static void derp() {

        log(DEBUG, "Derp!");
    }

    public static void derp(String message) {

        log(DEBUG, "Derp! " + message);
    }
    
    public static void derpRand() {
        
        String s = "";
        for (int i = 0; i < SilentGems.instance.random.nextInt(6); ++i) {
            s += " ";
        }
        log(DEBUG, s + "Derp!");
    }

    public static void yay() {

        log(DEBUG, "Yay!");
    }

    // Prints XYZ coordinates in a nice format.
    public static String coord(int x, int y, int z) {

        return "(" + x + ", " + y + ", " + z + ")";
    }

     public static String coordFromNBT(NBTTagCompound tags) {
    
     if (!NBTHelper.hasValidXYZD(tags)) { return "(invalid coords)"; }
    
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
        log(DEBUG, s);
    }
}
