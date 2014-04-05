package silent.gems.core.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.nbt.NBTTagCompound;
import silent.gems.lib.Reference;
import cpw.mods.fml.common.FMLLog;

public class LogHelper {

    private static Logger abyssLogger = Logger.getLogger(Reference.MOD_ID);

    public static void init() {

        abyssLogger.setParent(FMLLog.getLogger());
    }

    public static void log(Level logLevel, Object object) {

        abyssLogger.log(logLevel, object.toString());
    }

    public static void severe(Object object) {

        log(Level.SEVERE, object.toString());
    }

    public static void debug(Object object) {

        log(Level.WARNING, "[DEBUG] " + object.toString());
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
        
        log(Level.WARNING, "Derp!");
    }
    
    public static void derp(String message) {
        
        log(Level.WARNING, "Derp! " + message);
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
        log(Level.WARNING, "[DEBUG] " + s);
    }
}
