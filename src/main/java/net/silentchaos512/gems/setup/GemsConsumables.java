package net.silentchaos512.gems.setup;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

import java.util.List;

public class GemsConsumables {
    public static final Consumable SUGAR_COOKIE = Consumables.defaultFood()
            .onConsume(
                    new ApplyStatusEffectsConsumeEffect(
                            List.of(
                                    new MobEffectInstance(MobEffects.SPEED, 600),
                                    new MobEffectInstance(MobEffects.HASTE, 400)
                            )
                    )
            )
            .build();
    public static final Consumable CUP_OF_COFFEE = Consumables.defaultDrink()
            .onConsume(
                    new ApplyStatusEffectsConsumeEffect(
                            List.of(
                                    new MobEffectInstance(MobEffects.SPEED, 3600),
                                    new MobEffectInstance(MobEffects.HASTE, 1800)
                            )
                    )
            )
            .build();
    public static final Consumable IRON_POTATO = Consumables.defaultFood()
            .onConsume(
                    new ApplyStatusEffectsConsumeEffect(
                            List.of(
                                    new MobEffectInstance(MobEffects.ABSORPTION, 10 * 60 * 20, 4),
                                    new MobEffectInstance(MobEffects.RESISTANCE, 5 * 60 * 20, 0),
                                    new MobEffectInstance(MobEffects.STRENGTH, 5 * 60 * 20, 1)
                            )
                    )
            )
            .build();
}
