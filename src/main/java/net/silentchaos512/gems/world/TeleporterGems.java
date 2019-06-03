package net.silentchaos512.gems.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ITeleporter;
import net.silentchaos512.lib.util.DimensionalPosition;

import javax.annotation.Nullable;

public final class TeleporterGems implements ITeleporter {
    private final double posX;
    private final double posY;
    private final double posZ;
    private final int dim;

    public static TeleporterGems of(DimensionalPosition pos) {
        return new TeleporterGems(pos.x + 0.5, pos.y + 1.1, pos.z + 0.5, pos.dim);
    }

    private TeleporterGems(double posX, double posY, double posZ, int dim) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.dim = dim;
    }

    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        entity.motionX = entity.motionY = entity.motionZ = 0;
        entity.fallDistance = 0;

        if (entity instanceof EntityPlayerMP && ((EntityPlayerMP) entity).connection != null) {
            ((EntityPlayerMP) entity).connection.setPlayerLocation(posX, posY, posZ, yaw, entity.rotationPitch);
        } else {
            entity.setLocationAndAngles(posX, posY, posZ, yaw, entity.rotationPitch);
        }
    }

    @Nullable
    public Entity teleport(@Nullable Entity entity) {
        if (entity == null || entity.world.isRemote) {
            return entity;
        }

        // TODO: Logging?

        if (dim != entity.dimension) {
            return entity.changeDimension(this.dim, this);
        }

        placeEntity(entity.world, entity, entity.rotationYaw);
        return entity;
    }
}
