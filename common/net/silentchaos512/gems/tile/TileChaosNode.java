package net.silentchaos512.gems.tile;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.gems.api.energy.IChaosProvider;
import net.silentchaos512.gems.entity.packet.EntityChaosNodePacket;
import net.silentchaos512.gems.entity.packet.EntityPacketAttack;
import net.silentchaos512.gems.entity.packet.EntityPacketLevitation;
import net.silentchaos512.gems.entity.packet.EntityPacketRegen;
import net.silentchaos512.gems.entity.packet.EntityPacketRepair;
import net.silentchaos512.gems.entity.packet.EntityPacketSaturation;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.util.ChaosUtil;
import net.silentchaos512.lib.util.Color;

public class TileChaosNode extends TileEntity implements ITickable, IChaosProvider {

  /** Entity search radius squared */
  public static final double SEARCH_RADIUS_SQUARED = 16 * 16;
  /** Block search radius */
  public static final int SEARCH_RADIUS_BLOCK = 6;

  // Chaos generation and transfer
  public static final int SEND_CHAOS_DELAY = 200;
  public static final int SEND_CHAOS_AMOUNT = 2000;
  public static final int MAX_CHAOS_STORED = 10000;
  public static final int CHAOS_GENERATION_RATE = 20;

  // Player effects
  public static final int TRY_REPAIR_DELAY = 200;
  public static final float TRY_REPAIR_CHANCE = 0.2f;
  public static final int TRY_REGEN_DELAY = 300;
  public static final float TRY_REGEN_CHANCE = 0.3f;
  public static final int TRY_SATURATION_DELAY = 512;
  public static final float TRY_SATURATION_CHANCE = 0.1f;

  // Hostile effects
  public static final int TRY_ATTACK_HOSTILES_DELAY = 160;
  public static final float ATTACK_HOSTILE_CHANCE = 0.25f;
  public static final float ATTACK_HOSTILE_BASE_DAMAGE = 4.0f;
  public static final float ATTACK_HOSTILE_DAMAGE_DEVIATION = 0.75f;
  public static final int TRY_LEVITATION_DELAY = 425;
  public static final float TRY_LEVITATION_CHANCE = 0.25f;

  // Variables
  /** The amount of Chaos the node has created and is storing. */
  int chaosStored = 0;
  /** List of nearby players. Cleared each tick, populated only on ticks where it is used. */
  List<EntityPlayerMP> players = Lists.newArrayList();
  /** List of nearby hostiles. Cleared each tick, populated only on ticks where it is used. */
  List<EntityMob> hostiles = Lists.newArrayList();

  @Override
  public void update() {

    if (!worldObj.isRemote) {
      // Generate chaos.
      chaosStored = Math.min(getCharge() + CHAOS_GENERATION_RATE, getMaxCharge());

      // Clear entity lists each tick. These will be repopulated when necessary.
      players.clear();
      hostiles.clear();

      long time = worldObj.getTotalWorldTime();
      boolean playSound = false;

      // Send Chaos?
      if (time % SEND_CHAOS_DELAY == 0) {
        List<IChaosAccepter> accepters = ChaosUtil.getNearbyAccepters(worldObj, pos,
            SEARCH_RADIUS_BLOCK, SEARCH_RADIUS_BLOCK);

        if (!players.isEmpty() || !accepters.isEmpty()) {
          final int amountForEach = Math.min(SEND_CHAOS_AMOUNT,
              getCharge() / (accepters.size() + players.size()));

          playSound |= sendChaosToPlayers(amountForEach);
          playSound |= sendChaosToAccepters(accepters, amountForEach);
        }
      }

      // Send other effects?
      if (time % TRY_REGEN_DELAY == 0)
        playSound |= tryGiveRegen();
      if (time % TRY_SATURATION_DELAY == 0)
        playSound |= tryGiveSaturation();
      if (time % TRY_LEVITATION_DELAY == 0)
        playSound |= tryLevitateHostiles();
      if (time % TRY_REPAIR_DELAY == 0)
        playSound |= tryRepairItems();
      if (time % TRY_ATTACK_HOSTILES_DELAY == 0)
        playSound |= tryAttackHostiles();

      if (playSound)
        worldObj.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_HOE_TILL,
            SoundCategory.AMBIENT, 1.0f, (float) (0.4f + 0.05f * SilentGems.random.nextGaussian()));
    }

    if (worldObj.isRemote)
      spawnParticles();
  }

  private void getPlayersInRange() {

    if (!players.isEmpty())
      return;

    players = worldObj.getPlayers(EntityPlayerMP.class,
        player -> player.getDistanceSq(getPos()) < SEARCH_RADIUS_SQUARED);
  }

  private void getHostilesInRange() {

    if (!hostiles.isEmpty())
      return;

    Entity entity;
    for (int i = 0; i < worldObj.loadedEntityList.size(); ++i) {
      entity = worldObj.loadedEntityList.get(i);
      if (entity instanceof EntityMob && entity.getDistanceSq(pos) < SEARCH_RADIUS_SQUARED)
        hostiles.add((EntityMob) entity);
    }
  }

  private void spawnPacketInWorld(EntityChaosNodePacket packet) {

    packet.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    worldObj.spawnEntityInWorld(packet);
  }

  // ==============
  // = Send Chaos =
  // ==============

  private boolean sendChaosToPlayers(int amountForEach) {

    getPlayersInRange();

    boolean flag = false;
    for (EntityPlayerMP player : players) {
      if (getCharge() <= 0) {
        return flag;
      }
      int amount = extractEnergy(amountForEach, true);
      int amountPlayerCanAccept = ChaosUtil.getAmountPlayerCanAccept(player, amount);
      if (amountPlayerCanAccept > 0) {
        amount = Math.min(amount, amountPlayerCanAccept);
        extractEnergy(amount, false);
        ChaosUtil.spawnPacketToEntity(worldObj, pos, player, amount);
        flag = true;
      }
    }
    return flag;
  }

  private boolean sendChaosToAccepters(List<IChaosAccepter> list, int amountForEach) {

    boolean flag = false;

    for (IChaosAccepter accepter : list) {
      if (getCharge() <= 0) {
        return flag;
      }
      int amount = extractEnergy(amountForEach, true);
      amount = accepter.receiveCharge(amount, true);
      if (amount > 0) {
        extractEnergy(amount, false);
        ChaosUtil.spawnPacketToBlock(worldObj, pos, ((TileEntity) accepter).getPos(), amount);
        flag = true;
      }
    }
    return flag;
  }

  // ==================
  // = Player Effects =
  // ==================

  private boolean tryGiveRegen() {

    getPlayersInRange();

    boolean flag = false;
    Random rand = SilentGems.random;
    for (EntityPlayerMP player : players) {
      if (player.getHealth() < player.getMaxHealth() && rand.nextFloat() < TRY_REGEN_CHANCE) {
        spawnPacketInWorld(new EntityPacketRegen(worldObj, player));
        flag = true;
      }
    }
    return flag;
  }

  private boolean tryGiveSaturation() {

    getPlayersInRange();

    boolean flag = false;
    Random rand = SilentGems.random;
    for (EntityPlayerMP player : players) {
      if (player.getFoodStats().needFood() && rand.nextFloat() < TRY_SATURATION_CHANCE) {
        spawnPacketInWorld(new EntityPacketSaturation(worldObj, player));
        flag = true;
      }
    }
    return flag;
  }

  private boolean tryRepairItems() {

    getPlayersInRange();

    boolean flag = false;
    Random rand = SilentGems.random;
    for (EntityPlayerMP player : players) {
      if (rand.nextFloat() < TRY_REPAIR_CHANCE) {
        spawnPacketInWorld(new EntityPacketRepair(worldObj, player));
        flag = true;
      }
    }
    return flag;
  }

  // ===================
  // = Hostile Effects =
  // ===================

  private boolean tryAttackHostiles() {

    getHostilesInRange();

    int num = 0;
    Random rand = SilentGems.random;

    for (EntityMob mob : hostiles) {
      if (rand.nextFloat() < ATTACK_HOSTILE_CHANCE) {
        // Send an attack packet to the mob.
        float amount = (float) (ATTACK_HOSTILE_BASE_DAMAGE
            + ATTACK_HOSTILE_DAMAGE_DEVIATION * rand.nextGaussian());
        spawnPacketInWorld(new EntityPacketAttack(worldObj, mob, amount));
        ++num;
        if (num > 10) break;
      }
    }
    return num > 0;
  }

  private boolean tryLevitateHostiles() {

    getHostilesInRange();

    int num = 0;
    Random rand = SilentGems.random;

    for (EntityMob mob : hostiles) {
      if (rand.nextFloat() < TRY_LEVITATION_CHANCE) {
        // Send levitation packet
        spawnPacketInWorld(new EntityPacketLevitation(worldObj, mob));
        ++num;
        if (num > 10) break;
      }
    }

    return num > 0;
  }

  // =============
  // = Particles =
  // =============

  private void spawnParticles() {

    Random rand = SilentGems.random;
    for (int i = 0; i < 3 / (1 + SilentGems.proxy.getParticleSettings()); ++i) {
      if (worldObj.isRemote) {
        double motionX = rand.nextGaussian() * 0.03f;
        double motionY = rand.nextGaussian() * 0.006f;
        double motionZ = rand.nextGaussian() * 0.03f;
        SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS, selectParticleColor(rand),
            getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, motionX, motionY,
            motionZ);
      }
    }
  }

  public static Color selectParticleColor(Random rand) {

    float shade = 0.7f + 0.3f * rand.nextFloat();
    float variation = 0.1875f;
    return new Color(
        MathHelper.clamp_float((float) (shade + variation * rand.nextGaussian()), 0f, 1f),
        MathHelper.clamp_float((float) (shade + variation * rand.nextGaussian()), 0f, 1f),
        MathHelper.clamp_float((float) (shade + variation * rand.nextGaussian()), 0f, 1f));
  }

  // ==============
  // = TileEntity =
  // ==============

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("Energy", getCharge());
    return new SPacketUpdateTileEntity(pos, getBlockMetadata(), tags);
  }

  @Override
  public NBTTagCompound getUpdateTag() {

    NBTTagCompound tags = super.getUpdateTag();
    tags.setInteger("Energy", getCharge());
    return tags;
  }

  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {

    chaosStored = packet.getNbtCompound().getInteger("Energy");
  }

  // ==================
  // = IChaosProvider =
  // ==================

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
