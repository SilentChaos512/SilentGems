package net.silentchaos512.gems.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effects;
import net.silentchaos512.gear.api.item.GearType;
import net.silentchaos512.gear.api.stats.ItemStats;
import net.silentchaos512.gear.api.traits.ITrait;
import net.silentchaos512.gear.data.trait.*;
import net.silentchaos512.gear.gear.trait.PotionEffectTrait;
import net.silentchaos512.gear.util.DataResource;
import net.silentchaos512.gems.gear.trait.CriticalStrikeTrait;
import net.silentchaos512.gems.setup.GemsTraits;

import java.util.ArrayList;
import java.util.Collection;

public class GemsTraitsProvider extends TraitsProvider {
    public GemsTraitsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected Collection<TraitBuilder> getTraits() {
        Collection<TraitBuilder> ret = new ArrayList<>();

        ret.add(new StatModifierTraitBuilder(GemsTraits.BARRIER_JACKET, 5)
                .addStatMod(ItemStats.MAGIC_ARMOR, -0.1f, true, true)
        );

        ret.add(new PotionTraitBuilder(GemsTraits.BOOSTER, 5)
                .addEffect(GearType.CURIO, PotionEffectTrait.LevelType.TRAIT_LEVEL, Effects.MOVEMENT_SPEED, 1, 2, 3, 4, 5)
        );

        ret.add(new PotionTraitBuilder(GemsTraits.CLOAKING, 1)
                .addEffect(GearType.CURIO, PotionEffectTrait.LevelType.TRAIT_LEVEL, Effects.INVISIBILITY, 1)
                .addEffect(GearType.CURIO, PotionEffectTrait.LevelType.TRAIT_LEVEL, Effects.HUNGER, 2)
        );

        ret.add(criticalStrike(GemsTraits.CRITICAL_STRIKE, 1, 0.5f, 0.25f));

        ret.add(new StatModifierTraitBuilder(GemsTraits.FRACTAL, 5)
                .addStatMod(ItemStats.ARMOR_TOUGHNESS, 0.075f, true, true)
                .addStatMod(ItemStats.MAGIC_ARMOR, -0.075f, true, true)
        );

        ret.add(new AttributeTraitBuilder(GemsTraits.HEARTY, 6)
                .addModifier(GearType.CURIO, "", Attributes.MAX_HEALTH, AttributeModifier.Operation.ADDITION, 1, 2, 3, 4, 5, 6)
        );

        ret.add(new PotionTraitBuilder(GemsTraits.LEAPING, 5)
                .addEffect(GearType.CURIO, PotionEffectTrait.LevelType.TRAIT_LEVEL, Effects.JUMP, 1, 2, 3, 4, 5)
                .addEffect(GearType.CURIO, PotionEffectTrait.LevelType.TRAIT_LEVEL, Effects.SLOW_FALLING, 1, 1, 1, 1, 1)
        );

        return ret;
    }

    private TraitBuilder criticalStrike(DataResource<ITrait> trait, int maxLevel, float damageMulti, float activationChance) {
        return new TraitBuilder(trait, maxLevel, CriticalStrikeTrait.SERIALIZER)
                .extraData(json -> CriticalStrikeTrait.serialize(json, damageMulti, activationChance));
    }
}
