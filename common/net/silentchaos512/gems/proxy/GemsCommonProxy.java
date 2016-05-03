package net.silentchaos512.gems.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.gui.GuiHandlerSilentGems;
import net.silentchaos512.gems.event.GemsCommonEvents;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.network.NetworkHandler;
import net.silentchaos512.lib.registry.SRegistry;
import net.silentchaos512.lib.util.Color;

public class GemsCommonProxy extends net.silentchaos512.lib.proxy.CommonProxy {

  @Override
  public void preInit(SRegistry registry) {

    super.preInit(registry);

    NetworkHandler.init();

    NetworkRegistry.INSTANCE.registerGuiHandler(SilentGems.instance, new GuiHandlerSilentGems());

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

  public void spawnParticles(EnumModParticles type, Color color, World world, double x, double y,
      double z, double motionX, double motionY, double motionZ) {

  }

  public int getParticleSettings() {

    return 0;
  }

  public EntityPlayer getClientPlayer() {

    return null;
  }

  public boolean isClientPlayerHoldingDebugItem() {

    EntityPlayer player = getClientPlayer();
    if (player == null) {
      return false;
    }

    ItemStack mainhand = player.getHeldItemMainhand();
    ItemStack offhand = player.getHeldItemOffhand();
    return (mainhand != null && mainhand.getItem() == ModItems.debugItem)
        || (offhand != null && offhand.getItem() == ModItems.debugItem);
  }
}
