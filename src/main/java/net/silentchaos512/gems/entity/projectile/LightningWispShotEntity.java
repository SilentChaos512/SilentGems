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
import net.silentchaos512.utils.MathUtils;

public class LightningWispShotEntity extends AbstractWispShotEntity {
    private static final int SHOCK_DURATION = TimeUtils.ticksFromSeconds(5);

    public LightningWispShotEntity(World worldIn) {
        this(WispTypes.LIGHTNING.getShotType(), worldIn);
    }

    public LightningWispShotEntity(EntityType<? extends AbstractWispShotEntity> typeIn, World worldIn) {
        super(typeIn, worldIn);
    }

    public LightningWispShotEntity(EntityType<? extends AbstractWispShotEntity> typeIn, double posXIn, double posYIn, double posZIn, double accelX, double accelY, double accelZ, World worldIn) {
        super(typeIn, posXIn, posYIn, posZIn, accelX, accelY, accelZ, worldIn);
    }

    public LightningWispShotEntity(EntityType<? extends AbstractWispShotEntity> typeIn, LivingEntity shooterIn, double accelX, double accelY, double accelZ, World worldIn) {
        super(typeIn, shooterIn, accelX, accelY, accelZ, worldIn);
    }

    @Override
    protected void onEntityImpact(Entity entityIn) {
        if (entityIn instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entityIn;

            if (attackEntity(livingEntity)) {
                EffectInstance effect = new EffectInstance(GemsEffects.shocking, SHOCK_DURATION, 0, false, false);
                livingEntity.addPotionEffect(effect);
            }
        }
    }

    private boolean attackEntity(LivingEntity entity) {
        if (entity.getActivePotionEffect(GemsEffects.grounded) != null)
            return false;
        return entity.attackEntityFrom(ModDamageSource.causeWispShotDamage(this, this.shootingEntity), 4f);
    }

    @Override
    protected void onBlockImpact(BlockPos pos, Direction side) {
        if (MathUtils.tryPercentage(0.25)) {
            BlockPos blockPos = pos.offset(side);
            if (this.world.isAirBlock(blockPos)) {
                this.world.setBlockState(blockPos, Blocks.FIRE.getDefaultState());
            }
        }
    }
}
