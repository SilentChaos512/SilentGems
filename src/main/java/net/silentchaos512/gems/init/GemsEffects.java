package net.silentchaos512.gems.init;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.gems.potion.BaseEffect;
import net.silentchaos512.gems.potion.FreezingEffect;
import net.silentchaos512.gems.potion.ShockingEffect;
import net.silentchaos512.lib.util.TimeUtils;
import net.silentchaos512.utils.Color;

import java.util.function.Supplier;

public final class GemsEffects {
    public static final RegistryObject<FreezingEffect> FREEZING = registerEffect("freezing", FreezingEffect::new);
    public static final RegistryObject<ShockingEffect> SHOCKING = registerEffect("shocking", ShockingEffect::new);
    public static final RegistryObject<Effect> INSULATED = registerEffect("insulated", () ->
            new BaseEffect(EffectType.BENEFICIAL, 0x009499));
    public static final RegistryObject<Effect> GROUNDED = registerEffect("grounded", () ->
            new BaseEffect(EffectType.BENEFICIAL, 0x919900));
    public static final RegistryObject<Effect> CHAOS_SICKNESS = registerEffect("chaos_sickness", () ->
            new BaseEffect(EffectType.HARMFUL, Color.MEDIUMPURPLE.getColor()));

    public static final RegistryObject<Potion> INSULATING_POTION = registerPotion("insulating", () ->
            new Potion(new EffectInstance(INSULATED.get(), TimeUtils.ticksFromMinutes(3))));
    public static final RegistryObject<Potion> GROUNDING_POTION = registerPotion("grounding", () ->
            new Potion(new EffectInstance(GROUNDED.get(), TimeUtils.ticksFromMinutes(3))));

    private GemsEffects() {}

    static void register() {}

    private static <T extends Effect> RegistryObject<T> registerEffect(String name, Supplier<T> effect) {
        return Registration.EFFECTS.register(name, effect);
    }

    private static <T extends Potion> RegistryObject<T> registerPotion(String name, Supplier<T> potion) {
        return Registration.POTIONS.register(name, potion);
    }
}
