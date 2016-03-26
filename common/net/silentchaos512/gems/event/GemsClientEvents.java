package net.silentchaos512.gems.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.gems.handler.PlayerDataHandler;

public class GemsClientEvents {

//  @SubscribeEvent
//  public void onRenderGameOverlay(RenderGameOverlayEvent event) {
//
//    if (event.getType() != ElementType.TEXT) {
//      return;
//    }
//
//    FontRenderer fontRender = Minecraft.getMinecraft().fontRendererObj;
//    EntityPlayer player = Minecraft.getMinecraft().thePlayer;
//    int chaos = PlayerDataHandler.get(player).getCurrentChaos();
//    fontRender.drawStringWithShadow("" + chaos, 5, 5, 0xFFFFFF);
//  }
}
