package net.silentchaos512.gems.util;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.level.ItemLike;
import net.silentchaos512.lib.util.NameUtils;

public final class TextUtil {
    private final String modId;

    public TextUtil(String modId) {
        this.modId = modId;
    }

    public MutableComponent translate(String prefix, String suffix, Object... params) {
        String key = String.format("%s.%s.%s", prefix, this.modId, suffix);
        return Component.translatable(key, params);
    }

    public MutableComponent misc(String suffix, Object... params) {
        String key = String.format("misc.%s.%s", this.modId, suffix);
        return Component.translatable(key, params);
    }

    public static MutableComponent itemSub(ItemLike item, String suffix, Object... params) {
        String key = Util.makeDescriptionId("item", NameUtils.fromItem(item));
        return Component.translatable(key + "." + suffix, params);
    }

    public static MutableComponent withColor(MutableComponent text, int color) {
        return text.withStyle(text.getStyle().withColor(TextColor.fromRgb(color & 16777215)));
    }

    public static MutableComponent withColor(MutableComponent text, net.silentchaos512.utils.Color color) {
        return withColor(text, color.getColor());
    }

    public static MutableComponent withColor(MutableComponent text, ChatFormatting color) {
        int colorCode = color.getColor() != null ? color.getColor() : 16777215;
        return withColor(text, colorCode);
    }
}
