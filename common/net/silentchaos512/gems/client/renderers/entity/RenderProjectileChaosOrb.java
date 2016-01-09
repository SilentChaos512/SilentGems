package net.silentchaos512.gems.client.renderers.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.projectile.EntityProjectileChaosOrb;

public class RenderProjectileChaosOrb extends Render {

  public RenderProjectileChaosOrb() {

    this(Minecraft.getMinecraft().getRenderManager());
  }

  protected RenderProjectileChaosOrb(RenderManager renderManager) {

    super(renderManager);
  }

  private static final ResourceLocation particleTextures = new ResourceLocation(
      "textures/particle/particles.png");

  public static final ResourceLocation TEXTURE = new ResourceLocation(
      SilentGems.MOD_ID.toLowerCase(), "textures/effects/ChaosOrb.png");

  @Override
  public void doRender(Entity entity, double posX, double posY, double posZ, float par4,
      float par5) {

    int colorR = 255;
    int colorG = 255;
    int colorB = 255;

    if (entity instanceof EntityProjectileChaosOrb) {
      EntityProjectileChaosOrb shot = (EntityProjectileChaosOrb) entity;
      colorR = shot.color >> 16 & 255;
      colorG = shot.color >> 8 & 255;
      colorB = shot.color & 255;
    }

    GL11.glPushMatrix();

    GL11.glTranslated(posX, posY, posZ);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glDepthMask(false);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
    float f2 = 0.7f;
    GL11.glScalef(f2, f2, f2);

    // bindTexture(TEXTURE);
    bindTexture(particleTextures);
    Tessellator tess = Tessellator.getInstance();
    WorldRenderer worldRenderer = tess.getWorldRenderer();

    GL11.glRotatef(180f - renderManager.playerViewY, 0f, 1f, 0f);
    GL11.glRotatef(-renderManager.playerViewX, 1f, 0f, 0f);

    float uMin = 0.25f;
    float uMax = uMin + 0.25f;
    float vMin = 0.125f;
    float vMax = vMin + 0.25f;
    float f7 = 1f;
    float f8 = 0.5f;
    float f9 = 0.25f;

    worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
    worldRenderer.color(colorR, colorG, colorB, 255);
    worldRenderer.normal(0f, 1f, 0f);
    worldRenderer.pos(0f - f8, 0f - f9, 0d).tex(uMin, vMax);
    worldRenderer.pos(f7 - f8, 0.0F - f9, 0.0D).tex(uMax, vMax);
    worldRenderer.pos(f7 - f8, 1.0F - f9, 0.0D).tex(uMax, vMin);
    worldRenderer.pos(0.0F - f8, 1.0F - f9, 0.0D).tex(uMin, vMin);
    tess.draw();

    GL11.glDisable(GL11.GL_BLEND);
    GL11.glDepthMask(true);
    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

    GL11.glPopMatrix();
  }

  @Override
  protected ResourceLocation getEntityTexture(Entity p_110775_1_) {

    return TEXTURE;
  }

}
