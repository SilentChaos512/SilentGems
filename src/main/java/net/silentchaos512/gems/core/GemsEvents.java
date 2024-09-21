package net.silentchaos512.gems.core;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.setup.GemsAttachmentTypes;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.setup.GemsTags;
import net.silentchaos512.lib.util.TimeUtils;

@EventBusSubscriber
public class GemsEvents {
    public static final int COFFEE_TIMER_DELAY = TimeUtils.ticksFromMinutes(10f);

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        handleCoffeeTimerTicks(event);
    }

    private static void handleCoffeeTimerTicks(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity livingEntity && livingEntity.getType().is(GemsTags.EntityTypes.COFFEE_PRODUCERS)) {
            int coffeeTimer = livingEntity.getData(GemsAttachmentTypes.COFFEE_TIMER);
            if (coffeeTimer >= COFFEE_TIMER_DELAY) {
                spawnCoffeeAtEntity(livingEntity);
                livingEntity.setData(GemsAttachmentTypes.COFFEE_TIMER, 0);
            } else {
                livingEntity.setData(GemsAttachmentTypes.COFFEE_TIMER, coffeeTimer + 1);
            }
        }
    }

    private static void spawnCoffeeAtEntity(LivingEntity livingEntity) {
        var itemEntity = livingEntity.spawnAtLocation(GemsItems.CUP_OF_COFFEE);
        if (itemEntity != null) {
            itemEntity.setDeltaMovement(
                    itemEntity.getDeltaMovement()
                            .add(
                                    (SilentGems.RANDOM.nextFloat() - SilentGems.RANDOM.nextFloat()) * 0.1F,
                                    SilentGems.RANDOM.nextFloat() * 0.05F,
                                    (SilentGems.RANDOM.nextFloat() - SilentGems.RANDOM.nextFloat()) * 0.1F
                            )
            );

            livingEntity.level().playSound(
                    null,
                    livingEntity.position().x,
                    livingEntity.position().y,
                    livingEntity.position().z,
                    SoundEvents.FROGLIGHT_STEP,
                    SoundSource.NEUTRAL
            );
        }
    }
}
