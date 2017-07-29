package net.silentchaos512.gems.client.render.particle;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.silentchaos512.lib.client.particle.ParticleSL;
import net.silentchaos512.lib.client.render.BufferBuilderSL;

public class ParticleCompass extends ParticleSL {

  public static final int MAX_AGE = 5;
  public static final int MAX_SCALE = 1;

  protected ParticleCompass(World world, double posX, double posY, double posZ) {

    this(world, posX, posY, posZ, 0, 0, 0, MAX_SCALE, MAX_AGE, 0.0f, 0.3f, 0.8f);
  }

  public ParticleCompass(World world, double posX, double posY, double posZ, double motionX,
      double motionY, double motionZ) {

    this(world, posX, posY, posZ, motionX, motionY, motionZ, MAX_SCALE, MAX_AGE, 0.0f, 0.3f, 0.8f);
  }

  public ParticleCompass(World world, double posX, double posY, double posZ, double motionX,
      double motionY, double motionZ, float scale, int maxAge, float red, float green, float blue) {

    super(world, posX, posY, posZ, 0, 0, 0);
    this.motionX = motionX;
    this.motionY = motionY;
    this.motionZ = motionZ;
    this.particleTextureIndexX = 4;
    this.particleTextureIndexY = 11;
    this.particleRed = red;
    this.particleGreen = green;
    this.particleBlue = blue;
    // this.particleRed = rand.nextFloat();
    // this.particleGreen = rand.nextFloat();
    // this.particleBlue = rand.nextFloat();
    this.particleScale = scale;
    this.particleMaxAge = maxAge;
    this.canCollide = false;
    this.particleGravity = 0.05f;
    this.particleAlpha = 0.9f;
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

//    this.particleScale *= 0.9f;
//    this.particleAlpha -= 0.8f / (particleMaxAge * 1.25f);
  }

  @Override
  public void clRenderParticle(BufferBuilderSL worldRendererIn, Entity entityIn, float partialTicks,
      float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

    float uMin = (float) this.particleTextureIndexX / 16.0F;
    float uMax = uMin + .0625f;
    float vMin = (float) this.particleTextureIndexY / 16.0F;
    float vMax = vMin + .0625f;
    float f4 = 0.1F * this.particleScale;

    if (this.particleTexture != null) {
      uMin = this.particleTexture.getMinU();
      uMax = this.particleTexture.getMaxU();
      vMin = this.particleTexture.getMinV();
      vMax = this.particleTexture.getMaxV();
    }

    float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
    float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
    float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);

    int i = this.getBrightnessForRender(partialTicks);
    int j = i >> 16 & 65535;
    int k = i & 65535;

    worldRendererIn
        .pos((double) (f5 - rotationX * f4 - rotationXY * f4), (double) (f6 - rotationZ * f4),
            (double) (f7 - rotationYZ * f4 - rotationXZ * f4))
        .tex((double) uMax, (double) vMax)
        .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
        .lightmap(j, k).endVertex();
    worldRendererIn
        .pos((double) (f5 - rotationX * f4 + rotationXY * f4), (double) (f6 + rotationZ * f4),
            (double) (f7 - rotationYZ * f4 + rotationXZ * f4))
        .tex((double) uMax, (double) vMin)
        .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
        .lightmap(j, k).endVertex();
    worldRendererIn
        .pos((double) (f5 + rotationX * f4 + rotationXY * f4), (double) (f6 + rotationZ * f4),
            (double) (f7 + rotationYZ * f4 + rotationXZ * f4))
        .tex((double) uMin, (double) vMin)
        .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
        .lightmap(j, k).endVertex();
    worldRendererIn
        .pos((double) (f5 + rotationX * f4 - rotationXY * f4), (double) (f6 - rotationZ * f4),
            (double) (f7 + rotationYZ * f4 - rotationXZ * f4))
        .tex((double) uMin, (double) vMax)
        .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
        .lightmap(j, k).endVertex();
  }
}
