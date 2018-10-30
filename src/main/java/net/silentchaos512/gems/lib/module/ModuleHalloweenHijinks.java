package net.silentchaos512.gems.lib.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.Skulls;
import net.silentchaos512.gems.config.GemsConfig;

import java.util.Calendar;

public class ModuleHalloweenHijinks {
    public static ModuleHalloweenHijinks instance = new ModuleHalloweenHijinks();

    private static final String NBT_MARKER = "SG_HW_Mob";

    private Calendar today;
    private boolean rightDay = checkDate();
    private boolean moduleEnabled = true;
    private boolean forcedOn = false;
    private float costumeChance = 0.3f;
    private float costumeDropRate = 0.01f;

    private boolean checkDate() {
        today = Calendar.getInstance();
        int month = today.get(Calendar.MONTH);
        int date = today.get(Calendar.DATE);

        // October 28th to 31st
        rightDay = month == Calendar.OCTOBER && date >= 28 && date <= 31;
        return rightDay;
    }

    public boolean isEnabled() {
        return moduleEnabled || forcedOn;
    }

    public void loadConfig(Configuration c) {
        String cat = GemsConfig.CAT_MISC + c.CATEGORY_SPLITTER + "halloween_hijinks";
        c.setCategoryComment(cat,
                "Halloween event options. WARNING: This feature is known to cause some lag spikes, as it"
                        + " loads skins. The spikes may reduce in frequency as you play.");

        moduleEnabled = c.getBoolean("Enabled", cat, true,
                "Some mobs may dress up for the end of October.");
        forcedOn = c.getBoolean("Forced On", cat, false,
                "Halloween all year round! (See the warning in the category comment first).");
        costumeChance = c.getFloat("Costume Chance", cat, 0.3f, 0f, 1f,
                "The chance of a mob receiving a \"costume\".");
        costumeDropRate = c.getFloat("Costume Drop Rate", cat, 0.01f, 0f, 1f,
                "The chance that a mob will drop their \"costume\" when killed.");
    }

    @SubscribeEvent
    public void onLivingSpawn(LivingUpdateEvent event) {
        if (!forcedOn && !(rightDay && moduleEnabled)) {
            return;
        }

        // Hostile mobs only.
        Entity entity = event.getEntity();
        if (entity instanceof EntityLiving && entity instanceof IMob) {
            // Check for marker.
            EntityLivingBase entityLiving = event.getEntityLiving();
            if (!entityLiving.getEntityData().getBoolean(NBT_MARKER)) {
                // Mark the entity as processed, even if it does not receive a costume.
                entityLiving.getEntityData().setBoolean(NBT_MARKER, true);
                if (SilentGems.random.nextFloat() < costumeChance) {
                    // Give costume
                    ItemStack skull = Skulls.selectRandom(SilentGems.random);
                    entityLiving.setItemStackToSlot(EntityEquipmentSlot.HEAD, skull);
                    ((EntityLiving) entityLiving).setDropChance(EntityEquipmentSlot.HEAD, costumeDropRate);
                }
            }
        }
    }

    private long ticksRunning = 0;

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
