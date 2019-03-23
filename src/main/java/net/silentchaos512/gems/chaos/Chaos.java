package net.silentchaos512.gems.chaos;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.chaos.IChaosSource;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.lib.util.DimPos;

public final class Chaos {
    // TODO: Probably should lower this substantially when pylons are back
    private static final int DISSIPATION_SCALE = 200;

    private Chaos() {throw new IllegalAccessError("Utility class");}

    public static void generate(ICapabilityProvider obj, int amount) {
        if (amount == 0) return;
        obj.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source -> source.addChaos(amount));

        if (amount > 0) {
            SilentGems.LOGGER.debug("Generated {} chaos", String.format("%,d", amount));
        } else {
            SilentGems.LOGGER.debug("Dissipated {} chaos", String.format("%,d", -amount));
        }
    }

    public static void dissipate(World world, int amount) {
        if (amount <= 0) return;
        IChaosSource worldSource = world.getCapability(ChaosSourceCapability.INSTANCE).orElseThrow(IllegalStateException::new);
        int amountLeft = worldSource.dissipateChaos(amount);
        if (amountLeft <= 0) return;

        // If all world chaos is gone, dissipate from players
        int amountPerPlayer = amountLeft / world.playerEntities.size();
        for (EntityPlayer player : world.playerEntities) {
            player.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source ->
                    source.dissipateChaos(amountPerPlayer));
        }
    }

    public static int getDissipationRate(World world) {
        return world.playerEntities.size() * DISSIPATION_SCALE;
    }

    public static int getChaosGeneratedByTeleport(EntityLivingBase entity, DimPos destination) {
        // Free for creative players
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).abilities.isCreativeMode)
            return 0;
        DimPos source = DimPos.of(entity.getPosition(), entity.dimension.getId());
        return getChaosGeneratedByTeleport(source, destination);
    }

    public static int getChaosGeneratedByTeleport(DimPos source, DimPos destination) {
        // Crossing dimensions is a fixed cost
        if (source.getDimension() != destination.getDimension()) {
            return GemsConfig.TELEPORTER_COST_CROSS_DIMENSION;
        }

        // Calculate distance (ignore Y-axis)
        double x = source.getX() - destination.getX();
        double z = source.getZ() - destination.getZ();
        double distance = Math.sqrt(x * x + z * z);

        // Free for short teleports
        if (distance < GemsConfig.TELEPORTER_FREE_RANGE) return 0;
        return (int) (GemsConfig.TELEPORTER_COST_PER_BLOCK * distance);
    }
}
