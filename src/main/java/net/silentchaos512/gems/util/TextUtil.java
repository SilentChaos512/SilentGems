package net.silentchaos512.gems.util;

import net.minecraft.item.Item;
import net.minecraft.util.Util;
import net.minecraft.util.text.*;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.silentchaos512.lib.util.NameUtils;

public final class TextUtil {
    private final String modId;

    public TextUtil(String modId) {
        this.modId = modId;
    }

    public IFormattableTextComponent translate(String prefix, String suffix, Object... params) {
        String key = String.format("%s.%s.%s", prefix, this.modId, suffix);
        return new TranslationTextComponent(key, params);
    }

    public IFormattableTextComponent misc(String suffix, Object... params) {
        String key = String.format("misc.%s.%s", this.modId, suffix);
        return new TranslationTextComponent(key, params);
    }

    public static IFormattableTextComponent itemSub(IForgeRegistryEntry<Item> item, String suffix, Object... params) {
        String key = Util.makeTranslationKey("item", NameUtils.from(item));
        return new TranslationTextComponent(key + "." + suffix, params);
    }

    public static IFormattableTextComponent withColor(IFormattableTextComponent text, int color) {
        return text.mergeStyle(text.getStyle().setColor(Color.fromInt(color & 16777215)));
    }

    public static IFormattableTextComponent withColor(IFormattableTextComponent text, net.silentchaos512.utils.Color color) {
        return withColor(text, color.getColor());
    }

    public static IFormattableTextComponent withColor(IFormattableTextComponent text, TextFormatting color) {
        int colorCode = color.getColor() != null ? color.getColor() : 16777215;
        return withColor(text, colorCode);
    }
}
