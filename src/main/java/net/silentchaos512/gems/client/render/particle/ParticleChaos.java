package net.silentchaos512.gems.client.render.particle;

import java.util.ArrayDeque;
import java.util.Queue;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.client.particle.ParticleSL;
import net.silentchaos512.lib.client.render.BufferBuilderSL;

public class ParticleChaos extends ParticleSL {

  public static final ResourceLocation particles = new ResourceLocation("silentgems:textures/misc/wisplarge.png");

  private static final Queue<ParticleChaos> queuedRenders = new ArrayDeque<>();

  // Queue values
  private float f;
  private float f1;
  private float f2;
  private float f3;
  private float f4;
  private float f5;

  public boolean noClip = false;
  public final int particle = 16;

  public ParticleChaos(World world, double x, double y, double z, float size, float red, float green, float blue, int maxAge) {

    super(world, x, y, z, 0.0D, 0.0D, 0.0D);

    particleRed = red;
    particleGreen = green;
    particleBlue = blue;
    particleAlpha = 0.5F; // So MC renders us on the alpha layer, value not actually used
    particleGravity = 0;
    // particleTextureIndexX = 4;
    // particleTextureIndexY = 2; // 11;
    motionX = motionY = motionZ = SilentGems.random.nextGaussian() * 0.045f;
    particleScale *= size;
    particleMaxAge = maxAge;
    noClip = false;
    setSize(0.01F, 0.01F);
    prevPosX = posX;
    prevPosY = posY;
    prevPosZ = posZ;
  }

  @Override
  public void onUpdate() {

    if (this.particleAge++ >= this.particleMaxAge) {
      this.setExpired();
    }

    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;

    this.move(this.motionX, this.motionY, this.motionZ);

    this.particleScale *= 0.95f;
    this.particleAlpha -= 0.8f / (particleMaxAge * 1.25f);
  }

  public static void dispatchQueuedRenders(Tessellator tessellator) {

    ParticleRenderDispatcher.countChaos = 0;

    GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
    Minecraft.getMinecraft().renderEngine.bindTexture(particles);

    if (!queuedRenders.isEmpty()) {
      BufferBuilderSL buffer = BufferBuilderSL.INSTANCE.acquireBuffer(tessellator);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
      for (ParticleChaos wisp : queuedRenders)
        wisp.renderQueued(buffer);
      tessellator.draw();
    }

    queuedRenders.clear();
  }

  private void renderQueued(BufferBuilderSL buffer) {

    ParticleRenderDispatcher.countChaos++;

    float agescale = (float) particleAge / (float) (particleMaxAge / 2);
    if (agescale > 1F)
      agescale = 2 - agescale;

    float f10 = 0.5F * particleScale;
    float f11 = (float) (prevPosX + (posX - prevPosX) * f - interpPosX);
    float f12 = (float) (prevPosY + (posY - prevPosY) * f - interpPosY);
    float f13 = (float) (prevPosZ + (posZ - prevPosZ) * f - interpPosZ);
    int combined = 15 << 20 | 15 << 4;
    int k3 = combined >> 16 & 0xFFFF;
    int l3 = combined & 0xFFFF;
    buffer.pos(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10).tex(0, 1).lightmap(k3, l3)
        .color(particleRed, particleGreen, particleBlue, 0.5F).endVertex();
    buffer.pos(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10).tex(1, 1).lightmap(k3, l3)
        .color(particleRed, particleGreen, particleBlue, 0.5F).endVertex();
    buffer.pos(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10).tex(1, 0).lightmap(k3, l3)
        .color(particleRed, particleGreen, particleBlue, 0.5F).endVertex();
    buffer.pos(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10).tex(0, 0).lightmap(k3, l3)
        .color(particleRed, particleGreen, particleBlue, 0.5F).endVertex();
  }

  @Override
  public void clRenderParticle(BufferBuilderSL buffer, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {

    this.f = f;
    this.f1 = f1;
    this.f2 = f2;
    this.f3 = f3;
    this.f4 = f4;
    this.f5 = f5;

    queuedRenders.add(this);
  }

  public ParticleChaos setSpeed(double mx, double my, double mz) {

    motionX = mx;
    motionY = my;
    motionZ = mz;
    return this;
  }
}
