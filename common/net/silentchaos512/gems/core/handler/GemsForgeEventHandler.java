package net.silentchaos512.gems.core.handler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
            str = String.format(str, player.getName(), stack.getDisplayName());
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
        IBlockState state = event.state;
        Block block = state.getBlock();
        boolean isWood = block.isWood(event.entity.worldObj, event.pos);
        final int x = event.pos.getX();
        final int y = event.pos.getY();
        final int z = event.pos.getZ();
        if (isWood && EnchantmentLumberjack.detectTree(player.worldObj, x, y, z, block)) {
          event.newSpeed *= EnchantmentLumberjack.DIG_SPEED_MULTIPLIER;
        }
      }
    }
  }
}
