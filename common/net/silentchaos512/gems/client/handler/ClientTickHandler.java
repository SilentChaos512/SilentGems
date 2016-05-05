package net.silentchaos512.gems.client.handler;

import java.util.ArrayDeque;
import java.util.Queue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.tile.TileChaosNode;
import net.silentchaos512.lib.util.Color;

// Borrowed from Psi... Still much to learn.
// https://github.com/Vazkii/Psi/blob/master/src/main/java/vazkii/psi/client/core/handler/ClientTickHandler.java
public class ClientTickHandler {

  public static volatile Queue<Runnable> scheduledActions = new ArrayDeque();

  public static int ticksInGame = 0;
  public static float partialTicks = 0f;
  public static float delta = 0f;
  public static float total = 0f;

  public static float fovModifier;

  public static Color nodeMoverColor = TileChaosNode
      .selectParticleColor(SilentGems.instance.random);

  private void calcDelta() {

    float oldTotal = total;
    total = ticksInGame + partialTicks;
    delta = total - oldTotal;
  }

  @SubscribeEvent
  public void renderTick(RenderTickEvent event) {

    if (event.phase == Phase.START) {
      partialTicks = event.renderTickTime;

      EntityPlayer player = Minecraft.getMinecraft().thePlayer;
      if (player != null) {
        ItemStack heldItem = player.getHeldItemMainhand();
        if (heldItem != null && heldItem.isItemEqual(ModItems.craftingMaterial.magnifyingGlass)) {
          if (fovModifier < 30f) {
            fovModifier += partialTicks / 3;
          }
        } else if (fovModifier > 0f) {
          fovModifier = Math.max(fovModifier - partialTicks / 3, 0f);
        }
      }
    }
  }

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

      // HUD?

      GuiScreen gui = mc.currentScreen;
      if (gui == null || !gui.doesGuiPauseGame()) {
        ++ticksInGame;
        partialTicks = 0;

        if (ticksInGame % 7 == 0) {
          nodeMoverColor = TileChaosNode.selectParticleColor(SilentGems.instance.random);
        }
      }

      calcDelta();
    }
  }
}
