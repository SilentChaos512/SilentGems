package net.silentchaos512.gems.api.chaos;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Locale;

public enum ChaosEmissionRate {
    NONE(0),
    MINIMAL(8),
    SMALL(64),
    MODERATE(512),
    HIGH(4096),
    VERY_HIGH(32_768),
    EXTREME(262_144);

    private final int maxValue;

    ChaosEmissionRate(int maxValue) {
        this.maxValue = maxValue;
    }

    public static ChaosEmissionRate fromAmount(int amount) {
        for (ChaosEmissionRate e : values()) {
            if (e.maxValue >= amount) {
                return e;
            }
        }
        return EXTREME;
    }

    public ITextComponent getDisplayName() {
        String name = name().toLowerCase(Locale.ROOT);
        return new TextComponentTranslation("chaos.silentgems.emissionRate." + name);
    }

    public ITextComponent getDisplayName(int chaos) {
        if (this == NONE || this == MINIMAL) {
            return getDisplayName();
        }

        ChaosEmissionRate previous = values()[this.ordinal() - 1];
        int diff = this.maxValue - previous.maxValue;

        if (chaos > (previous.maxValue + 2 * diff / 3)) {
            ITextComponent text = new TextComponentTranslation("chaos.silentgems.emissionRate.plus2");
            return getDisplayName().appendSibling(text);
        }
        if (chaos > (previous.maxValue + diff / 3)) {
            ITextComponent text = new TextComponentTranslation("chaos.silentgems.emissionRate.plus1");
            return getDisplayName().appendSibling(text);
        }

        return getDisplayName();
    }

    public ITextComponent getEmissionText() {
        return new TextComponentTranslation("chaos.silentgems.emission", getDisplayName());
    }

    public ITextComponent getEmissionText(int chaos) {
        return new TextComponentTranslation("chaos.silentgems.emission", getDisplayName(chaos));
    }
}
