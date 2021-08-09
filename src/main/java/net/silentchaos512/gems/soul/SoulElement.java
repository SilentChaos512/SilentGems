package net.silentchaos512.gems.soul;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.silentchaos512.gems.util.TextUtil;
import net.silentchaos512.utils.Color;
import net.silentchaos512.utils.MathUtils;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public enum SoulElement {
    NONE(Builder.create(1, Color.WHITE)),
    FORK(Builder.create(13, Color.CRIMSON)
            .modifier(Builder.MELEE_DAMAGE, 0.15f)
            .modifier(Builder.ARMOR, -0.15f)),
    QUIRK(Builder.create(12, Color.CORNFLOWERBLUE)
            .modifier(Builder.MAGIC_DAMAGE, 0.15f)
            .modifier(Builder.HARVEST_SPEED, -0.15f)),
    EJECT(Builder.create(11, Color.MEDIUMSEAGREEN)
            .modifier(Builder.RANGED_SPEED, 0.15f)
            .modifier(Builder.MELEE_DAMAGE, -0.15f)),
    RIBBON(Builder.create(10, Color.HOTPINK)
            .modifier(Builder.REPAIR_EFFICIENCY, 0.15f)
            .modifier(Builder.DURABILITY, -0.10f)
            .modifier(Builder.ARMOR_DURABILITY, -0.10f)),
    SNACK(Builder.create(18, Color.GOLDENROD)
            .modifier(Builder.DURABILITY, 0.10f)
            .modifier(Builder.ARMOR_DURABILITY, 0.10f)
            .modifier(Builder.RANGED_SPEED, -0.15f)),
    GLOOM(Builder.create(17, Color.BLUEVIOLET)
            .modifier(Builder.MAGIC_DAMAGE, 0.20f)
            .modifier(Builder.MELEE_DAMAGE, -0.10f)),
    PEPPER(Builder.create(16, Color.ORANGERED)
            .modifier(Builder.HARVEST_SPEED, 0.10f)
            .modifier(Builder.RANGED_SPEED, 0.10f)
            .modifier(Builder.REPAIR_EFFICIENCY, -0.10f)),
    JOY(Builder.create(15, Color.YELLOW)
            .modifier(Builder.ARMOR, 0.15f)
            .modifier(Builder.MAGIC_DAMAGE, -0.15f)),
    PLUG(Builder.create(5, Color.DARKSEAGREEN)
            .modifier(Builder.HARVEST_SPEED, 0.15f)
            .modifier(Builder.RANGED_DAMAGE, -0.15f)),
    STUN(Builder.create(6, Color.STEELBLUE)
            .modifier(Builder.MELEE_DAMAGE, 0.10f)
            .modifier(Builder.RANGED_DAMAGE, 0.10f)
            .modifier(Builder.ARMOR, -0.15f)),
    KING(Builder.create(7, Color.ROYALBLUE)
            .modifier(Builder.HARVEST_SPEED, 0.10f)
            .modifier(Builder.REPAIR_EFFICIENCY, 0.10f)
            .modifier(Builder.RANGED_SPEED, -0.15f)),
    FLEET(Builder.create(8, Color.MEDIUMPURPLE)
            .modifier(Builder.ARMOR, 0.10f)
            .modifier(Builder.REPAIR_EFFICIENCY, 0.10f)
            .modifier(Builder.HARVEST_SPEED, -0.15f));

    public final int weight;
    public final int color;
    private final Map<String, Float> modifiers;

    SoulElement(Builder builder) {
        this.weight = builder.weight;
        this.color = builder.color;
        this.modifiers = builder.modifiers;
    }

    public float getStatModifier(String stat) {
        return modifiers.getOrDefault(stat, 0f);
    }

    public Component getDisplayName() {
        String translationKey = "soul.silentgems.element." + this.name().toLowerCase(Locale.ROOT);
        return TextUtil.withColor(new TranslatableComponent(translationKey), this.color);
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

    public static SoulElement read(FriendlyByteBuf buffer) {
        return buffer.readEnum(SoulElement.class);
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeByte(ordinal());
    }

    private static final class Builder {
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
        final Map<String, Float> modifiers = new LinkedHashMap<>();

        private Builder(int weight, int color) {
            this.weight = weight;
            this.color = color;
        }

        static Builder create(int weight, Color color) {
            return new Builder(weight, color.getColor());
        }

        Builder modifier(String stat, float value) {
            this.modifiers.put(stat, value);
            return this;
        }
    }
}
