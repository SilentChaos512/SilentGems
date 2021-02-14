package net.silentchaos512.gems.util;

import net.minecraft.item.Item;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.silentchaos512.lib.util.NameUtils;

public final class TextUtil {
    private final String modId;

    public TextUtil(String modId) {
        this.modId = modId;
    }

    public ITextComponent translate(String prefix, String suffix, Object... params) {
        String key = String.format("%s.%s.%s", prefix, this.modId, suffix);
        return new TranslationTextComponent(key, params);
    }

    public ITextComponent misc(String suffix, Object... params) {
        String key = String.format("misc.%s.%s", this.modId, suffix);
        return new TranslationTextComponent(key, params);
    }

    public ITextComponent itemSub(IForgeRegistryEntry<Item> item, String suffix, Object... params) {
        String key = Util.makeTranslationKey("item", NameUtils.from(item));
        return new TranslationTextComponent(key + "." + suffix, params);
    }
}
