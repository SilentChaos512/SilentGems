package net.silentchaos512.gems.entity.projectile;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.GemsEffects;
import net.silentchaos512.gems.lib.WispTypes;
import net.silentchaos512.gems.util.ModDamageSource;
import net.silentchaos512.lib.util.TimeUtils;

public class IceWispShotEntity extends AbstractWispShotEntity {
    private static final int FREEZE_DURATION = TimeUtils.ticksFromSeconds(5);

    public IceWispShotEntity(World worldIn) {
        this(WispTypes.ICE.getShotType(), worldIn);
    }

    public IceWispShotEntity(EntityType<? extends AbstractWispShotEntity> typeIn, World worldIn) {
        super(typeIn, worldIn);
    }

    public IceWispShotEntity(EntityType<? extends AbstractWispShotEntity> typeIn, double posXIn, double posYIn, double posZIn, double accelX, double accelY, double accelZ, World worldIn) {
        super(typeIn, posXIn, posYIn, posZIn, accelX, accelY, accelZ, worldIn);
    }

    public IceWispShotEntity(EntityType<? extends AbstractWispShotEntity> typeIn, LivingEntity shooterIn, double accelX, double accelY, double accelZ, World worldIn) {
        super(typeIn, shooterIn, accelX, accelY, accelZ, worldIn);
    }

    @Override
    public WispTypes getWispType() {
        return WispTypes.ICE;
    }

    @Override
    protected void onEntityImpact(Entity entityIn) {
        if (entityIn instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entityIn;

            if (attackEntity(livingEntity)) {
                EffectInstance effect = new EffectInstance(GemsEffects.FREEZING.get(), FREEZE_DURATION, 0, false, false);
                livingEntity.addPotionEffect(effect);
            }
        }
    }

    private boolean attackEntity(LivingEntity entity) {
        if (entity.getActivePotionEffect(GemsEffects.INSULATED.get()) != null)
            return false;
        return entity.attackEntityFrom(ModDamageSource.causeWispShotDamage(this, this.func_234616_v_()), 4f);
    }

    @Override
    protected void onBlockImpact(BlockPos pos, Direction side) {
        BlockPos blockPos = pos.offset(side);
        if (this.world.getBlockState(blockPos).getBlock() == Blocks.WATER) {
            this.world.setBlockState(blockPos, Blocks.ICE.getDefaultState());
        }
    }
}
