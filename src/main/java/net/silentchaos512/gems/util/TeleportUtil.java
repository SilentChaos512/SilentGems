package net.silentchaos512.gems.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.event.ServerTickHandler;
import net.silentchaos512.gems.world.TeleporterGems;
import net.silentchaos512.lib.util.DimensionalPosition;

public final class TeleportUtil {
    private TeleportUtil() {}

    public static boolean teleport(Entity entity, DimensionalPosition pos) {
        TeleporterGems teleporter = TeleporterGems.of(pos);

        // Teleport mount as well
        Entity mount = entity.getRidingEntity();
        if (mount != null) {
            entity.dismountRidingEntity();
            teleporter.teleport(mount);
        }

        teleporter.teleport(entity);
        return true;
    }
}
