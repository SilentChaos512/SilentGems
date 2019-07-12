package net.silentchaos512.gems.client.render.particle;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.TexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleShocking extends TexturedParticle {
    private static final int MAX_AGE = 10;
    private static final int MAX_SCALE = 1;

    private ParticleShocking(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, float scale, int maxAge, float red, float green, float blue) {
        super(world, posX, posY, posZ, 0, 0, 0);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
//        this.particleTextureIndexX = 7;
//        this.particleTextureIndexY = 10;
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        this.particleScale = scale;
        this.maxAge = maxAge;
        this.canCollide = false;
        this.particleGravity = 0.5f;
        // this.particleAlpha = 0.9f;
    }

    @Override
    public void tick() {
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.move(this.motionX, this.motionY, this.motionZ);

        motionY -= particleGravity / 20f;

//        this.particleTextureIndexX = 7 - 7 * age / maxAge;
        this.particleScale *= 0.95f;
        // this.particleAlpha -= 0.8f / (particleMaxAge * 1.25f);
    }

    @Override
    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float f = 1.5f * ((float) this.age + partialTicks) / (float) this.maxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        this.particleScale = MAX_SCALE * f;
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    @Override
    protected float getMinU() {
        return 0;
    }

    @Override
    protected float getMaxU() {
        return 1;
    }

    @Override
    protected float getMinV() {
        return 0;
    }

    @Override
    protected float getMaxV() {
        return 1;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }
}
