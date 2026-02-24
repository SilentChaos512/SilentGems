package net.silentchaos512.gems.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.Tags;
import net.silentchaos512.gear.api.data.trait.TraitBuilder;
import net.silentchaos512.gear.api.data.trait.TraitsProviderBase;
import net.silentchaos512.gear.gear.trait.effect.*;
import net.silentchaos512.gear.setup.gear.GearProperties;
import net.silentchaos512.gear.setup.gear.GearTypes;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.gear.trait.CriticalStrikeTraitEffect;
import net.silentchaos512.gems.setup.GemsTags;
import net.silentchaos512.gems.setup.GemsTraits;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class GemsTraitsProvider extends TraitsProviderBase {
    public GemsTraitsProvider(CompletableFuture<HolderLookup.Provider> lookupProvider, DataGenerator generator) {
        super(lookupProvider, generator, SilentGems.MOD_ID);
    }

    @Override
    public Collection<TraitBuilder> getTraits(HolderLookup.Provider provider) {
        var items = provider.lookupOrThrow(Registries.ITEM);
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
                                        MobEffects.SPEED,
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

        ret.add(TraitBuilder.of(GemsTraits.CRITICAL_STRIKE, 5)
                .effects(
                        new CriticalStrikeTraitEffect(0.5f, 0.1f)
                )
        );

        ret.add(TraitBuilder.of(GemsTraits.ENDERBANE, 5)
                .effects(
                        ExtraDamageTraitEffect.affecting(GemsTags.EntityTypes.END_MONSTERS, 2.0f)
                )
        );

        ret.add(TraitBuilder.of(GemsTraits.FRACTAL, 5)
                .effects(
                        new ItemMagnetTraitEffect(
                                0.08f,
                                4.0f,
                                items.getOrThrow(Tags.Items.GEMS),
                                "gems"
                        ),
                        NumberPropertyModifierTraitEffect.builder()
                                .add(GearProperties.ARMOR_TOUGHNESS, 0.075f, true, true)
                                .add(GearProperties.MAGIC_ARMOR, -0.075f, true, true)
                                .build()
                )
        );

        ret.add(TraitBuilder.of(GemsTraits.FREEZE_RESISTANT, 5)
                .withGearTypeCondition(GearTypes.ARMOR)
                .effects(
                        new NegateDamageTraitEffect(DamageTypeTags.IS_FREEZING, 0.04f)
                )
        );

        ret.add(TraitBuilder.of(GemsTraits.HASTY, 5)
                .effects(
                        AttributeTraitEffect.builder()
                                .add(
                                        GearTypes.CURIO,
                                        EquipmentSlotGroup.ANY,
                                        Attributes.MINING_EFFICIENCY,
                                        AttributeModifier.Operation.ADD_VALUE,
                                        2, 4, 6, 8, 10
                                )
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
                                        MobEffects.JUMP_BOOST,
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

        ret.add(TraitBuilder.of(GemsTraits.NEPTUNES_BLESSING, 5)
                .effects(
                        new NegateDamageTraitEffect(
                                GemsTags.DamageTypes.NEPTUNES_BLESSING_PROTECTS,
                                0.05f
                        ),
                        AttributeTraitEffect.builder()
                                .add(
                                        GearTypes.CURIO,
                                        EquipmentSlotGroup.ANY,
                                        Attributes.OXYGEN_BONUS,
                                        AttributeModifier.Operation.ADD_VALUE,
                                        1, 2, 3, 4, 5
                                )
                                .add(
                                        GearTypes.ARMOR,
                                        EquipmentSlotGroup.ARMOR,
                                        Attributes.OXYGEN_BONUS,
                                        AttributeModifier.Operation.ADD_VALUE,
                                        0.25f, 0.5f, 0.75f, 1.0f, 1.25f
                                )
                                .build()
                )
        );

        ret.add(TraitBuilder.of(GemsTraits.POWER, 5)
                .effects(
                        AttributeTraitEffect.builder()
                                .add(
                                        GearTypes.CURIO,
                                        EquipmentSlotGroup.ANY,
                                        Attributes.ATTACK_DAMAGE,
                                        AttributeModifier.Operation.ADD_VALUE,
                                        1, 2, 3, 4, 5
                                )
                                .build()
                )
                .extraWikiLines("Increases attack damage slightly when on curios")
        );

        ret.add(TraitBuilder.of(GemsTraits.STEP_UP, 1)
                .effects(
                        AttributeTraitEffect.builder()
                                .add(
                                        GearTypes.CURIO,
                                        EquipmentSlotGroup.ANY,
                                        Attributes.STEP_HEIGHT,
                                        AttributeModifier.Operation.ADD_VALUE,
                                        0.5f
                                )
                                .build()
                )
                .extraWikiLines("Increases sneaking speed when on curios")
        );

        ret.add(TraitBuilder.of(GemsTraits.TWINKLETOES, 5)
                .effects(
                        AttributeTraitEffect.builder()
                                .add(
                                        GearTypes.CURIO,
                                        EquipmentSlotGroup.ANY,
                                        Attributes.SNEAKING_SPEED,
                                        AttributeModifier.Operation.ADD_VALUE,
                                        0.1f, 0.2f, 0.3f, 0.4f, 0.5f
                                )
                                .build()
                )
                .extraWikiLines("Increases sneaking speed when on curios")
        );

        return ret;
    }
}
