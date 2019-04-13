package net.silentchaos512.gems.chaos;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.silentchaos512.gems.api.chaos.IChaosSource;
import net.silentchaos512.gems.block.pedestal.PedestalTileEntity;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.ChaosOrb;
import net.silentchaos512.lib.util.DimPos;

import java.util.ArrayList;
import java.util.Collection;

public final class Chaos {
    // TODO: Probably should lower this substantially when pylons are back
    private static final int DISSIPATION_SCALE = 200;
    private static final int PEDESTAL_SEARCH_RADIUS = 6;

    private Chaos() {throw new IllegalAccessError("Utility class");}

    public static void generate(EntityPlayer player, int amount, boolean allowOrbs) {
        if (amount == 0) return;

        int amountLeft = amount;
        if (allowOrbs) {
            for (ItemStack stack : player.inventory.mainInventory) {
                if (!stack.isEmpty() && stack.getItem() instanceof ChaosOrb) {
                    amountLeft = ChaosOrb.absorbChaos(player, stack, amountLeft);
/*                    if (SilentGems.LOGGER.isDebugEnabled()) {
                        SilentGems.LOGGER.debug("{}'s {} absorbed {} chaos ({} left)",
                                player.getScoreboardName(), stack, amount - amountLeft, amountLeft);
                    }*/
                    break;
                }
            }
        }

        generate(player, amountLeft, player.getPosition());
    }

    public static void generate(ICapabilityProvider obj, int amount, BlockPos pos) {
        if (amount == 0) return;
        int amountLeft = amount;

        if (obj instanceof World) {
            World world = (World) obj;
            Collection<PedestalTileEntity> pedestals = getNearbyPedestals(world, pos);

            if (!pedestals.isEmpty()) {
                amountLeft = 0;
                int amountPerOrb = amount / pedestals.size();

                for (PedestalTileEntity pedestal : pedestals) {
                    ItemStack stack = pedestal.getItem();
                    amountLeft += ChaosOrb.absorbChaos(world, pedestal.getPos(), stack, amountPerOrb);
                    pedestal.setItem(stack);
                }
            }
        }

        final int remainder = amountLeft;
        if (remainder > 0) {
            obj.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source -> source.addChaos(remainder));
        }

/*        if (SilentGems.LOGGER.isDebugEnabled()) {
            if (amount > 0) {
                SilentGems.LOGGER.debug("Generated {} chaos", String.format("%,d", amount));
            } else {
                SilentGems.LOGGER.debug("Dissipated {} chaos", String.format("%,d", -amount));
            }
        }*/
    }

    @SuppressWarnings("OverlyNestedMethod") // Maybe clean this up a bit later?
    private static Collection<PedestalTileEntity> getNearbyPedestals(World world, BlockPos pos) {
        Collection<PedestalTileEntity> list = new ArrayList<>();
        int xMin = pos.getX() - PEDESTAL_SEARCH_RADIUS;
        int xMax = pos.getX() + PEDESTAL_SEARCH_RADIUS;
        int yMin = pos.getY() - PEDESTAL_SEARCH_RADIUS;
        int yMax = pos.getY() + PEDESTAL_SEARCH_RADIUS;
        int zMin = pos.getZ() - PEDESTAL_SEARCH_RADIUS;
        int zMax = pos.getZ() + PEDESTAL_SEARCH_RADIUS;

        if (world.isAreaLoaded(xMin, yMin, zMin, xMax, yMax, zMax, true)) {
            try (BlockPos.PooledMutableBlockPos blockPos = BlockPos.PooledMutableBlockPos.retain()) {
                for (int x = xMin; x <= xMax; ++x) {
                    for (int y = yMin; y <= yMax; ++y) {
                        for (int z = zMin; z <= zMax; ++z) {
                            TileEntity te = world.getTileEntity(blockPos.setPos(x, y, z));
                            if (te instanceof PedestalTileEntity) {
                                ItemStack stack = ((PedestalTileEntity) te).getItem();
                                if (!stack.isEmpty() && stack.getItem() instanceof ChaosOrb) {
                                    list.add((PedestalTileEntity) te);
                                }
                            }
                        }
                    }
                }
            }
        }

        return list;
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
