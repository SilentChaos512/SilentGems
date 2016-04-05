package net.silentchaos512.gems.tile;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosProvider;
import net.silentchaos512.gems.entity.packet.EntityChaosNodePacket;
import net.silentchaos512.gems.entity.packet.EntityPacketAttack;
import net.silentchaos512.gems.entity.packet.EntityPacketRepair;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.util.ChaosUtil;
import net.silentchaos512.lib.util.Color;

public class TileChaosNode extends TileEntity implements ITickable, IChaosProvider {

  public static final double SEARCH_RADIUS_SQUARED = 16 * 16;
  public static final int SEND_CHAOS_DELAY = 200;
  public static final int SEND_CHAOS_AMOUNT = 2000;
  public static final int TRY_POTION_DELAY = 100;

  public static final int TRY_REPAIR_DELAY = 200;
  public static final float TRY_REPAIR_CHANCE = 0.25f;

  public static final int TRY_ATTACK_HOSTILES_DELAY = 100;
  public static final float ATTACK_HOSTILE_CHANCE = 0.25f;
  public static final float ATTACK_HOSTILE_BASE_DAMAGE = 4.0f;
  public static final float ATTACK_HOSTILE_DAMAGE_DEVIATION = 0.75f;

  public static final int MAX_CHAOS_STORED = 10000;
  public static final int CHAOS_GENERATION_RATE = 20;

  private int chaosStored = 0;

  @Override
  public void update() {

    if (!worldObj.isRemote) {
      // Generate chaos.
      // int currentChaos = chaosStored;
      chaosStored = Math.min(getCharge() + CHAOS_GENERATION_RATE, getMaxCharge());
      // if (chaosStored != currentChaos) {
      // markDirty();
      // }
      // SilentGems.instance.logHelper.debug(getCharge());

      // Send energy or effects to nearby players?
      List<EntityPlayerMP> players = getPlayersInRange();
      long time = worldObj.getTotalWorldTime();

      if (time % SEND_CHAOS_DELAY == 0) {
        sendChaosToPlayers(players);
      }
      if (time % TRY_REPAIR_DELAY == 0) {
        tryRepairItems(players);
      }
      if (time % TRY_POTION_DELAY == 0) {
        tryGivePotionEffects(players);
      }
      if (time % TRY_ATTACK_HOSTILES_DELAY == 0) {
        tryAttackHostiles();
      }
    }

    if (worldObj.isRemote) {
      spawnParticles();
    }
  }

  private List<EntityPlayerMP> getPlayersInRange() {

    return worldObj.getPlayers(EntityPlayerMP.class,
        player -> player.getDistanceSq(getPos()) < SEARCH_RADIUS_SQUARED);
  }

  private void spawnPacketInWorld(EntityChaosNodePacket packet) {

    packet.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    worldObj.spawnEntityInWorld(packet);
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

  private void tryRepairItems(List<EntityPlayerMP> players) {

    Random rand = SilentGems.instance.random;
    for (EntityPlayerMP player : players) {
      if (rand.nextFloat() < TRY_REPAIR_CHANCE) {
        spawnPacketInWorld(new EntityPacketRepair(worldObj, player));
      }
    }
  }

  private void tryGivePotionEffects(List<EntityPlayerMP> players) {

    for (EntityPlayerMP player : players) {
      // TODO
    }
  }

  private void tryAttackHostiles() {

    Random rand = SilentGems.instance.random;
    for (EntityLivingBase entity : worldObj.getEntities(EntityMob.class,
        mob -> mob.getDistanceSq(pos) < SEARCH_RADIUS_SQUARED
            && rand.nextFloat() < ATTACK_HOSTILE_CHANCE)) {
      // Send an attack packet to the mob.
      float amount = (float) (ATTACK_HOSTILE_BASE_DAMAGE
          + ATTACK_HOSTILE_DAMAGE_DEVIATION * rand.nextGaussian());
      spawnPacketInWorld(new EntityPacketAttack(worldObj, entity, amount));
    }
  }

  private void spawnParticles() {

    Random rand = SilentGems.instance.random;
    for (int i = 0; i < 6 / (1 + 2 * SilentGems.instance.proxy.getParticleSettings()); ++i) {
      if (worldObj.isRemote) {
        double motionX = rand.nextGaussian() * 0.05f;
        double motionY = rand.nextGaussian() * 0.006f;
        double motionZ = rand.nextGaussian() * 0.05f;
        float shade = 0.7f + 0.3f * rand.nextFloat();
        float variation = 0.075f;
        float red = MathHelper.clamp_float((float) (shade + variation * rand.nextGaussian()), 0f, 1f);
        float green = MathHelper.clamp_float((float) (shade + variation * rand.nextGaussian()), 0f, 1f);
        float blue = MathHelper.clamp_float((float) (shade + variation * rand.nextGaussian()), 0f, 1f);
        SilentGems.instance.proxy.spawnParticles(EnumModParticles.CHAOS,
            new Color(red, green, blue), getWorld(), pos.getX() + 0.5, pos.getY() + 0.5,
            pos.getZ() + 0.5, motionX, motionY, motionZ);
      }
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
