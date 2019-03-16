package net.silentchaos512.gems.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.fluid.Fluid;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.particles.IParticleData;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModEntities;

import javax.annotation.Nonnull;

public class EntityEnderSlime extends EntitySlime {
    private static final ResourceLocation LOOT_TABLE = SilentGems.getId("ender_slime");

    public EntityEnderSlime(World worldIn) {
        super(ModEntities.ENDER_SLIME.type(), worldIn);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double) 0.2F);
    }

    @Override
    public boolean canSpawn(IWorld worldIn, boolean p_205020_2_) {
        return worldIn.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    @Override
    public boolean isNotColliding(IWorldReaderBase worldIn) {
        return worldIn.checkNoEntityCollision(this, this.getBoundingBox()) && worldIn.isCollisionBoxesEmpty(this, this.getBoundingBox()) && !worldIn.containsAnyLiquid(this.getBoundingBox());
    }

    @Override
    protected void setSlimeSize(int size, boolean resetHealth) {
        super.setSlimeSize(size, resetHealth);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue((double) (size * 3));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @Nonnull
    @Override
    protected IParticleData func_195404_m() {
        return Particles.DRAGON_BREATH;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_TABLE;
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
        this.motionY = (double) (0.42F + (float) this.getSlimeSize() * 0.1F);
        this.isAirBorne = true;
        net.minecraftforge.common.ForgeHooks.onLivingJump(this);
    }

    @Override
    protected void handleFluidJump(Tag<Fluid> p_180466_1_) {
        if (p_180466_1_ == FluidTags.LAVA) {
            this.motionY = (double) (0.22F + (float) this.getSlimeSize() * 0.05F);
            this.isAirBorne = true;
        } else {
            super.handleFluidJump(p_180466_1_);
        }

    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected boolean canDamagePlayer() {
        return this.isServerWorld();
    }

    @Override
    protected int getAttackStrength() {
        return super.getAttackStrength() + 2;
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
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.posY + (double) (this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(d0, d1, d2);
    }

    protected boolean teleportToEntity(Entity p_70816_1_) {
        Vec3d vec3d = new Vec3d(this.posX - p_70816_1_.posX, this.getBoundingBox().minY + (double) (this.height / 2.0F) - p_70816_1_.posY + (double) p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
        vec3d = vec3d.normalize();
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.x * 16.0D;
        double d2 = this.posY + (double) (this.rand.nextInt(16) - 8) - vec3d.y * 16.0D;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.z * 16.0D;
        return this.teleportTo(d1, d2, d3);
    }

    private boolean teleportTo(double x, double y, double z) {
        net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());
        if (flag) {
            this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }

        return flag;
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
        } else if (source instanceof EntityDamageSourceIndirect) {
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
