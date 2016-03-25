package net.silentchaos512.gems.entity;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosStorage;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.gems.lib.EnumModParticles;

public class EntityChaosTransfer
    extends EntityThrowable /* implements IEntityAdditionalSpawnData */ {

  public static final float SPEED = 0.166f;
  public static final float SIZE = 0.5f;
  public static final int MAX_LIFE = 200;
  public static final double RENDER_DISTANCE_WEIGHT = 10D;

  public static final String NBT_CHARGE = "Charge";
  public static final String NBT_TARGET = "Target";

  protected EntityPlayer targetPlayer;
  protected int charge;

  public EntityChaosTransfer(World worldIn) {

    super(worldIn);
//    setRenderDistanceWeight(RENDER_DISTANCE_WEIGHT);
    setSize(SIZE, SIZE);
    this.targetPlayer = null;
    this.charge = 0;
  }

  public EntityChaosTransfer(World worldIn, EntityPlayer targetPlayer, int charge) {

    super(worldIn);
//    setRenderDistanceWeight(RENDER_DISTANCE_WEIGHT);
    setSize(SIZE, SIZE);
    this.targetPlayer = targetPlayer;
    this.charge = charge;
  }

  @Override
  public void readEntityFromNBT(NBTTagCompound tagCompund) {

    super.readEntityFromNBT(tagCompund);

    charge = tagCompund.getInteger(NBT_CHARGE);
    if (tagCompund.hasKey(NBT_TARGET)) {
      targetPlayer = worldObj.getPlayerEntityByName(tagCompund.getString(NBT_TARGET));
      if (targetPlayer == null) {
        setDead();
      }
    }
  }

  @Override
  public void writeEntityToNBT(NBTTagCompound tagCompound) {

    super.writeEntityToNBT(tagCompound);

    tagCompound.setInteger(NBT_CHARGE, charge);
    if (targetPlayer != null) {
      tagCompound.setString(NBT_TARGET, targetPlayer.getName());
    }
  }

  // @Override
  // public void readSpawnData(ByteBuf additionalData) {
  //
  // // TODO ?
  // }
  //
  // @Override
  // public void writeSpawnData(ByteBuf buffer) {
  //
  // // TODO ?
  // }

  @Override
  public void onUpdate() {

    if (targetPlayer == null) {
      return;
    }

    // Move towards target player.
    Vec3d vec = targetPlayer.getPositionVector();
    Vec3d moveVec = new Vec3d(vec.xCoord - posX, vec.yCoord + 1.0 - posY, vec.zCoord - posZ)
        .normalize().scale(SPEED);
    motionX = moveVec.xCoord;
    motionY = moveVec.yCoord;
    motionZ = moveVec.zCoord;

    super.onUpdate();

    Vec3d vec1 = new Vec3d(posX, posY, posZ);
    Vec3d vec2 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
    RayTraceResult mop = worldObj.rayTraceBlocks(vec1, vec2, false, true, false);
    if (mop != null) {
      onImpact(mop);
    }

    if (ticksExisted > MAX_LIFE) {
      setDead();
    }

    // Tail particles
    if (worldObj.getTotalWorldTime() % (1 + SilentGems.proxy.getParticleSettings()) == 0) {
      double mx = worldObj.rand.nextGaussian() * 0.01f;
      double my = worldObj.rand.nextGaussian() * 0.01f;
      double mz = worldObj.rand.nextGaussian() * 0.01f;
      // TODO
      SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS, 0xFFFFFF, worldObj, posX, posY, posZ,
          mx, my, mz);
    }

    // SilentGems.instance.logHelper.debug(posX, posY, posZ);
  }

  @Override
  public float getGravityVelocity() {

    return 0f;
  }

  public void onImpact(RayTraceResult mop) {

    // TODO
    if (mop.typeOfHit == Type.ENTITY && mop.entityHit == targetPlayer) {
      int amountLeft = charge;

      // Try to give directly to player first.
      PlayerData data = PlayerDataHandler.get(targetPlayer);
      amountLeft -= data.sendChaos(amountLeft);

      if (amountLeft > 0) {
        List<ItemStack> list = Lists.newArrayList();

        // Get all stacks.
        for (ItemStack stack : targetPlayer.inventory.mainInventory) {
          if (stack != null) {
            list.add(stack);
          }
        }
        for (ItemStack stack : targetPlayer.inventory.armorInventory) {
          if (stack != null) {
            list.add(stack);
          }
        }
        for (ItemStack stack : targetPlayer.inventory.offHandInventory) {
          if (stack != null) {
            list.add(stack);
          }
        }

        for (ItemStack stack : list) {
          if (stack.getItem() instanceof IChaosStorage) {
            amountLeft -= ((IChaosStorage) stack.getItem()).receiveCharge(stack, amountLeft, false);
            if (amountLeft <= 0) {
              break;
            }
          }
        }
      }
      setDead();
    }
  }

  @Override
  public void setPortal(BlockPos pos) {

  }
}
