package net.silentchaos512.gems.client.render.tile;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.lib.util.StackHelper;

public class RenderTileChaosAltar extends TileEntitySpecialRenderer<TileChaosAltar> {

  // ModelAltarDiamond diamond;

  @Override
  public void renderTileEntityAt(TileChaosAltar te, double x, double y, double z,
      float partialTicks, int destroyStage) {

    if (te != null && te.getWorld() != null && !te.getWorld().isBlockLoaded(te.getPos(), false)) {
      return;
    }

    // if (diamond==null)
    // {
    // diamond = new ModelAltarDiamond();
    // }

    GlStateManager.pushMatrix();
    GlStateManager.translate(x + 0.5, y + 0.85, z + 0.5);
    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    double worldTime = (te.getWorld() == null) ? 0
        : (double) (ClientTickHandler.ticksInGame + partialTicks)
            + new Random(te.getPos().hashCode()).nextInt(360);

    GlStateManager.translate(0, 0.05f * Math.sin(worldTime / 15), 0);
    GlStateManager.rotate((float) worldTime * 2, 0, 1, 0);

    // diamond.renderDiamond();

    renderItem(te.getWorld(), te.getStackToRender(), partialTicks);

    GlStateManager.popMatrix();
  }

  private void renderItem(World world, ItemStack stack, float partialTicks) {

    RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
    if (stack != null) {
      // GlStateManager.translate(0.5, 0.5, 0.5);
      EntityItem entityitem = new EntityItem(world, 0.0D, 0.0D, 0.0D, stack.copy());
      StackHelper.setCount(entityitem.getEntityItem(), 1);
      entityitem.hoverStart = 0.0F;
      GlStateManager.pushMatrix();
      GlStateManager.disableLighting();

      float rotation = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
      GlStateManager.rotate(rotation, 0.0F, 1.0F, 0);
      Vec3d vec = new Vec3d(0.0, 0.25, 0.0);
      GlStateManager.translate(vec.xCoord, vec.yCoord, vec.zCoord);

      float scale = 0.5f;
      GlStateManager.scale(scale, scale, scale);
      GlStateManager.pushAttrib();
      RenderHelper.enableStandardItemLighting();
      itemRenderer.renderItem(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
      RenderHelper.disableStandardItemLighting();
      GlStateManager.popAttrib();

      GlStateManager.enableLighting();
      GlStateManager.popMatrix();
    }
  }
}
