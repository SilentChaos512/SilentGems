package net.silentchaos512.gems.event;

import java.util.Random;

import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.CoffeeModule;
import net.silentchaos512.gems.lib.Greetings;
import net.silentchaos512.gems.loot.LootHandler;
import net.silentchaos512.gems.skills.SkillAreaMiner;
import net.silentchaos512.gems.skills.SkillLumberjack;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.PlayerHelper;

public class GemsCommonEvents {

  @SubscribeEvent
  public void onPlayerLoggedIn(PlayerLoggedInEvent event) {

    Greetings.greetPlayer(event.player);

    SilentGems.instance.logHelper
        .info("Recalculating tool and armor stats for " + event.player.getDisplayNameString());
    // Recalculate tool stats.
    for (ItemStack stack : PlayerHelper.getNonNullStacks(event.player)) {
      if (stack != null) {
        if (stack.getItem() instanceof ITool)
          ToolHelper.recalculateStats(stack);
        if (stack.getItem() instanceof IArmor)
          ArmorHelper.recalculateStats(stack);
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
        if (ToolHelper.getToolTier(mainHand) == EnumMaterialTier.SUPER
            && ToolHelper.isSpecialAbilityEnabled(mainHand)) {
          SkillAreaMiner.INSTANCE.onGetBreakSpeed(event);
        }
      } else if (mainHand.getItem() == ModItems.axe) {
        if (ToolHelper.getToolTier(mainHand) == EnumMaterialTier.SUPER
            && ToolHelper.isSpecialAbilityEnabled(mainHand)) {
          SkillLumberjack.INSTANCE.onGetBreakSpeed(event);
        }
      }
    }
  }

  @SubscribeEvent
  public void onLootLoad(LootTableLoadEvent event) {

    LootHandler.init(event);
  }

  @SubscribeEvent
  public void onLivingHurt(LivingHurtEvent event) {

  }

  @SubscribeEvent
  public void onLivingUpdate(LivingUpdateEvent event) {

    if (!event.getEntity().worldObj.isRemote && event.getEntityLiving() instanceof EntityRabbit) {
      EntityRabbit rabbit = (EntityRabbit) event.getEntityLiving();
      CoffeeModule.tickRabbit(rabbit);
    }
  }

  // @SubscribeEvent
  // public void onExplosionDetonate(ExplosionEvent.Detonate event) {
  //
//    // @formatter:off
//    if (event.getAffectedBlocks().removeIf(pos ->
//        event.getWorld().getBlockState(pos) == ModBlocks.gemBlockSuper
//        || event.getWorld().getBlockState(pos) == ModBlocks.gemBlockSuperDark)) {
//      SilentGems.instance.logHelper.derp();
//    }
//    // @formatter:on
  // }
}
