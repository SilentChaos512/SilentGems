package net.silentchaos512.gems.entity;

import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.init.Particles;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;

public class EntityEnderSlime extends EntityMagmaCube {
    public EntityEnderSlime(World worldIn) {
        super(worldIn);
    }

    @Override
    protected IParticleData func_195404_m() {
        return Particles.DRAGON_BREATH;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return new ResourceLocation(SilentGems.MOD_ID, "ender_slime");
    }
}
