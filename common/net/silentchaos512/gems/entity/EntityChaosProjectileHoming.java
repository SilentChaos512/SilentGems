package net.silentchaos512.gems.entity;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;

public class EntityChaosProjectileHoming extends EntityChaosProjectile {

  public static final double HOMING_TIGHTNESS = 0.075;
  public static final double HOMING_SPEED = 0.3;
  protected Entity homingTarget = null;

  public EntityChaosProjectileHoming(World worldIn) {

    super(worldIn);
  }

  public EntityChaosProjectileHoming(EntityLivingBase shooter, ItemStack castingStack, float damage,
      boolean flyUpBeforeHoming) {

    super(shooter, castingStack, damage);

    Vec3d vec = shooter.getLookVec();
    if (flyUpBeforeHoming) {
      vec = vec.rotateYaw(2 * SilentGems.instance.random.nextFloat() - 0.5f);
      vec = new Vec3d(vec.x, 2, vec.z).normalize()
          .scale(0.25 + 1.5 * SilentGems.instance.random.nextDouble());
    }

    motionX = vec.x;
    motionY = vec.y;
    motionZ = vec.z;
  }

  public void findHomingTarget() {

    if (world.isRemote || homingTarget != null || (ticksExisted % 100 != 0 && ticksExisted > 20)) {
      return;
    }

    homingTarget = null;
    final EntityChaosProjectile projectile = this;

    Predicate<EntityLivingBase> predicate = input -> {
      // @formatter:off
        return shooter != null
            && shooter != input
            && !input.isOnSameTeam(shooter);
        // @formatter:on
    };

    int minDistance = Integer.MAX_VALUE;
    Entity entity;
    EntityLivingBase entityLiving;
    // Go through all entities in the world
    for (int i = 0; i < world.loadedEntityList.size(); ++i) {
      entity = world.loadedEntityList.get(i);
      // Target living entities
      if (entity instanceof EntityLivingBase) {
        entityLiving = (EntityLivingBase) entity;
        // Target entities that are not the shooter and not on the same team.
        if (predicate.apply(entityLiving)) {
          // Add some randomness so that different shots will select different targets.
          int distance = (int) entityLiving.getDistanceSqToEntity(projectile)
              + SilentGems.instance.random.nextInt(512);
          // Within certain range and closer than the closest selected entity so far.
          if (distance < 1200 && distance < minDistance) {
            minDistance = distance;
            homingTarget = entityLiving;
          }
        }
      }
    }
  }

  @Override
  public void onUpdate() {

    super.onUpdate();

    if (ticksExisted > 1) {
      findHomingTarget();
    }

    if (homingTarget != null && ticksExisted > 10) {
      posX = lastTickPosX;
      posY = lastTickPosY;
      posZ = lastTickPosZ;

      Vec3d vec = new Vec3d(motionX, motionY, motionZ);
      Vec3d toTarget = new Vec3d(homingTarget.posX - posX,
          homingTarget.posY + homingTarget.height / 2 - posY, homingTarget.posZ - posZ).normalize();
      vec = vec.add(toTarget.scale(HOMING_TIGHTNESS)).normalize().scale(HOMING_SPEED);

      motionX = vec.x;
      motionY = vec.y;
      motionZ = vec.z;
      posX += motionX;
      posY += motionY;
      posZ += motionZ;
    }
  }

  @Override
  public float getGravityVelocity() {

    return homingTarget == null ? 0.02f : 0.0f;
  }
}
