package net.silentchaos512.gems.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
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
    SilentGems.logHelper.debug("Put tool soul " + soul + " in the map! Total count: " + map.size());
    return soul;
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
    SilentGems.logHelper.debug("Saved " + count + " tool(s) for " + player.getName());
  }

  @SubscribeEvent
  public void onTick(TickEvent event) {

    if (event.phase == Phase.END
        && (event instanceof ClientTickEvent || event instanceof ServerTickEvent)) {
      if (++ticks % CLEAR_DELAY == 0) {
        map.clear();
        SilentGems.logHelper.debug("Cleared out the tool soul map.");
      }
    }
  }

  @SubscribeEvent
  public void onLivingHurt(LivingHurtEvent event) {

    if (event.getSource().getTrueSource() instanceof EntityPlayer) {
      EntityLivingBase hurt = event.getEntityLiving();
      EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
      ItemStack mainHand = player.getHeldItemMainhand();

      // XP for causing damage.
      ToolSoul toolSoul = SoulManager.getSoul(mainHand);
      if (toolSoul != null) {
        // NOTE: event.getAmount() is before armor and potions are applied!
        // Should we consider that somehow?
        float damageAmount = Math.min(event.getAmount(), hurt.getMaxHealth());
        int xp = (int) (ToolSoul.XP_FACTOR_KILLS * damageAmount);
        SilentGems.logHelper.debug(damageAmount, xp);
        xp = MathHelper.clamp(xp, 1, 1000);
        toolSoul.addXp(xp, mainHand, player);
      }
    }
  }
}
