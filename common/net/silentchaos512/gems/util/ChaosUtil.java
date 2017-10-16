package net.silentchaos512.gems.util;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.gems.api.energy.IChaosStorage;
import net.silentchaos512.gems.compat.BaublesCompat;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.lib.collection.ItemStackList;
import net.silentchaos512.lib.util.PlayerHelper;

public class ChaosUtil {

  /**
   * Gets nearby IChaosAccepters that are within the search box and can be seen from center.
   * 
   * @param world
   * @param center
   *          The location of the sender.
   * @param horizontalRadius
   *          The maximum number of blocks to look out of the x- and z-axis.
   * @param verticalRadius
   *          The maximum number of blocks to look up/down on the y-axis.
   * @return List of reachable accepters.
   */
  public static List<IChaosAccepter> getNearbyAccepters(World world, final BlockPos center,
      final int horizontalRadius, final int verticalRadius) {

    List<IChaosAccepter> list = Lists.newArrayList();

    int startX = center.getX() - horizontalRadius;
    int endX = center.getX() + horizontalRadius + 1;
    int startZ = center.getZ() - horizontalRadius;
    int endZ = center.getZ() + horizontalRadius + 1;
    int startY = center.getY() - verticalRadius;
    int endY = center.getY() + verticalRadius + 1;

    TileEntity tile;

    for (int y = startY; y < endY; ++y) {
      for (int x = startX; x < endX; ++x) {
        for (int z = startZ; z < endZ; ++z) {
          BlockPos targetPos = new BlockPos(x, y, z);
          tile = world.getTileEntity(targetPos);
          if (tile != null && tile instanceof IChaosAccepter && canSee(world, center, targetPos)) {
            list.add((IChaosAccepter) tile);
          }
        }
      }
    }

    return list;
  }

  /**
   * Determine if the target position can be "seen" from the source position. Ignores liquids and blocks with no
   * collision.
   * 
   * @return True if there is a clear line of sight, false otherwise.
   */
  public static boolean canSee(World world, BlockPos source, BlockPos target) {

    Vec3d startPos = new Vec3d(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5);
    Vec3d targetPos = new Vec3d(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5);
    // Move start position ahead a bit so it doesn't collide with the source block!
    Vec3d direction = targetPos.subtract(startPos).normalize();
    startPos = startPos.add(direction);
    RayTraceResult result = world.rayTraceBlocks(startPos, targetPos, false, true, false);
    return result == null || result.getBlockPos().equals(target);
  }

  /**
   * Determines if the target entity can be "seen" from the source position. Ignores liquids and blocks with no
   * collision.
   * 
   * @return True if there is a clear line of sight, false otherwise.
   */
  public static boolean canSee(World world, BlockPos source, Entity target) {

    Vec3d startPos = new Vec3d(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5);
    Vec3d targetPos = target.getPositionVector().addVector(0, target.height / 2, 0);
    RayTraceResult result = world.rayTraceBlocks(startPos, targetPos, false, true, false);
    return result == null
        || result.getBlockPos().equals(target.getPosition().up((int) (target.height / 2)));
  }

  /**
   * Gets the total amount of chaos the player has available, including chaos stored in items.
   */
  public static int getTotalChaosAvailable(EntityPlayer player) {

    PlayerData data = PlayerDataHandler.get(player);
    int amount = data.getCurrentChaos();

    for (ItemStack stack : getChaosStorageItems(player)) {
      if (stack.getItem() instanceof IChaosStorage) {
        amount += ((IChaosStorage) stack.getItem()).getCharge(stack);
      }
    }

    return amount;
  }

  /**
   * Drains chaos from the player, draining from items that can provide chaos first.
   */
  public static void drainChaosFromPlayerAndInventory(EntityPlayer player, int amount) {

    int startAmount = amount;
    for (ItemStack stack : getChaosStorageItems(player)) {
      if (stack.getItem() instanceof IChaosStorage) {
        amount -= ((IChaosStorage) stack.getItem()).extractCharge(stack, amount, false);
        if (amount <= 0)
          break;
      }
    }

    PlayerDataHandler.get(player).drainChaos(amount);
  }

  @Deprecated // Use getAmountPlayerCanAccept instead
  public static boolean canPlayerAcceptFullAmount(EntityPlayer player, int amount) {

    PlayerData data = PlayerDataHandler.get(player);
    amount -= data.getMaxChaos() - data.getCurrentChaos();
    for (ItemStack stack : getChaosStorageItems(player)) {
      if (stack.getItem() instanceof IChaosStorage) {
        amount -= ((IChaosStorage) stack.getItem()).receiveCharge(stack, amount, true);
      }
    }
    return amount <= 0;
  }

  /**
   * Gets the amount of chaos the player could receive, include available capacity in items that store chaos.
   */
  public static int getAmountPlayerCanAccept(EntityPlayer player, int maxToSend) {

    PlayerData data = PlayerDataHandler.get(player);
    int amount = data.getMaxChaos() - data.getCurrentChaos();

    for (ItemStack stack : getChaosStorageItems(player)) {
      amount += ((IChaosStorage) stack.getItem()).receiveCharge(stack, maxToSend - amount, true);
      if (amount >= maxToSend)
        return maxToSend;
    }

    return amount;
  }

  /**
   * Send chaos to an entity. Replaces the old packet spawning methods. Also spawns particles.
   * 
   * @return True if the entity was able to receive chaos, false otherwise.
   */
  public static boolean sendEnergyTo(World world, BlockPos start, EntityLivingBase target,
      int amount) {

    if (target instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) target;
      amount -= PlayerDataHandler.get(player).sendChaos(amount, true);

      for (ItemStack stack : getChaosStorageItems(player)) {
        if (stack.getItem() instanceof IChaosStorage) {
          amount -= ((IChaosStorage) stack.getItem()).receiveCharge(stack, amount, false);
          if (amount <= 0) {
            break;
          }
        }
      }

      NodePacketHelper.spawnParticles(world, start,
          target.getPositionVector().addVector(0, target.height / 2, 0), 0xFFFF99);
      return true;
    }

    return false;
  }

  /**
   * Send chaos to a tile entity. Replaces the old packet spawning methods. Also spawns particles.
   * 
   * @return True if the tile entity was able to receive chaos, false otherwise.
   */
  public static boolean sendEnergyTo(World world, BlockPos start, BlockPos target, int amount) {

    TileEntity tile = world.getTileEntity(target);
    if (tile instanceof IChaosAccepter) {
      ((IChaosAccepter) tile).receiveCharge(amount, false);
      NodePacketHelper.spawnParticles(world, start, target, 0xFFFF99);
      return true;
    }
    return false;
  }

  /**
   * Gets all chaos-storing items on the player, including baubles.
   */
  public static ItemStackList getChaosStorageItems(EntityPlayer player) {

    ItemStackList list = BaublesCompat.getBaubles(player,
        s -> s.getItem() instanceof IChaosStorage);
    for (ItemStack stack : PlayerHelper.getNonEmptyStacks(player))
      if (stack.getItem() instanceof IChaosStorage)
        list.add(stack);
    return list;
  }
}
