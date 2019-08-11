package net.silentchaos512.gems.chaos;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.gems.api.chaos.IChaosSource;
import net.silentchaos512.gems.block.pedestal.PedestalTileEntity;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.ChaosOrbItem;
import net.silentchaos512.lib.util.DimPos;
import net.silentchaos512.lib.util.TimeUtils;
import net.silentchaos512.lib.util.WorldUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Chaos {
    // Rate of natural dissipation
    private static final int DISSIPATION_SCALE = 20;
    // Chaos will balance out to certain levels, which will vary slightly over time
    private static final int EQUILIBRIUM_BASE = 150_000;
    private static final int EQUILIBRIUM_VARIATION = 100_000;
    private static final int EQUILIBRIUM_CYCLE_LENGTH = TimeUtils.ticksFromHours(4);
    private static final double EQUILIBRIUM_CYCLE_CONSTANT = 2 * Math.PI / EQUILIBRIUM_CYCLE_LENGTH;
    // Distance to search for pedestals with chaos orb
    private static final int PEDESTAL_SEARCH_RADIUS = 6;

    private Chaos() {throw new IllegalAccessError("Utility class");}

    /**
     * Adds to the chaos of the player.
     *
     * @param player    The player
     * @param amount    The amount of chaos
     * @param allowOrbs If true, chaos will first be sent to one chaos orb in the player's
     *                  inventory, with leakage spilling over to the player
     */
    public static void generate(PlayerEntity player, int amount, boolean allowOrbs) {
        if (amount == 0) return;

        int amountLeft = amount;
        if (allowOrbs) {
            // Chaos orbs absorb chaos, but put it all into the first we find
            for (ItemStack stack : player.inventory.mainInventory) {
                if (!stack.isEmpty() && stack.getItem() instanceof ChaosOrbItem) {
                    amountLeft = ChaosOrbItem.absorbChaos(player, stack, amountLeft);
                    break;
                }
            }
        }

        generate(player, amountLeft, player.getPosition(), false);
    }

    /**
     * Adds to the chaos of object. Sends chaos to nearby pedestals with chaos orbs first. This
     * version is typically used for {@link World}s.
     *
     * @param obj    The chaos source (typically World, could be EntityPlayer)
     * @param amount The amount of chaos
     * @param pos    The position of whatever generated the chaos
     */
    public static void generate(ICapabilityProvider obj, int amount, BlockPos pos) {
        generate(obj, amount, pos, true);
    }

    /**
     * Adds to the chaos of object. This version is typically used for {@link World}s.
     *
     * @param obj             The chaos source
     * @param amount          The amount of chaos
     * @param pos             The position of whatever generated the chaos
     * @param sendToPedestals If true, chaos will be sent to nearby pedestals with chaos orbs. The
     *                        remainder is sent directly to obj.
     */
    public static void generate(ICapabilityProvider obj, int amount, BlockPos pos, boolean sendToPedestals) {
        if (amount == 0) return;
        int amountLeft = amount;

        if (sendToPedestals && obj instanceof World) {
            // Search for pedestals with chaos orbs to send chaos to
            World world = (World) obj;
            Collection<PedestalTileEntity> pedestals = getNearbyPedestals(world, pos);

            if (!pedestals.isEmpty()) {
                // Divide evenly between pedestals
                int amountPerOrb = amount / pedestals.size();
                amountLeft = 0;

                for (PedestalTileEntity pedestal : pedestals) {
                    ItemStack stack = pedestal.getItem();
                    amountLeft += ChaosOrbItem.absorbChaos(world, pedestal.getPos(), stack, amountPerOrb);
                    pedestal.setItem(stack);
                }
            }
        }

        final int remainder = amountLeft;
        if (remainder > 0) {
            obj.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source -> source.addChaos(remainder));
        }
    }

    /**
     * Dissipate chaos from the world. If the world's chaos is drained, it dissipates chaos from all
     * players instead.
     *
     * @param world  The world
     * @param amount The amount to dissipate
     */
    public static void dissipate(World world, int amount) {
        if (amount <= 0) return;
        IChaosSource worldSource = world.getCapability(ChaosSourceCapability.INSTANCE).orElseThrow(IllegalStateException::new);
        int amountLeft = worldSource.dissipateChaos(amount);
        if (amountLeft <= 0) return;

        // If all world chaos is gone, dissipate from players
        List<? extends PlayerEntity> players = world.getPlayers();
        int amountPerPlayer = amountLeft / players.size();
        for (PlayerEntity player : players) {
            player.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source ->
                    source.dissipateChaos(amountPerPlayer));
        }
    }

    /**
     * Gets the chaos dissipation rate, which scales with the number of players in the world.
     *
     * @param world The world
     * @return The dissipation rate
     */
    public static int getDissipationRate(World world) {
        return world.getPlayers().size() * DISSIPATION_SCALE;
    }

    public static int getEquilibriumPoint(World world) {
        long time = world.getGameTime();
        return (int) (EQUILIBRIUM_BASE + EQUILIBRIUM_VARIATION * Math.cos(EQUILIBRIUM_CYCLE_CONSTANT * time));
    }

    public static int getChaos(ICapabilityProvider provider) {
        LazyOptional<IChaosSource> optional = provider.getCapability(ChaosSourceCapability.INSTANCE);
        return optional.isPresent() ? optional.orElseGet(ChaosSourceCapability::new).getChaos() : 0;
    }

    /**
     * Gets nearby pedestals holding chaos orbs. Pedestals holding nothing or other items are not
     * included.
     *
     * @param world The world
     * @param pos   The center point
     * @return List of pedestals holding chaos orbs
     */
    private static Collection<PedestalTileEntity> getNearbyPedestals(World world, BlockPos pos) {
        Collection<PedestalTileEntity> list = new ArrayList<>();
        int xMin = pos.getX() - PEDESTAL_SEARCH_RADIUS;
        int xMax = pos.getX() + PEDESTAL_SEARCH_RADIUS;
        int yMin = pos.getY() - PEDESTAL_SEARCH_RADIUS;
        int yMax = pos.getY() + PEDESTAL_SEARCH_RADIUS;
        int zMin = pos.getZ() - PEDESTAL_SEARCH_RADIUS;
        int zMax = pos.getZ() + PEDESTAL_SEARCH_RADIUS;
        return WorldUtils.getTileEntities(PedestalTileEntity.class, world, xMin, yMin, zMin, xMax, yMax, zMax).values();
    }

    /**
     * Get the chaos generated when this entity teleports to the given position. Used by teleporter
     * blocks and return home charms.
     *
     * @param entity      The entity (most likely a player)
     * @param destination The destination
     * @return The amount of chaos to generate
     */
    public static int getChaosGeneratedByTeleport(Entity entity, DimPos destination) {
        // Free for creative players
        if (entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.isCreativeMode)
            return 0;
        DimPos source = DimPos.of(entity.getPosition(), entity.dimension.getId());
        return getChaosGeneratedByTeleport(source, destination);
    }

    public static int getChaosGeneratedByTeleport(DimPos source, DimPos destination) {
        // Crossing dimensions is a fixed cost
        if (source.getDimension() != destination.getDimension()) {
            return GemsConfig.COMMON.teleporterChaosCrossDimension.get();
        }

        // Calculate distance (ignore Y-axis)
        double x = source.getX() - destination.getX();
        double z = source.getZ() - destination.getZ();
        double distance = Math.sqrt(x * x + z * z);

        // Free for short teleports
        if (distance < GemsConfig.COMMON.teleporterFreeRange.get()) return 0;
        return (int) (GemsConfig.COMMON.teleporterChaosPerBlock.get() * distance);
    }
}
