package net.silentchaos512.gems.chaos;

import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public final class Chaos {
    private static final int DISSIPATION_SCALE = 5;

    private Chaos() {throw new IllegalAccessError("Utility class");}

    public static void generate(ICapabilityProvider obj, int amount) {
        obj.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source ->
                source.addChaos(amount));
    }

    public static void dissipate(ICapabilityProvider obj, int amount) {
        generate(obj, -amount);
    }

    public static int getDissipationRate(World world) {
        return world.playerEntities.size() * DISSIPATION_SCALE;
    }
}
