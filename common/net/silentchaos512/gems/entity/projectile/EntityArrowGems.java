package net.silentchaos512.gems.entity.projectile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityArrowGems extends EntityArrow {

  public final ItemStack firingBow;

  public EntityArrowGems(World world, EntityLivingBase shooter, float f, ItemStack bow) {

    super(world, shooter, f);
    this.firingBow = bow;
  }

  // I wanted to allow bows to track hits, but arrows have so many private fields... Not sure what to do at the moment.
//  public void onUpdate() {
//    
//    EntityArrow.class.getDeclaredField("field_145791_d").setAccessible(true);
//
//    super.onUpdate();
//
//    if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
//      float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
//      this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ)
//          * 180.0D / Math.PI);
//      this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f)
//          * 180.0D / Math.PI);
//    }
//
//    Block block = this.worldObj.getBlock(this.field_145791_d, this.field_145792_e,
//        this.field_145789_f);
//
//    if (block.getMaterial() != Material.air) {
//      block.setBlockBoundsBasedOnState(this.worldObj, this.field_145791_d, this.field_145792_e,
//          this.field_145789_f);
//      AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(this.worldObj,
//          this.field_145791_d, this.field_145792_e, this.field_145789_f);
//
//      if (axisalignedbb != null
//          && axisalignedbb.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))) {
//        this.inGround = true;
//      }
//    }
//
//    if (this.arrowShake > 0) {
//      --this.arrowShake;
//    }
//
//    if (this.inGround) {
//      int j = this.worldObj.getBlockMetadata(this.field_145791_d, this.field_145792_e,
//          this.field_145789_f);
//
//      if (block == this.field_145790_g && j == this.inData) {
//        ++this.ticksInGround;
//
//        if (this.ticksInGround == 1200) {
//          this.setDead();
//        }
//      } else {
//        this.inGround = false;
//        this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
//        this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
//        this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
//        this.ticksInGround = 0;
//        this.ticksInAir = 0;
//      }
//    } else {
//      ++this.ticksInAir;
//      Vec3 vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
//      Vec3 vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY,
//          this.posZ + this.motionZ);
//      MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec31, vec3, false,
//          true, false);
//      vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
//      vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY,
//          this.posZ + this.motionZ);
//
//      if (movingobjectposition != null) {
//        vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord,
//            movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
//      }
//
//      Entity entity = null;
//      List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox
//          .addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
//      double d0 = 0.0D;
//      int i;
//      float f1;
//
//      for (i = 0; i < list.size(); ++i) {
//        Entity entity1 = (Entity) list.get(i);
//
//        if (entity1.canBeCollidedWith()
//            && (entity1 != this.shootingEntity || this.ticksInAir >= 5)) {
//          f1 = 0.3F;
//          AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double) f1, (double) f1,
//              (double) f1);
//          MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31,
//              vec3);
//
//          if (movingobjectposition1 != null) {
//            double d1 = vec31.distanceTo(movingobjectposition1.hitVec);
//
//            if (d1 < d0 || d0 == 0.0D) {
//              entity = entity1;
//              d0 = d1;
//            }
//          }
//        }
//      }
//
//      if (entity != null) {
//        movingobjectposition = new MovingObjectPosition(entity);
//      }
//
//      if (movingobjectposition != null && movingobjectposition.entityHit != null
//          && movingobjectposition.entityHit instanceof EntityPlayer) {
//        EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;
//
//        if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer
//            && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
//          movingobjectposition = null;
//        }
//      }
//
//      float f2;
//      float f4;
//
//      if (movingobjectposition != null) {
//        if (movingobjectposition.entityHit != null) {
//          f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY
//              + this.motionZ * this.motionZ);
//          int k = MathHelper.ceiling_double_int((double) f2 * this.damage);
//
//          if (this.getIsCritical()) {
//            k += this.rand.nextInt(k / 2 + 2);
//          }
//
//          DamageSource damagesource = null;
//
//          if (this.shootingEntity == null) {
//            damagesource = DamageSource.causeArrowDamage(this, this);
//          } else {
//            damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
//          }
//
//          if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman)) {
//            movingobjectposition.entityHit.setFire(5);
//          }
//
//          if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float) k)) {
//            if (movingobjectposition.entityHit instanceof EntityLivingBase) {
//              EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;
//
//              if (!this.worldObj.isRemote) {
//                entitylivingbase
//                    .setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
//              }
//
//              if (this.knockbackStrength > 0) {
//                f4 = MathHelper
//                    .sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
//
//                if (f4 > 0.0F) {
//                  movingobjectposition.entityHit.addVelocity(
//                      this.motionX * (double) this.knockbackStrength * 0.6000000238418579D
//                          / (double) f4,
//                      0.1D, this.motionZ * (double) this.knockbackStrength * 0.6000000238418579D
//                          / (double) f4);
//                }
//              }
//
//              if (this.shootingEntity != null && this.shootingEntity instanceof EntityLivingBase) {
//                EnchantmentHelper.func_151384_a(entitylivingbase, this.shootingEntity);
//                EnchantmentHelper.func_151385_b((EntityLivingBase) this.shootingEntity,
//                    entitylivingbase);
//              }
//
//              if (this.shootingEntity != null
//                  && movingobjectposition.entityHit != this.shootingEntity
//                  && movingobjectposition.entityHit instanceof EntityPlayer
//                  && this.shootingEntity instanceof EntityPlayerMP) {
//                ((EntityPlayerMP) this.shootingEntity).playerNetServerHandler
//                    .sendPacket(new S2BPacketChangeGameState(6, 0.0F));
//              }
//            }
//
//            this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
//
//            if (!(movingobjectposition.entityHit instanceof EntityEnderman)) {
//              this.setDead();
//            }
//          } else {
//            this.motionX *= -0.10000000149011612D;
//            this.motionY *= -0.10000000149011612D;
//            this.motionZ *= -0.10000000149011612D;
//            this.rotationYaw += 180.0F;
//            this.prevRotationYaw += 180.0F;
//            this.ticksInAir = 0;
//          }
//        } else {
//          this.field_145791_d = movingobjectposition.blockX;
//          this.field_145792_e = movingobjectposition.blockY;
//          this.field_145789_f = movingobjectposition.blockZ;
//          this.field_145790_g = this.worldObj.getBlock(this.field_145791_d, this.field_145792_e,
//              this.field_145789_f);
//          this.inData = this.worldObj.getBlockMetadata(this.field_145791_d, this.field_145792_e,
//              this.field_145789_f);
//          this.motionX = (double) ((float) (movingobjectposition.hitVec.xCoord - this.posX));
//          this.motionY = (double) ((float) (movingobjectposition.hitVec.yCoord - this.posY));
//          this.motionZ = (double) ((float) (movingobjectposition.hitVec.zCoord - this.posZ));
//          f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY
//              + this.motionZ * this.motionZ);
//          this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
//          this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
//          this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;
//          this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
//          this.inGround = true;
//          this.arrowShake = 7;
//          this.setIsCritical(false);
//
//          if (this.field_145790_g.getMaterial() != Material.air) {
//            this.field_145790_g.onEntityCollidedWithBlock(this.worldObj, this.field_145791_d,
//                this.field_145792_e, this.field_145789_f, this);
//          }
//        }
//      }
//
//      if (this.getIsCritical()) {
//        for (i = 0; i < 4; ++i) {
//          this.worldObj.spawnParticle("crit", this.posX + this.motionX * (double) i / 4.0D,
//              this.posY + this.motionY * (double) i / 4.0D,
//              this.posZ + this.motionZ * (double) i / 4.0D, -this.motionX, -this.motionY + 0.2D,
//              -this.motionZ);
//        }
//      }
//
//      this.posX += this.motionX;
//      this.posY += this.motionY;
//      this.posZ += this.motionZ;
//      f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
//      this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
//
//      for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f2) * 180.0D
//          / Math.PI); this.rotationPitch
//              - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
//        ;
//      }
//
//      while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
//        this.prevRotationPitch += 360.0F;
//      }
//
//      while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
//        this.prevRotationYaw -= 360.0F;
//      }
//
//      while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
//        this.prevRotationYaw += 360.0F;
//      }
//
//      this.rotationPitch = this.prevRotationPitch
//          + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
//      this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
//      float f3 = 0.99F;
//      f1 = 0.05F;
//
//      if (this.isInWater()) {
//        for (int l = 0; l < 4; ++l) {
//          f4 = 0.25F;
//          this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) f4,
//              this.posY - this.motionY * (double) f4, this.posZ - this.motionZ * (double) f4,
//              this.motionX, this.motionY, this.motionZ);
//        }
//
//        f3 = 0.8F;
//      }
//
//      if (this.isWet()) {
//        this.extinguish();
//      }
//
//      this.motionX *= (double) f3;
//      this.motionY *= (double) f3;
//      this.motionZ *= (double) f3;
//      this.motionY -= (double) f1;
//      this.setPosition(this.posX, this.posY, this.posZ);
//      this.func_145775_I();
//    }
//  }
}
