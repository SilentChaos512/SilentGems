package net.silentchaos512.gems.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelper {

  public static void drawRect(float x, float y, float u, float v, float width, float height) {

    Tessellator tess = Tessellator.getInstance();
    WorldRenderer worldRender = tess.getWorldRenderer();
    worldRender.begin(7, DefaultVertexFormats.POSITION_TEX);
    worldRender.pos(x, y + height, 0).tex(0, 1).endVertex();
    worldRender.pos(x + width, y + height, 0).tex(1, 1).endVertex();
    worldRender.pos(x + width, y, 0).tex(1, 0).endVertex();
    worldRender.pos(x, y, 0).tex(0, 0).endVertex();
    tess.draw();
  }
}
