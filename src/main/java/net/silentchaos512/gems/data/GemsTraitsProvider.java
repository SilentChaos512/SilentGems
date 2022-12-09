package net.silentchaos512.gems.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.silentchaos512.gear.api.data.trait.*;
import net.silentchaos512.gear.api.item.GearType;
import net.silentchaos512.gear.api.stats.ItemStats;
import net.silentchaos512.gear.api.traits.ITrait;
import net.silentchaos512.gear.api.util.DataResource;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.gear.trait.CriticalStrikeTrait;
import net.silentchaos512.gems.setup.GemsTraits;

import java.util.ArrayList;
import java.util.Collection;

public class GemsTraitsProvider extends TraitsProviderBase {
    public GemsTraitsProvider(DataGenerator generator) {
        super(generator, GemsBase.MOD_ID);
    }

    @Override
    public Collection<TraitBuilder> getTraits() {
        Collection<TraitBuilder> ret = new ArrayList<>();

        ret.add(new StatModifierTraitBuilder(GemsTraits.BARRIER_JACKET, 5)
                .addStatMod(ItemStats.MAGIC_ARMOR, -0.1f, true, true)
        );

        ret.add(new WielderEffectTraitBuilder(GemsTraits.BOOSTER, 5)
                .addEffect(GearType.CURIO, WielderEffectTraitBuilder.LevelType.TRAIT_LEVEL, MobEffects.MOVEMENT_SPEED, 1, 2, 3, 4, 5)
        );

        ret.add(new WielderEffectTraitBuilder(GemsTraits.CLOAKING, 1)
                .addEffect(GearType.CURIO, WielderEffectTraitBuilder.LevelType.TRAIT_LEVEL, MobEffects.INVISIBILITY, 1)
                .addEffect(GearType.CURIO, WielderEffectTraitBuilder.LevelType.TRAIT_LEVEL, MobEffects.HUNGER, 2)
        );

        ret.add(criticalStrike(GemsTraits.CRITICAL_STRIKE, 1, 0.5f, 0.25f));

        ret.add(new StatModifierTraitBuilder(GemsTraits.FRACTAL, 5)
                .addStatMod(ItemStats.ARMOR_TOUGHNESS, 0.075f, true, true)
                .addStatMod(ItemStats.MAGIC_ARMOR, -0.075f, true, true)
        );

        ret.add(new AttributeTraitBuilder(GemsTraits.HEARTY, 6)
                .addModifier(GearType.CURIO, "", Attributes.MAX_HEALTH, AttributeModifier.Operation.ADDITION, 1, 2, 3, 4, 5, 6)
        );

        ret.add(new WielderEffectTraitBuilder(GemsTraits.LEAPING, 5)
                .addEffect(GearType.CURIO, WielderEffectTraitBuilder.LevelType.TRAIT_LEVEL, MobEffects.JUMP, 1, 2, 3, 4, 5)
                .addEffect(GearType.CURIO, WielderEffectTraitBuilder.LevelType.TRAIT_LEVEL, MobEffects.SLOW_FALLING, 1, 1, 1, 1, 1)
        );

        return ret;
    }

    private TraitBuilder criticalStrike(DataResource<ITrait> trait, int maxLevel, float damageMulti, float activationChance) {
        return new TraitBuilder(trait, maxLevel, CriticalStrikeTrait.SERIALIZER.getName())
                .extraData(json -> CriticalStrikeTrait.serialize(json, damageMulti, activationChance));
    }
}
