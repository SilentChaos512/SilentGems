package net.silentchaos512.gems.tile;

import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosProvider;
import net.silentchaos512.gems.entity.EntityChaosTransfer;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.util.ChaosUtil;

public class TileChaosNode extends TileEntity implements ITickable, IChaosProvider {

  public static final double SEARCH_RADIUS_SQUARED = 12D * 12D;
  public static final int SEND_CHAOS_DELAY = 200;
  public static final int SEND_CHAOS_AMOUNT = 2000;
  public static final int TRY_POTION_DELAY = 100;

  public static final int MAX_CHAOS_STORED = 10000;
  public static final int CHAOS_GENERATION_RATE = 20;

  private int chaosStored = 0;

  @Override
  public void update() {

    if (!worldObj.isRemote) {
      // Generate chaos.
//      int currentChaos = chaosStored;
      chaosStored = Math.min(getCharge() + CHAOS_GENERATION_RATE, getMaxCharge());
//      if (chaosStored != currentChaos) {
//        markDirty();
//      }
//      SilentGems.instance.logHelper.debug(getCharge());

      // Send energy or effects to nearby players?
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

  @Override
  public Packet getDescriptionPacket() {

    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("Energy", getCharge());
    return new SPacketUpdateTileEntity(pos, getBlockMetadata(), tags);
  }

  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {

    chaosStored = packet.getNbtCompound().getInteger("Energy");
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
      if (getCharge() <= 0) {
        return;
      }
      int amount = extractEnergy(SEND_CHAOS_AMOUNT, false);
      ChaosUtil.spawnTransferEntity(worldObj, pos, player, amount);
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

  @Override
  public int getCharge() {

    return chaosStored;
  }

  @Override
  public int getMaxCharge() {

    return MAX_CHAOS_STORED;
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {

    int amount = Math.min(getCharge(), maxExtract);
    if (!simulate) {
      chaosStored -= amount;
    }
    return amount;
  }
}
