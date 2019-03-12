package net.silentchaos512.gems.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.world.TeleporterGems;
import net.silentchaos512.lib.event.ServerTicks;
import net.silentchaos512.lib.util.DimPos;

import javax.annotation.Nullable;

public final class TeleportUtil {
    private TeleportUtil() {throw new IllegalAccessError("Utility class");}

    public static boolean teleportPlayerTo(EntityPlayerMP player, DimPos pos) {
        WorldServer world = getServerWorld(player, pos);
        return teleportPlayerTo(player, world, pos);
    }

    public static boolean teleportPlayerTo(EntityPlayerMP player, WorldServer destinationWorld, DimPos pos) {
        int oldDimension = player.dimension.getId();

        DimPos sourcePos = DimPos.of(player.getPosition(), player.dimension.getId());
        SilentGems.LOGGER.debug("Teleporting {} from {} to {}", player, sourcePos, pos);

        if (pos.getDimension() != oldDimension) {
            // Dismount and teleport mount
            if (player.getRidingEntity() != null) {
                Entity mount = player.getRidingEntity();
                player.dismountEntity(mount);
                teleportEntityTo(mount, pos);
            }

            // Teleport player to dimension, using a custom teleporter to prevent Nether portal spawns
            DimensionType dimensionType = DimensionType.getById(pos.getDimension());
            if (dimensionType == null) return false;
            player.changeDimension(dimensionType, new TeleporterGems(destinationWorld));
        }

        ServerTicks.scheduleAction(() -> player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5));

        return true;
    }

    public static boolean teleportEntityTo(Entity entity, DimPos pos) {
        int oldDimension = entity.world.getDimension().getType().getId();
        if (pos.getDimension() != oldDimension) {
            //entity.changeDimension(pos.dim);
            return false; // TODO: Fix cross-dimension entity teleportation?
            // MinecraftServer.getServer().worldServerForDimension(dimension);
            // entity.travelToDimension(dimension);
        }

        if (entity instanceof EntityLivingBase) {
            // Is this necessary?
            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            entityLivingBase.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
        } else {
            entity.setPosition(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
        }

        return true;
    }

    public static boolean isDestinationSafe(EntityLivingBase entity, DimPos destination) {
        WorldServer world = getServerWorld(entity, destination);
        if (world == null) return false;

        BlockPos headLevel = destination.getPos().up(Math.round(entity.getEyeHeight()));
        return !world.getBlockState(headLevel).causesSuffocation();
    }

    @Nullable
    private static WorldServer getServerWorld(EntityLivingBase entity, DimPos pos) {
        MinecraftServer server = entity.getServer();
        if (server == null) return null;

        DimensionType dimensionType = DimensionType.getById(pos.getDimension());
        if (dimensionType == null) return null;

        return server.getWorld(dimensionType);
    }
}
