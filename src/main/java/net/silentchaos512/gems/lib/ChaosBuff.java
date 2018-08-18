package net.silentchaos512.gems.lib;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.Loader;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.gems.network.NetworkHandler;
import net.silentchaos512.gems.network.message.MessageSetFlight;

public class ChaosBuff {

  public static final ChaosBuff SPEED;
  public static final ChaosBuff SLOWNESS;
  public static final ChaosBuff HASTE;
  public static final ChaosBuff MINING_FATIGUE;
  public static final ChaosBuff STRENGTH;
  public static final ChaosBuff JUMP_BOOST;
  public static final ChaosBuff FLIGHT;
  public static final ChaosBuff NAUSEA;
  public static final ChaosBuff REGENERATION;
  public static final ChaosBuff RESISTANCE;
  public static final ChaosBuff FIRE_RESISTANCE;
  public static final ChaosBuff WATER_BREATHING;
  public static final ChaosBuff INVISIBILITY;
  public static final ChaosBuff BLINDNESS;
  public static final ChaosBuff NIGHT_VISION;
  public static final ChaosBuff HUNGER;
  public static final ChaosBuff WEAKNESS;
  public static final ChaosBuff POISON;
  public static final ChaosBuff WITHER;
  public static final ChaosBuff GLOWING;
  public static final ChaosBuff LEVITATION;
  public static final ChaosBuff CAPACITY;
  public static final ChaosBuff RECHARGE;
  // ToughAsNails
  public static final @Nullable ChaosBuff COLD_RESISTANCE;
  public static final @Nullable ChaosBuff HEAT_RESISTANCE;
  public static final @Nullable ChaosBuff THIRST;

  static final Map<String, ChaosBuff> buffMap = new LinkedHashMap<>();

  /** A unique identifier for the buff. */
  final @Nonnull String key;
  /** The potion effect the buff applies, if any. */
  final @Nullable Potion potion;
  /** The maximum level of the buff that can be applied (assuming the gem has enough free slots. */
  final int maxLevel;
  /**
   * The base number of slots used. Additional slots are used for higher levels.
   *
   * @see {@link ChaosBuff#getSlotsUsed(int)}
   */
  final int slotsUsed;
  /**
   * The amount of Chaos used per tick to apply the buff. Higher levels consume additional Chaos.
   *
   * @see {@link ChaosBuff#getChaosCost(int)}
   */
  final int chaosCost;
  /**
   * Indicates that the chaos cost will vary by circustances. Only used for tooltips.
   */
  final boolean variableCost;
  /**
   * The duration to apply. By default, this is done per tick, but there are exceptions. Note that smaller durations are
   * better. I use 20 ticks for most, but night vision requires more than 400 ticks to prevent the flashing effect.
   * Regeneration also has a special exception.
   *
   * @see {@link ChaosBuff#getApplyDuration(EntityPlayer, int)}
   */
  final int applyDuration;

  public ChaosBuff(String key, int maxLevel, int slotsUsed, int chaosCost, boolean variableCost,
      int applyDuration, @Nullable Potion potion) {

    if (buffMap.containsKey(key))
      throw new IllegalArgumentException("Buff with key " + key + " has already been added!");

    this.key = key;
    this.maxLevel = maxLevel;
    this.potion = potion;
    this.slotsUsed = slotsUsed;
    this.chaosCost = chaosCost;
    this.applyDuration = applyDuration;
    this.variableCost = variableCost;

    buffMap.put(key, this);
  }

  static {

    final String prefix = SilentGems.RESOURCE_PREFIX;
    final int dur = 30;
    // @formatter:off
    //                                                       mLvl slots cost        duration
    CAPACITY        = new ChaosBuff(prefix + "capacity",        4,   1,   0, false, dur, null);
    RECHARGE        = new ChaosBuff(prefix + "recharge",        4,   1,   0, false, dur, null);
    FLIGHT          = new ChaosBuff(prefix + "flight",          1,  10,  80,  true, dur, null);
    SPEED           = new ChaosBuff(prefix + "speed",           4,   4,  20, false, dur, MobEffects.SPEED);
    HASTE           = new ChaosBuff(prefix + "haste",           2,   4,  30, false, dur, MobEffects.HASTE);
    JUMP_BOOST      = new ChaosBuff(prefix + "jump_boost",      4,   4,  10, false, dur, MobEffects.JUMP_BOOST);
    STRENGTH        = new ChaosBuff(prefix + "strength",        2,  10,  50, false, dur, MobEffects.STRENGTH);
    REGENERATION    = new ChaosBuff(prefix + "regeneration",    2,   8,  42, false,  80, MobEffects.REGENERATION);
    RESISTANCE      = new ChaosBuff(prefix + "resistance",      2,   6,  40, false, dur, MobEffects.RESISTANCE);
    FIRE_RESISTANCE = new ChaosBuff(prefix + "fire_resistance", 1,   8, 400,  true, dur, MobEffects.FIRE_RESISTANCE);
    WATER_BREATHING = new ChaosBuff(prefix + "water_breathing", 1,   4,  30, false, dur, MobEffects.WATER_BREATHING);
    NIGHT_VISION    = new ChaosBuff(prefix + "night_vision",    1,   2,  10, false, 410, MobEffects.NIGHT_VISION);
    INVISIBILITY    = new ChaosBuff(prefix + "invisibility",    1,   6,  25, false, dur, MobEffects.INVISIBILITY);
    LEVITATION      = new ChaosBuff(prefix + "levitation",      4,   3,  20, false, dur, MobEffects.LEVITATION);
    GLOWING         = new ChaosBuff(prefix + "glowing",         1,   0,   5, false, dur, MobEffects.GLOWING);
    SLOWNESS        = new ChaosBuff(prefix + "slowness",        3,  -2,   5, false, dur, MobEffects.SLOWNESS);
    MINING_FATIGUE  = new ChaosBuff(prefix + "mining_fatigue",  3,  -2,   5, false, dur, MobEffects.MINING_FATIGUE);
    NAUSEA          = new ChaosBuff(prefix + "nausea",          3,  -4,   5, false, dur, MobEffects.NAUSEA);
    BLINDNESS       = new ChaosBuff(prefix + "blindness",       3,  -3,   5, false, dur, MobEffects.BLINDNESS);
    HUNGER          = new ChaosBuff(prefix + "hunger",          3,  -2,   5, false, dur, MobEffects.HUNGER);
    WEAKNESS        = new ChaosBuff(prefix + "weakness",        3,  -2,   5, false, dur, MobEffects.WEAKNESS);
    POISON          = new ChaosBuff(prefix + "poison",          3,  -2,   5, false,  90, MobEffects.POISON);
    WITHER          = new ChaosBuff(prefix + "wither",          2,  -3,   5, false,  80, MobEffects.WITHER);

    if (Loader.isModLoaded("toughasnails")) {
      Potion coldResist = Potion.getPotionFromResourceLocation("toughasnails:cold_resistance");
      Potion heatResist = Potion.getPotionFromResourceLocation("toughasnails:heat_resistance");
      Potion thirst = Potion.getPotionFromResourceLocation("toughasnails:thirst");
      COLD_RESISTANCE = new ChaosBuff("toughasnails:cold_resistance", 1,  8, 50, false, dur, coldResist);
      HEAT_RESISTANCE = new ChaosBuff("toughasnails:heat_resistance", 1,  8, 50, false, dur, heatResist);
      THIRST          = new ChaosBuff("toughasnails:thirst",          1, -4,  5, false, dur, thirst);
    } else {
      COLD_RESISTANCE = null;
      HEAT_RESISTANCE = null;
      THIRST = null;
    }
    // @formatter:on
  }

  public static @Nullable ChaosBuff byKey(String key) {

    return buffMap.get(key);
  }

  public static Collection<ChaosBuff> getAllBuffs() {

    return buffMap.values();
  }

  // Save reference to these potion effects, because heat/cold resistance remove them.
  static final Potion hyperthermia = Potion
      .getPotionFromResourceLocation("toughasnails:hyperthermia");
  static final Potion hypothermia = Potion
      .getPotionFromResourceLocation("toughasnails:hypothermia");

  public void applyToPlayer(EntityPlayer player, int level, ItemStack stack) {

    if (potion != null) {
      int duration = getApplyDuration(player, level);
      if (duration > 0) {
        player.addPotionEffect(new PotionEffect(potion, duration, level - 1, false, false));

        // (Tough As Nails) Remove hyper/hypothermia when using heat/cold resistance.
        if (this == HEAT_RESISTANCE)
          player.removeActivePotionEffect(hyperthermia);
        else if (this == COLD_RESISTANCE)
          player.removeActivePotionEffect(hypothermia);
      }
    }

    if (this == FLIGHT) {
      player.capabilities.allowFlying = true;
      PlayerData data = PlayerDataHandler.get(player);
      if (data != null)
        data.flightTime = 100;
      if (!player.world.isRemote && player.world.getTotalWorldTime() % 20 == 0) {
        NetworkHandler.INSTANCE.sendTo(new MessageSetFlight(true), (EntityPlayerMP) player);
      }
    }
  }

  public void removeFromPlayer(EntityPlayer player, int level, ItemStack stack) {

    if (potion != null) {
      player.removePotionEffect(potion);
    }

    if (this == FLIGHT && !player.capabilities.isCreativeMode) {
      player.capabilities.allowFlying = false;
      player.capabilities.isFlying = false;
      player.fallDistance = 0;
      if (!player.world.isRemote) {
        NetworkHandler.INSTANCE.sendTo(new MessageSetFlight(false), (EntityPlayerMP) player);
      }
    }
  }

  /**
   * @return The number of slots used. Each level beyond 1 adds a slot (or subtracts it if slotsUsed is negative)
   */
  public int getSlotsUsed(int level) {

    if (slotsUsed == 0)
      return 0;
    return slotsUsed + (level - 1) * (slotsUsed / Math.abs(slotsUsed));
  }

  /**
   * Get the Chaos cost for this buff.
   *
   * @param level
   *          The buff level. Should be greater than zero.
   * @param player
   *          The player (can be null). Cost can depend on the player's state. If null, this should return the maximum
   *          possible cost.
   * @return The amount of Chaos drained per tick by this buff. Each level beyond 1 adds 20% to the cost.
   */
  public int getChaosCost(int level, @Nullable EntityPlayer player) {

    int normalCost = chaosCost + chaosCost * (level - 1) / 5;

    // Flight drain is unique. There's no cost when active but not flying and 10% cost when falling fast.
    if (this == FLIGHT && player != null) {
      boolean notFlying = !player.capabilities.isFlying || player.capabilities.isCreativeMode;
      // Falling above set speed when not flying.
      if (notFlying && player.motionY < -0.88)
        return normalCost / 10;
      else if (notFlying)
        return 0;
    } else if (this == FIRE_RESISTANCE && player != null) {
      return player.isBurning() ? chaosCost : 0;
    }

    return normalCost;
  }

  /**
   * @return The duration to apply for this effect. In most cases, this is just applyDuration, but some effects have
   *         special requirements.
   */
  public int getApplyDuration(EntityPlayer player, int level) {

    if (this == REGENERATION || this == POISON || this == WITHER) {
      // Should apply every 2 seconds for regen I, every second for regen II.
      // Regen resets its timer when reapplied, so it won't work if applied too often.
      // Version 2.6.3: Modified a bit to handle poison and wither as well.
      boolean shouldApply = false;

      PotionEffect activeEffect = player.getActivePotionEffect(this.potion);
      if (activeEffect == null) {
        shouldApply = true;
      } else {
        int remainingTime = activeEffect.getDuration();
        int healTime = level == 2 ? 20 : 40;
        if (remainingTime + healTime <= applyDuration)
          shouldApply = true;
      }

      return shouldApply ? applyDuration : 0;
    }

    return applyDuration;
  }

  public String getLocalizedName(int level) {

    String str;

    if (potion != null)
      str = SilentGems.i18n.translate(potion.getName());
    else
      str = SilentGems.i18n.translate("buff." + key);

    if (level > 1)
      str += " " + (level == 2 ? "II" : level == 3 ? "III" : level == 4 ? "IV" : "" + level);
    return str;
  }

  public String getDescription() {

    String descKey = "buff." + key + ".desc";
    String desc = SilentGems.i18n.translate(descKey);
    if (!desc.equals(descKey))
      return desc;
    if (potion == null)
      return "Null potion!";
    String potionName = SilentGems.i18n.translate(potion.getName());
    return SilentGems.i18n.itemSubText(Names.CHAOS_RUNE, "appliesEffect", potionName);
  }

  public int getColor() {

    if (potion != null)
      return potion.getLiquidColor();

    if (this == CAPACITY)
      return 0xA538C9;
    if (this == RECHARGE)
      return 0xFFF79E;

    return 0xFFFFFF;
  }

  public int getMaxLevel() {

    return maxLevel;
  }

  public String getKey() {

    return key;
  }

  public @Nullable Potion getPotion() {

    return potion;
  }

  public boolean hasVariableCost() {

    return variableCost;
  }
}
