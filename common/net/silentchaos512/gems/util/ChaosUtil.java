package net.silentchaos512.gems.util;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.gems.api.energy.IChaosStorage;
import net.silentchaos512.gems.entity.packet.EntityPacketChaos;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.lib.util.PlayerHelper;

public class ChaosUtil {

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
          tile = world.getTileEntity(new BlockPos(x, y, z));
          if (tile != null && tile instanceof IChaosAccepter) {
            list.add((IChaosAccepter) tile);
          }
        }
      }
    }

    return list;
  }

  public static int getTotalChaosAvailable(EntityPlayer player) {

    PlayerData data = PlayerDataHandler.get(player);
    int amount = data.getCurrentChaos();

    for (ItemStack stack : PlayerHelper.getNonNullStacks(player)) {
      if (stack.getItem() instanceof IChaosStorage) {
        amount += ((IChaosStorage) stack.getItem()).getCharge(stack);
      }
    }

    return amount;
  }

  public static void drainChaosFromPlayerAndInventory(EntityPlayer player, int amount) {

    int startAmount = amount;
    for (ItemStack stack : PlayerHelper.getNonNullStacks(player)) {
      if (stack.getItem() instanceof IChaosStorage) {
        amount -= ((IChaosStorage) stack.getItem()).extractCharge(stack, amount, false);
        if (amount <= 0)
          break;
      }
    }

    PlayerDataHandler.get(player).drainChaos(amount);
  }

  public static boolean canPlayerAcceptFullAmount(EntityPlayer player, int amount) {

    PlayerData data = PlayerDataHandler.get(player);
    amount -= data.getMaxChaos() - data.getCurrentChaos();
    for (ItemStack stack : PlayerHelper.getNonNullStacks(player)) {
      if (stack.getItem() instanceof IChaosStorage) {
        amount -= ((IChaosStorage) stack.getItem()).receiveCharge(stack, amount, true);
      }
    }
    return amount <= 0;
  }

  public static void spawnPacketToEntity(World world, BlockPos start, EntityLivingBase target,
      int amount) {

    EntityPacketChaos entity = new EntityPacketChaos(world, target, amount);
    entity.setPosition(start.getX() + 0.5, start.getY() + 0.5, start.getZ() + 0.5);
    world.spawnEntityInWorld(entity);
  }

  public static void spawnPacketToBlock(World world, BlockPos start, BlockPos target, int amount) {

    EntityPacketChaos entity = new EntityPacketChaos(world, target, amount);
    entity.setPosition(start.getX() + 0.5, start.getY() + 0.5, start.getZ() + 0.5);
    world.spawnEntityInWorld(entity);
  }
}
