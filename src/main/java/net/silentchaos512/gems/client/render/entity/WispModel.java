package net.silentchaos512.gems.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.entity.AbstractWispEntity;
import net.silentchaos512.utils.Color;

public class WispModel<T extends AbstractWispEntity> extends EntityModel<T> {
    private final RendererModel[] satellites = new RendererModel[12];
    private final RendererModel mainBody;

    public WispModel() {
        for (int i = 0; i < this.satellites.length; ++i) {
            this.satellites[i] = new RendererModel(this, 0, 16);
            this.satellites[i].addBox(0f, 0f, 0f, 2, 2, 2);
        }

        this.mainBody = new RendererModel(this, 0, 0);
        this.mainBody.addBox(-4f, -1f, -4f, 8, 8, 8);
    }

    @Override
    public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();

        Color color = entityIn.getWispType().getColor();
        GlStateManager.color3f(color.getRed(), color.getGreen(), color.getBlue());

        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.mainBody.render(scale);

        for (RendererModel model : this.satellites) {
            model.render(scale);
        }

        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        float f = ((float)Math.PI / 4F) + ageInTicks * (float)Math.PI * 0.03F;

        for (int i = 0; i < this.satellites.length; ++i) {
            this.satellites[i].rotationPointY = 2.0F + MathHelper.cos(((float)(i * 2) + ageInTicks) * 0.25F);
            this.satellites[i].rotationPointX = MathHelper.cos(f) * 7.0F;
            this.satellites[i].rotationPointZ = MathHelper.sin(f) * 7.0F;
            f += 0.525f;
        }

//        this.mainBody.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
//        this.mainBody.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.mainBody.rotateAngleY = -ageInTicks * (float)Math.PI * -0.05F;
    }
}
