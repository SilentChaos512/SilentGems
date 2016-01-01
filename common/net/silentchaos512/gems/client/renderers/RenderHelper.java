package net.silentchaos512.gems.client.renderers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;

@SideOnly(Side.CLIENT)
public class RenderHelper {

  public static void drawRect(float x, float y, float u, float v, float width, float height) {

    Tessellator tess = Tessellator.instance;
    tess.startDrawingQuads();
    tess.addVertexWithUV(x, y + height, 0, 0, 1);
    tess.addVertexWithUV(x + width, y + height, 0, 1, 1);
    tess.addVertexWithUV(x + width, y, 0, 1, 0);
    tess.addVertexWithUV(x, y, 0, 0, 0);
    tess.draw();
  }
}
