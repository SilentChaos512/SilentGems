package net.silentchaos512.gems.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.silentchaos512.gems.world.TeleporterGems;

public class TeleportUtil {

  public static void teleportPlayerTo(EntityPlayerMP player, int x, int y, int z, int dimension) {

    int oldDimension = player.worldObj.provider.dimensionId;
    if (dimension != oldDimension) {
      WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(dimension);
      // Teleport player to dimension, using a custom teleporter to prevent Nether portal spawns
      // TODO: Mounts
      player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dimension,
          new TeleporterGems(worldServer));
      if (oldDimension == 1) {
        // Fixes world not loading when teleporting from the End.
        worldServer.spawnEntityInWorld(player);
      }
    }
    player.setPositionAndUpdate(x + 0.5, y + 1.0, z + 0.5);
  }
  
  public static void teleportEntityTo(Entity entity, int x, int y, int z, int dimension) {
    
    int oldDimension = entity.worldObj.provider.dimensionId;
    if (dimension != oldDimension) {
      MinecraftServer.getServer().worldServerForDimension(dimension);
      entity.travelToDimension(dimension);
    }
    
    if (entity instanceof EntityLivingBase) {
      // Is this necessary?
      EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
      entityLivingBase.setPositionAndUpdate(x + 0.5, y + 1.0, z + 0.5);
    } else {
      entity.setPosition(x + 0.5, y + 1.0, z + 0.5);
    }
  }
}
