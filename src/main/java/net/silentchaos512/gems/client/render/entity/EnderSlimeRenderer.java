package net.silentchaos512.gems.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.EnderSlimeEntity;

import javax.annotation.Nonnull;

public final class EnderSlimeRenderer extends MobRenderer<EnderSlimeEntity, EnderSlimeModel> {
    private static final ResourceLocation TEXTURE = SilentGems.getId("textures/entity/ender_slime.png");

    public EnderSlimeRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new EnderSlimeModel(), 0.25F);
    }

    @Override
    public ResourceLocation getEntityTexture(@Nonnull EnderSlimeEntity entity) {
        return TEXTURE;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    @Override
    protected void preRenderCallback(EnderSlimeEntity entity, MatrixStack matrixStackIn, float partialTickTime) {
        int i = entity.getSlimeSize();
        float f = MathHelper.lerp(partialTickTime, entity.prevSquishFactor, entity.squishFactor) / ((float)i * 0.5F + 1.0F);
        float f1 = 1.0F / (f + 1.0F);
        matrixStackIn.scale(f1 * (float)i, 1.0F / f1 * (float)i, f1 * (float)i);
    }
}
