package net.silentchaos512.gems.entity.projectile;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.WispTypes;
import net.silentchaos512.utils.MathUtils;

public class FireWispShotEntity extends AbstractWispShotEntity {
    public FireWispShotEntity(World worldIn) {
        this(WispTypes.FIRE.getShotType(), worldIn);
    }

    public FireWispShotEntity(EntityType<? extends AbstractWispShotEntity> typeIn, World worldIn) {
        super(typeIn, worldIn);
    }

    public FireWispShotEntity(EntityType<? extends AbstractWispShotEntity> typeIn, double posXIn, double posYIn, double posZIn, double accelX, double accelY, double accelZ, World worldIn) {
        super(typeIn, posXIn, posYIn, posZIn, accelX, accelY, accelZ, worldIn);
    }

    public FireWispShotEntity(EntityType<? extends AbstractWispShotEntity> typeIn, LivingEntity shooterIn, double accelX, double accelY, double accelZ, World worldIn) {
        super(typeIn, shooterIn, accelX, accelY, accelZ, worldIn);
    }

    @Override
    protected void onEntityImpact(Entity entityIn) {
        if (!entityIn.isImmuneToFire()) {
            int i = entityIn.func_223314_ad();
            entityIn.setFire(5);
            boolean flag = entityIn.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 4f);
            if (flag) {
                this.applyEnchantments(this.shootingEntity, entityIn);
            } else {
                entityIn.func_223308_g(i);
            }
        }
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
