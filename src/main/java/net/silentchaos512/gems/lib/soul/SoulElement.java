package net.silentchaos512.gems.lib.soul;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.utils.EnumUtils;
import net.silentchaos512.utils.MathUtils;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public enum SoulElement {
    NONE(Builder.create(1, 0xFFFFFF, TextFormatting.ITALIC)),
    FIRE(Builder.create(13, 0xF48C42, TextFormatting.RED)
            .modifier(Builder.MELEE_DAMAGE, 0.15f)
            .modifier(Builder.RANGED_DAMAGE, 0.10f)
            .modifier(Builder.RANGED_SPEED, -0.05f)
            .modifier(Builder.ARMOR, -0.05f)
    ),
    WATER(Builder.create(12, 0x4189F4, TextFormatting.BLUE)
            .modifier(Builder.MELEE_DAMAGE, -0.10f)
            .modifier(Builder.MAGIC_DAMAGE, 0.15f)
            .modifier(Builder.RANGED_SPEED, 0.10f)
    ),
    EARTH(Builder.create(11, 0x1FC121, TextFormatting.DARK_GREEN)
            .modifier(Builder.DURABILITY, 0.15f)
            .modifier(Builder.ARMOR_DURABILITY, 0.10f)
            .modifier(Builder.MAGIC_DAMAGE, -0.05f)
            .modifier(Builder.RANGED_SPEED, -0.10f)
    ),
    WIND(Builder.create(10, 0x83F7C1, TextFormatting.AQUA)
            .modifier(Builder.REPAIR_EFFICIENCY, 0.05f)
            .modifier(Builder.HARVEST_SPEED, 0.15f)
            .modifier(Builder.MELEE_DAMAGE, -0.10f)
            .modifier(Builder.RANGED_SPEED, 0.15f)
    ),
    METAL(Builder.create(18, 0xAAAAAA, TextFormatting.GRAY)
            .modifier(Builder.DURABILITY, 0.20f)
            .modifier(Builder.ARMOR_DURABILITY, 0.20f)
            .modifier(Builder.REPAIR_EFFICIENCY, -0.10f)
            .modifier(Builder.MAGIC_DAMAGE, -0.10f)
            .modifier(Builder.ARMOR, 0.10f)
    ),
    ICE(Builder.create(17, 0x8EFFEC, TextFormatting.DARK_AQUA)
            .modifier(Builder.DURABILITY, -0.05f)
            .modifier(Builder.ARMOR_DURABILITY, -0.10f)
            .modifier(Builder.MAGIC_DAMAGE, 0.20f)
            .modifier(Builder.ARMOR, -0.05f)
    ),
    LIGHTNING(Builder.create(16, 0xFFFF47, TextFormatting.YELLOW)
            .modifier(Builder.DURABILITY, -0.10f)
            .modifier(Builder.ARMOR_DURABILITY, -0.05f)
            .modifier(Builder.HARVEST_SPEED, 0.10f)
            .modifier(Builder.MELEE_DAMAGE, 0.10f)
            .modifier(Builder.MAGIC_DAMAGE, 0.05f)
            .modifier(Builder.ARMOR, -0.10f)
    ),
    VENOM(Builder.create(15, 0x83C14D, TextFormatting.LIGHT_PURPLE)
            .modifier(Builder.DURABILITY, 0.10f)
            .modifier(Builder.HARVEST_SPEED, -0.15f)
            .modifier(Builder.MELEE_DAMAGE, 0.15f)
            .modifier(Builder.RANGED_DAMAGE, 0.10f)
    ),
    FLORA(Builder.create(5, 0x277C2F, TextFormatting.GREEN)
            .modifier(Builder.DURABILITY, -0.05f)
            .modifier(Builder.ARMOR_DURABILITY, 0.05f)
            .modifier(Builder.REPAIR_EFFICIENCY, 0.10f)
            .modifier(Builder.MELEE_DAMAGE, -0.10f)
            .modifier(Builder.MAGIC_DAMAGE, 0.10f)
    ),
    FAUNA(Builder.create(6, 0xFFA3D7, TextFormatting.DARK_RED)
            .modifier(Builder.MELEE_DAMAGE, 0.10f)
            .modifier(Builder.MAGIC_DAMAGE, -0.10f)
            .modifier(Builder.RANGED_SPEED, 0.05f)
            .modifier(Builder.ARMOR, -0.05f)
    ),
    MONSTER(Builder.create(7, 0x635538, TextFormatting.DARK_BLUE)
            .modifier(Builder.DURABILITY, 0.10f)
            .modifier(Builder.REPAIR_EFFICIENCY, -0.05f)
            .modifier(Builder.HARVEST_SPEED, -0.05f)
            .modifier(Builder.MAGIC_DAMAGE, -0.10f)
            .modifier(Builder.RANGED_DAMAGE, 0.10f)
    ),
    ALIEN(Builder.create(8, 0x8E42A5, TextFormatting.DARK_PURPLE)
            .modifier(Builder.REPAIR_EFFICIENCY, -0.05f)
            .modifier(Builder.HARVEST_SPEED, -0.10f)
            .modifier(Builder.MAGIC_DAMAGE, 0.15f)
            .modifier(Builder.RANGED_SPEED, 0.15f)
            .modifier(Builder.ARMOR, 0.05f)
    );

    public final int weight;
    public final int color;
    public final TextFormatting textColor;
    private final Map<String, Float> modifiers;

    SoulElement(Builder builder) {
        this.weight = builder.weight;
        this.color = builder.color;
        this.textColor = builder.textColor;
        this.modifiers = builder.modifiers;
    }

    public float getStatModifier(String stat) {
        return modifiers.getOrDefault(stat, 0f);
    }

    public ITextComponent getDisplayName() {
        String translationKey = "soul.silentgems.element." + this.name().toLowerCase(Locale.ROOT);
        return new TranslationTextComponent(translationKey).mergeStyle(this.textColor);
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

    public static SoulElement read(PacketBuffer buffer) {
        return EnumUtils.byOrdinal(buffer.readByte(), NONE);
    }

    public void write(PacketBuffer buffer) {
        buffer.writeByte(ordinal());
    }

    private static class Builder {
        // Soul elements exist without Silent Gear, so we can't point directly to the stats
        // Creating constants to reduce the chance of typos
        private static final String DURABILITY = "durability";
        private static final String ARMOR_DURABILITY = "armor_durability";
        private static final String REPAIR_EFFICIENCY = "repair_efficiency";
        private static final String HARVEST_SPEED = "harvest_speed";
        private static final String MELEE_DAMAGE = "melee_damage";
        private static final String MAGIC_DAMAGE = "magic_damage";
        private static final String RANGED_DAMAGE = "ranged_damage";
        private static final String RANGED_SPEED = "ranged_speed";
        private static final String ARMOR = "armor";

        final int weight;
        final int color;
        final TextFormatting textColor;
        final Map<String, Float> modifiers = new LinkedHashMap<>();

        private Builder(int weight, int color, TextFormatting textColor) {
            this.weight = weight;
            this.color = color;
            this.textColor = textColor;
        }

        static Builder create(int weight, int color, TextFormatting textColor) {
            return new Builder(weight, color, textColor);
        }

        Builder modifier(String stat, float value) {
            this.modifiers.put(stat, value);
            return this;
        }
    }
}
