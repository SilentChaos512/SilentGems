package net.silentchaos512.gems.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.enchantment.EnchantmentAOE;
import net.silentchaos512.gems.enchantment.ModEnchantments;

public class GemsForgeEventHandler {
  
  @SubscribeEvent
  public void onEntityConstructing(EntityConstructing event) {

    if (event.entity instanceof EntityPlayer
        && GemsExtendedPlayer.get((EntityPlayer) event.entity) == null) {
      GemsExtendedPlayer.register((EntityPlayer) event.entity);
    }

    if (event.entity instanceof EntityPlayer
        && event.entity.getExtendedProperties(GemsExtendedPlayer.PROPERTY_NAME) == null) {
      event.entity.registerExtendedProperties(GemsExtendedPlayer.PROPERTY_NAME,
          new GemsExtendedPlayer((EntityPlayer) event.entity));
    }
  }

  @SubscribeEvent
  public void onGetBreakSpeed(PlayerEvent.BreakSpeed event) {

    ItemStack heldItem = event.entityPlayer.getCurrentEquippedItem();
    if (heldItem != null) {
      // Reduce speed of Area Miner tools.
      int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.aoe.effectId, heldItem);
      if (level > 0) {
        event.newSpeed *= EnchantmentAOE.DIG_SPEED_MULTIPLIER;
      }
    }
  }
}
