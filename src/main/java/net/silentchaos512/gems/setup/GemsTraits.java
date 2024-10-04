package net.silentchaos512.gems.setup;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.gear.api.traits.TraitEffectType;
import net.silentchaos512.gear.api.util.DataResource;
import net.silentchaos512.gear.gear.trait.Trait;
import net.silentchaos512.gear.setup.SgRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.gear.trait.CriticalStrikeTraitEffect;

import java.util.function.Supplier;

public final class GemsTraits {
    public static final DataResource<Trait> BARRIER_JACKET = trait("barrier_jacket");
    public static final DataResource<Trait> BOOSTER = trait("booster");
    public static final DataResource<Trait> CLOAKING = trait("cloaking");
    public static final DataResource<Trait> CRITICAL_STRIKE = trait("critical_strike");
    public static final DataResource<Trait> FRACTAL = trait("fractal");
    public static final DataResource<Trait> HEARTY = trait("hearty");
    public static final DataResource<Trait> LEAPING = trait("leaping");

    private GemsTraits() {}

    private static DataResource<Trait> trait(String name) {
        return DataResource.trait(SilentGems.getId(name));
    }

    public static class EffectTypes {
        public static final DeferredRegister<TraitEffectType<?>> REGISTRAR = DeferredRegister.create(SgRegistries.TRAIT_EFFECT_TYPE, SilentGems.MOD_ID);

        public static final Supplier<TraitEffectType<CriticalStrikeTraitEffect>> CRITICAL_STRIKE = REGISTRAR.register(
                "critical_strike",
                () -> new TraitEffectType<>(CriticalStrikeTraitEffect.CODEC, CriticalStrikeTraitEffect.STREAM_CODEC)
        );
    }
}
