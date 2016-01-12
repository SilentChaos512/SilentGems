package net.silentchaos512.gems.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.silentchaos512.gems.tile.TileChaosAltar;

public class RenderChaosAltar extends TileEntitySpecialRenderer<TileChaosAltar> {

  public static Minecraft mc = Minecraft.getMinecraft();

  @Override
  public void renderTileEntityAt(TileChaosAltar te, double x, double y, double z,
      float partialTicks, int destroyStage) {

    TileChaosAltar altar = (TileChaosAltar) te;
    float timer = altar.getTimer();
    timer += partialTicks;

    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y, z);
    GlStateManager.popMatrix();
  }
}
