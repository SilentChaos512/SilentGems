package net.silentchaos512.gems.client.render.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.TexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;

public class ParticleCompass extends TexturedParticle {
    private static final int MAX_AGE = 5;
    private static final int MAX_SCALE = 1;

    private ParticleCompass(ClientWorld world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, float scale, int maxAge, float red, float green, float blue) {
        super(world, posX, posY, posZ, 0, 0, 0);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
//        this.particleTextureIndexX = 4;
//        this.particleTextureIndexY = 11;
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        // this.particleRed = rand.nextFloat();
        // this.particleGreen = rand.nextFloat();
        // this.particleBlue = rand.nextFloat();
        this.particleScale = scale;
        this.maxAge = maxAge;
        this.canCollide = false;
        this.particleGravity = 0.05f;
        this.particleAlpha = 0.9f;
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

//    this.particleScale *= 0.9f;
//    this.particleAlpha -= 0.8f / (particleMaxAge * 1.25f);
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        super.renderParticle(buffer, renderInfo, partialTicks);
/*        float uMin = (float) this.particleTextureIndexX / 16.0F;
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

        buffer
                .pos((double) (f5 - rotationX * f4 - rotationXY * f4), (double) (f6 - rotationZ * f4),
                        (double) (f7 - rotationYZ * f4 - rotationXZ * f4))
                .tex((double) uMax, (double) vMax)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(j, k).endVertex();
        buffer
                .pos((double) (f5 - rotationX * f4 + rotationXY * f4), (double) (f6 + rotationZ * f4),
                        (double) (f7 - rotationYZ * f4 + rotationXZ * f4))
                .tex((double) uMax, (double) vMin)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(j, k).endVertex();
        buffer
                .pos((double) (f5 + rotationX * f4 + rotationXY * f4), (double) (f6 + rotationZ * f4),
                        (double) (f7 + rotationYZ * f4 + rotationXZ * f4))
                .tex((double) uMin, (double) vMin)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(j, k).endVertex();
        buffer
                .pos((double) (f5 + rotationX * f4 - rotationXY * f4), (double) (f6 - rotationZ * f4),
                        (double) (f7 + rotationYZ * f4 - rotationXZ * f4))
                .tex((double) uMin, (double) vMax)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(j, k).endVertex();*/
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
