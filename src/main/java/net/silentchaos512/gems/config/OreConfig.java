package net.silentchaos512.gems.config;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class OreConfig {
    private final String name;
    private final ForgeConfigSpec.IntValue count;
    private final ForgeConfigSpec.IntValue size;
    private final ForgeConfigSpec.IntValue rarity;
    private final ForgeConfigSpec.IntValue minHeight;
    private final ForgeConfigSpec.IntValue maxHeight;
    private ConfiguredFeature<?, ?> configuredFeature = null;

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
                .defineInRange(this.name + ".minHeight", defaults.minHeight, 0, 255);
        this.maxHeight = builder
                .comment("Maximum Y-coordinate (highest level) of veins")
                .defineInRange(this.name + ".maxHeight", defaults.maxHeight, 0, 255);
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

    public ConfiguredFeature<?, ?> createConfiguredFeature(RuleTest fillerType, BlockState state) {
        int bottom = getMinHeight();
        configuredFeature = Feature.ORE
                .withConfiguration(new OreFeatureConfig(fillerType, state, getSize()))
                .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(bottom, bottom, getMaxHeight())))
                .withPlacement(Placement.CHANCE.configure(new ChanceConfig(getRarity())))
                .square()
                .func_242731_b(getCount());
        return configuredFeature;
    }

    public ConfiguredFeature<?, ?> getConfiguredFeature() {
        if (configuredFeature == null) {
            throw new NullPointerException("Configured feature for ore " + name + " has not been created!");
        }
        return configuredFeature;
    }

    public static Defaults defaults(int count, int size, int rarity, int minHeight, int maxHeight) {
        return new Defaults(count, size, rarity, minHeight, maxHeight);
    }

    public static Defaults empty() {
        return Defaults.EMPTY;
    }

    public static class Defaults {
        static final Defaults EMPTY = new Defaults(0, 0, 1, 0, 128);

        public final int count;
        public final int size;
        public final int rarity;
        public final int minHeight;
        public final int maxHeight;

        public Defaults(int count, int size, int rarity, int minHeight, int maxHeight) {
            this.count = count;
            this.size = size;
            this.rarity = rarity;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
        }
    }
}
