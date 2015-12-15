package net.silentchaos512.gems.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.proxy.ClientProxy;
import net.silentchaos512.gems.core.util.LogHelper;

public class EntityParticleFXChaosTransfer extends EntityFX {

  public static final int MAX_AGE = 20;
  public static final int MAX_SCALE = 6;

  public EntityParticleFXChaosTransfer(World world, double posX, double posY, double posZ) {

    this(world, posX, posY, posZ, 0.0001, 0.0001, 0.0001, 1.0f, 10, 0.0f, 0.2f, 0.8f);
  }

  public EntityParticleFXChaosTransfer(World world, double posX, double posY, double posZ,
      double motionX, double motionY, double motionZ) {

    this(world, posX, posY, posZ, motionX, motionY, motionZ, MAX_SCALE, MAX_AGE, 0.0f, 0.3f, 0.8f);
  }

  public EntityParticleFXChaosTransfer(World world, double posX, double posY, double posZ,
      double motionX, double motionY, double motionZ, float scale, int maxAge, float red,
      float green, float blue) {

    super(world, posX, posY, posZ, 0.0, 0.0, 0.0);
    this.motionX = motionX;
    this.motionY = motionY;
    this.motionZ = motionZ;
    this.particleTextureIndexX = 4;
    this.particleTextureIndexY = 2;
    this.particleRed = red;
    this.particleGreen = green;
    this.particleBlue = blue;
    this.particleScale = scale;
    this.particleMaxAge = maxAge;
    this.noClip = true;
    this.particleGravity = 0.0f;
    this.particleAlpha = 0.2f;
  }

  @Override
  public void onUpdate() {

    if (this.particleAge++ >= this.particleMaxAge - 1) {
      this.setDead();
    }

    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;

    this.moveEntity(this.motionX, this.motionY, this.motionZ);

    this.particleScale -= MAX_SCALE / (MAX_AGE * 1.5f);
    this.particleAlpha += 0.8f * 1f / MAX_AGE;

    // Spawn trail particles
    if (SilentGems.proxy.getParticleSettings() == 0) {
      double mx = worldObj.rand.nextGaussian() * 0.025;
      double my = worldObj.rand.nextGaussian() * 0.025;
      double mz = worldObj.rand.nextGaussian() * 0.025;
      SilentGems.proxy.spawnParticles(ClientProxy.FX_CHAOS_TRAIL, worldObj, posX, posY, posZ, mx,
          my, mz);
    }
  }

  @Override
  public void renderParticle(Tessellator tess, float posX, float posY, float posZ, float par5,
      float par6, float par7) {

    float f6 = (float) this.particleTextureIndexX / 16.0F;
    float f7 = f6 + .25f;
    float f8 = (float) this.particleTextureIndexY / 16.0F;
    float f9 = f8 + .25f;
    float f10 = 0.1F * this.particleScale;

    if (this.particleIcon != null) {
      f6 = this.particleIcon.getMinU();
      f7 = this.particleIcon.getMaxU();
      f8 = this.particleIcon.getMinV();
      f9 = this.particleIcon.getMaxV();
    }

    float f11 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) posX - interpPosX);
    float f12 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) posX - interpPosY);
    float f13 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) posX - interpPosZ);
    tess.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue,
        this.particleAlpha);
    tess.addVertexWithUV((double) (f11 - posY * f10 - par6 * f10), (double) (f12 - posZ * f10),
        (double) (f13 - par5 * f10 - par7 * f10), (double) f7, (double) f9);
    tess.addVertexWithUV((double) (f11 - posY * f10 + par6 * f10), (double) (f12 + posZ * f10),
        (double) (f13 - par5 * f10 + par7 * f10), (double) f7, (double) f8);
    tess.addVertexWithUV((double) (f11 + posY * f10 + par6 * f10), (double) (f12 + posZ * f10),
        (double) (f13 + par5 * f10 + par7 * f10), (double) f6, (double) f8);
    tess.addVertexWithUV((double) (f11 + posY * f10 - par6 * f10), (double) (f12 - posZ * f10),
        (double) (f13 + par5 * f10 - par7 * f10), (double) f6, (double) f9);
  }
}
