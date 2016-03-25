package net.silentchaos512.gems.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.lib.Greetings;
import net.silentchaos512.gems.util.ToolHelper;

public class GemsCommonEvents {

  @SubscribeEvent
  public void onPlayerLoggedIn(PlayerLoggedInEvent event) {

    Greetings.greetPlayer(event.player);

    SilentGems.instance.logHelper
        .info("Recalculating tool stats for " + event.player.getDisplayNameString());
    // Recalculate tool stats.
    for (ItemStack stack : event.player.inventory.mainInventory) {
      if (stack != null && stack.getItem() instanceof ITool) {
        ToolHelper.recalculateStats(stack);
      }
    }
  }

  @SubscribeEvent
  public void onItemCrafted(ItemCraftedEvent event) {

    if (event.crafting.getItem() instanceof ITool) {
      ToolHelper.setOriginalOwner(event.crafting, event.player);
    }
  }
}
