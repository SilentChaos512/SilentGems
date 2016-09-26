package net.silentchaos512.gems.handler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.energy.IChaosStorage;
import net.silentchaos512.gems.network.NetworkHandler;
import net.silentchaos512.gems.network.message.MessageDataSync;
import net.silentchaos512.gems.util.ToolHelper;

// Lots of inspiration from Psi here. Previous version of the mod used IExtendedEntityProperties,
// but that doesn't seem to work in 1.9.
public class PlayerDataHandler {

  public static final String NBT_ROOT = SilentGems.MOD_ID + "Data";

  private static Map<Integer, PlayerData> playerData = new HashMap();

  public static PlayerData get(EntityPlayer player) {

    int key = getKey(player);
    if (!playerData.containsKey(key)) {
      playerData.put(key, new PlayerData(player));
    }

    PlayerData data = playerData.get(key);
    if (data.playerWR.get() != player) {
      NBTTagCompound tags = new NBTTagCompound();
      data.writeToNBT(tags);
      playerData.remove(key);
      data = get(player);
      data.readFromNBT(tags);
    }

    return data;
  }

  public static void cleanup() {

    List<Integer> remove = new ArrayList();

    for (int i : playerData.keySet()) {
      PlayerData d = playerData.get(i);
      if (d != null && d.playerWR.get() == null) {
        remove.add(i);
      }
    }

    for (int i : remove) {
      playerData.remove(i);
    }
  }

  private static int getKey(EntityPlayer player) {

    return player.hashCode() << 1 + (player.worldObj.isRemote ? 1 : 0);
  }

  public static NBTTagCompound getDataCompoundForPlayer(EntityPlayer player) {

    NBTTagCompound forgeData = player.getEntityData();
    if (!forgeData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
      forgeData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
    }

    NBTTagCompound persistentData = forgeData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
    if (!persistentData.hasKey(NBT_ROOT)) {
      persistentData.setTag(NBT_ROOT, new NBTTagCompound());
    }

    return persistentData.getCompoundTag(NBT_ROOT);
  }

  public static class EventHandler {

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event) {

      if (event.phase == Phase.END) {
        PlayerDataHandler.cleanup();
      }
    }

    @SubscribeEvent
    public void onPlayerTick(LivingUpdateEvent event) {

      if (event.getEntityLiving() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        PlayerDataHandler.get(player).tick();
      }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {

      if (event.player instanceof EntityPlayerMP) {
        MessageDataSync message = new MessageDataSync(get(event.player));
        NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) event.player);
      }
    }
  }

  public static class PlayerData {

    public static final int CHAOS_MAX_TRANSFER = 1000;
    public static final int MAGIC_COOLDOWN_TIME = 10;
    public static final int RECHARGE_COOLDOWN_TIME = 100;

    private static final String NBT_CHAOS = "Chaos";
    private static final String NBT_MAX_CHAOS = "MaxChaos";
    private static final String NBT_RECHARGE_COOLDOWN = "RechargeCooldown";

    public int chaos;
    public int maxChaos;
    public int magicCooldown;
    public int rechargeCooldown;

    public WeakReference<EntityPlayer> playerWR;
    private final boolean client;

    public PlayerData(EntityPlayer player) {

      playerWR = new WeakReference<EntityPlayer>(player);
      client = player.worldObj.isRemote;

      load();
    }

    public void tick() {

      EntityPlayer player = playerWR.get();

      if (maxChaos == 0) {
        maxChaos = 10000;
      }

      boolean shouldSave = false;

      // Magic cooldown?
      if (magicCooldown > 0) {
        --magicCooldown;
        shouldSave = true;
      }

      // Recharge cooldown?
      if (rechargeCooldown > 0) {
        --rechargeCooldown;
        shouldSave = true;
      }

      if (shouldSave)
        save();
    }

    public void drainChaos(int amount) {

      if (amount == 0)
        return;

      chaos = Math.max(chaos - amount, 0);
      rechargeCooldown = RECHARGE_COOLDOWN_TIME;
      save();
      sendUpdateMessage();
    }

    public int sendChaos(int amount) {

      return sendChaos(amount, false);
    }

    public int sendChaos(int amount, boolean ignoreCooldown) {

      if (rechargeCooldown > 0 && !ignoreCooldown) {
        return 0;
      }

      int pulled = Math.min(maxChaos - chaos, amount);
      chaos += pulled;
//      SilentGems.instance.logHelper.debug(client, pulled, chaos, maxChaos);

      if (pulled > 0) {
        save();
        sendUpdateMessage();
      }

      return pulled;
    }

    private void sendUpdateMessage() {

      if (!client) {
        EntityPlayer player = playerWR.get();
        MessageDataSync message = new MessageDataSync(get(player));
        NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
      }
    }

    public int getCurrentChaos() {

      return chaos;
    }

    public int getMaxChaos() {

      return maxChaos;
    }

    public int getChaosChargeSpeed() {

      EntityPlayer player = playerWR.get();
      ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);
      ItemStack offHand = player.getHeldItem(EnumHand.OFF_HAND);

      float multi = 0.0f;
      if (mainHand != null && mainHand.getItem() instanceof ITool) {
        multi += ToolHelper.getChargeSpeed(mainHand);
      }
      if (offHand != null && offHand.getItem() instanceof ITool) {
        multi += ToolHelper.getChargeSpeed(offHand);
      }
      if (multi == 0.0f) {
        multi = 1.0f;
      }

//      SilentGems.instance.logHelper.debug(multi);
      return (int) (multi * CHAOS_MAX_TRANSFER);
    }

    public void save() {

      if (!client) {
        EntityPlayer player = playerWR.get();

        if (player != null) {
          NBTTagCompound tags = getDataCompoundForPlayer(player);
          writeToNBT(tags);
        }
      }
    }

    public void writeToNBT(NBTTagCompound tags) {

      tags.setInteger(NBT_CHAOS, chaos);
      tags.setInteger(NBT_MAX_CHAOS, maxChaos);
      tags.setInteger(NBT_RECHARGE_COOLDOWN, rechargeCooldown);
    }

    public void load() {

      if (!client) {
        EntityPlayer player = playerWR.get();

        if (player != null) {
          NBTTagCompound tags = getDataCompoundForPlayer(player);
          readFromNBT(tags);
        }
      }
    }

    public void readFromNBT(NBTTagCompound tags) {

      chaos = tags.getInteger(NBT_CHAOS);
      maxChaos = tags.getInteger(NBT_MAX_CHAOS);
      rechargeCooldown = tags.getInteger(NBT_RECHARGE_COOLDOWN);
    }
  }
}
