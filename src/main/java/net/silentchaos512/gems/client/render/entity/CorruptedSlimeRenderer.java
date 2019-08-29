package net.silentchaos512.gems.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeGelLayer;
import net.minecraft.client.renderer.entity.model.SlimeModel;
import net.minecraft.util.ResourceLocation;
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
    protected ResourceLocation getEntityTexture(@Nonnull CorruptedSlimeEntity entity) {
        return TEXTURE;
    }
}
