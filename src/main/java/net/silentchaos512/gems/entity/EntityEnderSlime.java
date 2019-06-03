package net.silentchaos512.gems.entity;

import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;

import javax.annotation.Nullable;

public class EntityEnderSlime extends EntityMagmaCube {
    public EntityEnderSlime(World worldIn) {
        super(worldIn);
    }

    @Override
    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.DRAGON_BREATH;
    }

    protected EntitySlime createInstance() {
        return new EntityEnderSlime(this.world);
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return new ResourceLocation(SilentGems.MODID, "ender_slime");
    }
}
