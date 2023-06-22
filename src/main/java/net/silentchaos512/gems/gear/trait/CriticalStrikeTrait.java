package net.silentchaos512.gems.gear.trait;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.silentchaos512.gear.api.traits.ITraitSerializer;
import net.silentchaos512.gear.api.traits.TraitActionContext;
import net.silentchaos512.gear.gear.trait.SimpleTrait;
import net.silentchaos512.gems.GemsBase;

import java.util.Collection;
import java.util.Collections;

public class CriticalStrikeTrait extends SimpleTrait {
    public static final ITraitSerializer<CriticalStrikeTrait> SERIALIZER = new Serializer<>(
            GemsBase.getId("critical_strike"),
            CriticalStrikeTrait::new,
            (trait, json) -> {
                trait.damageMulti = GsonHelper.getAsFloat(json, "damage_multiplier");
                trait.activationChance = GsonHelper.getAsFloat(json, "activation_chance");
            },
            (trait, buffer) -> {
                trait.damageMulti = buffer.readFloat();
                trait.activationChance = buffer.readFloat();
            },
            (trait, buffer) -> {
                buffer.writeFloat(trait.damageMulti);
                buffer.writeFloat(trait.activationChance);
            }
    );

    private float damageMulti;
    private float activationChance;

    public CriticalStrikeTrait(ResourceLocation id) {
        super(id, SERIALIZER);
    }

    @Override
    public float onAttackEntity(TraitActionContext context, LivingEntity target, float baseValue) {
        if (GemsBase.RANDOM.nextFloat() < this.activationChance) {
            target.level().playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0f, 1.5f);
            return baseValue * (1f + this.damageMulti) * context.getTraitLevel();
        }
        return baseValue;
    }

    @Override
    public Collection<String> getExtraWikiLines() {
        return Collections.singleton(String.format("Attacks deal %d%% more damage about %d%% of the time",
                (int) (100 * this.damageMulti),
                (int) (100 * this.activationChance)));
    }

    public static void serialize(JsonObject json, float damageMulti, float activationChance) {
        json.addProperty("damage_multiplier", damageMulti);
        json.addProperty("activation_chance", activationChance);
    }
}
