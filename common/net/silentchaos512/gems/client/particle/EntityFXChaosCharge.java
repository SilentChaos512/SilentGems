package net.silentchaos512.gems.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.Strings;

public class EntityFXChaosCharge extends EntityFX {

  public static final IIcon PARTICLE_ICON = new IconParticle(
      Strings.RESOURCE_PREFIX + "textures/effects/DiamondHollow.png", 16, 16);

  public EntityFXChaosCharge(World world, double posX, double posY, double posZ, double motionX,
      double motionY, double motionZ) {

    super(world, posX, posY, posZ, motionX, motionY, motionZ);
    particleIcon = PARTICLE_ICON; // Why does this not work?
  }

  @Override
  public void renderParticle(Tessellator tesselator, float par2, float par3, float par4, float par5,
      float par6, float par7) {

    super.renderParticle(tesselator, par2, par3, par4, par5, par6, par7);
  }
}
