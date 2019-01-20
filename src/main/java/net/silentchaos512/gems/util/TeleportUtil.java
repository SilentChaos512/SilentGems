package net.silentchaos512.gems.util;

public class TeleportUtil {

//  public static boolean teleportPlayerTo(EntityPlayerMP player, DimensionalPosition pos) {
//
//    int oldDimension = player.dimension;
//
//    String debugLine = "Teleporting %s from {%s} to {%s}.";
//    debugLine = String.format(debugLine, player.getName(), new DimensionalPosition(player.getPosition(), player.dimension), pos);
//    SilentGems.logHelper.info(debugLine);
//
//    WorldServer newWorldServer = player.getServer().getWorld(pos.dim);
//    if (pos.dim != oldDimension) {
//      // Dismount and teleport mount
//      if (player.getRidingEntity() != null) {
//        Entity mount = player.getRidingEntity();
//        player.dismountEntity(mount);
//        teleportEntityTo(mount, pos);
//      }
//
//      // Teleport player to dimension, using a custom teleporter to prevent Nether portal spawns
//      player.getServer().getPlayerList().transferPlayerToDimension(player, pos.dim, new TeleporterGems(newWorldServer));
//
//      if (oldDimension == 1) {
//        // Fixes world not loading when teleporting from the End.
//        newWorldServer.spawnEntity(player);
//      }
//    }
//
//    ServerTickHandler.schedule(() -> player.setPositionAndUpdate(pos.x + 0.5, pos.y + 1.0, pos.z + 0.5));
//
//    return true;
//  }
//
//  public static boolean teleportEntityTo(Entity entity, DimensionalPosition pos) {
//
//    int oldDimension = entity.world.provider.getDimension();
//    if (pos.dim != oldDimension) {
//      //entity.changeDimension(pos.dim);
//      return false; // TODO: Fix cross-dimension entity teleportation?
//      // MinecraftServer.getServer().worldServerForDimension(dimension);
//      // entity.travelToDimension(dimension);
//    }
//
//    if (entity instanceof EntityLivingBase) {
//      // Is this necessary?
//      EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
//      entityLivingBase.setPositionAndUpdate(pos.x + 0.5, pos.y + 1.0, pos.z + 0.5);
//    } else {
//      entity.setPosition(pos.x + 0.5, pos.y + 1.0, pos.z + 0.5);
//    }
//
//    return true;
//  }
}
