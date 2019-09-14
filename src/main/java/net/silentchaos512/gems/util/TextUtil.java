package net.silentchaos512.gems.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.gems.SilentGems;

public class TextUtil {
    public static ITextComponent translate(String prefix, String suffix, Object... params) {
        String key = String.format("%s.%s.%s", prefix, SilentGems.MOD_ID, suffix);
        return new TranslationTextComponent(key, params);
    }
}
