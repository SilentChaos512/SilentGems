package net.silentchaos512.gems.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.silentchaos512.gear.api.data.trait.TraitBuilder;
import net.silentchaos512.gear.api.data.trait.TraitsProviderBase;
import net.silentchaos512.gear.gear.trait.effect.AttributeTraitEffect;
import net.silentchaos512.gear.gear.trait.effect.ItemMagnetTraitEffect;
import net.silentchaos512.gear.gear.trait.effect.NumberPropertyModifierTraitEffect;
import net.silentchaos512.gear.gear.trait.effect.WielderEffectTraitEffect;
import net.silentchaos512.gear.setup.gear.GearProperties;
import net.silentchaos512.gear.setup.gear.GearTypes;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.gear.trait.CriticalStrikeTraitEffect;
import net.silentchaos512.gems.setup.GemsTraits;

import java.util.ArrayList;
import java.util.Collection;

public class GemsTraitsProvider extends TraitsProviderBase {
    public GemsTraitsProvider(DataGenerator generator) {
        super(generator, SilentGems.MOD_ID);
    }

    @Override
    public Collection<TraitBuilder> getTraits() {
        Collection<TraitBuilder> ret = new ArrayList<>();

        ret.add(TraitBuilder.of(GemsTraits.BARRIER_JACKET, 5)
                .effects(
                        NumberPropertyModifierTraitEffect.builder()
                                .add(GearProperties.MAGIC_ARMOR, -0.1f, true, true)
                                .build()
                )
        );

        ret.add(TraitBuilder.of(GemsTraits.BOOSTER, 5)
                .effects(
                        WielderEffectTraitEffect.builder()
                                .add(
                                        GearTypes.CURIO,
                                        WielderEffectTraitEffect.LevelType.TRAIT_LEVEL,
                                        MobEffects.MOVEMENT_SPEED,
                                        1, 2, 3, 4, 5
                                )
                                .build()
                )
        );

        ret.add(TraitBuilder.of(GemsTraits.CLOAKING, 1)
                .effects(
                        WielderEffectTraitEffect.builder()
                                .add(
                                        GearTypes.CURIO,
                                        WielderEffectTraitEffect.LevelType.TRAIT_LEVEL,
                                        MobEffects.INVISIBILITY,
                                        1
                                )
                                .add(
                                        GearTypes.CURIO,
                                        WielderEffectTraitEffect.LevelType.TRAIT_LEVEL,
                                        MobEffects.HUNGER,
                                        2
                                )
                                .build()
                )
        );

        ret.add(TraitBuilder.of(GemsTraits.CRITICAL_STRIKE, 1)
                .effects(
                        new CriticalStrikeTraitEffect(0.5f, 0.25f)
                )
        );

        ret.add(TraitBuilder.of(GemsTraits.FRACTAL, 5)
                .effects(
                        new ItemMagnetTraitEffect(
                                0.08f,
                                4.0f,
                                Ingredient.of(Tags.Items.GEMS),
                                "gems"
                        ),
                        NumberPropertyModifierTraitEffect.builder()
                                .add(GearProperties.ARMOR_TOUGHNESS, 0.075f, true, true)
                                .add(GearProperties.MAGIC_ARMOR, -0.075f, true, true)
                                .build()
                )
        );

        ret.add(TraitBuilder.of(GemsTraits.HEARTY, 6)
                .effects(
                        AttributeTraitEffect.builder()
                                .add(
                                        GearTypes.CURIO,
                                        EquipmentSlotGroup.ANY,
                                        Attributes.MAX_HEALTH,
                                        AttributeModifier.Operation.ADD_VALUE,
                                        1, 2, 3, 4, 5, 6
                                )
                                .build()
                )
        );

        ret.add(TraitBuilder.of(GemsTraits.LEAPING, 5)
                .effects(
                        WielderEffectTraitEffect.builder()
                                .add(
                                        GearTypes.CURIO,
                                        WielderEffectTraitEffect.LevelType.TRAIT_LEVEL,
                                        MobEffects.JUMP,
                                        1, 2, 3, 4, 5
                                )
                                .add(
                                        GearTypes.CURIO,
                                        WielderEffectTraitEffect.LevelType.TRAIT_LEVEL,
                                        MobEffects.SLOW_FALLING,
                                        1, 1, 1, 1, 1
                                )
                                .build()
                )
        );

        return ret;
    }
}
