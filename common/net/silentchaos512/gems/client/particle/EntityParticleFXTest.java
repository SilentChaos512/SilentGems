package net.silentchaos512.gems.client.particle;

import net.minecraft.client.particle.EntityCritFX;
import net.minecraft.world.World;

public class EntityParticleFXTest extends EntityCritFX {

//  protected ResourceLocation particleTexture;

  public EntityParticleFXTest(World world, double posX, double posY, double posZ, double motionX,
      double motionY, double motionZ) {

    this(world, posX, posY, posZ, 0.0, 0.0, 0.0, 1.0f, 40);
  }

  public EntityParticleFXTest(World world, double posX, double posY, double posZ, double motionX,
      double motionY, double motionZ, float scale, int maxAge) {

    super(world, posX, posY, posZ, 0.0, 0.0, 0.0);
//    this.particleTexture = new ResourceLocation("silentgems:textures/particle/test.png");
    this.motionX = motionX;
    this.motionY = motionY;
    this.motionZ = motionZ;
//    this.particleTextureIndexX = 0;
//    this.particleTextureIndexY = 0;
    this.particleRed = 1.0f;
    this.particleGreen = 1.0f;
    this.particleBlue = 1.0f;
    this.particleScale = scale;
    this.particleMaxAge = maxAge;
    this.noClip = true;
    this.particleGravity = 0.0f;
  }

//  @Override
//  public void renderParticle(Tessellator tesselator, float par2, float par3, float par4,
//      float par5, float par6, float par7) {
//
////    super.renderParticle(tesselator, par2, par3, par4, par5, par6, par7);
//
//    tesselator.draw();
//    Minecraft.getMinecraft().getTextureManager().bindTexture(this.particleTexture);
//    tesselator.startDrawingQuads();
//    tesselator.setBrightness(200);
//    super.renderParticle(tesselator, par2, par3, par4, par5, par6, par7);
//    tesselator.draw();
//    Minecraft.getMinecraft().getTextureManager()
//        .bindTexture(new ResourceLocation("textures/particle/particles.png"));
//    tesselator.startDrawingQuads();
//  }
}
