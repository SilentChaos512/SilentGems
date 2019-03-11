package net.silentchaos512.gems.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.world.TeleporterGems;
import net.silentchaos512.lib.event.ServerTicks;
import net.silentchaos512.lib.util.DimPos;

public final class TeleportUtil {
    private TeleportUtil() {throw new IllegalAccessError("Utility class");}

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
            MinecraftServer server = player.getServer();
            if (server == null) return false;
            server.getPlayerList().transferEntityToWorld(
                    player,
                    DimensionType.getById(pos.getDimension()),
                    player.getServerWorld(),
                    destinationWorld,
                    new TeleporterGems(destinationWorld)
            );
            if (oldDimension == 1) {
                // Fixes world not loading when teleporting from the End.
                destinationWorld.spawnEntity(player);
            }
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
}
