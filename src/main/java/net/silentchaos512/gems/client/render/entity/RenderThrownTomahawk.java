package net.silentchaos512.gems.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nullable;

public class RenderThrownTomahawk extends Render<EntityThrownTomahawk> {
    private RenderThrownTomahawk(RenderManager manager) {
        super(manager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityThrownTomahawk entity) {
        return null;
    }

    @Override
    public void doRender(@Nullable EntityThrownTomahawk entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity == null) return;
        ItemStack stack = entity.getThrownStack();
        if (stack == null || stack.isEmpty()) return;

        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

        EntityItem entityitem = new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, stack);
        entityitem.getItem().setCount(1);
        entityitem.hoverStart = 0.0F;
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();

        GlStateManager.translate(x, y, z);

        GlStateManager.rotate(90 - entity.throwYaw, 0, 1, 0);
        float rotation = entity.spin + (entity.inAir ? EntityThrownTomahawk.SPIN_RATE * partialTicks : 0);
        GlStateManager.rotate(rotation, 0, 0, 1);

        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderItem(entityitem.getItem(), ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    public static class Factory implements IRenderFactory<EntityThrownTomahawk> {
        @Override
        public Render<? super EntityThrownTomahawk> createRenderFor(RenderManager manager) {
            return new RenderThrownTomahawk(manager);
        }
    }
}
