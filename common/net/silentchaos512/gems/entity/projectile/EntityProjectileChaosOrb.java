package net.silentchaos512.gems.entity.projectile;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.proxy.ClientProxy;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.material.ModMaterials;

public class EntityProjectileChaosOrb extends EntityThrowable
    implements IEntityAdditionalSpawnData {

  public static final double RENDER_DISTANCE_WEIGHT = 10D;
  public static final float SIZE = 0.5F;
  public static final int MAX_LIFE = 120;
  public static final int[] COLORS = { 0xE61D1D, 0xE63F1D, 0xE6601D, 0xE6C41D, 0xA3E61D, 0x2EE61D,
      0x1DC4E6, 0x1D3FE6, 0x3F1DE6, 0x821DE6, 0xEF70C5, 0x010101, 0x6B9F93, 0x474747, 0xD7B5FF };

  public static final String TAG_COLOR = "Color";
  public static final String TAG_DAMAGE = "Damage";
  public static final String TAG_GRAVITY = "Gravity";
  public static final String TAG_SHOOTER = "Shooter";

  public Entity shootingEntity;
  public int color = 0xFFFFFF;
  public float damage = 0;
  public boolean gravity = false;
  public int bounces = 4;

  public EntityProjectileChaosOrb(World world) {

    super(world);
    renderDistanceWeight = RENDER_DISTANCE_WEIGHT;
    setSize(SIZE, SIZE);
  }

  public EntityProjectileChaosOrb(World world, EntityLivingBase shooter, float damage, int gemId,
      boolean gravity) {

    super(world, shooter);
    shootingEntity = shooter;
    renderDistanceWeight = RENDER_DISTANCE_WEIGHT;
    setSize(SIZE, SIZE);

    float speedMulti = 0.7f;
    motionX += shooter.motionX;
    motionY += shooter.motionY;
    motionZ += shooter.motionZ;
    motionX *= speedMulti;
    motionY *= speedMulti;
    motionZ *= speedMulti;
    this.gravity = gravity;

    setDamage(damage);
    gemId = gemId == -1 ? ModMaterials.CHAOS_GEM_ID : gemId;
    if (gemId >= 0 && gemId < COLORS.length) {
      setColor(COLORS[gemId]);
    } else {
      setColor(0xFFFFFF);
      LogHelper.debug("EntityProjectileChaosOrb (constructor): Unknown gemId! " + gemId);
    }

    onUpdate();
  }

  @Override
  public void setPositionAndRotation2(double posX, double posY, double posZ, float rotationYaw,
      float rotationPitch, int unknown) {

    this.setPosition(posX, posY, posZ);
    this.setRotation(rotationYaw, rotationPitch);
  }

  @Override
  public void onUpdate() {

    super.onUpdate();

    if (ticksExisted > MAX_LIFE) {
      setDead();
    }

    if (SilentGems.proxy.getParticleSettings() == 0) {
      double mx = worldObj.rand.nextGaussian() * 0.01f;
      double my = worldObj.rand.nextGaussian() * 0.01f;
      double mz = worldObj.rand.nextGaussian() * 0.01f;
      SilentGems.proxy.spawnParticles(ClientProxy.FX_CHAOS_TRAIL, color, worldObj, posX, posY, posZ,
          mx, my, mz);
    }
  }

  @Override
  public float getGravityVelocity() {

    return gravity ? 0.02f : 0f;
  }

  @Override
  protected void onImpact(MovingObjectPosition mop) {

    if (mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit != shootingEntity) {
      // Collide with Entity?
      mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, shootingEntity), damage);
//      worldObj.createExplosion(this, posX, posY, posZ, 1.25f, false);
      setDead();
    } else if (mop.typeOfHit == MovingObjectType.BLOCK) {
      // Collide with Block?
      Block block = worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);
      AxisAlignedBB boundingBox = block.getCollisionBoundingBoxFromPool(worldObj, mop.blockX,
          mop.blockY, mop.blockZ);

      // Bounce off of blocks that can be collided with.
      if (bounces > 0 && boundingBox != null) {
        --bounces;
        switch (mop.sideHit) {
          case 0:
          case 1:
            motionY *= -0.95;
            break;
          case 2:
          case 3:
            motionZ *= -0.95;
            break;
          case 4:
          case 5:
            motionX *= -0.95;
            break;
        }
         spawnHitParticles(16);
        worldObj.playSoundAtEntity(this, "dig.stone", 0.5f, 0.65f);
      } else if (boundingBox != null) {
        setDead();
      }
    }
  }

  @Override
  public void setDead() {

    spawnHitParticles(64);
    worldObj.playSoundAtEntity(this, "fireworks.blast", 0.75f, 0.75f);

    super.setDead();
  }

  protected void spawnHitParticles(int count) {

    double mX, mY, mZ;
    for (int i = 0; i < count; ++i) {
      mX = rand.nextGaussian() * 0.05;
      mY = rand.nextGaussian() * 0.05;
      mZ = rand.nextGaussian() * 0.05;
      worldObj.spawnParticle("smoke", posX, posY, posZ, mX, mY, mZ);
    }
  }

  @Override
  protected void entityInit() {

  }

  public EntityProjectileChaosOrb setColor(int color) {

    this.color = color;
    return this;
  }

  public EntityProjectileChaosOrb setDamage(float damage) {

    this.damage = damage;
    return this;
  }

  @Override
  public void readEntityFromNBT(NBTTagCompound tags) {

    super.readEntityFromNBT(tags);

    color = tags.getInteger(TAG_COLOR);
    damage = tags.getFloat(TAG_DAMAGE);
    gravity = tags.getBoolean(TAG_GRAVITY);
    if (tags.hasKey(TAG_SHOOTER)) {
      shootingEntity = worldObj.getPlayerEntityByName(tags.getString(TAG_SHOOTER));
      if (shootingEntity == null) {
        setDead();
      }
    }
  }

  @Override
  public void writeEntityToNBT(NBTTagCompound tags) {

    super.writeEntityToNBT(tags);

    tags.setInteger(TAG_COLOR, color);
    tags.setFloat(TAG_DAMAGE, damage);
    tags.setBoolean(TAG_GRAVITY, gravity);
    if (shootingEntity != null) {
      tags.setString(TAG_SHOOTER, shootingEntity.getCommandSenderName());
    }
  }

  @Override
  public void readSpawnData(ByteBuf data) {

    try {
      color = data.readInt();
      damage = data.readFloat();
      gravity = data.readBoolean();
    } catch (Exception ex) {
    }
  }

  @Override
  public void writeSpawnData(ByteBuf data) {

    data.writeInt(color);
    data.writeFloat(damage);
    data.writeBoolean(gravity);
  }
}
