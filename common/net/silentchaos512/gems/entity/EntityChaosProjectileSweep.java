package net.silentchaos512.gems.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;

public class EntityChaosProjectileSweep extends EntityChaosProjectile {

  public EntityChaosProjectileSweep(World world) {

    super(world);
  }

  public EntityChaosProjectileSweep(EntityLivingBase shooter, ItemStack castingStack,
      float damage, float yaw) {

    super(shooter, castingStack, damage);

    Vec3d vec = shooter.getLookVec();
    vec = vec.rotateYaw(yaw);
    motionX = vec.xCoord;
    motionY = vec.yCoord;
    motionZ = vec.zCoord;
//    SilentGems.instance.logHelper.debug(motionX, motionY, motionZ);

    float speedMulti = 0.7f;
    motionX += shooter.motionX;
    motionY += shooter.motionY;
    motionZ += shooter.motionZ;
    motionX *= speedMulti;
    motionY *= speedMulti;
    motionZ *= speedMulti;
  }

  @Override
  public int getMaxLife() {

    return 80;
  }

  @Override
  protected void onImpact(RayTraceResult mop) {

    if (mop.typeOfHit == Type.ENTITY && mop.entityHit != shooter) {
      mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, shooter), damage);
    } else if (mop.typeOfHit == Type.BLOCK) {
      switch (mop.sideHit) {
        case UP:
          motionY = 0.0;
          break;
        case DOWN:
          motionY = -motionY;
          break;
        case EAST:
        case NORTH:
        case SOUTH:
        case WEST:
          posY += 1.1;
          break;
      }
    }
  }

  @Override
  public float getGravityVelocity() {

    return 0.06f;
  }
}
