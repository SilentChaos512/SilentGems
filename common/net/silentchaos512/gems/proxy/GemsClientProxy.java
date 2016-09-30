package net.silentchaos512.gems.proxy;

import org.apache.commons.lang3.NotImplementedException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.fx.FXChaos;
import net.silentchaos512.gems.client.gui.GuiChaosBar;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.client.render.ModBlockRenderers;
import net.silentchaos512.gems.client.render.entity.RenderChaosProjectile;
import net.silentchaos512.gems.client.render.entity.RenderEntityPacket;
import net.silentchaos512.gems.client.render.entity.RenderThrownTomahawk;
import net.silentchaos512.gems.client.render.particle.EntityFXCompass;
import net.silentchaos512.gems.entity.EntityChaosProjectile;
import net.silentchaos512.gems.entity.EntityThrownTomahawk;
import net.silentchaos512.gems.entity.packet.EntityChaosNodePacket;
import net.silentchaos512.gems.event.GemsClientEvents;
import net.silentchaos512.gems.guide.GuideSilentGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.ColorHandlers;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.lib.registry.SRegistry;
import net.silentchaos512.lib.util.Color;

public class GemsClientProxy extends net.silentchaos512.gems.proxy.GemsCommonProxy {

  @Override
  public void preInit(SRegistry registry) {

    super.preInit(registry);
    OBJLoader.INSTANCE.addDomain(SilentGems.MOD_ID);
    MinecraftForge.EVENT_BUS.register(KeyTracker.INSTANCE);
    MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
    MinecraftForge.EVENT_BUS.register(new GemsClientEvents());
    MinecraftForge.EVENT_BUS.register(GuiChaosBar.INSTANCE);
    MinecraftForge.EVENT_BUS.register(ModItems.toolRenderHelper);
    registry.clientPreInit();
    ModBlockRenderers.init(SilentGems.instance.registry);
    ModItems.enchantmentToken.setColorsForDefaultTokens();
  }

  @Override
  public void init(SRegistry registry) {

    super.init(registry);
    registry.clientInit();
    registerRenderers();
    registerColorHandlers();
    EntityChaosNodePacket.initColors();
    if (Loader.isModLoaded("guideapi"))
      GuideSilentGems.registerGuideBookModel();
  }

  @Override
  public void postInit(SRegistry registry) {

    super.postInit(registry);
    registry.clientPostInit();
  }

  private void registerRenderers() {

    SRegistry reg = SilentGems.instance.registry;

    // ModBlockRenderers.init(reg);

    reg.registerEntityRenderer(EntityChaosNodePacket.class, new RenderEntityPacket());
    reg.registerEntityRenderer(EntityChaosProjectile.class, new RenderChaosProjectile());
    reg.registerEntityRenderer(EntityThrownTomahawk.class, new RenderThrownTomahawk());
  }

  private void registerColorHandlers() {

    ColorHandlers.init();
  }

  // Particles

  @Override
  public void spawnParticles(EnumModParticles type, Color color, World world, double x, double y,
      double z, double motionX, double motionY, double motionZ) {

    Particle fx = null;

    float r = color.getRed();
    float g = color.getGreen();
    float b = color.getBlue();

    switch (type) {
      case CHAOS:
//        fx = new EntityFXChaos(world, x, y, z, motionX, motionY, motionZ, 2.0f, 25, r, g, b);
        fx = new FXChaos(world, x, y, z, .2f, r, g, b, 25).setSpeed(motionX, motionY, motionZ);
        break;
      case CHAOS_PROJECTILE_BODY:
//        fx = new EntityFXChaos(world, x, y, z, 0f, 0f, 0f, 3.0f, 1, r, g, b);
        fx = new FXChaos(world, x, y, z, .3f, r, g, b, 2);
        break;
      case CHAOS_PACKET_HEAD:
//        fx = new EntityFXChaos(world, x, y, z, motionX, motionY, motionZ, 2.0f, 0, r, g, b);
        fx = new FXChaos(world, x, y, z, .2f, r, g, b, 1).setSpeed(motionX, motionY, motionZ);
        break;
      case CHAOS_PACKET_TAIL:
//        fx = new EntityFXChaos(world, x, y, z, motionX, motionY, motionZ, 1.0f, 25, r, g, b);
        fx = new FXChaos(world, x, y, z, .1f, r, g, b, 15).setSpeed(motionX, motionY, motionZ);
        break;
      case CHAOS_NODE:
//        fx = new EntityFXChaos(world, x, y, z, motionX, motionY, motionZ, 3.0f, 20, r, g, b);
        fx = new FXChaos(world, x, y, z, .3f, r, g, b, 20).setSpeed(motionX, motionY, motionZ);
        break;
      case PHANTOM_LIGHT:
//        fx = new EntityFXChaos(world, x, y, z, motionX, motionY, motionZ, 1.0f, 15, r, g, b);
        fx = new FXChaos(world, x, y, z, .1f, r, g, b, 15).setSpeed(motionX, motionY, motionZ);
        break;
      case DRAWING_COMPASS:
        fx = new EntityFXCompass(world, x, y, z, motionX, motionY, motionZ, 1.0f, 10, r, g, b);
        break;
      default:
        throw new NotImplementedException("Unknown particle type: " + type);
    }

    if (fx != null) {
      Minecraft.getMinecraft().effectRenderer.addEffect(fx);
    }
  }

  @Override
  public int getParticleSettings() {

    return Minecraft.getMinecraft().gameSettings.particleSetting;
  }

  @Override
  public EntityPlayer getClientPlayer() {

    return Minecraft.getMinecraft().thePlayer;
  }
}
