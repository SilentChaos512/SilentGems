package net.silentchaos512.gems.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class OreConfig {
    private final ForgeConfigSpec.IntValue count;
    private final ForgeConfigSpec.IntValue size;
    private final ForgeConfigSpec.IntValue rarity;
    private final ForgeConfigSpec.IntValue minHeight;
    private final ForgeConfigSpec.IntValue maxHeight;

    public OreConfig(ForgeConfigSpec.Builder builder, String name, Defaults defaults) {
        this.count = builder
                .comment("Number of veins per chunk")
                .defineInRange(name + ".count", defaults.count, 0, Integer.MAX_VALUE);
        this.size = builder
                .comment("Size of veins")
                .defineInRange(name + ".size", defaults.size, 0, 1000);
        this.rarity = builder
                .comment("The chance (1 in N) of generating in any given chunk. Higher numbers means more rare.")
                .defineInRange(name + ".rarity", defaults.rarity, 0, Integer.MAX_VALUE);
        this.minHeight = builder
                .comment("Minimum Y-coordinate (base height) of veins")
                .defineInRange(name + ".minHeight", defaults.minHeight, 0, 255);
        this.maxHeight = builder
                .comment("Maximum Y-coordinate (highest level) of veins")
                .defineInRange(name + ".maxHeight", defaults.maxHeight, 0, 255);
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
