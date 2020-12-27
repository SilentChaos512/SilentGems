package net.silentchaos512.gems.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effects;
import net.silentchaos512.gear.api.item.GearType;
import net.silentchaos512.gear.api.stats.ItemStats;
import net.silentchaos512.gear.data.trait.*;
import net.silentchaos512.gear.gear.trait.PotionEffectTrait;
import net.silentchaos512.gems.util.GemsConst;

import java.util.ArrayList;
import java.util.Collection;

public class GemsTraitsProvider extends TraitsProvider {
    public GemsTraitsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected Collection<TraitBuilder> getTraits() {
        Collection<TraitBuilder> ret = new ArrayList<>();

        ret.add(new StatModifierTraitBuilder(GemsConst.Traits.BARRIER_JACKET, 1)
                .addStatMod(ItemStats.MAGIC_ARMOR, -0.5f, true, true)
        );

        ret.add(new PotionTraitBuilder(GemsConst.Traits.BOOSTER, 5)
                .addEffect(GearType.CURIO, PotionEffectTrait.LevelType.TRAIT_LEVEL, Effects.SPEED, 1, 2, 3, 4, 5)
        );

        ret.add(new PotionTraitBuilder(GemsConst.Traits.CLOAKING, 1)
                .addEffect(GearType.CURIO, PotionEffectTrait.LevelType.TRAIT_LEVEL, Effects.INVISIBILITY, 1)
                .addEffect(GearType.CURIO, PotionEffectTrait.LevelType.TRAIT_LEVEL, Effects.HUNGER, 2)
        );

        ret.add(new AttributeTraitBuilder(GemsConst.Traits.HEARTY, 6)
                .addModifier(GearType.CURIO, "", Attributes.MAX_HEALTH, AttributeModifier.Operation.ADDITION, 1, 2, 3, 4, 5, 6)
        );

        ret.add(new PotionTraitBuilder(GemsConst.Traits.LEAPING, 5)
                .addEffect(GearType.CURIO, PotionEffectTrait.LevelType.TRAIT_LEVEL, Effects.JUMP_BOOST, 1, 2, 3, 4, 5)
                .addEffect(GearType.CURIO, PotionEffectTrait.LevelType.TRAIT_LEVEL, Effects.SLOW_FALLING, 1, 1, 1, 1, 1)
        );

        return ret;
    }
}
