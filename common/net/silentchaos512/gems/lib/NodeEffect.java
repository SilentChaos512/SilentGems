package net.silentchaos512.gems.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.util.ModDamageSource;
import net.silentchaos512.lib.collection.ItemStackList;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.PlayerHelper;

public class NodeEffect {

  public static List<NodeEffect> ALL_EFFECTS = new ArrayList<>();

  //@formatter:off
  public static NodeEffect ATTACK = new NodeEffect(
      SilentGems.RESOURCE_PREFIX + "attack", 8f, 0.25f, 0xFF0000, false, false, true);
  public static NodeEffect LEVITATE = new NodeEffectPotion(
      SilentGems.RESOURCE_PREFIX + "levitate", 24f, 0.25f, 0x00FF00, false, false, true,
      MobEffects.LEVITATION, 60, 120, 1);
  public static NodeEffect REPAIR = new NodeEffect(
      SilentGems.RESOURCE_PREFIX + "repair", 10f, 0.2f, 0x00AEFF, true, false, false);
  public static NodeEffect REGEN = new NodeEffectPotion(
      SilentGems.RESOURCE_PREFIX + "regeneration", 15f, 0.3f, 0xFF66C9, true, true, false,
      MobEffects.REGENERATION, 80, 200, 0);
  public static NodeEffect SATURATION = new NodeEffect(
      SilentGems.RESOURCE_PREFIX + "saturation", 25f, 0.1f, 0xE07300, true, false, false);
  //@formatter:on

  protected final String key;
  protected final int delay;
  protected final int delaySalt;
  protected final float successChance;
  public final int color;
  public final boolean targetPlayers;
  public final boolean targetPassives;
  public final boolean targetHostiles;

  public NodeEffect(String key, float delayInSeconds, float successChance, int color,
      boolean targetPlayers, boolean targetPassives, boolean targetHostiles) {

    this.key = key;
    this.delay = (int) (20 * delayInSeconds);
    this.delaySalt = (int) (delay * (SilentGems.random.nextDouble() - 0.5));
    this.successChance = successChance;
    this.color = color;
    this.targetPlayers = targetPlayers;
    this.targetPassives = targetPassives;
    this.targetHostiles = targetHostiles;

    ALL_EFFECTS.add(this);
  }

  public boolean applyToEntity(EntityLivingBase entity, Random random) {

    if (random.nextFloat() > successChance || !needsEffect(entity)) {
      return false;
    }

    if (this == ATTACK) {
      if (!entity.world.isRemote)
        entity.attackEntityFrom(ModDamageSource.NODE_ATTACK,
            (float) (4.0f + 0.75f * random.nextGaussian()));
      return true;
    } else if (this == REPAIR) {
      // Repair damaged items.
      if (!(entity instanceof EntityPlayer))
        return false;

      EntityPlayer player = (EntityPlayer) entity;
      int amount = (int) (10 + 2 * random.nextGaussian());
      ItemStack stackToRepair = null;

      // Select a random item.
      ItemStackList items = PlayerHelper.getNonEmptyStacks(player);
      items.removeIf(stack -> !canRepair(stack));
      if (items.size() > 0) {
        stackToRepair = items.get(random.nextInt(items.size()));
      }

      if (stackToRepair != null) {
        ItemHelper.attemptDamageItem(stackToRepair, (int) -amount, random, player);
        entity.world.playSound(null, entity.getPosition(), SoundEvents.BLOCK_ANVIL_USE,
            SoundCategory.BLOCKS, 0.5f, 2.0f + (float) (0.2 * random.nextGaussian()));
        return true;
      }
      return false;
    } else if (this == SATURATION) {
      // Saturation. Not using the potion effect, just restore some hunger directly.
      if (!(entity instanceof EntityPlayer))
        return false;

      EntityPlayer player = (EntityPlayer) entity;
      player.getFoodStats().addStats(2, 0.1f);
      entity.world.playSound(null, entity.getPosition(), SoundEvents.ENTITY_PLAYER_BURP,
          SoundCategory.BLOCKS, 0.5f, 1.2f + (float) (0.05 * random.nextGaussian()));
      return true;
    } else if (this instanceof NodeEffectPotion) {
      // General potion effect handler.
      NodeEffectPotion effect = (NodeEffectPotion) this;
      int duration = effect.durationMin
          + random.nextInt(effect.durationMax - effect.durationMin + 1);
      entity.addPotionEffect(new PotionEffect(effect.potion, duration, effect.amplifier));
      return true;
    }

    SilentGems.logHelper.warning("Potentially unknown node effect: " + key);
    return false;
  }

  /**
   * Return true if the entity needs the effect. For example, I don't want to send regen if the entity does not need
   * healing. Potion logic is in NodeEffectPotion.
   * 
   * @param entity
   * @return
   */
  protected boolean needsEffect(EntityLivingBase entity) {

    if (this == REGEN) {
      return entity.getHealth() < entity.getMaxHealth();
    } else if (this == SATURATION) {
      if (!(entity instanceof EntityPlayer))
        return false;
      EntityPlayer player = (EntityPlayer) entity;
      return player.getFoodStats().getFoodLevel() <= 18;
    }

    return true;
  }

  public boolean isTimeToTry(long time) {

    return time % (delay + delaySalt) == 0;
  }

  protected boolean canRepair(ItemStack stack) {

    if (GemsConfig.NODE_REPAIR_BLACKLIST.contains(stack.getItem())) {
      return false;
    }

    return stack.getItemDamage() > 0 && stack.isItemStackDamageable()
        && (stack.getItem().isRepairable()
            || GemsConfig.NODE_REPAIR_WHITELIST.contains(stack.getItem()));
  }
}
