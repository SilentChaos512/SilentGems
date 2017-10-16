package net.silentchaos512.gems.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.gems.api.energy.IChaosProvider;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.lib.NodeEffect;
import net.silentchaos512.gems.util.ChaosUtil;
import net.silentchaos512.gems.util.NodePacketHelper;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.lib.tile.TileEntitySL;
import net.silentchaos512.lib.util.Color;

public class TileChaosNode extends TileEntitySL implements ITickable, IChaosProvider {

  /** Entity search radius squared */
  public static final double SEARCH_RADIUS_SQUARED = 16 * 16;
  /** Block search radius */
  public static final int SEARCH_RADIUS_BLOCK = 6;

  // Chaos generation and transfer
  public static final int SEND_CHAOS_DELAY = 200;
  public static final int SEND_CHAOS_AMOUNT = 2000;
  public static final int MAX_CHAOS_STORED = 10000;
  public static final int CHAOS_GENERATION_RATE = 20;

  // Variables
  /** The amount of Chaos the node has created and is storing. */
  @SyncVariable(name = "Energy")
  int chaosStored = 0;
  /** List of nearby players. Cleared each tick, populated only on ticks where it is used. */
  List<EntityPlayerMP> players = new ArrayList<>();
  /** List of nearby passive mobs. Cleared each tick, populated only on ticks where it is used. */
  List<EntityLivingBase> passives = new ArrayList<>();
  /** List of nearby hostile mobs. Cleared each tick, populated only on ticks where it is used. */
  List<EntityLivingBase> hostiles = new ArrayList<>();
  /**
   * A value added to world time to make different nodes fire at different times. Based on a product of the x-, y-, and
   * z-coordinates of the node. Set in update if less than 0.
   */
  int timeSalt = -1;

  @Override
  public void update() {

    if (timeSalt < 0) {
      if (GemsConfig.CHAOS_NODE_SALT_DELAY)
        timeSalt = (pos.getX() * pos.getY() * pos.getZ()) % 400;
      else
        timeSalt = 0;
    }

    // Generate chaos.
    chaosStored = Math.min(getCharge() + CHAOS_GENERATION_RATE, getMaxCharge());

    // Clear entity lists each tick. These will be repopulated when necessary.
    players.clear();
    passives.clear();
    hostiles.clear();

    long time = world.getTotalWorldTime() + timeSalt;
    // Using a random seeded with world time seems to keep the particles (client) synced with which ones actually are
    // affected (server)... at least in singleplayer.
    Random random = new Random(time);
    boolean playSound = false;

    // Send Chaos?
    if (time % SEND_CHAOS_DELAY == 0) {
      List<IChaosAccepter> accepters = ChaosUtil.getNearbyAccepters(world, pos, SEARCH_RADIUS_BLOCK,
          SEARCH_RADIUS_BLOCK);
      getPlayersInRange();

      if (!players.isEmpty() || !accepters.isEmpty()) {
        final int amountForEach = Math.min(SEND_CHAOS_AMOUNT,
            getCharge() / (accepters.size() + players.size()));

        playSound |= sendChaosToPlayers(amountForEach);
        playSound |= sendChaosToAccepters(accepters, amountForEach);
      }
    }

    // Try effects
    for (NodeEffect effect : NodeEffect.ALL_EFFECTS) {
      if (effect.isTimeToTry(time)) {
        if (effect.targetPlayers) {
          getPlayersInRange();
          for (EntityPlayer player : players) {
            playSound |= tryApplyEffectToEntity(player, effect, random);
          }
        }
        if (effect.targetPassives) {
          getPassivesInRange();
          for (EntityLivingBase passive : passives) {
            playSound |= tryApplyEffectToEntity(passive, effect, random);
          }
        }
        if (effect.targetHostiles) {
          getHostilesInRange();
          for (EntityLivingBase hostile : hostiles) {
            playSound |= tryApplyEffectToEntity(hostile, effect, random);
          }
        }
      }
    }

    if (playSound)
      world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_HOE_TILL,
          SoundCategory.AMBIENT, 1.0f, (float) (0.4f + 0.05f * SilentGems.random.nextGaussian()));

    if (world.isRemote)
      spawnParticles();
  }

  public boolean tryApplyEffectToEntity(EntityLivingBase entity, NodeEffect effect, Random random) {

    if (effect.applyToEntity(entity, random)) {
      NodePacketHelper.spawnParticles(world, pos,
          entity.getPositionVector().addVector(0, entity.height / 2, 0), effect.color);
      return true;
    }
    return false;
  }

  private void getPlayersInRange() {

    if (!players.isEmpty())
      return;

    players = world.getPlayers(EntityPlayerMP.class,
        player -> player.getDistanceSq(getPos()) < SEARCH_RADIUS_SQUARED && canSee(player));
  }

  private void getPassivesInRange() {

    if (!passives.isEmpty())
      return;

    Entity entity;
    for (int i = 0; i < world.loadedEntityList.size(); ++i) {
      entity = world.loadedEntityList.get(i);
      if (entity instanceof EntityLivingBase && !(entity instanceof IMob)
          && entity.getDistanceSq(pos) < SEARCH_RADIUS_SQUARED && canSee(entity))
        passives.add((EntityLivingBase) entity);
    }
  }

  private void getHostilesInRange() {

    if (!hostiles.isEmpty())
      return;

    Entity entity;
    for (int i = 0; i < world.loadedEntityList.size(); ++i) {
      entity = world.loadedEntityList.get(i);
      if (entity instanceof EntityLivingBase && entity instanceof IMob
          && entity.getDistanceSq(pos) < SEARCH_RADIUS_SQUARED && canSee(entity))
        hostiles.add((EntityLivingBase) entity);
    }
  }

  private boolean canSee(BlockPos target) {

    return ChaosUtil.canSee(world, pos, target);
  }

  private boolean canSee(Entity target) {

    return ChaosUtil.canSee(world, pos, target);
  }

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
        ChaosUtil.sendEnergyTo(world, pos, player, amount);
        flag = true;
      }
    }
    return flag;
  }

  private boolean sendChaosToAccepters(List<IChaosAccepter> list, int amountForEach) {

    getPlayersInRange();

    boolean flag = false;

    for (IChaosAccepter accepter : list) {
      if (getCharge() <= 0) {
        return flag;
      }
      int amount = extractEnergy(amountForEach, true);
      amount = accepter.receiveCharge(amount, true);
      if (amount > 0) {
        extractEnergy(amount, false);
        ChaosUtil.sendEnergyTo(world, pos, ((TileEntity) accepter).getPos(), amount);
        flag = true;
      }
    }
    return flag;
  }

  private void spawnParticles() {

    Random rand = SilentGems.instance.random;

    // Get particle settings, using the node-specific override config if applicable.
    int particleSetting = GemsConfig.CHAOS_NODE_PARTICLE_OVERRIDE;
    if (particleSetting < 0) {
      particleSetting = SilentGems.proxy.getParticleSettings();
    }

    for (int i = 0; i < 3 / (1 + particleSetting); ++i) {
      if (world.isRemote) {
        double motionX = rand.nextGaussian() * 0.03f;
        double motionY = rand.nextGaussian() * 0.006f;
        double motionZ = rand.nextGaussian() * 0.03f;
        SilentGems.instance.proxy.spawnParticles(EnumModParticles.CHAOS, selectParticleColor(rand),
            getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, motionX, motionY,
            motionZ);
      }
    }
  }

  public static Color selectParticleColor(Random rand) {

    float shade = 0.7f + 0.3f * rand.nextFloat();
    float variation = 0.1875f;
    return new Color(MathHelper.clamp((float) (shade + variation * rand.nextGaussian()), 0f, 1f),
        MathHelper.clamp((float) (shade + variation * rand.nextGaussian()), 0f, 1f),
        MathHelper.clamp((float) (shade + variation * rand.nextGaussian()), 0f, 1f));
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
