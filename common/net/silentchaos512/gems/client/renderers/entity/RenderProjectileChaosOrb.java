package net.silentchaos512.gems.client.renderers.entity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.projectile.EntityProjectileChaosOrb;

public class RenderProjectileChaosOrb extends Render {

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
    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    float f2 = 0.35f;
    GL11.glScalef(f2, f2, f2);

    bindTexture(TEXTURE);
    Tessellator tess = Tessellator.instance;

    GL11.glRotatef(180f - renderManager.playerViewY, 0f, 1f, 0f);
    GL11.glRotatef(-renderManager.playerViewX, 1f, 0f, 0f);

    float uMin = 0f;
    float uMax = 1f;
    float vMin = 0f;
    float vMax = 1f;
    float f7 = 1f;
    float f8 = 0.5f;
    float f9 = 0.25f;

    tess.startDrawingQuads();
    tess.setColorRGBA(colorR, colorG, colorB, 64);
    tess.setNormal(0f, 1f, 0f);
    tess.addVertexWithUV(0f - f8, 0f - f9, 0d, uMin, vMax);
    tess.addVertexWithUV(f7 - f8, 0.0F - f9, 0.0D, uMax, vMax);
    tess.addVertexWithUV(f7 - f8, 1.0F - f9, 0.0D, uMax, vMin);
    tess.addVertexWithUV(0.0F - f8, 1.0F - f9, 0.0D, uMin, vMin);
    tess.draw();
    GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    GL11.glPopMatrix();
  }

  @Override
  protected ResourceLocation getEntityTexture(Entity p_110775_1_) {

    return TEXTURE;
  }

}
