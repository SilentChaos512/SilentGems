package net.silentchaos512.gems.event;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.entity.EntityChaosProjectile;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Greetings;
import net.silentchaos512.gems.lib.module.ModuleCoffee;
import net.silentchaos512.gems.lib.module.ModuleEntityRandomEquipment;
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
    } else if (event.crafting.getItem() instanceof IArmor) {
      ArmorHelper.setOriginalOwner(event.crafting, event.player);
    }
    // Transfer Chaos when upgrading an orb.
    else if (event.crafting.getItem() == ModItems.chaosOrb) {
      for (int i = 0; i < event.craftMatrix.getSizeInventory(); ++i) {
        ItemStack stack = event.craftMatrix.getStackInSlot(i);
        if (stack != null && stack.getItem() == ModItems.chaosOrb) {
          int oldCharge = ModItems.chaosOrb.getCharge(stack);
          ModItems.chaosOrb.receiveCharge(event.crafting, oldCharge, false);
        }
      }
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
        if (ToolHelper.getToolTier(mainHand).ordinal() >= EnumMaterialTier.SUPER.ordinal()
            && ToolHelper.isSpecialAbilityEnabled(mainHand)) {
          SkillAreaMiner.INSTANCE.onGetBreakSpeed(event);
        }
      } else if (mainHand.getItem() == ModItems.axe) {
        if (ToolHelper.getToolTier(mainHand).ordinal() >= EnumMaterialTier.SUPER.ordinal()
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
  public void onLifeSteal(LivingAttackEvent event) {

    if (event.getSource().getEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.getSource().getEntity();
      ItemStack mainHand = player.getHeldItemMainhand();
      ItemStack offHand = player.getHeldItemOffhand();
      int lifeStealLevel = 0;

      // Life Steal on main?
      if (mainHand != null)
        lifeStealLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.lifeSteal, mainHand);
      // If not, is it on off hand?
      if (lifeStealLevel < 1 && offHand != null)
        lifeStealLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.lifeSteal, offHand);

      // Do life steal?
      if (lifeStealLevel > 0) {
        float amount = Math.min(event.getAmount(), event.getEntityLiving().getHealth());
        float healAmount = ModEnchantments.lifeSteal.getAmountHealed(lifeStealLevel, amount);
        player.heal(healAmount);
      }
    }
  }

  @SubscribeEvent
  public void onLivingDeath(LivingDeathEvent event) {

    Entity entitySource = event.getSource().getSourceOfDamage();
    EntityPlayer player = null;

    if (entitySource instanceof EntityPlayer) {
      player = (EntityPlayer) entitySource;
    } else if (entitySource instanceof EntityChaosProjectile) {
      EntityChaosProjectile projectile = (EntityChaosProjectile) entitySource;
      EntityLivingBase shooter = projectile.getShooter();
      if (shooter instanceof EntityPlayer)
        player = (EntityPlayer) shooter;
    }

    if (player != null) {
      ItemStack weapon = player.getHeldItem(EnumHand.MAIN_HAND);
      if (weapon != null && weapon.getItem() instanceof ITool)
        ToolHelper.incrementStatKillCount(weapon, 1);
    }
  }

  @SubscribeEvent
  public void onLivingUpdate(LivingUpdateEvent event) {

    if (!event.getEntity().worldObj.isRemote && event.getEntityLiving() instanceof EntityRabbit) {
      EntityRabbit rabbit = (EntityRabbit) event.getEntityLiving();
      ModuleCoffee.tickRabbit(rabbit);
    }
  }

  @SubscribeEvent
  public void onEntityJoinWorld(EntityJoinWorldEvent event) {

    Entity entity = event.getEntity();
    if (entity instanceof EntityLivingBase)
      ModuleEntityRandomEquipment.tryGiveMobEquipment((EntityLivingBase) entity);
  }

  @SubscribeEvent
  public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {

    if (event.getModID().toLowerCase().equals(SilentGems.MOD_ID_LOWER)) {
      GemsConfig.load();
      GemsConfig.save();
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
