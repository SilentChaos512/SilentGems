package net.silentchaos512.gems.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Greetings;
import net.silentchaos512.gems.skills.SkillAreaMiner;
import net.silentchaos512.gems.skills.SkillLumberjack;
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

  @SubscribeEvent
  public void onGetBreakSpeed(PlayerEvent.BreakSpeed event) {

    EntityPlayer player = event.getEntityPlayer();
    ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);

    if (mainHand != null) {
      // Shears on Fluffy Blocks
      if (event.getState() == ModBlocks.fluffyBlock) {
        ModBlocks.fluffyBlock.onGetBreakSpeed(event);
      }

      // TODO: Something for 'no speed penalty' tools?

      // Reduce speed for Area Miner and Lumberjack.
      if (mainHand.getItem() == ModItems.pickaxe || mainHand.getItem() == ModItems.shovel) {
        SkillAreaMiner.INSTANCE.onGetBreakSpeed(event);
      } else if (mainHand.getItem() == ModItems.axe) {
        SkillLumberjack.INSTANCE.onGetBreakSpeed(event);
      }
    }
  }
}
