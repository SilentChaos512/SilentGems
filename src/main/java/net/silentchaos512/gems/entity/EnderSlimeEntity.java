package net.silentchaos512.gems.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;

import javax.annotation.Nonnull;
import java.util.Random;

public class EnderSlimeEntity extends SlimeEntity {
    public EnderSlimeEntity(EntityType<? extends EnderSlimeEntity> typeIn, World worldIn) {
        super(typeIn, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
        // Fix the hit box. Not sure why this happens.
        EntitySize size = super.getSize(poseIn);
        return size.scale(1.1f * size.height / size.width, 1.1f);
    }

    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
        return worldIn.getDifficulty() != Difficulty.PEACEFUL;
    }

    public static boolean canSpawnAt(EntityType<EnderSlimeEntity> type, IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && pos.getY() < 65 && MonsterEntity.isValidLightLevel(world, pos, random);
    }

    @Override
    public boolean isNotColliding(IWorldReader worldIn) {
        return worldIn.checkNoEntityCollision(this) && !worldIn.containsAnyLiquid(this.getBoundingBox());
    }

    @Override
    protected void setSlimeSize(int size, boolean resetHealth) {
        super.setSlimeSize(size, resetHealth);
        this.getAttribute(Attributes.ARMOR).setBaseValue(size * 3);
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @Nonnull
    @Override
    protected IParticleData getSquishParticle() {
        return ParticleTypes.DRAGON_BREATH;
    }

    @Override
    protected int getJumpDelay() {
        return super.getJumpDelay() * 4;
    }

    @Override
    protected void alterSquishAmount() {
        this.squishAmount *= 0.9F;
    }

    @Override
    protected void jump() {
        Vector3d vec3d = this.getMotion();
        this.setMotion(vec3d.x, 0.42F + (float)this.getSlimeSize() * 0.1F, vec3d.z);
        this.isAirBorne = true;
        net.minecraftforge.common.ForgeHooks.onLivingJump(this);
    }

    @Override
    protected void handleFluidJump(ITag<Fluid> tag) {
        if (tag == FluidTags.LAVA) {
            Vector3d vec3d = this.getMotion();
            this.setMotion(vec3d.x, 0.22F + this.getSlimeSize() * 0.05F, vec3d.z);
            this.isAirBorne = true;
        } else {
            super.handleFluidJump(tag);
        }
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected boolean canDamagePlayer() {
        return this.isServerWorld();
    }

    @Override
    protected float func_225512_er_() {
        return super.func_225512_er_() + 2;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return this.isSmallSlime() ? SoundEvents.ENTITY_MAGMA_CUBE_HURT_SMALL : SoundEvents.ENTITY_MAGMA_CUBE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_MAGMA_CUBE_DEATH_SMALL : SoundEvents.ENTITY_MAGMA_CUBE_DEATH;
    }

    @Nonnull
    @Override
    protected SoundEvent getSquishSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_MAGMA_CUBE_SQUISH_SMALL : SoundEvents.ENTITY_MAGMA_CUBE_SQUISH;
    }

    @Nonnull
    @Override
    protected SoundEvent getJumpSound() {
        return SoundEvents.ENTITY_MAGMA_CUBE_JUMP;
    }

    //region Teleport code copied from EntityEnderman

    protected boolean teleportRandomly() {
        double d0 = this.getPosX() + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.getPosY() + (double) (this.rand.nextInt(64) - 32);
        double d2 = this.getPosZ() + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(d0, d1, d2);
    }

    private boolean teleportTo(double x, double y, double z) {
        BlockPos.Mutable blockPos = new BlockPos.Mutable(x, y, z);

        while(blockPos.getY() > 0 && !this.world.getBlockState(blockPos).getMaterial().blocksMovement()) {
            blockPos.move(Direction.DOWN);
        }

        if (!this.world.getBlockState(blockPos).getMaterial().blocksMovement()) {
            return false;
        } else {
            net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
            boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag) {
                this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }

            return flag;
        }
    }

    @Override
    protected void updateAITasks() {
        if (this.isInWaterRainOrBubbleColumn()) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }

        super.updateAITasks();
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (source instanceof IndirectEntityDamageSource) {
            for (int i = 0; i < 64; ++i) {
                if (this.teleportRandomly()) {
                    return true;
                }
            }

            return false;
        } else {
            boolean flag = super.attackEntityFrom(source, amount);
            if (source.isUnblockable() && this.rand.nextInt(10) != 0) {
                this.teleportRandomly();
            }

            return flag;
        }
    }

    //endregion
}
