package net.silentchaos512.gems.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.Skulls;
import net.silentchaos512.gems.entity.EntityChaosProjectile;
import net.silentchaos512.gems.entity.EntityChaosProjectileScatter;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModEnchantments;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ItemBlockPlacer;
import net.silentchaos512.gems.item.ItemSoulGem;
import net.silentchaos512.gems.item.ItemSoulGem.Soul;
import net.silentchaos512.gems.lib.Greetings;
import net.silentchaos512.gems.lib.module.ModuleCoffee;
import net.silentchaos512.gems.lib.module.ModuleEntityRandomEquipment;
import net.silentchaos512.gems.lib.soul.SoulSkill;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.loot.LootHandler;
import net.silentchaos512.gems.skills.ToolSkill;
import net.silentchaos512.gems.skills.ToolSkillDigger;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.PlayerHelper;

public class GemsCommonEvents {

  @SubscribeEvent
  public void onPlayerLoggedIn(PlayerLoggedInEvent event) {

    Greetings.greetPlayer(event.player);

    SilentGems.logHelper.info("Recalculating tool and armor stats for " + event.player.getDisplayNameString());
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

    // Set original owner, update model cache of tools/armor.
    if (event.crafting.getItem() instanceof ITool || event.crafting.getItem() instanceof IArmor) {
      ToolHelper.setOriginalOwner(event.crafting, event.player);
      ModItems.toolRenderHelper.updateModelCache(event.crafting);
    }
    // Transfer Chaos when upgrading an orb.
    else if (event.crafting.getItem() == ModItems.chaosOrb) {
      for (int i = 0; i < event.craftMatrix.getSizeInventory(); ++i) {
        ItemStack stack = event.craftMatrix.getStackInSlot(i);
        if (!stack.isEmpty() && stack.getItem() == ModItems.chaosOrb) {
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

    if (!mainHand.isEmpty()) {
      // Shears on Fluffy Blocks
      if (event.getState().getBlock() == ModBlocks.fluffyBlock) {
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
  public void onLivingAttack(LivingAttackEvent event) {

    if (event.getSource().getTrueSource() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
      ItemStack mainHand = player.getHeldItemMainhand();
      ItemStack offHand = player.getHeldItemOffhand();

      int lifeStealLevel = 0;
      int iceAspectLevel = 0;
      int lightningAspectLevel = 0;

      // Get levels of relevant enchantments.
      if (!mainHand.isEmpty()) {
        lifeStealLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.lifeSteal, mainHand);
        iceAspectLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.iceAspect, mainHand);
        lightningAspectLevel = EnchantmentHelper
            .getEnchantmentLevel(ModEnchantments.lightningAspect, mainHand);
      }
      // If not, is it on off hand?
      if (lifeStealLevel < 1 && !offHand.isEmpty()) {
        lifeStealLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.lifeSteal, offHand);
      }

      // Do life steal?
      if (lifeStealLevel > 0) {
        float amount = Math.min(event.getAmount(), event.getEntityLiving().getHealth());
        float healAmount = ModEnchantments.lifeSteal.getAmountHealed(lifeStealLevel, amount);
        player.heal(healAmount);
      }

      // Ice Aspect
      if (iceAspectLevel > 0) {
        ModEnchantments.iceAspect.applyTo(event.getEntityLiving(), iceAspectLevel);
      }

      // Lightning Aspect
      if (lightningAspectLevel > 0) {
        ModEnchantments.lightningAspect.applyTo(event.getEntityLiving(), lightningAspectLevel);
      }
    }
  }

  @SubscribeEvent
  public void onLivingDeath(LivingDeathEvent event) {

    Entity entitySource = event.getSource().getTrueSource();
    EntityPlayer player = null;

    if (event.getEntityLiving() instanceof EntityPlayer) {
      EntityPlayer deadPlayer = (EntityPlayer) event.getEntityLiving();
      PlayerData data = PlayerDataHandler.get(deadPlayer);
      data.haloTime = data.HALO_TIME_DEFAULT;
    }

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
      if (!weapon.isEmpty() && weapon.getItem() instanceof ITool)
        ToolHelper.incrementStatKillCount(weapon, 1);
    }
  }

  @SubscribeEvent
  public void onLivingUpdate(LivingUpdateEvent event) {

    EntityLivingBase entity = event.getEntityLiving();
    if (!entity.world.isRemote) {
      // Rabbit coffee
      if (entity instanceof EntityRabbit) {
        EntityRabbit rabbit = (EntityRabbit) event.getEntityLiving();
        ModuleCoffee.tickRabbit(rabbit);
      }
    }
  }

  @SubscribeEvent
  public void onLivingDrops(LivingDropsEvent event) {

    Entity entity = event.getSource().getTrueSource();

    if (entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entity;
      ItemStack weapon = player.getHeldItemMainhand();

      // Soul Gems
      EntityLivingBase killed = event.getEntityLiving();
      Soul soul = ModItems.soulGem.getSoul(killed.getClass());
      if (soul != null && SilentGems.random.nextFloat() < soul.getDropRate()) {
        ItemStack soulGem = ModItems.soulGem.getStack(soul);
        if (!soulGem.isEmpty()) {
          EntityItem entityItem = new EntityItem(killed.world, killed.posX,
              killed.posY + killed.height / 2f, killed.posZ, soulGem);
          event.getDrops().add(entityItem);
        }
      }

      ToolSoul toolSoul = SoulManager.getSoul(weapon);
      if (toolSoul != null) {
        // Head bonus?
        if (toolSoul.hasSkill(SoulSkill.HEAD_BONUS)) {
          int level = toolSoul.getSkillLevel(SoulSkill.HEAD_BONUS);
          float rate = Skulls.getDropRate(killed);
          if (SilentGems.random.nextFloat() < 1.5f * level * rate) {
            ItemStack skull = Skulls.getSkull(killed);
            if (!skull.isEmpty()) {
              EntityItem entityItem = new EntityItem(killed.world, killed.posX,
                  killed.posY + killed.height / 2f, killed.posZ, skull);
              event.getDrops().add(entityItem);
            }
          }
        }
      }
    }
  }

  @SubscribeEvent
  public void onBlockDrops(BlockEvent.HarvestDropsEvent event) {

    ItemSoulGem.Soul soul = ModItems.soulGem.getSoul(event);
    Block block = event.getState().getBlock();
    boolean isCrop = block instanceof BlockCrops;
    boolean isMature = isCrop && ((BlockCrops) block).isMaxAge(event.getState());
    if (soul != null && (!isCrop || (isCrop && isMature))) {
      float dropRate = soul.getDropRate() * (1f + 0.15f * event.getFortuneLevel());
      if (SilentGems.random.nextFloat() < dropRate) {
        event.getDrops().add(soul.getStack());
      }
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

    ItemStack entityStack = event.getItem().getItem();
    if (entityStack.getItem() instanceof ItemBlock) {
      for (ItemStack stack : PlayerHelper.getNonEmptyStacks(event.getEntityPlayer())) {
        if (stack.getItem() instanceof ItemBlockPlacer) {
          ItemBlockPlacer itemPlacer = (ItemBlockPlacer) stack.getItem();
          if (itemPlacer.getAutoFillMode(stack)) {
            IBlockState state = ((ItemBlock) entityStack.getItem()).getBlock()
                .getStateFromMeta(entityStack.getItemDamage());
            if (state.equals(itemPlacer.getBlockPlaced(stack))) {
              // Absorb blocks into block placer.
              int amountAbsorbed = itemPlacer.absorbBlocks(stack, entityStack);
              if (amountAbsorbed > 0) {
                entityStack.shrink(amountAbsorbed);
                if (entityStack.getCount() <= 0) {
                  event.getItem().setDead();
                }
                event.getEntityPlayer().world.playSound(null, event.getItem().getPosition(),
                    SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f,
                    (SilentGems.random.nextFloat() - SilentGems.random.nextFloat()) * 1.4F + 2.0F);
                break;
              }
            }
          }
        }
      }
    }
  }

  @SubscribeEvent
  public void onServerTickEnd(ServerTickEvent event) {

    if (event.phase == Phase.END) {
      EntityChaosProjectileScatter.DamageQueue.processDamage();
    }
  }
}
