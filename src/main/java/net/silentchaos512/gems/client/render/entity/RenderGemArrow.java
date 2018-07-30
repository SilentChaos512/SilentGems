package net.silentchaos512.gems.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.silentchaos512.gems.entity.EntityGemArrow;

public class RenderGemArrow extends RenderTippedArrow {
    private RenderGemArrow(RenderManager manager) {
        super(manager);
    }

    @Override
    public void doRender(EntityTippedArrow entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    public static class Factory implements IRenderFactory<EntityGemArrow> {
        @Override
        public Render<? super EntityGemArrow> createRenderFor(RenderManager manager) {
            return new RenderGemArrow(manager);
        }
    }
}
