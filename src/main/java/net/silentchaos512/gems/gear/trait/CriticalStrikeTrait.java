package net.silentchaos512.gems.gear.trait;

import com.google.gson.JsonObject;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.*;
import net.silentchaos512.gear.api.traits.ITraitSerializer;
import net.silentchaos512.gear.api.traits.TraitActionContext;
import net.silentchaos512.gear.gear.trait.SimpleTrait;
import net.silentchaos512.gems.GemsBase;

public class CriticalStrikeTrait extends SimpleTrait {
    public static final ITraitSerializer<CriticalStrikeTrait> SERIALIZER = new Serializer<>(
            GemsBase.getId("critical_strike"),
            CriticalStrikeTrait::new,
            (trait, json) -> {
                trait.damageMulti = JSONUtils.getFloat(json, "damage_multiplier");
                trait.activationChance = JSONUtils.getFloat(json, "activation_chance");
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
            target.world.playSound(null, target.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1.0f, 1.5f);
            return baseValue * (1f + this.damageMulti) * context.getTraitLevel();
        }
        return baseValue;
    }

    public static void serialize(JsonObject json, float damageMulti, float activationChance) {
        json.addProperty("damage_multiplier", damageMulti);
        json.addProperty("activation_chance", activationChance);
    }
}
