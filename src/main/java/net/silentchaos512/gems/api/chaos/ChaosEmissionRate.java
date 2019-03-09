package net.silentchaos512.gems.api.chaos;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Locale;

public enum ChaosEmissionRate {
    NONE(0),
    MINIMAL(10),
    SMALL(100),
    MODERATE(1000),
    HIGH(10_000),
    VERY_HIGH(100_000),
    EXTREME(1_000_000);

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

    public ITextComponent getEmissionText() {
        return new TextComponentTranslation("chaos.silentgems.emission", getDisplayName());
    }
}
