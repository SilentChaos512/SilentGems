package net.silentchaos512.gems.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.block.pedestal.PedestalTileEntity;
import net.silentchaos512.lib.event.ClientTicks;

public class PedestalRenderer extends TileEntityRenderer<PedestalTileEntity> {
    public PedestalRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(PedestalTileEntity te, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (te.getWorld() == null || !te.getWorld().isAreaLoaded(te.getPos(), 1)) return;

        ItemStack stack = te.getItem();
        if (stack.isEmpty()) return;

        Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        float worldTime = ClientTicks.ticksInGame() + partialTicks + (te.getPos().hashCode() % 360);
        renderItem(te.getWorld(), stack, matrixStackIn, bufferIn, combinedLightIn, partialTicks, worldTime);
    }

    @SuppressWarnings("deprecation")
    private static void renderItem(World world, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, float partialTicks, float worldTime) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        if (stack != null) {
            ItemStack itemStack = stack.copy();
            itemStack.setCount(1);
            matrixStack.push();
            RenderSystem.disableLighting();

            matrixStack.translate(0.5, 1.35 + 0.05f * Math.sin(worldTime / 15), 0.5);
            float rotation = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) + worldTime * 2f;
            matrixStack.rotate(new Quaternion(0, rotation, 0, true));
            float scale = 0.5f;
            matrixStack.scale(scale, scale, scale);

            RenderSystem.pushLightingAttributes();
            RenderHelper.enableStandardItemLighting();
            itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED, combinedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
            RenderHelper.disableStandardItemLighting();
            RenderSystem.popAttributes();

            RenderSystem.enableLighting();
            matrixStack.pop();
        }
    }
}
