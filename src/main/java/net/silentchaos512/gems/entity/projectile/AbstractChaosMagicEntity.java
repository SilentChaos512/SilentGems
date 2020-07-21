package net.silentchaos512.gems.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public abstract class AbstractChaosMagicEntity extends DamagingProjectileEntity implements IEntityAdditionalSpawnData {
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(AbstractChaosMagicEntity.class, DataSerializers.VARINT);

    private int ticksAlive;
    private int ticksInAir;

    public AbstractChaosMagicEntity(EntityType<? extends AbstractChaosMagicEntity> typeIn, World worldIn) {
        super(typeIn, worldIn);
    }

    public AbstractChaosMagicEntity(EntityType<? extends AbstractChaosMagicEntity> typeIn, double posXIn, double posYIn, double posZIn, double accelX, double accelY, double accelZ, World worldIn) {
        super(typeIn, posXIn, posYIn, posZIn, accelX, accelY, accelZ, worldIn);
    }

    public AbstractChaosMagicEntity(EntityType<? extends AbstractChaosMagicEntity> typeIn, LivingEntity shooterIn, double accelX, double accelY, double accelZ, World worldIn) {
        super(typeIn, shooterIn, accelX, accelY, accelZ, worldIn);
    }

    protected abstract void onEntityImpact(Entity entityIn);

    protected abstract void onBlockImpact(BlockPos pos, Direction side);

    public int getColor() {
        return this.getDataManager().get(COLOR);
    }

    public void setColor(int color) {
        this.getDataManager().set(COLOR, color);
    }

    @Override
    protected void registerData() {
        this.getDataManager().register(COLOR, 0xFFFFFF);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) result).getEntity();
                onEntityImpact(entity);
            } else if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.func_234616_v_())) {
                BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) result;
                onBlockImpact(blockraytraceresult.getPos(), blockraytraceresult.getFace());
            }

            this.remove();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected IParticleData getParticle() {
        return ParticleTypes.CRIT;
    }

    @Override
    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeVarInt(this.getColor());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.setColor(additionalData.readVarInt());
    }
}
