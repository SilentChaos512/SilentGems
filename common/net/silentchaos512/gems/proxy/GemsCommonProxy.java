package net.silentchaos512.gems.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.silentchaos512.gems.event.GemsCommonEvents;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.network.NetworkHandler;
import net.silentchaos512.lib.registry.SRegistry;

public class GemsCommonProxy extends net.silentchaos512.lib.proxy.CommonProxy {

  @Override
  public void preInit(SRegistry registry) {

    super.preInit(registry);

    NetworkHandler.init();

    MinecraftForge.EVENT_BUS.register(new PlayerDataHandler.EventHandler());
    MinecraftForge.EVENT_BUS.register(new GemsCommonEvents());
  }

  @Override
  public void init(SRegistry registry) {

    super.init(registry);
  }

  @Override
  public void postInit(SRegistry registry) {

    super.postInit(registry);
  }

  public void spawnParticles(EnumModParticles type, int color, World world, double x, double y,
      double z, double motionX, double motionY, double motionZ) {

  }

  public int getParticleSettings() {

    return 0;
  }

  public EntityPlayer getClientPlayer() {

    return null;
  }
}
