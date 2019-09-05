package net.silentchaos512.gems.client.render.tile;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.silentchaos512.gems.block.pedestal.PedestalTileEntity;
import net.silentchaos512.lib.event.ClientTicks;

public class PedestalRenderer extends TileEntityRenderer<PedestalTileEntity> {
    @Override
    public void render(PedestalTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (te.getWorld() == null || !te.getWorld().isAreaLoaded(te.getPos(), 1)) return;

        ItemStack stack = te.getItem();
        if (stack.isEmpty()) return;

        GlStateManager.pushMatrix();
        GlStateManager.translated(x + 0.5, y + 1.1, z + 0.5);
        Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        double worldTime = ClientTicks.ticksInGame() + partialTicks + (te.getPos().hashCode() % 360);

//        GlStateManager.translated(0, 0.05f * Math.sin(worldTime / 15), 0);
        GlStateManager.rotated(worldTime * 2, 0, 1, 0);

        renderItem(te.getWorld(), stack, partialTicks);

        GlStateManager.popMatrix();
    }

    private static void renderItem(World world, ItemStack stack, float partialTicks) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        if (stack != null) {
            ItemStack itemStack = stack.copy();
            itemStack.setCount(1);
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            float rotation = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
            GlStateManager.rotatef(rotation, 0.0F, 1.0F, 0);
            Vec3d vec = new Vec3d(0.0, 0.25, 0.0);
            GlStateManager.translated(vec.x, vec.y, vec.z);

            float scale = 0.5f;
            GlStateManager.scalef(scale, scale, scale);
            GlStateManager.pushLightingAttributes();
            RenderHelper.enableStandardItemLighting();
            itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttributes();

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}
