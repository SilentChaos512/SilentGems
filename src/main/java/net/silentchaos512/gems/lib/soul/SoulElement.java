package net.silentchaos512.gems.lib.soul;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.silentchaos512.utils.MathUtils;

import java.util.Locale;
import java.util.Random;

public enum SoulElement {
    NONE(1, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0xFFFFFF, TextFormatting.ITALIC),
    FIRE(13, 0.00f, 0.00f, +0.15f, 0.00f, -0.05f, 0xF48C42, TextFormatting.RED),
    WATER(12, 0.00f, 0.00f, -0.05f, +0.15f, 0.00f, 0x4189F4, TextFormatting.BLUE),
    EARTH(11, +0.15f, 0.00f, 0.00f, -0.05f, 0.00f, 0x1FC121, TextFormatting.DARK_GREEN),
    WIND(10, 0.00f, +0.15f, -0.05f, 0.00f, 0.00f, 0x83F7C1, TextFormatting.AQUA),
    METAL(18, +0.20f, 0.00f, 0.00f, -0.10f, +0.10f, 0xAAAAAA, TextFormatting.GRAY),
    ICE(17, -0.05f, 0.00f, 0.00f, +0.20f, -0.05f, 0x8EFFEC, TextFormatting.DARK_AQUA),
    LIGHTNING(16, -0.05f, +0.10f, +0.10f, +0.05f, -0.10f, 0xFFFF47, TextFormatting.YELLOW),
    VENOM(15, +0.10f, -0.15f, +0.15f, 0.00f, 0.00f, 0x83C14D, TextFormatting.LIGHT_PURPLE),
    FLORA(5, -0.05f, 0.00f, -0.10f, +0.10f, 0.00f, 0x277C2F, TextFormatting.GREEN),
    FAUNA(6, 0.00f, 0.00f, +0.10f, -0.10f, -0.05f, 0xFFA3D7, TextFormatting.DARK_RED),
    MONSTER(7, +0.10f, -0.05f, 0.00f, -0.10f, 0.00f, 0x635538, TextFormatting.DARK_BLUE),
    ALIEN(8, 0.00f, -0.10f, 0.00f, +0.15f, +0.05f, 0x8E42A5, TextFormatting.DARK_PURPLE);

    public final int weight;
    public final float durabilityModifier;
    public final float harvestSpeedModifier;
    public final float meleeDamageModifier;
    public final float magicDamageModifier;
    public final float protectionModifier;
    public final int color;
    public final TextFormatting textColor;

    SoulElement(int weight, float durability, float harvestSpeed, float meleeDamage, float magicDamage, float protection, int color, TextFormatting textColor) {
        this.weight = weight;
        this.durabilityModifier = durability;
        this.harvestSpeedModifier = harvestSpeed;
        this.meleeDamageModifier = meleeDamage;
        this.magicDamageModifier = magicDamage;
        this.protectionModifier = protection;
        this.color = color;
        this.textColor = textColor;
    }

    public ITextComponent getDisplayName() {
        String translationKey = "soul.silentgems.element." + this.name().toLowerCase(Locale.ROOT);
        return new TextComponentTranslation(translationKey).applyTextStyle(this.textColor);
    }

    public static SoulElement selectRandom(Random random) {
        return selectRandom(random, 0);
    }

    public static SoulElement selectRandom(Random random, float chanceOfNone) {
        if (MathUtils.tryPercentage(random, chanceOfNone))
            return NONE;
        return values()[MathUtils.nextIntInclusive(random, 1, SoulElement.values().length - 1)];
    }

    public static SoulElement fromString(String str) {
        for (SoulElement element : values()) {
            if (element.name().equalsIgnoreCase(str)) {
                return element;
            }
        }
        return NONE;
    }
}
