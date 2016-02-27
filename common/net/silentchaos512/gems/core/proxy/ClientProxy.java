package net.silentchaos512.gems.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.client.particle.EntityFXChaosCharge;
import net.silentchaos512.gems.client.particle.EntityFXChaosTrail;
import net.silentchaos512.gems.client.particle.EntityParticleFXChaosTransfer;
import net.silentchaos512.gems.client.render.ModRenderers;
import net.silentchaos512.gems.client.render.entity.RenderProjectileChaosOrb;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.entity.projectile.EntityProjectileChaosOrb;

public class ClientProxy extends CommonProxy {

  @Override
  public void preInit() {

    super.preInit();
    SRegistry.clientPreInit();
    FMLInterModComms.sendMessage("IGWMod", "net.silentchaos512.gems.compat.igw.IGWHandler", "init");
  }

  @Override
  public void init() {

    super.init();
    SRegistry.clientInit();
  }

  @Override
  public void registerRenderers() {

    registerRenderersBlocks();
    registerRenderersItems();
    registerRenderersMobs();
    registerRenderersProjectiles();

    // added by M4thG33k
    ModRenderers.init();
  }

  private void registerRenderersProjectiles() {

    RenderingRegistry.registerEntityRenderingHandler(EntityProjectileChaosOrb.class,
        new RenderProjectileChaosOrb());
  }

  private void registerRenderersMobs() {

  }

  private void registerRenderersItems() {

  }

  private void registerRenderersBlocks() {

  }

  @Override
  public void registerKeyHandlers() {

    KeyTracker.init();
  }

  public static final String FX_CHAOS_TRANSFER = "chaosTransfer";
  public static final String FX_CHAOS_NO_ALTAR = "chaosNoAltar";
  public static final String FX_CHAOS_CHARGE = "chaosCharge";
  public static final String FX_CHAOS_TRAIL = "chaosTrail";

  @Override
  public void spawnParticles(String type, World world, double x, double y, double z, double motionX,
      double motionY, double motionZ) {

    spawnParticles(type, -1, world, x, y, z, motionX, motionY, motionZ);
  }

  @Override
  public void spawnParticles(String type, int color, World world, double x, double y, double z,
      double motionX, double motionY, double motionZ) {

    EntityFX particleFX = null;
    float red = (color >> 16 & 255) / 255f;
    float green = (color >> 8 & 255) / 255f;
    float blue = (color & 255) / 255f;

    if (type.equals(FX_CHAOS_TRANSFER)) {
      particleFX = new EntityParticleFXChaosTransfer(world, x, y, z, motionX, motionY, motionZ);
    } else if (type.equals(FX_CHAOS_NO_ALTAR)) {
      // Chaos transfer (no target)
      particleFX = new EntityParticleFXChaosTransfer(world, x, y, z, motionX, motionY, motionZ,
          EntityParticleFXChaosTransfer.MAX_SCALE, EntityParticleFXChaosTransfer.MAX_AGE, 1.0f,
          0.0f, 0.0f);
    } else if (type.equals(FX_CHAOS_CHARGE)) {
      // Chaos sword charging up
      particleFX = new EntityFXChaosCharge(world, x, y, z, motionX, motionY, motionZ,
          EntityFXChaosCharge.MAX_SCALE, EntityFXChaosCharge.MAX_AGE, red, green, blue);
    } else if (type.equals(FX_CHAOS_TRAIL)) {
      // Chaos trails
      if (color > -1) {
        // Chaos orb
        particleFX = new EntityFXChaosTrail(world, x, y, z, motionX, motionY, motionZ,
            EntityFXChaosTrail.MAX_SCALE * 1.5f, EntityFXChaosTrail.MAX_AGE * 4, red, green, blue);
      } else {
        // Pylon transfer to target
        particleFX = new EntityFXChaosTrail(world, x, y, z, motionX, motionY, motionZ);
      }
    }

    if (particleFX != null) {
      Minecraft.getMinecraft().effectRenderer.addEffect(particleFX);
    }
  }

  @Override
  public int getParticleSettings() {

    return Minecraft.getMinecraft().gameSettings.particleSetting;
  }
}
