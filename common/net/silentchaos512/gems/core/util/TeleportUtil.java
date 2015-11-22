package net.silentchaos512.gems.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.silentchaos512.gems.world.TeleporterGems;

public class TeleportUtil {

  public static boolean teleportPlayerTo(EntityPlayerMP player, int x, int y, int z, int dimension) {

    int oldDimension = player.worldObj.provider.getDimensionId();
    if (dimension != oldDimension) {
      WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(dimension);
      
      // Dismount and teleport mount
      if (player.ridingEntity != null) {
        Entity mount = player.ridingEntity;
        player.mountEntity((Entity) null);
        teleportEntityTo(mount, x, y, z, dimension);
      }
      
      // Teleport player to dimension, using a custom teleporter to prevent Nether portal spawns
      player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dimension,
          new TeleporterGems(worldServer));
      
      if (oldDimension == 1) {
        // Fixes world not loading when teleporting from the End.
        worldServer.spawnEntityInWorld(player);
      }
    }
    player.setPositionAndUpdate(x + 0.5, y + 1.0, z + 0.5);
    
    return true;
  }
  
  public static boolean teleportEntityTo(Entity entity, int x, int y, int z, int dimension) {
    
    int oldDimension = entity.worldObj.provider.getDimensionId();
    if (dimension != oldDimension) {
      return false; // TODO: Fix cross-dimension entity teleportation?
//      MinecraftServer.getServer().worldServerForDimension(dimension);
//      entity.travelToDimension(dimension);
    }
    
    if (entity instanceof EntityLivingBase) {
      // Is this necessary?
      EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
      entityLivingBase.setPositionAndUpdate(x + 0.5, y + 1.0, z + 0.5);
    } else {
      entity.setPosition(x + 0.5, y + 1.0, z + 0.5);
    }
    
    return true;
  }
}
