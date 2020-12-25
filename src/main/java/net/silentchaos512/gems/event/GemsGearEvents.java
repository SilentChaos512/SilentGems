package net.silentchaos512.gems.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.soul.GearSoul;
import net.silentchaos512.gems.util.SoulManager;

@Mod.EventBusSubscriber(modid = SilentGems.MOD_ID)
public final class GemsGearEvents {
    private GemsGearEvents() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            float amountPerPiece = event.getAmount() / 4f;

            for (ItemStack gear : event.getEntityLiving().getEquipmentAndArmor()) {
                if (gear.getItem() instanceof ArmorItem) {
                    GearSoul soul = SoulManager.getSoul(gear);

                    if (soul != null) {
                        SoulManager.addSoulXp((int) (GearSoul.XP_FACTOR_DAMAGE_TAKEN * amountPerPiece), gear, null);
                    }
                }
            }
        }
    }
}
