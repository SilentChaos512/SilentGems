package net.silentchaos512.gems.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.lib.util.Color;

public class NodePacketHelper {

  public static void spawnParticles(World world, BlockPos from, BlockPos to, int color) {

    spawnParticles(world, new Vec3d(from.getX() + 0.5, from.getY() + 0.5, from.getZ() + 0.5),
        new Vec3d(to.getX() + 0.5, to.getY() + 0.5, to.getZ() + 0.5), color);
  }

  public static void spawnParticles(World world, BlockPos from, Vec3d to, int color) {

    spawnParticles(world, new Vec3d(from.getX() + 0.5, from.getY() + 0.5, from.getZ() + 0.5), to,
        color);
  }

  public static void spawnParticles(World world, Vec3d from, Vec3d to, int color) {

    final int stepCount = 20 - 4 * SilentGems.proxy.getParticleSettings();
    final double distance = (float) from.distanceTo(to);
    final double stepX = (to.x - from.x) / stepCount;
    final double stepY = (to.y - from.y) / stepCount;
    final double stepZ = (to.z - from.z) / stepCount;

    for (int i = 0; i <= stepCount; ++i) {
      double x = from.x + stepX * i;
      double y = from.y + stepY * i;
      double z = from.z + stepZ * i;
      double motionX = 0.0075 * SilentGems.random.nextGaussian();
      double motionY = 0.0075 * SilentGems.random.nextGaussian();
      double motionZ = 0.0075 * SilentGems.random.nextGaussian();
      for (int j = 0; j < 4 - SilentGems.proxy.getParticleSettings(); ++j) {
        SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS_PACKET_TAIL, new Color(color), world,
            x, y, z, motionX, motionY, motionZ);
      }
    }
  }
}
