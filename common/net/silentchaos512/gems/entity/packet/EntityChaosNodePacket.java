package net.silentchaos512.gems.entity.packet;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.lib.util.Color;

public class EntityChaosNodePacket extends Entity implements IEntityAdditionalSpawnData {

  public static final double ENTITY_COLLISION_RADIUS_SQUARED = 3.0;
  public static final double BLOCK_COLLISION_RADIUS_SQUARED = 1.5;
  public static final String NBT_TARGET_ENTITY = "TargetEntity";
  public static final String NBT_TARGET_POS = "TargetPos";
  public static final String NBT_AMOUNT = "Amount";

  protected static final Map<Integer, ColorPair> colors = new HashMap();

  private Color colorHead;
  private Color colorTail;
  private int colorIndex;

  protected int maxLife = 200;
  protected double movementSpeed = 0.185;
  protected double acceleration = 0.008;
  protected double homingTightness = movementSpeed / 10;

  /**
   * A general-purpose variable for different packets, to reduce boiler plate code.
   */
  protected float amount = 0;

  protected EntityLivingBase targetEntity = null;
  protected BlockPos targetPos = null;

  public static void initColors() {

    colors.clear();
    colors.put(0, new ColorPair(Color.WHITE, Color.WHITE));
    colors.put(EntityPacketChaos.COLOR_INDEX, EntityPacketChaos.COLOR_PAIR);
    colors.put(EntityPacketRepair.COLOR_INDEX, EntityPacketRepair.COLOR_PAIR);
    colors.put(EntityPacketAttack.COLOR_INDEX, EntityPacketAttack.COLOR_PAIR);
  }

  public EntityChaosNodePacket(World worldIn) {

    super(worldIn);
    setSize(0.5f, 0.5f);
    motionX = SilentGems.instance.random.nextGaussian() * 0.1;
    motionY = SilentGems.instance.random.nextGaussian() * 0.2 + 0.25;
    motionZ = SilentGems.instance.random.nextGaussian() * 0.1;
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
    }

    // Move the packet.
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.prevRotationPitch = this.rotationPitch;
    this.prevRotationYaw = this.rotationYaw;

    moveEntity(motionX, motionY, motionZ);

    // Collision with target?
    // TODO: Allow collision with others?
    // TODO: Improve collision detection!
    if (targetEntity != null
        && this.getDistanceSqToEntity(targetEntity) < ENTITY_COLLISION_RADIUS_SQUARED) {
      onImpactWithEntity(targetEntity);
    } else if (targetPos != null
        && this.getPosition().distanceSq(targetPos) < BLOCK_COLLISION_RADIUS_SQUARED) {
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
    Vec3d toTarget = new Vec3d(entity.posX - posX, entity.posY + entity.height * 0.6 - posY,
        entity.posZ - posZ);
    vec = vec.add(toTarget.scale(homingTightness)).normalize().scale(speed);
    setVelocity(vec);
  }

  protected void homeOnBlock(BlockPos pos) {

    Vec3d vec = new Vec3d(motionX, motionY, motionZ);
    double speed = accelerate(vec.lengthVector());
    Vec3d toTarget = new Vec3d(pos.getX() + 0.5 - posX, pos.getY() + 0.5 - posY,
        pos.getZ() + 0.5 - posZ);
    vec = vec.add(toTarget.scale(homingTightness)).normalize().scale(speed);
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

    // SilentGems.instance.logHelper.debug(worldObj.isRemote, getColorIndex(),
    // String.format("%08X", getColorHead().getColor()));

    SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS_PACKET_HEAD, getColorHead(), worldObj,
        posX, posY, posZ, 0, 0, 0);
  }

  protected void spawnTailParticles() {

    if (!firstUpdate && ticksExisted % (1 + SilentGems.proxy.getParticleSettings()) == 0) {
      double mx = worldObj.rand.nextGaussian() * 0.02f;
      double my = worldObj.rand.nextGaussian() * 0.02f;
      double mz = worldObj.rand.nextGaussian() * 0.02f;
      SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS_PACKET_TAIL, getColorTail(), worldObj,
          prevPosX, prevPosY, prevPosZ, mx, my, mz);
    }
  }

  public Color getColorHead() {

    return colorHead;
  }

  public Color getColorTail() {

    return colorTail;
  }

  public ColorPair getColorPair() {

    return new ColorPair(getColorHead(), getColorTail());
  }

  public int getColorIndex() {

    return colorIndex;
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

    // This is never called!?
    SilentGems.instance.logHelper.debug("Node packet read NBT");
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
      SilentGems.instance.logHelper.warning("Chaos node packet entity with no target? " + this);
      setDead();
    }
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound tagCompound) {

    // This is never called!?
    SilentGems.instance.logHelper.debug("Node packet write NBT");
    tagCompound.setFloat(NBT_AMOUNT, amount);
    if (targetEntity != null) {
      tagCompound.setInteger(NBT_TARGET_ENTITY, targetEntity.getEntityId());
    } else if (targetPos != null) {
      tagCompound.setInteger(NBT_TARGET_POS + "X", targetPos.getX());
      tagCompound.setInteger(NBT_TARGET_POS + "Y", targetPos.getY());
      tagCompound.setInteger(NBT_TARGET_POS + "Z", targetPos.getZ());
    }
  }

  @Override
  public void writeSpawnData(ByteBuf buffer) {

    if (targetEntity != null) {
      // Target entity, mode 0
      buffer.writeByte(0);
      buffer.writeInt(targetEntity.getEntityId());
    } else if (targetPos != null) {
      // Target block, mode 1
      buffer.writeByte(1);
      buffer.writeInt(targetPos.getX());
      buffer.writeInt(targetPos.getY());
      buffer.writeInt(targetPos.getZ());
    } else {
      // No target?
      return;
    }

    // Color
    buffer.writeByte((byte) getColorIndex());
  }

  @Override
  public void readSpawnData(ByteBuf additionalData) {

    byte mode = additionalData.readByte();
    if (mode == 0) {
      // Target entity.
      Entity entity = worldObj.getEntityByID(additionalData.readInt());
      if (entity != null && entity instanceof EntityLivingBase) {
        targetEntity = (EntityLivingBase) entity;
      }
    } else if (mode == 1) {
      int x = additionalData.readInt();
      int y = additionalData.readInt();
      int z = additionalData.readInt();
      targetPos = new BlockPos(x, y, z);
    } else {
      return;
    }

    // Color
    colorIndex = (int) additionalData.readByte();
    ColorPair pair = colors.get(colorIndex);
    if (pair == null) {
      // SilentGems.instance.logHelper.debug("Derp!", mode, colorIndex, colors.size(), colors.keySet(),
      // colors.values());
      pair = colors.get(0);
    }
    // SilentGems.instance.logHelper.debug(colorIndex, targetEntity, targetPos);
    colorHead = pair.head;
    colorTail = pair.tail;
  }

  public static class ColorPair {

    public final Color head, tail;

    public ColorPair(Color head, Color tail) {

      this.head = head;
      this.tail = tail;
    }
  }
}
