package net.silentchaos512.gems.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.gems.GemsBase;

public final class TextUtil {
    private TextUtil() {throw new IllegalAccessError("Utility class");}

    public static ITextComponent translate(String prefix, String suffix, Object... params) {
        String key = String.format("%s.%s.%s", prefix, GemsBase.MOD_ID, suffix);
        return new TranslationTextComponent(key, params);
    }
}
