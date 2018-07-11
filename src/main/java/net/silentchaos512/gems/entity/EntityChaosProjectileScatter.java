package net.silentchaos512.gems.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;

public class EntityChaosProjectileScatter extends EntityChaosProjectile {

  static final float SPEED_MULTI = 2.0f;
  // Why does this number need to be so big?
  static final float SCATTER_ANGLE = (float) (1.1f * Math.PI);

  public EntityChaosProjectileScatter(World worldIn) {

    super(worldIn);
    setProperties();
  }

  public EntityChaosProjectileScatter(EntityLivingBase shooter, ItemStack castingStack,
      float damage) {

    super(shooter, castingStack, damage);
    setProperties();
  }

  private void setProperties() {

    bounciness = 0.5f;
    bounces = 2;
    gravity = 0f;
  }

  @Override
  protected void setStartingVelocity(EntityLivingBase shooter, Random random) {

    float pitch = (float) (shooter.rotationPitch + SCATTER_ANGLE * (2 * random.nextDouble() - 1));
    float yaw = (float) (shooter.rotationYaw + SCATTER_ANGLE * (2 * random.nextDouble() - 1));
    Vec3d vec = Vec3d.fromPitchYaw(pitch, yaw);

    motionX = SPEED_MULTI * vec.x + shooter.motionX;
    motionY = SPEED_MULTI * vec.y + shooter.motionY;
    motionZ = SPEED_MULTI * vec.z + shooter.motionZ;
  }

  @Override
  public int getMaxLife() {

    return 40;
  }

  @Override
  public float getGravityVelocity() {

    return 0.0f;
  }

  @Override
  public void onImpactWithEntity(RayTraceResult mop) {

    if (!mop.entityHit.world.isRemote) {
      DamageQueue.addDamage(mop.entityHit, shooter, damage);
    }
    setDead();
  }

  /**
   * The scatter shot is intended to hit the same entity with multiple shots on the same frame. Instead of damage the
   * entity multiple times in one frame, this class adds up the damage then applies it at the end of the server tick.
   * Ghast screams are unpleasant enough without playing them 8 times at once...
   */
  public static class DamageQueue {

    private static List<DamageQueue> list = new ArrayList<>();

    Entity target;
    Entity shooter;
    float totalDamage;

    static void addDamage(Entity target, Entity shooter, float amount) {

      for (DamageQueue d : list) {
        if (d.target == target && d.shooter == shooter) {
          d.totalDamage += amount;
          return;
        }
      }

      DamageQueue d = new DamageQueue();
      d.target = target;
      d.shooter = shooter;
      d.totalDamage = amount;
      list.add(d);
    }

    /**
     * Damage targets and clear the list. Called in GemsCommonEvents#onServerTickEnd.
     */
    public static void processDamage() {

      for (DamageQueue d : list) {
        d.target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(d.shooter, d.shooter),
            d.totalDamage);
        // Allow the entity to be hurt again immediately. Sometimes not all the shots land on the same frame.
        d.target.hurtResistantTime = 0;
      }
      list.clear();
    }
  }
}
