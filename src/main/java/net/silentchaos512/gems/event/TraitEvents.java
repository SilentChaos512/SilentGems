package net.silentchaos512.gems.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.silentchaos512.gear.util.GearHelper;
import net.silentchaos512.gear.util.TraitHelper;
import net.silentchaos512.gems.lib.GemsTraits;
import net.silentchaos512.utils.MathUtils;

public final class TraitEvents {
    public static final TraitEvents INSTANCE = new TraitEvents();

    private TraitEvents() { }

    @SubscribeEvent
    public void criticalHit(CriticalHitEvent event) {
        PlayerEntity player = event.getEntityPlayer();
        ItemStack weapon = player.getHeldItemMainhand();
        if (GearHelper.isGear(weapon)) {
            int critical = TraitHelper.getTraitLevel(weapon, GemsTraits.CRITICAL);
            if (critical > 0 && MathUtils.tryPercentage(0.1 * critical)) {
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
    }
}
