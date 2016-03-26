package net.silentchaos512.gems.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.util.Color;

@SideOnly(Side.CLIENT)
public class GuiChaosBar extends Gui {

  public static final GuiChaosBar INSTANCE = new GuiChaosBar(Minecraft.getMinecraft());

  public static final ResourceLocation TEXTURE_FRAME = new ResourceLocation(SilentGems.MOD_ID,
      "textures/gui/ChaosBarFrame.png");
  public static final ResourceLocation TEXTURE_BAR = new ResourceLocation(SilentGems.MOD_ID,
      "textures/gui/ChaosBar.png");

  public static final int POPUP_TIME = 6000;

  private int currentChaos = 0;
  private int maxChaos = 10000;
  private long lastUpdateTime = 0;
  private long currentTime;

  private Minecraft mc;

  public GuiChaosBar(Minecraft mc) {

    super();
    this.mc = mc;
  }

  public void update(int currentChaos, int maxChaos) {

    this.currentChaos = currentChaos;
    this.maxChaos = maxChaos < currentChaos ? currentChaos : maxChaos;
    lastUpdateTime = System.currentTimeMillis();
  }

  @SubscribeEvent
  public void onRenderGameOverlay(RenderGameOverlayEvent event) {

    if (event.isCancelable() || event.getType() != ElementType.TEXT) {
      return;
    }

    currentTime = System.currentTimeMillis();
    if (currentTime > lastUpdateTime + POPUP_TIME) {
      return;
    }

    ScaledResolution res = new ScaledResolution(mc);

    int posX, posY;
    float scale;

    final Color color = new Color(1f, 1f, 1f, 0.75f);
    final int barWidth = 80;
    final int barHeight = 8;
    final float xOffset = 0.5f;
    final float yOffset = 0.15f;
    final float fraction = (float) currentChaos / maxChaos;

    GlStateManager.disableLighting();
    GlStateManager.enableBlend();

    scale = 1.0f;
    if (scale > 0f) {
      GlStateManager.pushMatrix();

      posX = (int) (res.getScaledWidth() / scale * xOffset - barWidth / 2);
      posY = (int) (res.getScaledHeight() / scale * yOffset);
      GlStateManager.scale(scale, scale, 1);

      drawBar(posX, posY, barWidth, barHeight, color, fraction);
      drawBarFrame(posX, posY, barWidth, barHeight, new Color(1f, 1f, 1f, 1f));

//      String line = "" + currentChaos;
//      int lineWidth = mc.fontRendererObj.getStringWidth(line);
//      mc.fontRendererObj.drawStringWithShadow(line, res.getScaledWidth() * xOffset - lineWidth / 2,
//          res.getScaledHeight() * (yOffset + 0.025f), 0xFFFFFF);

      GlStateManager.popMatrix();
    }

    // GlStateManager.disableBlend();
    // GlStateManager.enableLighting();
  }

  protected void drawBar(float x, float y, float width, float height, Color color, float fraction) {

    mc.renderEngine.bindTexture(TEXTURE_BAR);
    GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

    float barPosWidth = width * fraction;
    float barPosX = x + width * (1f - fraction) / 2;
    drawRect(barPosX, y, 0, 0, barPosWidth, height);
  }

  protected void drawBarFrame(float x, float y, float width, float height, Color color) {

    mc.renderEngine.bindTexture(TEXTURE_FRAME);
    GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    drawRect(x, y, 0, 0, width, height);
  }

  public void drawRect(float x, float y, float u, float v, float width, float height) {

    Tessellator tess = Tessellator.getInstance();
    VertexBuffer buff = tess.getBuffer();
    buff.begin(7, DefaultVertexFormats.POSITION_TEX);
    buff.pos(x, y + height, 0).tex(0, 1).endVertex();
    buff.pos(x + width, y + height, 0).tex(1, 1).endVertex();
    buff.pos(x + width, y, 0).tex(1, 0).endVertex();
    buff.pos(x, y, 0).tex(0, 0).endVertex();
    tess.draw();
  }
}
