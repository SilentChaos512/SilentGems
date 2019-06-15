package net.silentchaos512.gems.client.render.entity;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.entity.EnderSlimeEntity;

public class ModelEnderSlime extends EntityModel<EnderSlimeEntity> {
    private final RendererModel[] segments = new RendererModel[8];
    private final RendererModel core;

    public ModelEnderSlime() {
        for (int i = 0; i < this.segments.length; ++i) {
            int j = 0;
            int k = i;
            if (i == 2) {
                j = 24;
                k = 10;
            } else if (i == 3) {
                j = 24;
                k = 19;
            }

            this.segments[i] = new RendererModel(this, j, k);
            this.segments[i].addBox(-4.0F, (float) (16 + i), -4.0F, 8, 1, 8);
        }

        this.core = new RendererModel(this, 0, 16);
        this.core.addBox(-2.0F, 18.0F, -2.0F, 4, 4, 4);
    }

    @Override
    public void setLivingAnimations(EnderSlimeEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        float f = MathHelper.lerp(partialTick, entityIn.prevSquishFactor, entityIn.squishFactor);
        if (f < 0.0F) {
            f = 0.0F;
        }

        for (int i = 0; i < this.segments.length; ++i) {
            this.segments[i].rotationPointY = (float) (-(4 - i)) * f * 1.7F;
        }
    }

    @Override
    public void render(EnderSlimeEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.core.render(scale);

        for (RendererModel renderermodel : this.segments) {
            renderermodel.render(scale);
        }
    }
}
