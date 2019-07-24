package net.silentchaos512.gems.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.AbstractWispEntity;

import javax.annotation.Nullable;

public class WispRenderer extends MobRenderer<AbstractWispEntity, WispModel<AbstractWispEntity>> {
    private static final ResourceLocation TEXTURE = SilentGems.getId("textures/entity/wisp.png");

    public WispRenderer(EntityRendererManager rendererManagerIn) {
        super(rendererManagerIn, new WispModel<>(), 0.5f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(AbstractWispEntity entity) {
        return TEXTURE;
    }
}
