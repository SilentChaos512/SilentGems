package net.silentchaos512.gems.entity.packet;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.lib.util.Color;

public class EntityChaosNodePacket extends Entity {

  public static final double ENTITY_COLLISION_RADIUS_SQUARED = 3.0;
  public static final double BLOCK_COLLISION_RADIUS_SQUARED = 1.5;
  public static final String NBT_TARGET_ENTITY = "TargetEntity";
  public static final String NBT_TARGET_POS = "TargetPos";
  public static final String NBT_AMOUNT = "Amount";

  protected int maxLife = 200;
  protected double movementSpeed = 0.185;
  protected double acceleration = 0.008;
  protected double homingTightness = movementSpeed / 10;
  protected Color colorHead = Color.WHITE;
  protected Color colorTail = Color.WHITE;

  /**
   * A general-purpose variable for different packets, to reduce boiler plate code.
   */
  protected float amount = 0;

  protected EntityLivingBase targetEntity = null;
  protected BlockPos targetPos = null;

  public EntityChaosNodePacket(World worldIn) {

    super(worldIn);
    setSize(0.5f, 0.5f);
    motionX = SilentGems.instance.random.nextGaussian() * 0.2;
    motionY = SilentGems.instance.random.nextGaussian() * 0.2 + 0.2;
    motionZ = SilentGems.instance.random.nextGaussian() * 0.2;
  }

  public EntityChaosNodePacket(World worldIn, EntityLivingBase target) {

    this(worldIn);
    this.targetEntity = target;
  }

  public EntityChaosNodePacket(World worldIn, BlockPos target) {

    this(worldIn);
    this.targetPos = target;
  }

  @Override
  public void onEntityUpdate() {

    // Homing on target.
    if (targetEntity != null) {
      homeOnEntity(targetEntity);
    } else if (targetPos != null) {
      homeOnBlock(targetPos);
    } else {
//      SilentGems.instance.logHelper.warning("Chaos node packet with no target! " + this);
      setDead();
    }

    // Move the packet.
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.prevRotationPitch = this.rotationPitch;
    this.prevRotationYaw = this.rotationYaw;

    moveEntity(motionX, motionY, motionZ);

    // Collision with target? TODO: Allow collision with others?
    if (targetEntity != null
        && this.getDistanceSqToEntity(targetEntity) < ENTITY_COLLISION_RADIUS_SQUARED) {
      onImpactWithEntity(targetEntity);
    } else if (targetPos != null
        && this.getDistanceSq(targetPos) < BLOCK_COLLISION_RADIUS_SQUARED) {
      IBlockState state = worldObj.getBlockState(targetPos);
      onImpactWithBlock(targetPos, state);
    }

    // Check life
    if (ticksExisted >= maxLife) {
      setDead();
    }

    spawnHeadParticles();
    spawnTailParticles();
    firstUpdate = false;
  }

  protected void homeOnEntity(EntityLivingBase entity) {

    Vec3d vec = new Vec3d(motionX, motionY, motionZ);
    double speed = accelerate(vec.lengthVector());
    Vec3d toTarget = new Vec3d(entity.posX - posX, entity.posY + entity.height * 0.75 - posY,
        entity.posZ - posZ);
    vec = vec.add(toTarget.scale(homingTightness)).normalize().scale(speed);
    setVelocity(vec);
  }

  protected void homeOnBlock(BlockPos pos) {

    Vec3d vec = new Vec3d(motionX, motionY, motionZ);
    double speed = accelerate(vec.lengthVector());
    Vec3d toTarget = new Vec3d(pos.getX() + 0.5 - posX, pos.getY() + 0.5 - posY,
        pos.getZ() + 0.5 - posZ);
    vec = vec.add(toTarget).scale(homingTightness).normalize().scale(movementSpeed);
    setVelocity(vec);
  }

  protected double accelerate(double currentSpeed) {

    if (currentSpeed > movementSpeed) {
      currentSpeed = Math.max(currentSpeed - acceleration, movementSpeed);
    } else if (currentSpeed < movementSpeed) {
      currentSpeed = Math.min(currentSpeed + acceleration, movementSpeed);
    }
    return currentSpeed;
  }

  public void setVelocity(Vec3d vec) {

    motionX = vec.xCoord;
    motionY = vec.yCoord;
    motionZ = vec.zCoord;
  }

  public void onImpactWithEntity(EntityLivingBase entity) {

    setDead();
  }

  public void onImpactWithBlock(BlockPos pos, IBlockState state) {

    setDead();
  }

  protected void spawnHeadParticles() {

    SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS_PACKET_HEAD, colorHead, worldObj, posX,
        posY, posZ, 0, 0, 0);
  }

  protected void spawnTailParticles() {

    if (!firstUpdate && ticksExisted % (1 + SilentGems.proxy.getParticleSettings()) == 0) {
      double mx = worldObj.rand.nextGaussian() * 0.01f;
      double my = worldObj.rand.nextGaussian() * 0.01f;
      double mz = worldObj.rand.nextGaussian() * 0.01f;
      SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS_PACKET_TAIL, colorTail, worldObj, posX,
          posY, posZ, mx, my, mz);
    }
  }

  /**
   * @return The targeted entity. May be null!
   */
  public EntityLivingBase getTargetEntity() {

    return targetEntity;
  }

  /**
   * @return The target block pos. May be null!
   */
  public BlockPos getTargetPos() {

    return targetPos;
  }

  @Override
  public void setPortal(BlockPos pos) {

  }

  @Override
  protected void entityInit() {

  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound tagCompund) {

    super.readFromNBT(tagCompund);
    amount = tagCompund.getFloat(NBT_AMOUNT);
    if (tagCompund.hasKey(NBT_TARGET_ENTITY)) {
      targetEntity = (EntityLivingBase) worldObj
          .getEntityByID(tagCompund.getInteger(NBT_TARGET_ENTITY));
    } else if (tagCompund.hasKey(NBT_TARGET_POS)) {
      int x = tagCompund.getInteger(NBT_TARGET_POS + "X");
      int y = tagCompund.getInteger(NBT_TARGET_POS + "Y");
      int z = tagCompund.getInteger(NBT_TARGET_POS + "Z");
      targetPos = new BlockPos(x, y, z);
    } else {
      setDead();
    }
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound tagCompound) {

    super.writeToNBT(tagCompound);
    tagCompound.setFloat(NBT_AMOUNT, amount);
    if (targetEntity != null) {
      tagCompound.setInteger(NBT_TARGET_ENTITY, targetEntity.getEntityId());
    } else if (targetPos != null) {
      tagCompound.setInteger(NBT_TARGET_POS + "X", targetPos.getX());
      tagCompound.setInteger(NBT_TARGET_POS + "Y", targetPos.getY());
      tagCompound.setInteger(NBT_TARGET_POS + "Z", targetPos.getZ());
    }
  }

}
