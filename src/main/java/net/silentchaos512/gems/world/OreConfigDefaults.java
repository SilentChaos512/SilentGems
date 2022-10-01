package net.silentchaos512.gems.world;

public record OreConfigDefaults(
        int count,
        int size,
        int rarity,
        int minHeight,
        int maxHeight,
        float discardChanceOnAirExposure
) {
    private static final OreConfigDefaults EMPTY = new OreConfigDefaults(0, 0, 1, 0, 128, 0f);

    public static OreConfigDefaults defaults(int count, int size, int rarity, int minHeight, int maxHeight) {
        return new OreConfigDefaults(count, size, rarity, minHeight, maxHeight, 0f);
    }

    public static OreConfigDefaults defaults(int count, int size, int rarity, int minHeight, int maxHeight, float discardChanceOnAirExposure) {
        return new OreConfigDefaults(count, size, rarity, minHeight, maxHeight, discardChanceOnAirExposure);
    }

    public static OreConfigDefaults empty() {
        return EMPTY;
    }

    public boolean isEnabled() {
        return count > 0 && size > 0;
    }
}
