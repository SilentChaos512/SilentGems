package net.silentchaos512.gems.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.TagKey;
import net.silentchaos512.gear.api.data.material.MaterialBuilder;
import net.silentchaos512.gear.api.data.material.MaterialsProviderBase;
import net.silentchaos512.gear.api.material.IMaterialCategory;
import net.silentchaos512.gear.api.material.TextureType;
import net.silentchaos512.gear.api.property.HarvestTier;
import net.silentchaos512.gear.api.util.DataResource;
import net.silentchaos512.gear.gear.material.MaterialCategories;
import net.silentchaos512.gear.gear.trait.condition.MaterialRatioTraitCondition;
import net.silentchaos512.gear.setup.gear.PartTypes;
import net.silentchaos512.gear.util.Const;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.setup.GemsTraits;
import net.silentchaos512.gems.util.Gems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class GemsMaterialsProvider extends MaterialsProviderBase {
    public GemsMaterialsProvider(DataGenerator generator) {
        super(generator, SilentGems.MOD_ID);
    }

    @Override
    protected Collection<MaterialBuilder<?>> getMaterials() {
        Collection<MaterialBuilder<?>> ret = new ArrayList<>();

        ret.add(gem(Gems.RUBY, MaterialCategories.INTERMEDIATE) // durability
                .mainStatsCommon(1024, 34, 12, 30, 1.3f)
                .mainStatsHarvest(harvestTier(Gems.RUBY), 6) // iron
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(1, 0)
                .mainStatsArmor(2, 6, 5, 2, 4, 6) //15
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.IMPERIAL, 2)
        );
        ret.add(gem(Gems.CARNELIAN, MaterialCategories.INTERMEDIATE) // durability
                .mainStatsCommon(1280, 37, 10, 45, 1.2f)
                .mainStatsHarvest(harvestTier(Gems.CARNELIAN), 7) // iron
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(3, 6, 5, 2, 4, 8) //16
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.GOLD_DIGGER, 2)
        );
        ret.add(gem(Gems.TOPAZ, MaterialCategories.INTERMEDIATE) // speed
                .mainStatsCommon(512, 21, 12, 30, 1.3f)
                .mainStatsHarvest(harvestTier(Gems.TOPAZ), 8) // iron
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(1, 0)
                .mainStatsArmor(2, 6, 5, 2, 4, 6) //15
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.LUSTROUS, 1)
                .trait(PartTypes.MAIN, Const.Traits.BRITTLE, 2)
        );
        ret.add(gem(Gems.CITRINE, MaterialCategories.INTERMEDIATE) // speed
                .mainStatsCommon(768, 26, 10, 45, 1.2f)
                .mainStatsHarvest(harvestTier(Gems.CITRINE), 10) // iron
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(3, 6, 5, 2, 4, 8) //16
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.ANCIENT, 4)
                .trait(PartTypes.MAIN, Const.Traits.BRITTLE, 3)
                .trait(PartTypes.SETTING, GemsTraits.BOOSTER, 1)
        );
        ret.add(gem(Gems.HELIODOR, MaterialCategories.ADVANCED) // speed
                .mainStatsCommon(1024, 34, 10, 60, 1.2f)
                .mainStatsHarvest(harvestTier(Gems.HELIODOR), 14) // diamond
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(1, 0)
                .mainStatsArmor(3, 6, 5, 2, 4, 8) //16
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.CHIPPING, 4)
                .trait(PartTypes.MAIN, Const.Traits.BRITTLE, 2)
                .trait(PartTypes.SETTING, Const.Traits.MIGHTY, 4)
        );
        ret.add(gem(Gems.MOLDAVITE, MaterialCategories.INTERMEDIATE) // damage
                .mainStatsCommon(512, 26, 10, 45, 1.2f)
                .mainStatsHarvest(harvestTier(Gems.MOLDAVITE), 6) // iron
                .mainStatsMelee(4, 0, 0)
                .mainStatsRanged(3, 0)
                .mainStatsArmor(3, 6, 5, 2, 4, 8) //16
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.SHARP, 3)
                .trait(PartTypes.MAIN, Const.Traits.BRITTLE, 3)
        );
        ret.add(gem(Gems.PERIDOT, MaterialCategories.INTERMEDIATE) // damage
                .mainStatsCommon(512, 21, 12, 30, 1.3f)
                .mainStatsHarvest(harvestTier(Gems.PERIDOT), 6) // iron
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(2, 6, 5, 2, 4, 6) //15
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.JAGGED, 2)
                .trait(PartTypes.MAIN, Const.Traits.BRITTLE, 3)
                .trait(PartTypes.SETTING, GemsTraits.HEARTY, 2)
        );
        ret.add(gem(Gems.TURQUOISE, MaterialCategories.ADVANCED) // all-rounder, magic armor
                .mainStatsCommon(1536, 40, 15, 45, 1.3f)
                .mainStatsHarvest(harvestTier(Gems.TURQUOISE), 8) // diamond
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(2, 6, 5, 2, 6, 20) //15
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.TURTLE, 1)
                .trait(PartTypes.MAIN, GemsTraits.BARRIER_JACKET, 5)
                .trait(PartTypes.SETTING, GemsTraits.LEAPING, 3)
        );
        ret.add(gem(Gems.KYANITE, MaterialCategories.ADVANCED) // all-rounder
                .mainStatsCommon(1280, 38, 17, 60, 1.4f)
                .mainStatsHarvest(harvestTier(Gems.KYANITE), 12) // netherite
                .mainStatsMelee(4, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(3, 8, 6, 3, 8, 14) //20
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.CHILLED, 4)
                .trait(PartTypes.MAIN, Const.Traits.STELLAR, 2)
        );
        ret.add(gem(Gems.SAPPHIRE, MaterialCategories.INTERMEDIATE) // armor
                .mainStatsCommon(512, 28, 12, 30, 1.3f)
                .mainStatsHarvest(harvestTier(Gems.SAPPHIRE), 6) // iron
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(1, 0)
                .mainStatsArmor(3, 8, 5, 2, 6, 10) //18
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.AQUATIC, 2)
                .trait(PartTypes.MAIN, Const.Traits.BRITTLE, 2)
        );
        ret.add(gem(Gems.IOLITE, MaterialCategories.INTERMEDIATE) // armor
                .mainStatsCommon(768, 32, 10, 45, 1.2f)
                .mainStatsHarvest(harvestTier(Gems.IOLITE), 7) // iron
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(3, 8, 6, 3, 6, 14) //20
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.CRUSHING, 3)
                .trait(PartTypes.MAIN, Const.Traits.BRITTLE, 2)
                .trait(PartTypes.SETTING, Const.Traits.CURE_POISON, 1)
        );
        ret.add(gem(Gems.ALEXANDRITE, MaterialCategories.INTERMEDIATE) // all-rounder
                .mainStatsCommon(1024, 29, 15, 45, 1.3f)
                .mainStatsHarvest(harvestTier(Gems.ALEXANDRITE), 9) // diamond
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(3, 8, 5, 2, 5, 10) //18
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.IMPERIAL, 5)
                .trait(PartTypes.MAIN, Const.Traits.BRITTLE, 1)
                .trait(PartTypes.SETTING, GemsTraits.CLOAKING, 1)
        );
        ret.add(gem(Gems.AMMOLITE, MaterialCategories.ADVANCED) // armor
                .mainStatsCommon(1024, 40, 12, 60, 1.2f)
                .mainStatsHarvest(harvestTier(Gems.AMMOLITE), 8) // diamond
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(1, 0)
                .mainStatsArmor(4, 9, 6, 3, 12, 16) //22
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, GemsTraits.FRACTAL, 4)
                .trait(PartTypes.MAIN, Const.Traits.BRITTLE, 3)
        );
        ret.add(gem(Gems.ROSE_QUARTZ, MaterialCategories.ADVANCED) // damage
                .mainStatsCommon(1024, 34, 12, 60, 1.2f)
                .mainStatsHarvest(harvestTier(Gems.ROSE_QUARTZ), 14) // netherite
                .mainStatsMelee(6, 0, 0)
                .mainStatsRanged(3, 0)
                .mainStatsArmor(3, 6, 5, 2, 4, 8) //16
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, GemsTraits.CRITICAL_STRIKE, 1)
                .trait(PartTypes.MAIN, Const.Traits.BRITTLE, 3)
        );
        ret.add(gem(Gems.BLACK_DIAMOND, MaterialCategories.ADVANCED) // super
                .mainStatsCommon(1792, 39, 12, 55, 1.0f)
                .mainStatsHarvest(harvestTier(Gems.BLACK_DIAMOND), 9) // netherite
                .mainStatsMelee(4, 0, 0)
                .mainStatsRanged(3, 0)
                .mainStatsArmor(3, 8, 6, 3, 8, 10) //20
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.FLAME_WARD, 1,
                        new MaterialRatioTraitCondition(0.7f))
                .trait(PartTypes.MAIN, Const.Traits.STURDY, 2)
                .trait(PartTypes.SETTING, Const.Traits.CURE_WITHER, 1)
        );
        ret.add(gem(Gems.WHITE_DIAMOND, MaterialCategories.ADVANCED) // super
                .mainStatsCommon(2048, 44, 14, 70, 1.0f)
                .mainStatsHarvest(harvestTier(Gems.WHITE_DIAMOND), 12) // netherite
                .mainStatsMelee(5, 0, 0)
                .mainStatsRanged(3, 0)
                .mainStatsArmor(4, 9, 7, 4, 16, 20) //24
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.VOID_WARD, 1,
                        new MaterialRatioTraitCondition(0.7f))
                .trait(PartTypes.MAIN, Const.Traits.STURDY, 2)
                .trait(PartTypes.SETTING, Const.Traits.MOONWALKER, 2)
        );

        return ret;
    }

    private static MaterialBuilder<?> gem(Gems gem, IMaterialCategory tier) {
        return MaterialBuilder.simple(DataResource.material(SilentGems.getId(gem.getName())))
                .crafting(
                        gem.getItemTag(),
                        MaterialCategories.GEM, tier
                )
                .display(
                        gem.getDisplayName(),
                        gem.getColor(),
                        TextureType.HIGH_CONTRAST
                );
    }

    public static HarvestTier harvestTier(Gems gem) {
        return new HarvestTier(
                gem.getName(),
                Optional.of(gem.getHarvestTierLevelHint()),
                TagKey.create(Registries.BLOCK, SilentGems.getId("incorrect_for_" + gem.getName() + "_tools"))
        );
    }
}
