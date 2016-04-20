package net.silentchaos512.gems.client.render.tile;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.tile.TileChaosAltar;

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

    // TODO: Render stored item?

    GlStateManager.popMatrix();
  }
}
