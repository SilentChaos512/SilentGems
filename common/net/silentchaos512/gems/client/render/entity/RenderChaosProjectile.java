package net.silentchaos512.gems.client.render.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.EntityChaosProjectile;

public class RenderChaosProjectile extends Render<EntityChaosProjectile> {

  public RenderChaosProjectile() {

    this(Minecraft.getMinecraft().getRenderManager());
  }

  protected RenderChaosProjectile(RenderManager renderManager) {

    super(renderManager);
  }

  // private static final ResourceLocation particleTextures = new ResourceLocation(
  // "textures/particle/particles.png");

  public static final ResourceLocation TEXTURE = new ResourceLocation(
      SilentGems.MOD_ID.toLowerCase(), "textures/effects/ChaosOrb.png");

  @Override
  public void doRender(EntityChaosProjectile entity, double x, double y, double z, float entityYaw,
      float partialTicks) {

    final boolean doRender = false;
    if (!doRender) {
      return;
    }

    final float scale = 1f;

    GlStateManager.pushMatrix();
    this.bindTexture(new ResourceLocation("silentgems:textures/effects/ChaosOrb.png"));
    GlStateManager.translate((float) x, (float) y, (float) z);
    GlStateManager.enableRescaleNormal();
    GlStateManager.enableBlend();
    GlStateManager.scale(scale, scale, scale);
    TextureAtlasSprite textureatlassprite = Minecraft.getMinecraft().getRenderItem()
        .getItemModelMesher().getParticleIcon(Items.fire_charge);
    Tessellator tessellator = Tessellator.getInstance();
    VertexBuffer vertexbuffer = tessellator.getBuffer();
    float f = textureatlassprite.getMinU();
    float f1 = textureatlassprite.getMaxU();
    float f2 = textureatlassprite.getMinV();
    float f3 = textureatlassprite.getMaxV();
    float f4 = 1.0F;
    float f5 = 0.5F;
    float f6 = 0.25F;
    GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1)
        * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(this.getTeamColor(entity));
    }

    vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
    vertexbuffer.pos(-0.5D, -0.25D, 0.0D).tex((double) f, (double) f3).normal(0.0F, 1.0F, 0.0F)
        .endVertex();
    vertexbuffer.pos(0.5D, -0.25D, 0.0D).tex((double) f1, (double) f3).normal(0.0F, 1.0F, 0.0F)
        .endVertex();
    vertexbuffer.pos(0.5D, 0.75D, 0.0D).tex((double) f1, (double) f2).normal(0.0F, 1.0F, 0.0F)
        .endVertex();
    vertexbuffer.pos(-0.5D, 0.75D, 0.0D).tex((double) f, (double) f2).normal(0.0F, 1.0F, 0.0F)
        .endVertex();
    tessellator.draw();

    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    }

    GlStateManager.disableBlend();
    GlStateManager.disableRescaleNormal();
    GlStateManager.popMatrix();
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }

  // @Override
  // public void doRender(EntityChaosProjectile entity, double posX, double posY, double posZ,
  // float entityYaw, float partialTicks) {
  //
  // int colorR = 255;
  // int colorG = 255;
  // int colorB = 255;
  //
  // colorR = entity.getColor() >> 16 & 255;
  // colorG = entity.getColor() >> 8 & 255;
  // colorB = entity.getColor() & 255;
  //
  // GlStateManager.pushMatrix();
  // GlStateManager.enableBlend();
  // GlStateManager.disableDepth();
  // GlStateManager.translate(posX, posY, posZ);
  // GlStateManager.enableRescaleNormal();
  // GlStateManager.rotate(-renderManager.playerViewY, 0f, 1f, 0f);
  // GlStateManager.rotate((renderManager.options.thirdPersonView == 2 ? -1 : 1) * renderManager.playerViewX, 1f, 0f,
  // 0f);
  // GlStateManager.rotate(180f, 0f, 1f, 0f);
  // bindTexture(new ResourceLocation("textures/particle/particles.png"));
  // //new ResourceLocation("silentgems:textures/effects/ChaosOrb.png")
  //
  //// GL11.glPushMatrix();
  //
  //// GL11.glTranslated(posX, posY, posZ);
  ////// GL11.glEnable(GL11.GL_BLEND);
  //// GL11.glDepthMask(false);
  //// GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
  //// GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
  //// float f2 = 0.7f;
  //// GL11.glScalef(f2, f2, f2);
  //
  // // bindTexture(TEXTURE);
  // Tessellator tess = Tessellator.getInstance();
  // VertexBuffer buff = tess.getBuffer();
  //
  //// GL11.glRotatef(180f - renderManager.playerViewY, 0f, 1f, 0f);
  //// GL11.glRotatef(-renderManager.playerViewX, 1f, 0f, 0f);
  //
  // float uMin = 0f;
  // float uMax = uMin + 1f;
  // float vMin = 0f;
  // float vMax = vMin + 1f;
  // float f7 = 1f;
  // float f8 = 0.5f;
  // float f9 = 0.25f;
  //
  // buff.begin(7, DefaultVertexFormats.POSITION_TEX);
  // buff.color(colorR, colorG, colorB, 255);
  // buff.normal(0f, 1f, 0f);
  // buff.pos(0f - f8, 0f - f9, 0d).tex(uMin, vMax);
  // buff.pos(f7 - f8, 0.0F - f9, 0.0D).tex(uMax, vMax);
  // buff.pos(f7 - f8, 1.0F - f9, 0.0D).tex(uMax, vMin);
  // buff.pos(0.0F - f8, 1.0F - f9, 0.0D).tex(uMin, vMin);
  // tess.draw();
  //
  //// GL11.glDisable(GL11.GL_BLEND);
  //// GL11.glDepthMask(true);
  //// GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
  ////
  //// GL11.glPopMatrix();
  //
  // GlStateManager.disableRescaleNormal();
  // GlStateManager.enableDepth();
  // GlStateManager.disableBlend();
  // GlStateManager.popMatrix();
  // }

  @Override
  protected ResourceLocation getEntityTexture(EntityChaosProjectile entity) {

    return TEXTURE;
  }
}
