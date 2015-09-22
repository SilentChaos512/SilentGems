package net.silentchaos512.gems.client.particle;

import net.minecraft.client.particle.EntityCritFX;
import net.minecraft.world.World;

public class EntityParticleFXChaosTransfer extends EntityCritFX {

  public EntityParticleFXChaosTransfer(World world, double posX, double posY, double posZ) {
    
    this(world, posX, posY, posZ, 0.0001, 0.0001, 0.0001, 1.0f, 10, 0.0f, 0.2f, 0.8f);
  }

  public EntityParticleFXChaosTransfer(World world, double posX, double posY, double posZ, double motionX,
      double motionY, double motionZ) {

    this(world, posX, posY, posZ, motionX, motionY, motionZ, 1.0f, 10, 0.0f, 0.2f, 0.8f);
  }

  public EntityParticleFXChaosTransfer(World world, double posX, double posY, double posZ, double motionX,
      double motionY, double motionZ, float scale, int maxAge, float red, float green, float blue) {

    super(world, posX, posY, posZ, 0.0, 0.0, 0.0);
    // this.particleTexture = new ResourceLocation("silentgems:textures/particle/test.png");
    this.motionX = motionX;
    this.motionY = motionY;
    this.motionZ = motionZ;
    // this.particleTextureIndexX = 0;
    // this.particleTextureIndexY = 0;
    this.particleRed = red;
    this.particleGreen = green;
    this.particleBlue = blue;
    this.particleScale = scale;
    this.particleMaxAge = maxAge;
    this.noClip = true;
    this.particleGravity = 0.0f;
  }

  @Override
  public void onUpdate() {

    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;

    if (this.particleAge++ >= this.particleMaxAge) {
      this.setDead();
    }
    
    this.moveEntity(this.motionX, this.motionY, this.motionZ);
    this.motionX *= 1.05;
    this.motionY *= 1.05;
    this.motionZ *= 1.05;
    
    this.particleAlpha *= 0.75f;
  }

  // @Override
  // public void renderParticle(Tessellator tesselator, float par2, float par3, float par4,
  // float par5, float par6, float par7) {
  //
  // // super.renderParticle(tesselator, par2, par3, par4, par5, par6, par7);
  //
  // tesselator.draw();
  // Minecraft.getMinecraft().getTextureManager().bindTexture(this.particleTexture);
  // tesselator.startDrawingQuads();
  // tesselator.setBrightness(200);
  // super.renderParticle(tesselator, par2, par3, par4, par5, par6, par7);
  // tesselator.draw();
  // Minecraft.getMinecraft().getTextureManager()
  // .bindTexture(new ResourceLocation("textures/particle/particles.png"));
  // tesselator.startDrawingQuads();
  // }
}
