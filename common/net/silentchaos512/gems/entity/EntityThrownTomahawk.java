package net.silentchaos512.gems.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.PlayerHelper;

public class EntityThrownTomahawk extends EntityThrowable implements IEntityAdditionalSpawnData {

  public static final int SPIN_RATE = 360 / 8;
  public static final float MAX_SPEED = 1.7f;

  protected EntityLivingBase thrower;
  protected ItemStack thrownStack;
  protected float initialSpeed = 0f;
  public float throwYaw = 0f;
  public int spin = 270;

  public boolean inAir = true;
  public boolean hasHit = false;

  public EntityThrownTomahawk(World worldIn) {

    super(worldIn);
    // TODO Auto-generated constructor stub
  }

  public EntityThrownTomahawk(EntityLivingBase throwerIn, ItemStack thrownStackIn, float speed) {

    super(throwerIn.worldObj);
    this.thrower = throwerIn;
    this.thrownStack = thrownStackIn;
    this.throwYaw = thrower.rotationYaw;

    Vec3d vec = thrower.getLookVec();
    initialSpeed = speed;
    motionX = speed * (vec.xCoord + thrower.motionX);
    motionY = speed * (vec.yCoord + thrower.motionY);
    motionZ = speed * (vec.zCoord + thrower.motionZ);
  }

  @Override
  public void writeEntityToNBT(NBTTagCompound tags) {

    super.writeEntityToNBT(tags);

    tags.setTag("item", thrownStack.serializeNBT());
  }

  @Override
  public void readEntityFromNBT(NBTTagCompound tags) {

    super.readEntityFromNBT(tags);
    thrownStack = new ItemStack(ModItems.tomahawk);
    thrownStack.deserializeNBT(tags.getCompoundTag("item"));
  }

  @Override
  public void writeSpawnData(ByteBuf data) {

    int id = thrower == null ? this.getEntityId() : thrower.getEntityId();
    data.writeInt(id);

    data.writeFloat(rotationYaw);
    data.writeInt(spin);

    data.writeDouble(motionX);
    data.writeDouble(motionY);
    data.writeDouble(motionZ);

    ByteBufUtils.writeItemStack(data, thrownStack);
  }

  @Override
  public void readSpawnData(ByteBuf data) {

    Entity entity = worldObj.getEntityByID(data.readInt());
    if (entity instanceof EntityLivingBase)
      thrower = (EntityLivingBase) entity;

    rotationYaw = data.readFloat();
    spin = data.readInt();

    motionX = data.readInt();
    motionY = data.readInt();
    motionZ = data.readInt();

    try {
      thrownStack = ByteBufUtils.readItemStack(data);
    } catch (Exception ex) {
      // Ignore the EOFException
    }
  }

  @Override
  public void onUpdate() {

    // SilentGems.logHelper.debug(FMLCommonHandler.instance().getEffectiveSide(), this, inAir, hasHit,
    // spin, thrower, thrownStack);

    super.onUpdate();

    if (thrownStack == null || thrownStack.getItem() == null)
      setDead();

    // Check for player pickup
    if (!inAir && ticksExisted > 10) {
      for (EntityPlayer player : worldObj.getPlayers(EntityPlayer.class,
          p -> p.getDistanceSq(posX, posY, posZ) < 2)) {
        // SilentGems.logHelper.debug(player.getName() + " is near a tomahawk.");
        for (ItemStack stack : PlayerHelper.getNonNullStacks(player)) {
          if (ToolHelper.areToolsEqual(stack, thrownStack)) {
            // SilentGems.logHelper.debug(player.getName() + " picked up the tomahawk.");
            ModItems.tomahawk.addAmmo(thrownStack, 1);
            worldObj.playSound(null, getPosition(), SoundEvents.ENTITY_ITEM_PICKUP,
                SoundCategory.AMBIENT, 0.5f, 1.5f);
            setDead();
          }
        }
      }
    }

    float spinRate = (float) (inAir ? SPIN_RATE : 30 * Math.abs(motionY));
    spin += spinRate;
    if (spin >= 360)
      spin -= 360;
  }

  @Override
  protected void onImpact(RayTraceResult result) {

    // TODO Auto-generated method stub
    if (result.typeOfHit == Type.ENTITY && result.entityHit != thrower && !hasHit) {
      Entity entityHit = result.entityHit;
      hasHit = true;
      motionX = 0.001f * SilentGems.random.nextGaussian();
      motionY = Math.abs(0.01f * SilentGems.random.nextGaussian());
      motionZ = 0.001f * SilentGems.random.nextGaussian();
      if (worldObj.isRemote)
        return;

      if (entityHit instanceof EntityLivingBase) {
        // Deal damage
        float damageDealt = ModItems.tomahawk.getThrownDamage(thrownStack) * initialSpeed
            / MAX_SPEED;
        if (thrower instanceof EntityPlayer)
          entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) thrower),
              damageDealt);
        else
          entityHit.attackEntityFrom(DamageSource.causeThrownDamage(thrower, entityHit),
              damageDealt);

        // Play attack sound
        worldObj.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG,
            SoundCategory.PLAYERS, 0.75f, 1f);
      }
    } else if (result.typeOfHit == Type.BLOCK) {
      // Get info on block hit
      BlockPos pos = result.getBlockPos();
      IBlockState state = worldObj.getBlockState(pos);
      Block block = state.getBlock();
      AxisAlignedBB boundingBox = state.getBoundingBox(worldObj, pos);

      // Collide only if it has a bounding box.
      if (boundingBox != null) {
        // Break glass blocks! But slows it down.
        if ((block == Blocks.GLASS || block == Blocks.GLASS_PANE || block == Blocks.STAINED_GLASS
            || block == Blocks.STAINED_GLASS_PANE)) {
          if (!worldObj.isRemote) {
            worldObj.setBlockToAir(pos);
            worldObj.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1f,
                1f);
          }
          motionX *= 0.7f;
          motionZ *= 0.7f;
        }
        // Ignore leaves
        else if (state.getMaterial() == Material.LEAVES) {
          SoundEvent sound = block.getSoundType(state, worldObj, pos, this).getPlaceSound();
          worldObj.playSound(null, pos, sound, SoundCategory.BLOCKS, 0.25f, 1f);
        }
        // Stop if we hit anything else
        else {
          if (motionX > 0.1f || motionZ > 0.1f) {
            SoundEvent sound = block.getSoundType(state, worldObj, pos, this).getPlaceSound();
            worldObj.playSound(null, pos, sound, SoundCategory.BLOCKS, 1f, 1f);
          }

          // Stop the tomahawk.
          hasHit = true;
          inAir = false;
          motionX = 0;
          motionY = 0;
          motionZ = 0;
        }
      }
    }
  }

  public EntityLivingBase getThrower() {

    return thrower;
  }

  public ItemStack getThrownStack() {

    return thrownStack;
  }

  @Override
  protected void setOnFireFromLava() {

  }

  @Override
  public void setPortal(BlockPos pos) {

  }

  // @Override
  // protected ItemStack getArrowStack() {
  //
  // return thrownStack; // FIXME?
  // }
}
