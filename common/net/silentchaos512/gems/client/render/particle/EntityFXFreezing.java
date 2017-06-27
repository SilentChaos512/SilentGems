package net.silentchaos512.gems.client.render.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;


public class EntityFXFreezing extends Particle {

  public static final int MAX_AGE = 40;
  public static final int MAX_SCALE = 1;

  protected EntityFXFreezing(World world, double posX, double posY, double posZ) {

    this(world, posX, posY, posZ, 0, 0, 0, MAX_SCALE, MAX_AGE, 0.5f, 0.7f, 1.0f);
  }

  public EntityFXFreezing(World world, double posX, double posY, double posZ, double motionX,
      double motionY, double motionZ) {

    this(world, posX, posY, posZ, motionX, motionY, motionZ, MAX_SCALE, MAX_AGE, 0.5f, 0.7f, 1.0f);
  }

  public EntityFXFreezing(World world, double posX, double posY, double posZ, double motionX,
      double motionY, double motionZ, float scale, int maxAge, float red, float green, float blue) {

    super(world, posX, posY, posZ, 0, 0, 0);
    this.motionX = motionX;
    this.motionY = motionY;
    this.motionZ = motionZ;
    this.particleTextureIndexX = 7;
    this.particleTextureIndexY = 0; //11;
    this.particleRed = red;
    this.particleGreen = green;
    this.particleBlue = blue;
    this.particleScale = scale;
    this.particleMaxAge = maxAge;
    this.canCollide = false;
    //this.particleGravity = 0.05f;
    //this.particleAlpha = 0.9f;
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

    this.particleTextureIndexX = (int) (7 - 7 * particleAge / particleMaxAge);
    this.particleScale *= 0.95f;
    //this.particleAlpha -= 0.8f / (particleMaxAge * 1.25f);
  }

  @Override
  public void renderParticle(VertexBuffer buffer, Entity entityIn, float partialTicks,
      float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

    float f = 1.5f * ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge * 32.0F;
    f = MathHelper.clamp(f, 0.0F, 1.0F);
    this.particleScale = MAX_SCALE * f;
    super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
  }
}
