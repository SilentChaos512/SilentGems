package net.silentchaos512.gems.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.entity.AbstractWispEntity;
import net.silentchaos512.utils.Color;

public class WispRenderer extends MobRenderer<AbstractWispEntity, WispModel<AbstractWispEntity>> {
    public WispRenderer(EntityRendererManager rendererManagerIn) {
        super(rendererManagerIn, new WispModel<>(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(AbstractWispEntity entity) {
        return entity.getWispType().getEntityTexture();
    }

    @Override
    public void render(AbstractWispEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void preRenderCallback(AbstractWispEntity entityIn, MatrixStack matrixStackIn, float partialTickTime) {
        Color color = entityIn.getWispType().getColor();
        RenderSystem.color3f(color.getRed(), color.getGreen(), color.getBlue());
    }
}
