package net.silentchaos512.gems.util;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.silentchaos512.lib.util.DimPos;
import net.silentchaos512.lib.util.TeleporterSL;

import javax.annotation.Nullable;

public final class TeleportUtil {
    private TeleportUtil() {throw new IllegalAccessError("Utility class");}

    public static boolean teleport(Entity entity, DimPos pos) {
        ServerWorld world = getServerWorld(entity, pos);
        if (world == null) return false;
        TeleporterSL teleporter = TeleporterSL.of(world, pos.offset(Direction.UP, 1));
        teleporter.teleportWithMount(entity);
        return true;
    }

    public static boolean isDestinationSafe(Entity entity, DimPos destination) {
        ServerWorld world = getServerWorld(entity, destination);
        if (world == null) return false;

        BlockPos headLevel = destination.getPos().up(Math.round(entity.getEyeHeight()));
        return !world.getBlockState(headLevel).causesSuffocation(world, destination.getPos());
    }

    @Nullable
    private static ServerWorld getServerWorld(Entity entity, DimPos pos) {
        MinecraftServer server = entity.getServer();
        if (server == null) return null;

        DimensionType dimensionType = DimensionType.getById(pos.getDimension());
        if (dimensionType == null) return null;

        return server.getWorld(dimensionType);
    }
}
