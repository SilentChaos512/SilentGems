package net.silentchaos512.gems.client.render.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.EntityEnderSlime;

import javax.annotation.Nonnull;

public final class RenderEnderSlime extends RenderLiving<EntityEnderSlime> {
    private static final ResourceLocation ENDER_SLIME_TEXTURES = new ResourceLocation(SilentGems.RESOURCE_PREFIX + "textures/entity/ender_slime.png");

    private RenderEnderSlime(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelEnderSlime(), 0.25F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call
     * Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityEnderSlime entity) {
        return ENDER_SLIME_TEXTURES;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    @Override
    protected void preRenderCallback(EntityEnderSlime entity, float partialTickTime) {
        int i = entity.getSlimeSize();
        float f = (entity.prevSquishFactor
                + (entity.squishFactor - entity.prevSquishFactor) * partialTickTime)
                / ((float) i * 0.5F + 1.0F);
        float f1 = 1.0F / (f + 1.0F);
        GlStateManager.scalef(f1 * (float) i, 1.0F / f1 * (float) i, f1 * (float) i);
    }

    public static class Factory implements IRenderFactory<EntityEnderSlime> {
        @Override
        public Render<? super EntityEnderSlime> createRenderFor(RenderManager manager) {
            return new RenderEnderSlime(manager);
        }
    }
}
