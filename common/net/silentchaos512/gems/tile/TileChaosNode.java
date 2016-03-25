package net.silentchaos512.gems.tile;

import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.EntityChaosTransfer;
import net.silentchaos512.gems.lib.EnumModParticles;

public class TileChaosNode extends TileEntity implements ITickable {

  public static final double SEARCH_RADIUS_SQUARED = 12D * 12D;
  public static final int SEND_CHAOS_DELAY = 200;
  public static final int SEND_CHAOS_AMOUNT = 1000;
  public static final int TRY_POTION_DELAY = 100;

  @Override
  public void update() {

    if (!worldObj.isRemote) {
      List<EntityPlayerMP> players = getPlayersInRange();
      if (worldObj.getTotalWorldTime() % SEND_CHAOS_DELAY == 0) {
        sendChaosToPlayers(players);
      }
      if (worldObj.getTotalWorldTime() % TRY_POTION_DELAY == 0) {
        tryGivePotionEffects(players);
      }
    }

    if (worldObj.isRemote) {
      spawnParticles();
    }
  }

  private List<EntityPlayerMP> getPlayersInRange() {

    Predicate<EntityPlayerMP> predicate = new Predicate<EntityPlayerMP>() {

      @Override
      public boolean apply(EntityPlayerMP player) {

        return player.getDistanceSq(getPos()) < SEARCH_RADIUS_SQUARED;
      }
    };

    return worldObj.getPlayers(EntityPlayerMP.class, predicate);
  }

  private void sendChaosToPlayers(List<EntityPlayerMP> players) {

    for (EntityPlayerMP player : players) {
      EntityChaosTransfer entity = new EntityChaosTransfer(worldObj, player, SEND_CHAOS_AMOUNT);
      entity.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
      worldObj.spawnEntityInWorld(entity);
    }
  }

  private void tryGivePotionEffects(List<EntityPlayerMP> players) {

    for (EntityPlayerMP player : players) {
      // TODO
    }
  }

  private void spawnParticles() {

    for (int i = 0; i < 6 / (1 + 2 * SilentGems.instance.proxy.getParticleSettings()); ++i) {
      if (worldObj.isRemote) {
        double motionX = getWorld().rand.nextGaussian() * 0.05f;
        double motionY = getWorld().rand.nextGaussian() * 0.01f;
        double motionZ = getWorld().rand.nextGaussian() * 0.05f;
        SilentGems.instance.proxy.spawnParticles(EnumModParticles.CHAOS, 0xFFFFFF, getWorld(),
            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, motionX, motionY, motionZ);
      }
    }
  }
}
