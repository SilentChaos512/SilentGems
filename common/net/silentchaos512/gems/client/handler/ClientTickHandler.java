package net.silentchaos512.gems.client.handler;

import java.util.ArrayDeque;
import java.util.Queue;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.silentchaos512.gems.handler.PlayerDataHandler;

// Borrowed from Psi... Still much to learn.
// https://github.com/Vazkii/Psi/blob/master/src/main/java/vazkii/psi/client/core/handler/ClientTickHandler.java
public class ClientTickHandler {

  public static volatile Queue<Runnable> scheduledActions = new ArrayDeque();

  @SubscribeEvent
  public void clientTickEnd(ClientTickEvent event) {

    if (event.phase == Phase.END) {
      Minecraft mc = Minecraft.getMinecraft();
      if (mc.theWorld == null) {
        PlayerDataHandler.cleanup();
      } else if (mc.thePlayer != null) {
        while (!scheduledActions.isEmpty()) {
          scheduledActions.poll().run();
        }
      }

      //
    }
  }
}
