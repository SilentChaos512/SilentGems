package net.silentchaos512.gems.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.item.ModItems;

public class GemsClientEvents {

  public static final ResourceLocation TEXTURE_CROSSHAIRS = new ResourceLocation(SilentGems.MOD_ID,
      "textures/gui/Crosshairs.png");

  @SubscribeEvent
  public void onRenderGameOverlay(RenderGameOverlayEvent event) {

    renderCrosshairs(event);
  }

  private void renderCrosshairs(RenderGameOverlayEvent event) {

    EntityPlayer player = Minecraft.getMinecraft().thePlayer;

    if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ModItems.pickaxe) {
      if (event.isCancelable() && event.getType() == ElementType.CROSSHAIRS) {
        event.setCanceled(true);
      }
    }

    if (event.isCancelable() || event.getType() != ElementType.CROSSHAIRS) {
      return;
    }

    ;
  }
}
