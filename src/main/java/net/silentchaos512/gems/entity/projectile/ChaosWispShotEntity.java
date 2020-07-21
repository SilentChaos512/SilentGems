package net.silentchaos512.gems.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.chaos.ChaosSourceCapability;
import net.silentchaos512.gems.lib.WispTypes;
import net.silentchaos512.gems.util.ModDamageSource;

public class ChaosWispShotEntity extends AbstractWispShotEntity {
    public ChaosWispShotEntity(World worldIn) {
        this(WispTypes.CHAOS.getShotType(), worldIn);
    }

    public ChaosWispShotEntity(EntityType<? extends AbstractWispShotEntity> typeIn, World worldIn) {
        super(typeIn, worldIn);
    }

    public ChaosWispShotEntity(EntityType<? extends AbstractWispShotEntity> typeIn, double posXIn, double posYIn, double posZIn, double accelX, double accelY, double accelZ, World worldIn) {
        super(typeIn, posXIn, posYIn, posZIn, accelX, accelY, accelZ, worldIn);
    }

    public ChaosWispShotEntity(EntityType<? extends AbstractWispShotEntity> typeIn, LivingEntity shooterIn, double accelX, double accelY, double accelZ, World worldIn) {
        super(typeIn, shooterIn, accelX, accelY, accelZ, worldIn);
    }

    @Override
    public WispTypes getWispType() {
        return WispTypes.CHAOS;
    }

    @Override
    protected void onEntityImpact(Entity entityIn) {
        if (entityIn.attackEntityFrom(ModDamageSource.causeWispShotDamage(this, this.func_234616_v_()), 4f)) {
            entityIn.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source -> {
                source.addChaos(500);
            });
        }
    }

    @Override
    protected void onBlockImpact(BlockPos pos, Direction side) {
    }
}
