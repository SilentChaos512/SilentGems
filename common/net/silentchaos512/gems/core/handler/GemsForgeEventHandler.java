package net.silentchaos512.gems.core.handler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.client.renderers.tool.ToolRenderHelper;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.DimensionalPosition;
import net.silentchaos512.gems.core.util.InventoryHelper;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.PlayerHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.enchantment.EnchantmentAOE;
import net.silentchaos512.gems.enchantment.EnchantmentLumberjack;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.TeleporterLinker;
import net.silentchaos512.gems.lib.HolidayCheer;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.material.ModMaterials;

public class GemsForgeEventHandler {

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onRenderGameOverlay(RenderGameOverlayEvent event) {

    if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
      Minecraft mc = Minecraft.getMinecraft();
      EntityPlayer player = mc.thePlayer;

      ItemStack heldItem = mc.thePlayer.getHeldItem();
      if (heldItem != null && heldItem.getItem() == ModItems.teleporterLinker) {
        TeleporterLinker linker = (TeleporterLinker) heldItem.getItem();

        ScaledResolution res = new ScaledResolution(mc);
        FontRenderer fontRender = mc.fontRendererObj;
        int width = res.getScaledWidth();
        int height = res.getScaledHeight();

        String str;
        if (linker.isLinked(heldItem)) {
          DimensionalPosition pos = linker.getLinkedPosition(heldItem);
          double x = pos.x - player.posX;
          double z = pos.z - player.posZ;
          int distance = (int) Math.sqrt(x * x + z * z);
          str = LocalizationHelper.getOtherItemKey(Names.TELEPORTER_LINKER, "Distance");
          str = String.format(str, distance);

          int textX = width / 2 - fontRender.getStringWidth(str) / 2;
          int textY = height * 3 / 5;
          // Text colored differently depending on situation.
          int color = 0xffff00; // Outside free range, same dimension
          if (pos.d != player.dimension) {
            color = 0xff6600; // Different dimension
            str = LocalizationHelper.getOtherItemKey(Names.TELEPORTER_LINKER, "DifferentDimension");
          } else if (distance < Config.TELEPORTER_XP_FREE_RANGE) {
            color = 0x00aaff; // Inside free range
          }
          fontRender.drawStringWithShadow(str, textX, textY, color);
        }
      }
    }
  }

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
      EntityPlayer player = (EntityPlayer) event.entity;
      HolidayCheer.greetPlayer(player);
      // Check for gem tools and update to the new NBT.
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
      // Shears on Fluffy Blocks
      if (heldItem.getItem() instanceof ItemShears) {
        int efficiency = EnchantmentHelper.getEfficiencyModifier(player);
        if (event.state.getBlock() == ModBlocks.fluffyBlock) {
          event.newSpeed *= 4;
          if (efficiency > 0) {
            event.newSpeed += (efficiency * efficiency + 1);
          }
        }
      }

      // Chaos Tools: No penalty for mining while flying.
      if (InventoryHelper.isGemTool(heldItem)) {
        if (ToolHelper.getToolGemId(heldItem) == ModMaterials.CHAOS_GEM_ID) {
          boolean isInAir = !player.onGround || player.capabilities.isFlying;
          if (isInAir) {
            event.newSpeed *= 5;
          }
          boolean isInWater = player.isInsideOfMaterial(Material.water);
          if (isInWater) {
            event.newSpeed *= 5;
          }
        }
      }

      // Reduce speed of Area Miner and Lumberjack tools.
      int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.aoe.effectId, heldItem);
      if (level > 0 && !player.isSneaking() && heldItem.getItem() instanceof ItemTool) {
        ItemTool itemTool = (ItemTool) heldItem.getItem();
        if (itemTool.getDigSpeed(heldItem, event.state) > 1f) {
          event.newSpeed *= EnchantmentAOE.DIG_SPEED_MULTIPLIER;
        }
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

  @SubscribeEvent
  public void onLivingAttack(LivingAttackEvent event) {

    if (event.source == DamageSource.fall && event.entityLiving instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.entityLiving;
      ItemStack wornBoots = player.inventory.armorItemInSlot(0);
      Item fluffyBoots = SRegistry.getItem("CottonBoots");
      if (wornBoots != null && wornBoots.getItem() == fluffyBoots) {
        // Reduce/negate fall damage
        float newDamage = Math.max(0, event.ammount - Config.FLUFFY_BOOTS_DAMAGE_REDUCTION);
        event.setCanceled(true);
        if (newDamage > 0) {
          // We create a new damage source instead of using DamageSource.fall so this code isn't called again.
          player.attackEntityFrom(new DamageSource("fall").setDamageBypassesArmor(), newDamage);
        }
        // Damage/destroy boots
        int damageToBoots = Math.min(Config.FLUFFY_BOOTS_DAMAGE_TAKEN, (int) (event.ammount / 2));
        if (wornBoots.attemptDamageItem(damageToBoots, SilentGems.instance.random)) {
          player.renderBrokenItemStack(wornBoots); // Does nothing?
          player.playSound("random.break", 1f, 1f); // Does nothing?
          player.setCurrentItemOrArmor(1, null);
        }
      }
    } else if (event.source instanceof EntityDamageSource) {
      EntityDamageSource source = (EntityDamageSource) event.source;
      Entity entity = source.getEntity();
      if (entity instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) entity;
        ItemStack heldItem = player.getHeldItem();
        if (heldItem != null) {
          int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.lifeSteal.effectId,
              heldItem);
          float amount = Math.min(event.ammount, event.entityLiving.getHealth());
          float healAmount = ModEnchantments.lifeSteal.getAmountHealed(level, amount);
//          float debug_prevHealth = player.getHealth();
          player.heal(healAmount);

//          if (debug_prevHealth != player.getHealth()) {
//            LogHelper.info(
//                String.format("LifeSteal (debug): Dealt=%f, Healed=%f, Player Health: %f -> %f",
//                    event.ammount, healAmount, debug_prevHealth, player.getHealth()));
//          }
        }
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onModelBake(ModelBakeEvent event) {

    ((ToolRenderHelper) ToolRenderHelper.instance).onModelBake(event);
    ModItems.returnHome.onModelBake(event);
  }

  @SubscribeEvent
  public void onLivingDropsEvent(LivingDropsEvent event) {

    // Drop Life Essence?
    if (event.entityLiving instanceof IBossDisplayData) {
      // From bosses
      if (Config.LIFE_ESSNECE_COUNT_FROM_BOSS > 0) {
        ItemStack stack = CraftingMaterial.getStack(Names.LIFE_ESSENCE,
            Config.LIFE_ESSNECE_COUNT_FROM_BOSS);
        LogHelper.debug(stack);
        event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX,
            event.entity.posY + event.entity.height / 2f, event.entity.posZ, stack));
      }
    } else {
      // From normal mobs
      float chance = Config.LIFE_ESSENCE_DROP_RATE;
      if (event.source.damageType.equals("player")) {
        chance *= (event.lootingLevel / 6f + 1f);
      }

      if (SilentGems.instance.random.nextFloat() <= chance) {
        event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX,
            event.entity.posY + event.entity.height / 2f, event.entity.posZ,
            CraftingMaterial.getStack(Names.LIFE_ESSENCE)));
      }
    }
  }

  @SubscribeEvent
  public void onPlayerDestroyItem(PlayerDestroyItemEvent event) {

    if (InventoryHelper.isGemTool(event.original)) {
      ItemStack brokenTool = ModItems.brokenTool.getFromTool(event.original);
      PlayerHelper.addItemToInventoryOrDrop(event.entityPlayer, brokenTool);
    }
  }
}
