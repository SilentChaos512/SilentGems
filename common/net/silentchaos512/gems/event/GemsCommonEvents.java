package net.silentchaos512.gems.event;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.Skulls;
import net.silentchaos512.gems.enchantment.EnchantmentIceAspect;
import net.silentchaos512.gems.enchantment.EnchantmentLightningAspect;
import net.silentchaos512.gems.entity.EntityChaosProjectile;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModEnchantments;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ItemBlockPlacer;
import net.silentchaos512.gems.item.ItemSoulGem;
import net.silentchaos512.gems.item.ItemSoulGem.Soul;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.lib.Greetings;
import net.silentchaos512.gems.lib.module.ModuleCoffee;
import net.silentchaos512.gems.lib.module.ModuleEntityRandomEquipment;
import net.silentchaos512.gems.lib.soul.SoulSkill;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.loot.LootHandler;
import net.silentchaos512.gems.skills.ToolSkill;
import net.silentchaos512.gems.skills.ToolSkillDigger;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.gems.util.ModDamageSource;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.Color;
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

    // Set original owner, update model cache of tools/armor.
    if (event.crafting.getItem() instanceof ITool || event.crafting.getItem() instanceof IArmor) {
      ToolHelper.setOriginalOwner(event.crafting, event.player);
      ModItems.toolRenderHelper.updateModelCache(event.crafting);
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
  public void onLivingAttack(LivingAttackEvent event) {

    if (event.getSource().getTrueSource() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
      ItemStack mainHand = player.getHeldItemMainhand();
      ItemStack offHand = player.getHeldItemOffhand();

      int lifeStealLevel = 0;
      int iceAspectLevel = 0;
      int lightningAspectLevel = 0;

      // Get levels of relevant enchantments.
      if (StackHelper.isValid(mainHand)) {
        lifeStealLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.lifeSteal, mainHand);
        iceAspectLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.iceAspect, mainHand);
        lightningAspectLevel = EnchantmentHelper
            .getEnchantmentLevel(ModEnchantments.lightningAspect, mainHand);
      }
      // If not, is it on off hand?
      if (lifeStealLevel < 1 && StackHelper.isValid(offHand)) {
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
      if (StackHelper.isValid(weapon) && weapon.getItem() instanceof ITool)
        ToolHelper.incrementStatKillCount(weapon, 1);

      // Soul Gems
      EntityLivingBase killed = event.getEntityLiving();
      Soul soul = ModItems.soulGem.getSoul(killed.getClass());
      if (soul != null && SilentGems.random.nextFloat() < soul.getDropRate()) {
        ItemStack soulGem = ModItems.soulGem.getStack(soul);
        if (StackHelper.isValid(soulGem)) {
          EntityItem entityItem = new EntityItem(killed.world, killed.posX,
              killed.posY + killed.height / 2f, killed.posZ, soulGem);
          killed.world.spawnEntity(entityItem);
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
            if (StackHelper.isValid(skull)) {
              EntityItem entityItem = new EntityItem(killed.world, killed.posX,
                  killed.posY + killed.height / 2f, killed.posZ, skull);
              killed.world.spawnEntity(entityItem);
            }
          }
        }
      }
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

      NBTTagCompound tags = entity.getEntityData();
      if (tags != null) {
        // Ice Aspect?
        int iceTimer = tags.getInteger(EnchantmentIceAspect.EFFECT_NBT);
        if (iceTimer > 0) {
          // Mob has been chilled by Ice Aspect.
          tags.setInteger(EnchantmentIceAspect.EFFECT_NBT, --iceTimer);

          // Continuous freeze damage.
          int damageDelay = EnchantmentIceAspect.CONTINUOUS_DAMAGE_DELAY;
          int damageAmount = EnchantmentIceAspect.CONTINUOUS_DAMAGE_AMOUNT;
          if (entity.isImmuneToFire()) {
            // Extra damage for mobs immune to fire (like blazes)
            damageDelay /= 4;
          }
          if (iceTimer % damageDelay == 0) {
            entity.attackEntityFrom(ModDamageSource.FREEZING, damageAmount);
          }

          // Spawn freeze effect particles.
          Random rand = SilentGems.random;
          for (int i = 0; i < 2; ++i) {
            double posX = entity.posX + 1.2f * (rand.nextFloat() - 0.5f) * entity.width;
            double posY = entity.posY + 1.1f * rand.nextFloat() * entity.height;
            double posZ = entity.posZ + 1.2f * (rand.nextFloat() - 0.5f) * entity.width;
            double motionX = 0.005 * rand.nextGaussian();
            double motionY = 0.005 * rand.nextGaussian();
            double motionZ = 0.005 * rand.nextGaussian();
            SilentGems.proxy.spawnParticles(EnumModParticles.FREEZING, new Color(0x76e3f2),
                entity.world, posX, posY, posZ, motionX, motionY, motionZ);
          }
        }

        // Lightning Aspect
        int shockTimer = tags.getInteger(EnchantmentLightningAspect.EFFECT_NBT);
        if (shockTimer > 0) {
          // Mob has been electrified by Lightning Aspect.
          tags.setInteger(EnchantmentLightningAspect.EFFECT_NBT, --shockTimer);

          // Chain to nearby mobs. Half this mob's time and effect level 1.
          int halfTime = shockTimer / 2;
          if (shockTimer % EnchantmentLightningAspect.CHAIN_DELAY == 0 && halfTime > 0) {
            //@formatter:off
            for (EntityLivingBase nearby : entity.world.getEntities(EntityLivingBase.class,
                e -> e.getDistanceSqToEntity(entity) < EnchantmentLightningAspect.CHAIN_RADIUS_SQUARED
                    && !(e instanceof EntityPlayer))) {
              //@formatter:on
              NBTTagCompound nearbyTags = nearby.getEntityData();
              if (nearbyTags != null) {
                int nearbyShockTime = nearbyTags.getInteger(EnchantmentLightningAspect.EFFECT_NBT);
                if (nearbyShockTime <= 0) {
                  ModEnchantments.lightningAspect.applyTo(nearby, 1, halfTime);
                }
              }
            }
          }

          // Continuous shock damage.
          int damageDelay = EnchantmentLightningAspect.CONTINUOUS_DAMAGE_DELAY;
          int damageAmount = EnchantmentLightningAspect.CONTINUOUS_DAMAGE_AMOUNT;
          if (shockTimer % damageDelay == 0) {
            entity.attackEntityFrom(ModDamageSource.SHOCKING, damageAmount);
          }

          // Spawn shock effect particles.
          Random rand = SilentGems.random;
          for (int i = 0; i < 2; ++i) {
            double posX = entity.posX + 1.2f * (rand.nextFloat() - 0.5f) * entity.width;
            double posY = entity.posY + 1.1f * rand.nextFloat() * entity.height;
            double posZ = entity.posZ + 1.2f * (rand.nextFloat() - 0.5f) * entity.width;
            double motionX = 0.02 * rand.nextGaussian();
            double motionY = 0.05 + Math.abs(0.1 * rand.nextGaussian());
            double motionZ = 0.02 * rand.nextGaussian();
            SilentGems.proxy.spawnParticles(EnumModParticles.SHOCKING, new Color(0xffef63),
                entity.world, posX, posY, posZ, motionX, motionY, motionZ);
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
          IBlockState state = ((ItemBlock) entityStack.getItem()).getBlock()
              .getStateFromMeta(entityStack.getItemDamage());
          if (state.equals(itemPlacer.getBlockPlaced(stack))) {
            // TODO
            // event.getItem().setDead();
            break;
          }
        }
      }
    }
  }
}
