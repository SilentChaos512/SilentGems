package net.silentchaos512.gems.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.tile.TileChaosFlowerPot;

public class RenderTileChaosFlowerPot extends TileEntitySpecialRenderer<TileChaosFlowerPot> {

  private static final double F = 0.4 * (1 - Math.sqrt(2) / 2);

  private ResourceLocation[] TEXTURES = new ResourceLocation[16];

  public RenderTileChaosFlowerPot() {

    for (int i = 0; i < TEXTURES.length; ++i) {
      TEXTURES[i] = new ResourceLocation("SilentGems:textures/blocks/GlowRose" + i + ".png");
    }
  }

  @Override
  public void renderTileEntityAt(TileChaosFlowerPot te, double x, double y, double z,
      float partialTicks, int destroyStage) {

    Tessellator tess = Tessellator.getInstance();
    VertexBuffer buff = tess.getBuffer();

    ItemStack stack = te.getFlowerItemStack();
    if (stack.isEmpty() || stack.getItemDamage() < 0 || stack.getItemDamage() >= TEXTURES.length) {
      return;
    }

    Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURES[stack.getItemDamage()]);

    double x1 = x + F;
    double x2 = x + 1 - F;
    double y1 = y + F;
    double y2 = y + 1.15 - F;
    double z1 = z + F;
    double z2 = z + 1 - F;

    GlStateManager.pushMatrix();
    GlStateManager.disableLighting();

    buff.begin(7, DefaultVertexFormats.POSITION_TEX);
    buff.pos(x1, y2, z1).tex(0, 0).endVertex();
    buff.pos(x2, y2, z2).tex(1, 0).endVertex();
    buff.pos(x2, y1, z2).tex(1, 1).endVertex();
    buff.pos(x1, y1, z1).tex(0, 1).endVertex();
    tess.draw();

    buff.begin(7, DefaultVertexFormats.POSITION_TEX);
    buff.pos(x1, y1, z1).tex(0, 1).endVertex();
    buff.pos(x2, y1, z2).tex(1, 1).endVertex();
    buff.pos(x2, y2, z2).tex(1, 0).endVertex();
    buff.pos(x1, y2, z1).tex(0, 0).endVertex();
    tess.draw();

    buff.begin(7, DefaultVertexFormats.POSITION_TEX);
    buff.pos(x2, y2, z1).tex(0, 0).endVertex();
    buff.pos(x1, y2, z2).tex(1, 0).endVertex();
    buff.pos(x1, y1, z2).tex(1, 1).endVertex();
    buff.pos(x2, y1, z1).tex(0, 1).endVertex();
    tess.draw();

    buff.begin(7, DefaultVertexFormats.POSITION_TEX);
    buff.pos(x2, y1, z1).tex(0, 1).endVertex();
    buff.pos(x1, y1, z2).tex(1, 1).endVertex();
    buff.pos(x1, y2, z2).tex(1, 0).endVertex();
    buff.pos(x2, y2, z1).tex(0, 0).endVertex();
    tess.draw();

    GlStateManager.popMatrix();
  }
}
