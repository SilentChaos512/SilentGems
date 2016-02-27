package net.silentchaos512.gems.compat.igw;

import igwmod.api.BlockWikiEvent;
import igwmod.api.WikiRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.core.util.LogHelper;

public class IGWHandler {

  private static IGWHandler instance;

  public static void init() {

    instance = new IGWHandler();
    MinecraftForge.EVENT_BUS.register(instance);
    WikiRegistry.registerWikiTab(new GemsWikiTab());
  }

  @SubscribeEvent
  public void onBlockWikiEvent(BlockWikiEvent event) {

    if (event.blockState.getBlock() instanceof IHasVariants) {
      String name = ((IHasVariants) event.blockState.getBlock()).getName();
      event.pageOpened = "silentgems:block/" + name;
    }
  }
}
