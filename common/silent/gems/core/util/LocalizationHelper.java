package silent.gems.core.util;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class LocalizationHelper {

    public final static String GEMS_PREFIX = "gems.silentgems:";
    public final static String MISC_PREFIX = "misc.silentgems:";
    
    public static String getLocalizedString(String key) {

        return StatCollector.translateToLocal(key).trim();
    }

    public static String getMessageText(String key) {

        return getMessageText(key, EnumChatFormatting.ITALIC);
    }

    public static String getMessageText(String key, EnumChatFormatting format) {

        return getMessageText(key, format.toString());
    }

    public static String getMessageText(String key, String format) {

        return (new StringBuilder()).append(format).append(getLocalizedString(MISC_PREFIX + key)).toString();
    }
    
    public static String getGemName(int id) {
        
        // TODO: Check this.
        return StatCollector.translateToLocal(GEMS_PREFIX + id).trim();
    }
}
