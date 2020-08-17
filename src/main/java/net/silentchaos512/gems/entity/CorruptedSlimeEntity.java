package net.silentchaos512.gems.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class CorruptedSlimeEntity extends SlimeEntity {
    public CorruptedSlimeEntity(EntityType<? extends CorruptedSlimeEntity> typeIn, World worldIn) {
        super(typeIn, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE);
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

    @Nonnull
    @Override
    protected IParticleData getSquishParticle() {
        return super.getSquishParticle();
    }
}
