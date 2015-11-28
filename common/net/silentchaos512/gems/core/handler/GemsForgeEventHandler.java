package net.silentchaos512.gems.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.InventoryHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.enchantment.EnchantmentAOE;
import net.silentchaos512.gems.enchantment.EnchantmentLumberjack;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.material.ModMaterials;

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
  public void onEntityJoinWorld(EntityJoinWorldEvent event) {

    if (event.entity instanceof EntityPlayer) {
      // Check for gem tools and update to the new NBT.
      EntityPlayer player = (EntityPlayer) event.entity;
      for (ItemStack stack : player.inventory.mainInventory) {
        if (InventoryHelper.isGemTool(stack)) {
          if (ToolHelper.convertToNewNBT(stack)) {
            String str = "Updated %s's %s to the new NBT system.";
            str = String.format(str, player.getCommandSenderName(), stack.getDisplayName());
            LogHelper.info(str);
          }
        }
      }
    }
  }

  @SubscribeEvent
  public void onGetBreakSpeed(PlayerEvent.BreakSpeed event) {

    ItemStack heldItem = event.entityPlayer.getCurrentEquippedItem();
    EntityPlayer player = event.entityPlayer;

    if (heldItem != null) {
      // Shears on Fluffy Blocks
      if (heldItem.getItem() instanceof ItemShears) {
        int efficiency = EnchantmentHelper.getEfficiencyModifier(player);
        if (event.block == ModBlocks.fluffyBlock) {
          event.newSpeed *= 4;
          if (efficiency > 0) {
            event.newSpeed += (efficiency * efficiency + 1);
          }
        }
      }

      // Chaos Tools: No penalty for mining while flying.
      if (player.capabilities.isFlying && InventoryHelper.isGemTool(heldItem)) {
        if (ToolHelper.getToolGemId(heldItem) == ModMaterials.CHAOS_GEM_ID) {
          event.newSpeed *= 5;
        }
      }

      // Reduce speed of Area Miner and Lumberjack tools.
      int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.aoe.effectId, heldItem);
      if (level > 0 && !player.isSneaking()) {
        event.newSpeed *= EnchantmentAOE.DIG_SPEED_MULTIPLIER;
      }
      level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.lumberjack.effectId, heldItem);
      if (level > 0 && !player.isSneaking()) {
        boolean isWood = event.block.isWood(player.worldObj, event.x, event.y, event.z);
        if (isWood && EnchantmentLumberjack.detectTree(player.worldObj, event.x, event.y, event.z,
            event.block)) {
          event.newSpeed *= EnchantmentLumberjack.DIG_SPEED_MULTIPLIER;
        }
      }
    }
  }

  @SubscribeEvent
  public void onLivingAttack(LivingAttackEvent event) {

    if (event.source == DamageSource.fall) {
      EntityLivingBase entityLivingBase = event.entityLiving;
      ItemStack[] lastActiveItems = entityLivingBase.getLastActiveItems();
      Item fluffyBoots = SRegistry.getItem("CottonBoots");
      for (ItemStack stack : lastActiveItems) {
        if (stack != null && stack.getItem() == fluffyBoots) {
          // Reduce/negate fall damage
          float newDamage = Math.max(0, event.ammount - Config.FLUFFY_BOOTS_DAMAGE_REDUCTION);
          event.setCanceled(true);
          if (newDamage > 0) {
            // We create a new damage source instead of using DamageSource.fall so this code isn't called again.
            entityLivingBase.attackEntityFrom(new DamageSource("fall").setDamageBypassesArmor(),
                newDamage);
          }
          // Damage/destroy boots
          int damageToBoots = Math.min(Config.FLUFFY_BOOTS_DAMAGE_TAKEN, (int) (event.ammount / 2));
          if (stack.attemptDamageItem(damageToBoots, SilentGems.instance.random)) {
            entityLivingBase.renderBrokenItemStack(stack); // Does nothing?
            entityLivingBase.playSound("random.break", 1f, 1f); // Does nothing?
            entityLivingBase.setCurrentItemOrArmor(1, null);
          }
        }
      }
    }
  }
}
