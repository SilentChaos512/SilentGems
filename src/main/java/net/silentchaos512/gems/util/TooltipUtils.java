package net.silentchaos512.gems.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public final class TooltipUtils {
    private TooltipUtils() {throw new IllegalAccessError("Utility class");}

    public static void addDescLineIfPresent(ItemStack stack, List<ITextComponent> tooltip) {
        String key = stack.getTranslationKey() + ".desc";
        if (I18n.hasKey(key)) {
            tooltip.add(new TranslationTextComponent(key).mergeStyle(TextFormatting.ITALIC));
        }
    }
}
