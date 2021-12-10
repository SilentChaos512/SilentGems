package net.silentchaos512.gems.config;

import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.BiFunction;
import java.util.function.Function;

public class OreConfig {
    private final String name;
    private final ForgeConfigSpec.IntValue count;
    private final ForgeConfigSpec.IntValue size;
    private final ForgeConfigSpec.IntValue rarity;
    private final ForgeConfigSpec.IntValue minHeight;
    private final ForgeConfigSpec.IntValue maxHeight;
    private final ForgeConfigSpec.DoubleValue discardChanceOnAirExposure;

    private ConfiguredFeature<?, ?> configuredFeature = null;
    private PlacedFeature placedFeature = null;

    public OreConfig(ForgeConfigSpec.Builder builder, String nameIn, Defaults defaults) {
        this.name = nameIn;
        this.count = builder
                .comment("Number of veins per chunk that the ore spawns in. Rarity is rolled against first and no veins will generate in the chunk if the check fails.")
                .defineInRange(this.name + ".count", defaults.count, 0, Integer.MAX_VALUE);
        this.size = builder
                .comment("Size of veins")
                .defineInRange(this.name + ".size", defaults.size, 0, 1000);
        this.rarity = builder
                .comment("The chance (1 in N) of generating in any given chunk. Higher numbers means more rare.")
                .defineInRange(this.name + ".rarity", defaults.rarity, 0, Integer.MAX_VALUE);
        this.minHeight = builder
                .comment("Minimum Y-coordinate (base height) of veins")
                .defineInRange(this.name + ".minHeight", defaults.minHeight, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.maxHeight = builder
                .comment("Maximum Y-coordinate (highest level) of veins")
                .defineInRange(this.name + ".maxHeight", defaults.maxHeight, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.discardChanceOnAirExposure = builder
                .comment("The chance (out of 1) that the ore will not generate if exposed to air.")
                .defineInRange(this.name + ".discardChanceOnAirExposure", defaults.discardChanceOnAirExposure, 0f, 1f);
    }

    public boolean isEnabled() {
        return getCount() > 0 && getSize() > 0;
    }

    public int getCount() {
        return count.get();
    }

    public int getSize() {
        return size.get();
    }

    public int getRarity() {
        return rarity.get();
    }

    public int getMinHeight() {
        return minHeight.get();
    }

    public int getMaxHeight() {
        return maxHeight.get();
    }

    public float getDiscardChanceOnAirExposure() {
        return discardChanceOnAirExposure.get().floatValue();
    }

    public ConfiguredFeature<?, ?> createConfiguredFeature(Function<OreConfig, ConfiguredFeature<?, ?>> configuredFeatureSupplier,
                                                           BiFunction<OreConfig, ConfiguredFeature<?, ?>, PlacedFeature> placedFeatureFactory) {
        configuredFeature = configuredFeatureSupplier.apply(this);
        placedFeature = placedFeatureFactory.apply(this, configuredFeature);
        return configuredFeature;
    }

    public ConfiguredFeature<?, ?> getConfiguredFeature() {
        if (configuredFeature == null) {
            throw new NullPointerException("Configured feature for ore " + name + " has not been created!");
        }
        return configuredFeature;
    }

    public PlacedFeature getPlacedFeature() {
        if (placedFeature == null) {
            throw new NullPointerException("Placed feature for ore " + name + " has not been created!");
        }
        return placedFeature;
    }

    public static Defaults defaults(int count, int size, int rarity, int minHeight, int maxHeight) {
        return new Defaults(count, size, rarity, minHeight, maxHeight, 0f);
    }

    public static Defaults defaults(int count, int size, int rarity, int minHeight, int maxHeight, float discardChanceOnAirExposure) {
        return new Defaults(count, size, rarity, minHeight, maxHeight, discardChanceOnAirExposure);
    }

    public static Defaults empty() {
        return Defaults.EMPTY;
    }

    public static class Defaults {
        static final Defaults EMPTY = new Defaults(0, 0, 1, 0, 128, 0f);

        public final int count;
        public final int size;
        public final int rarity;
        public final int minHeight;
        public final int maxHeight;
        public final float discardChanceOnAirExposure;

        public Defaults(int count, int size, int rarity, int minHeight, int maxHeight, float discardChanceOnAirExposure) {
            this.count = count;
            this.size = size;
            this.rarity = rarity;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
            this.discardChanceOnAirExposure = discardChanceOnAirExposure;
        }
    }
}
