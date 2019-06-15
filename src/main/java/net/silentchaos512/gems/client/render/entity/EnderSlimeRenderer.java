package net.silentchaos512.gems.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.EnderSlimeEntity;

import javax.annotation.Nonnull;

public final class EnderSlimeRenderer extends MobRenderer<EnderSlimeEntity, EnderSlimeModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SilentGems.RESOURCE_PREFIX + "textures/entity/ender_slime.png");

    private EnderSlimeRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new EnderSlimeModel(), 0.25F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call
     * Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EnderSlimeEntity entity) {
        return TEXTURE;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    @Override
    protected void preRenderCallback(EnderSlimeEntity entity, float partialTickTime) {
        int i = entity.getSlimeSize();
        float f = (entity.prevSquishFactor
                + (entity.squishFactor - entity.prevSquishFactor) * partialTickTime)
                / ((float) i * 0.5F + 1.0F);
        float f1 = 1.0F / (f + 1.0F);
        GlStateManager.scalef(f1 * (float) i, 1.0F / f1 * (float) i, f1 * (float) i);
    }

    public static class Factory implements IRenderFactory<EnderSlimeEntity> {
        @Override
        public EntityRenderer<? super EnderSlimeEntity> createRenderFor(EntityRendererManager manager) {
            return new EnderSlimeRenderer(manager);
        }
    }
}
