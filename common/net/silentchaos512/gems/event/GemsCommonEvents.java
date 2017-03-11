package net.silentchaos512.gems.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.entity.EntityChaosProjectile;
import net.silentchaos512.gems.item.ItemBlockPlacer;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Greetings;
import net.silentchaos512.gems.lib.module.ModuleCoffee;
import net.silentchaos512.gems.lib.module.ModuleEntityRandomEquipment;
import net.silentchaos512.gems.loot.LootHandler;
import net.silentchaos512.gems.skills.ToolSkill;
import net.silentchaos512.gems.skills.ToolSkillDigger;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.PlayerHelper;
import net.silentchaos512.lib.util.StackHelper;

public class GemsCommonEvents {

  @SubscribeEvent
  public void onPlayerLoggedIn(PlayerLoggedInEvent event) {

    Greetings.greetPlayer(event.player);

    SilentGems.instance.logHelper
        .info("Recalculating tool and armor stats for " + event.player.getDisplayNameString());
    // Recalculate tool stats.
    for (ItemStack stack : PlayerHelper.getNonEmptyStacks(event.player)) {
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
        if (StackHelper.isValid(stack) && stack.getItem() == ModItems.chaosOrb) {
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

    if (StackHelper.isValid(mainHand)) {
      // Shears on Fluffy Blocks
      if (event.getState() == ModBlocks.fluffyBlock) {
        ModBlocks.fluffyBlock.onGetBreakSpeed(event);
      }

      // Gravity enchantment.
      int gravityLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.gravity, mainHand);
      if (gravityLevel > 0)
        ModEnchantments.gravity.onGetBreakSpeed(event, mainHand, gravityLevel);

      // Reduce speed for Area Miner and Lumberjack.
      ToolSkill skill = ToolHelper.getSuperSkill(mainHand);
      if (skill instanceof ToolSkillDigger && ToolHelper.isSpecialAbilityEnabled(mainHand))
        ((ToolSkillDigger) skill).onGetBreakSpeed(event);
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
      if (StackHelper.isValid(mainHand))
        lifeStealLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.lifeSteal, mainHand);
      // If not, is it on off hand?
      if (lifeStealLevel < 1 && StackHelper.isValid(offHand))
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
      if (StackHelper.isValid(weapon) && weapon.getItem() instanceof ITool)
        ToolHelper.incrementStatKillCount(weapon, 1);
    }
  }

  @SubscribeEvent
  public void onLivingUpdate(LivingUpdateEvent event) {

    if (!event.getEntity().world.isRemote && event.getEntityLiving() instanceof EntityRabbit) {
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
  public void onItemPickup(EntityItemPickupEvent event) {

    ItemStack entityStack = event.getItem().getEntityItem();
    if (entityStack.getItem() instanceof ItemBlock) {
      for (ItemStack stack : PlayerHelper.getNonEmptyStacks(event.getEntityPlayer())) {
        if (stack.getItem() instanceof ItemBlockPlacer) {
          ItemBlockPlacer itemPlacer = (ItemBlockPlacer) stack.getItem();
          IBlockState state = ((ItemBlock) entityStack.getItem()).block
              .getStateFromMeta(entityStack.getItemDamage());
          if (state.equals(itemPlacer.getBlockPlaced(stack))) {
            // TODO
            //event.getItem().setDead();
            break;
          }
        }
      }
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
