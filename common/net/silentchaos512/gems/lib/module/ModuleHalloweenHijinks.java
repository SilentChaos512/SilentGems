package net.silentchaos512.gems.lib.module;

import java.util.Calendar;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.Skulls;
import net.silentchaos512.gems.config.GemsConfig;

public class ModuleHalloweenHijinks {

  public static ModuleHalloweenHijinks instance = new ModuleHalloweenHijinks();

  static final String NBT_MARKER = "SG_HW_Mob";
  static final float COSTUME_CHANCE = 0.66f;
  static final float COSTUME_DROP_RATE = 0.01f;

  Calendar today;
  boolean rightDay = checkDate();
  boolean moduleEnabled = true;

  private boolean checkDate() {

    today = Calendar.getInstance();
    int month = today.get(Calendar.MONTH);
    int date = today.get(Calendar.DATE);

    // October 28th to 31st
    rightDay = month == Calendar.OCTOBER && date >= 28 && date <= 31;
    return rightDay;
  }

  public void loadConfig(Configuration c) {

    moduleEnabled = c.getBoolean("Enable Halloween Hijinks", GemsConfig.CAT_MISC, true,
        "Some mobs may dress up for the end of October.");
  }

  @SubscribeEvent
  public void onLivingSpawn(LivingUpdateEvent event) {

    if (!rightDay || !moduleEnabled)
      return;

    // Hostile mobs only.
    Entity entity = event.getEntity();
    if (entity instanceof EntityLiving && entity instanceof IMob) {
      // Check for marker.
      EntityLivingBase entityLiving = event.getEntityLiving();
      if (!entityLiving.getEntityData().getBoolean(NBT_MARKER)) {
        // Mark the entity as processed, even if it does not receive a costume.
        entityLiving.getEntityData().setBoolean(NBT_MARKER, true);
        if (SilentGems.random.nextFloat() < COSTUME_CHANCE) {
          // Give costume
          ItemStack skull = Skulls.selectRandom(SilentGems.random);
          entityLiving.setItemStackToSlot(EntityEquipmentSlot.HEAD, skull);
          ((EntityLiving) entityLiving).setDropChance(EntityEquipmentSlot.HEAD, COSTUME_DROP_RATE);
        }
      }
    }
  }

  long ticksRunning = 0;

  @SubscribeEvent
  public void onServerTick(TickEvent.ServerTickEvent event) {

    ++ticksRunning;
    if (ticksRunning % 6000 == 0) {
      checkDate();
    }
  }

  @SubscribeEvent
  public void onPlayerJoinServer(PlayerLoggedInEvent event) {

    Skulls.putPlayer(event.player.getName());
  }
}
