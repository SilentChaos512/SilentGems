package net.silentchaos512.gems.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeGelLayer;
import net.minecraft.client.renderer.entity.model.SlimeModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.CorruptedSlimeEntity;

import javax.annotation.Nonnull;

public final class CorruptedSlimeRenderer extends MobRenderer<CorruptedSlimeEntity, SlimeModel<CorruptedSlimeEntity>> {
    private static final ResourceLocation TEXTURE = SilentGems.getId("textures/entity/corrupted_slime.png");

    public CorruptedSlimeRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SlimeModel<>(16), 0.25F);
        this.addLayer(new SlimeGelLayer<>(this));
    }

    @Override
    public void doRender(CorruptedSlimeEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.shadowSize = 0.25F * (float)entity.getSlimeSize();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(CorruptedSlimeEntity entity, float partialTickTime) {
        GlStateManager.scalef(0.999F, 0.999F, 0.999F);
        float f1 = (float)entity.getSlimeSize();
        float f2 = MathHelper.lerp(partialTickTime, entity.prevSquishFactor, entity.squishFactor) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        GlStateManager.scalef(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull CorruptedSlimeEntity entity) {
        return TEXTURE;
    }
}
