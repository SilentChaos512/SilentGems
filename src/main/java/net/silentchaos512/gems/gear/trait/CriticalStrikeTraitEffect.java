package net.silentchaos512.gems.gear.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.silentchaos512.gear.api.traits.TraitActionContext;
import net.silentchaos512.gear.api.traits.TraitEffect;
import net.silentchaos512.gear.api.traits.TraitEffectType;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.setup.GemsTraits;

import java.util.Collection;
import java.util.Collections;

public class CriticalStrikeTraitEffect extends TraitEffect {
    public static final MapCodec<CriticalStrikeTraitEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("damage_multiplier").forGetter(e -> e.damageMultiplier),
                    Codec.FLOAT.fieldOf("activation_chance").forGetter(e -> e.activationChance)
            ).apply(instance, CriticalStrikeTraitEffect::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CriticalStrikeTraitEffect> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, e -> e.damageMultiplier,
            ByteBufCodecs.FLOAT, e -> e.activationChance,
            CriticalStrikeTraitEffect::new
    );

    private final float damageMultiplier;
    private final float activationChance;

    public CriticalStrikeTraitEffect(float damageMultiplier, float activationChance) {
        this.damageMultiplier = damageMultiplier;
        this.activationChance = activationChance;
    }

    @Override
    public TraitEffectType<?> type() {
        return GemsTraits.EffectTypes.CRITICAL_STRIKE.get();
    }

    @Override
    public float onAttackEntity(TraitActionContext context, LivingEntity target, float baseValue) {
        if (SilentGems.RANDOM.nextFloat() < this.activationChance) {
            target.level().playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0f, 1.5f);
            return baseValue * (1f + this.damageMultiplier) * context.traitLevel();
        }
        return baseValue;
    }

    @Override
    public Collection<String> getExtraWikiLines() {
        return Collections.singleton(
                String.format(
                        "Attacks deal %d%% more damage per level about %d%% of the time",
                        (int) (100 * this.damageMultiplier),
                        (int) (100 * this.activationChance)
                )
        );
    }
}
