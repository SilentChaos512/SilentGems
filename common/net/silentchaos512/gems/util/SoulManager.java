package net.silentchaos512.gems.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.init.ModEnchantments;
import net.silentchaos512.gems.lib.soul.SoulSkill;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.lib.util.PlayerHelper;
import net.silentchaos512.lib.util.StackHelper;

public class SoulManager {

  static final int CLEAR_DELAY = 15 * 60 * 20;

  static Map<UUID, ToolSoul> map = new HashMap<>();
  static int ticks = 0;

  public static @Nullable ToolSoul getSoul(ItemStack tool) {

    if (StackHelper.isEmpty(tool)
        || !(tool.getItem() instanceof ITool || tool.getItem() instanceof IArmor)
        || ToolHelper.isExampleItem(tool)) {
      return null;
    }

    UUID uuid = ToolHelper.getSoulUUID(tool);

    // Soul already in map?
    ToolSoul soul = map.get(uuid);
    if (soul != null) {
      return soul;
    }

    // Does soul exist in NBT?
    ToolHelper.initRootTag(tool);
    if (!tool.getTagCompound().hasKey(ToolHelper.NBT_ROOT_TOOL_SOUL)) {
      return null;
    }

    // Read from NBT, place in map for fast access.
    soul = ToolSoul.readFromNBT(ToolHelper.getRootTag(tool, ToolHelper.NBT_ROOT_TOOL_SOUL));
    if (uuid == null) {
      ToolHelper.setRandomSoulUUID(tool);
      uuid = ToolHelper.getSoulUUID(tool);
    }
    map.put(uuid, soul);
    // SilentGems.logHelper.debug("Put tool soul " + soul + " in the map! Total count: " + map.size());
    return soul;
  }

  public static @Nullable ToolSoul getSoulByUuid(UUID uuid) {

    return map.get(uuid);
  }

  public static void setSoul(ItemStack tool, ToolSoul soul, boolean randomizeUuid) {

    ToolHelper.initRootTag(tool);
    NBTTagCompound tags = new NBTTagCompound();
    soul.writeToNBT(tags);
    tool.getTagCompound().setTag(ToolHelper.NBT_ROOT_TOOL_SOUL, tags);
    if (randomizeUuid) {
      ToolHelper.setRandomSoulUUID(tool);
    }
  }

  public static void addSoulXp(int amount, ItemStack tool, EntityPlayer player) {

    ToolSoul soul = getSoul(tool);
    if (soul != null && amount > 0) {
      int current = soul.getXp();
      soul.addXp(amount, tool, player);
    }
  }

  public static void writeToolSoulsToNBT(EntityPlayer player) {

    // Find all the players tools. Find the matching souls in the map.
    int count = 0;
    for (ItemStack tool : PlayerHelper.getNonEmptyStacks(player, true, true, true,
        s -> s.getItem() instanceof ITool || s.getItem() instanceof IArmor)) {
      // UUID uuid = getSoulUUID(tool);
      // ToolSoul soul = toolSoulMap.get(uuid);
      ToolSoul soul = getSoul(tool);
      if (soul != null) {
        setSoul(tool, soul, false);
        ++count;
      }
    }
    // SilentGems.logHelper.debug("Saved " + count + " tool(s) for " + player.getName());
  }

  @SubscribeEvent
  public void onTick(TickEvent event) {

    if (event.phase == Phase.END
        && (event instanceof ClientTickEvent || event instanceof ServerTickEvent)) {
      if (++ticks % CLEAR_DELAY == 0) {
        map.clear();
        // SilentGems.logHelper.debug("Cleared out the tool soul map.");
      }
    }
  }

  @SubscribeEvent
  public void onLivingHurt(LivingHurtEvent event) {

    if (event.getSource().getTrueSource() instanceof EntityPlayer) {
      EntityLivingBase hurt = event.getEntityLiving();
      EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
      ItemStack mainHand = player.getHeldItemMainhand();

      ToolSoul toolSoul = SoulManager.getSoul(mainHand);

      if (toolSoul != null) {
        // Activate skills.
        toolSoul.activateSkillsOnEntity(mainHand, player, hurt);

        // XP for causing damage.
        // NOTE: event.getAmount() is before armor and potions are applied!
        // Should we consider that somehow?
        float damageAmount = Math.min(event.getAmount(), hurt.getMaxHealth());
        int xp = Math.round(ToolSoul.XP_FACTOR_KILLS * damageAmount);
        // SilentGems.logHelper.debug(damageAmount, xp);
        xp = MathHelper.clamp(xp, 1, 1000);
        toolSoul.addXp(xp, mainHand, player);
      }
    }
  }

  @SubscribeEvent
  public void onGetBreakSpeed(PlayerEvent.BreakSpeed event) {

    EntityPlayer player = event.getEntityPlayer();
    ItemStack mainHand = player.getHeldItemMainhand();

    // Overridden by the Gravity enchantment.
    if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.gravity, mainHand) > 0)
      return;

    ToolSoul soul = getSoul(mainHand);
    if (soul != null) {
      // Aquatic or Aerial?
      int aquatic = soul.getSkillLevel(SoulSkill.AQUATIC);
      int aerial = soul.getSkillLevel(SoulSkill.AERIAL);
      if (aquatic > 0 && player.isInsideOfMaterial(Material.WATER))
        event.setNewSpeed(event.getNewSpeed() * (5f / (SoulSkill.AQUATIC.maxLevel - aquatic + 1)));
      else if (aerial > 0 && (!player.onGround || player.capabilities.isFlying))
        event.setNewSpeed(event.getNewSpeed() * (5f / SoulSkill.AERIAL.maxLevel - aerial + 1));
    }
  }

  @SubscribeEvent
  public void onPlayerDamage(LivingHurtEvent event) {

    // Player took fall damage?
    if (event.getSource() == DamageSource.FALL && event.getEntityLiving() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.getEntityLiving();
      ItemStack mainHand = player.getHeldItemMainhand();
      ItemStack offHand = player.getHeldItemOffhand();
      ToolSoul soulMain = getSoul(mainHand);
      ToolSoul soulOff = getSoul(offHand);

      // Get highest level of Aerial on either main or off hand.
      int levelMain = soulMain == null ? 0 : soulMain.getSkillLevel(SoulSkill.AERIAL);
      int levelOff = soulOff == null ? 0 : soulOff.getSkillLevel(SoulSkill.AERIAL);
      ToolSoul soulToDrain = levelMain > levelOff ? soulMain : soulOff;
      int aerialLevel = Math.max(levelMain, levelOff);

      if (aerialLevel > 0) {
        float amountToReduce = Math.max(2 + 2 * aerialLevel,
            0.15f * aerialLevel * event.getAmount());
        event.setAmount(event.getAmount() - amountToReduce);
        soulToDrain.addActionPoints(-SoulSkill.AERIAL.apCost);
      }
    }
  }

  @SubscribeEvent
  public void onPlayerTick(PlayerTickEvent event) {

    // Max air is 300, so 30 per bubble
    if (!event.player.world.isRemote && event.player.getAir() < 5) {
      EntityPlayer player = event.player;
      ItemStack mainHand = player.getHeldItemMainhand();
      ItemStack offHand = player.getHeldItemOffhand();
      ToolSoul soulMain = getSoul(mainHand);
      ToolSoul soulOff = getSoul(offHand);

      // Get highest level of Aquatic on either main or off hand.
      int levelMain = soulMain == null ? 0 : soulMain.getSkillLevel(SoulSkill.AQUATIC);
      int levelOff = soulOff == null ? 0 : soulOff.getSkillLevel(SoulSkill.AQUATIC);
      ToolSoul soulToDrain = levelMain > levelOff ? soulMain : soulOff;
      int aquaticLevel = Math.max(levelMain, levelOff);

      if (aquaticLevel > 0) {
        int amountToRestore = 60 * aquaticLevel;
        player.setAir(player.getAir() + amountToRestore);
        player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_HURT_DROWN,
            SoundCategory.PLAYERS, 1f, 1f);
        soulToDrain.addActionPoints(-SoulSkill.AQUATIC.apCost);
      }
    }
  }
}
