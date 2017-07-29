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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.Color;
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

    super(throwerIn.world);
    this.thrower = throwerIn;
    this.thrownStack = thrownStackIn;
    this.throwYaw = thrower.rotationYaw;

    Vec3d vec = thrower.getLookVec();
    initialSpeed = speed;
    motionX = speed * (vec.x + thrower.motionX);
    motionY = speed * (vec.y+ thrower.motionY);
    motionZ = speed * (vec.z+ thrower.motionZ);
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

    Entity entity = world.getEntityByID(data.readInt());
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

    super.onUpdate();

    // Check for proper collision.
    Vec3d vec1 = new Vec3d(posX, posY, posZ);
    Vec3d vec2 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
    RayTraceResult raytraceresult = world.rayTraceBlocks(vec1, vec2, false, true, false);
    if (raytraceresult != null && raytraceresult.typeOfHit == Type.BLOCK)
      onImpact(raytraceresult);

    // Delete if the tomahawk item somehow goes missing, or exists for 10 minutes.
    if (thrownStack == null || thrownStack.getItem() == null || ticksExisted > 12000) {
      setDead();
      if (ticksExisted > 10)
        for (int i = 0; i < 15; ++i)
          world.spawnParticle(EnumParticleTypes.FLAME, posX, posY + 0.5, posZ,
              0.01 * SilentGems.random.nextGaussian(), 0.05 * SilentGems.random.nextGaussian(),
              0.01 * SilentGems.random.nextGaussian());
    }

    // Check for player pickup
    if (!inAir && ticksExisted > 10) {
      for (EntityPlayer player : world.getPlayers(EntityPlayer.class,
          p -> p.getDistanceSq(posX, posY, posZ) < 2)) {
        // SilentGems.logHelper.debug(player.getName() + " is near a tomahawk.");
        for (ItemStack stack : PlayerHelper.getNonEmptyStacks(player)) {
          if (ToolHelper.areToolsEqual(stack, thrownStack)) {
            // SilentGems.logHelper.debug(player.getName() + " picked up the tomahawk.");
            ModItems.tomahawk.addAmmo(thrownStack, 1);
            world.playSound(null, getPosition(), SoundEvents.ENTITY_ITEM_PICKUP,
                SoundCategory.AMBIENT, 0.5f, 1.5f);
            setDead();
          }
        }
      }
    }

    // Spawn particles to indicate landing spot?
    if (!world.isRemote && hasHit && !inAir && ticksExisted % 4 == 0) {
      SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS, Color.WHITE, world, posX,
          posY, posZ, 0.01 * SilentGems.random.nextGaussian(), 0.125,
          0.01 * SilentGems.random.nextGaussian());
    }

    // Spin!
    float spinRate = (float) (inAir ? SPIN_RATE : 30 * Math.abs(motionY));
    spin += spinRate;
    if (spin >= 360)
      spin -= 360;
  }

  @Override
  protected void onImpact(RayTraceResult result) {

    if (result.typeOfHit == Type.ENTITY && result.entityHit != thrower && !hasHit)
      onImpactWithEntity(result);
    else if (result.typeOfHit == Type.BLOCK)
      onImpactWithBlock(result.getBlockPos(), world.getBlockState(result.getBlockPos()));
  }

  protected void onImpactWithEntity(RayTraceResult result) {

    Entity entityHit = result.entityHit;
    hasHit = true;
    motionX = 0.001f * SilentGems.random.nextGaussian();
    motionY = Math.abs(0.01f * SilentGems.random.nextGaussian());
    motionZ = 0.001f * SilentGems.random.nextGaussian();
    if (world.isRemote)
      return;

    if (entityHit instanceof EntityLivingBase) {
      EntityLivingBase entityLiving = (EntityLivingBase) entityHit;
      // Deal damage
      float entityHealthBeforeDamage = entityLiving.getHealth();
      boolean headshot = isHeadshot(result);
      float damageDealt = ModItems.tomahawk.getThrownDamage(thrownStack) * initialSpeed / MAX_SPEED;
      if (headshot)
        damageDealt *= 2.0f;

      if (thrower instanceof EntityPlayer)
        entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) thrower),
            damageDealt);
      else
        entityHit.attackEntityFrom(DamageSource.causeThrownDamage(thrower, entityHit), damageDealt);

      // Play attack sound
      world.playSound(null, posX, posY, posZ, headshot ? SoundEvents.ENTITY_PLAYER_ATTACK_CRIT
          : SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, 0.75f, 1f);

      // Spawn particles
      if (world instanceof WorldServer) {
        WorldServer world = (WorldServer) this.world;
        float healthDiff = entityHealthBeforeDamage - entityLiving.getHealth();
        world.spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, entityHit.posX,
            entityHit.posY + entityHit.height / 2, entityHit.posZ, (int) (healthDiff / 2), 0.1, 0.0,
            0.1, 0.2);

        if (headshot && thrower instanceof EntityPlayer)
          ((EntityPlayer) thrower).onCriticalHit(entityHit);
      }
    }
  }

  protected boolean isHeadshot(RayTraceResult result) {

    Entity entity = result.entityHit;
    float eyeHeight = entity.getEyeHeight();
    float height = entity.height;

    float minY = (float) (entity.posY + eyeHeight - height / 7);
    float maxY = (float) (entity.posY + eyeHeight + height / 2);

    return posY > minY && posY < maxY;
  }

  protected void onImpactWithBlock(BlockPos pos, IBlockState state) {

    // Get info on block hit
    Block block = state.getBlock();
    AxisAlignedBB boundingBox = state.getBoundingBox(world, pos);

    // Collide only if it has a bounding box.
    if (boundingBox != null) {
      Material mat = state.getMaterial();
      // Break glass blocks! But slows it down.
      if ((block == Blocks.GLASS || block == Blocks.GLASS_PANE || block == Blocks.STAINED_GLASS
          || block == Blocks.STAINED_GLASS_PANE)) {
        if (!world.isRemote) {
          world.setBlockToAir(pos);
          world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1f,
              1f);
        }
        motionX *= 0.7f;
        motionZ *= 0.7f;
      }
      // Ignore leaves and plants
      else if (mat == Material.LEAVES || mat == Material.PLANTS || mat == Material.VINE) {
        SoundEvent sound = block.getSoundType(state, world, pos, this).getPlaceSound();
        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 0.25f, 1f);
      }
      // Stop if we hit anything else
      else {
        if (motionX > 0.1f || motionZ > 0.1f) {
          SoundEvent sound = block.getSoundType(state, world, pos, this).getPlaceSound();
          world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1f, 1f);
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
