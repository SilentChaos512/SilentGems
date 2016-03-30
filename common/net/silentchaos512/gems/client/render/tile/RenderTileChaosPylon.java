package net.silentchaos512.gems.client.render.tile;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.client.render.models.ModelPylon;
import net.silentchaos512.gems.client.render.models.ModelPylonPlates;
import net.silentchaos512.gems.tile.TileChaosPylon;

public class RenderTileChaosPylon extends TileEntitySpecialRenderer<TileChaosPylon> {

  ModelPylon model;
  ModelPylonPlates plates;

  @Override
  public void renderTileEntityAt(TileChaosPylon te, double x, double y, double z,
      float partialTicks, int destroyStage) {

    if (te != null && te.getWorld() != null && !te.getWorld().isBlockLoaded(te.getPos(), false)) {
      return;
    }

    if (model == null) {
      model = new ModelPylon();
    }
    if (plates == null) {
      plates = new ModelPylonPlates();
    }

    double worldTime = (te.getWorld() == null) ? 0
        : (double) (ClientTickHandler.ticksInGame + partialTicks)
            + new Random(te.getPos().hashCode()).nextInt(360);
    int pylonType = te.getPylonType().getMeta();
    double bobFactor = 1.0;

    // render the pylon
    GlStateManager.pushMatrix();
    GlStateManager.enableRescaleNormal();
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GlStateManager.color(1F, 1F, 1F, 1F);
    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
    GlStateManager.translate(x + 0.5, y + 0.5 + 0.1 * Math.sin(worldTime / 20) * bobFactor,
        z + 0.5);
    GlStateManager.rotate((float) worldTime, 0.0f, 1.0f, 0.0f);
    // model = new ModelPylon();
    model.renderPylon(pylonType);
    GlStateManager.popMatrix();

    // render the plates
    GlStateManager.pushMatrix();
    GlStateManager.translate(x + 0.5, y + 0.5 + 0.1 * Math.sin(worldTime / 20) * bobFactor,
        z + 0.5);
    GlStateManager.rotate(-(float) worldTime, 0.0f, 1.0f, 0.0f);
    // model = new ModelPylon();
    plates.renderPylonPlates(pylonType);
    GlStateManager.popMatrix();
  }
}
