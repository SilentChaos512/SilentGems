package net.silentchaos512.gems.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.network.message.MessageDataSync;
import net.silentchaos512.gems.network.message.MessageItemRename;
import net.silentchaos512.gems.network.message.MessageSetFlight;
import net.silentchaos512.gems.network.message.MessageToggleChaosGem;
import net.silentchaos512.gems.network.message.MessageToggleSpecial;

public class NetworkHandler {

  public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE
      .newSimpleChannel(SilentGems.MOD_ID);

  private static int i = 0;

  public static void init() {

    register(MessageDataSync.class, Side.CLIENT);
    register(MessageToggleSpecial.class, Side.SERVER);
    register(MessageItemRename.class, Side.SERVER);
    register(MessageSetFlight.class, Side.CLIENT);
    register(MessageToggleChaosGem.class, Side.SERVER);
  }

  private static void register(Class clazz, Side handlerSide) {

    INSTANCE.registerMessage(clazz, clazz, i++, handlerSide);
  }
}
