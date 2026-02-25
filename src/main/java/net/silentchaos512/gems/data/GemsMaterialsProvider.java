package net.silentchaos512.gems.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.silentchaos512.gear.api.data.material.MaterialBuilder;
import net.silentchaos512.gear.api.data.material.MaterialsProviderBase;
import net.silentchaos512.gear.api.material.IMaterialCategory;
import net.silentchaos512.gear.api.material.MaterialCraftingData;
import net.silentchaos512.gear.api.material.TextureType;
import net.silentchaos512.gear.api.property.HarvestTier;
import net.silentchaos512.gear.api.util.DataResource;
import net.silentchaos512.gear.gear.material.MaterialCategories;
import net.silentchaos512.gear.gear.trait.condition.MaterialRatioTraitCondition;
import net.silentchaos512.gear.setup.gear.PartTypes;
import net.silentchaos512.gear.util.Const;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.setup.GemsTraits;
import net.silentchaos512.gems.setup.Gems;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class GemsMaterialsProvider extends MaterialsProviderBase {
    public GemsMaterialsProvider(CompletableFuture<HolderLookup.Provider> lookupProvider, DataGenerator generator) {
        super(lookupProvider, generator, SilentGems.MOD_ID);
    }

    @Override
    protected Collection<MaterialBuilder<?>> getMaterials(HolderLookup.Provider provider) {
        var items = provider.lookupOrThrow(Registries.ITEM);
        Collection<MaterialBuilder<?>> ret = new ArrayList<>();

        ret.add(gem(items, Gems.RUBY, MaterialCategories.INTERMEDIATE) // durability
                .mainStatsCommon(1024, 34, 12, 30, 1.3f)
                .mainStatsHarvest(harvestTier(Gems.RUBY), 6) // iron
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(1, 0)
                .mainStatsArmor(2, 6, 5, 2, 4, 6) //15
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.IMPERIAL, 2)
        );
        ret.add(gem(items, Gems.CARNELIAN, MaterialCategories.INTERMEDIATE) // durability
                .mainStatsCommon(1280, 37, 10, 45, 1.2f)
                .mainStatsHarvest(harvestTier(Gems.CARNELIAN), 7) // iron
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(3, 6, 5, 2, 4, 8) //16
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, Const.Traits.GOLD_DIGGER, 2)
        );
        ret.add(gem(items, Gems.TOPAZ, MaterialCategories.INTERMEDIATE) // speed
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
        ret.add(gem(items, Gems.CITRINE, MaterialCategories.INTERMEDIATE) // speed
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
        ret.add(gem(items, Gems.HELIODOR, MaterialCategories.ADVANCED) // speed
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
        ret.add(gem(items, Gems.MOLDAVITE, MaterialCategories.INTERMEDIATE) // damage
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
        ret.add(gem(items, Gems.PERIDOT, MaterialCategories.INTERMEDIATE) // damage
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
        ret.add(gem(items, Gems.TURQUOISE, MaterialCategories.ADVANCED) // all-rounder, magic armor
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
        ret.add(gem(items, Gems.KYANITE, MaterialCategories.ADVANCED) // all-rounder
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
        ret.add(gem(items, Gems.SAPPHIRE, MaterialCategories.INTERMEDIATE) // armor
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
        ret.add(gem(items, Gems.IOLITE, MaterialCategories.INTERMEDIATE) // armor
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
        ret.add(gem(items, Gems.ALEXANDRITE, MaterialCategories.INTERMEDIATE) // all-rounder
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
        ret.add(gem(items, Gems.AMMOLITE, MaterialCategories.ADVANCED) // armor
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
        ret.add(gem(items, Gems.ROSE_QUARTZ, MaterialCategories.ADVANCED) // damage
                .mainStatsCommon(1024, 34, 12, 60, 1.2f)
                .mainStatsHarvest(harvestTier(Gems.ROSE_QUARTZ), 14) // netherite
                .mainStatsMelee(6, 0, 0)
                .mainStatsRanged(3, 0)
                .mainStatsArmor(3, 6, 5, 2, 4, 8) //16
                .noProperties(PartTypes.ROD)
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, GemsTraits.CRITICAL_STRIKE, 3)
                .trait(PartTypes.MAIN, Const.Traits.BRITTLE, 3)
        );
        ret.add(gem(items, Gems.BLACK_DIAMOND, MaterialCategories.ADVANCED) // super
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
        ret.add(gem(items, Gems.WHITE_DIAMOND, MaterialCategories.ADVANCED) // super
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
        ret.add(gem(items, Gems.GARNET, MaterialCategories.INTERMEDIATE) // damage
                .mainStatsCommon(512, 21, 12, 40, 1.3f)
                .mainStatsHarvest(harvestTier(Gems.PERIDOT), 6) // iron
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(2, 6, 5, 2, 4, 6) //15
                .trait(PartTypes.MAIN, GemsTraits.CRITICAL_STRIKE, 1)
                .trait(PartTypes.ROD, Const.Traits.JAGGED, 3)
                .trait(PartTypes.ROD, Const.Traits.BRITTLE, 3)
                .trait(PartTypes.SETTING, GemsTraits.POWER, 2)
        );
        ret.add(gem(items, Gems.AQUAMARINE, MaterialCategories.INTERMEDIATE) // durability
                .mainStatsCommon(1024, 34, 12, 40, 1.3f)
                .mainStatsHarvest(harvestTier(Gems.AQUAMARINE), 6) // iron
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(1, 0)
                .mainStatsArmor(2, 6, 5, 2, 4, 6) //15
                .trait(PartTypes.MAIN, Const.Traits.FORTUNATE, 1)
                .trait(PartTypes.ROD, Const.Traits.FORTUNATE, 1)
                .trait(PartTypes.ROD, Const.Traits.BRITTLE, 3)
                .trait(PartTypes.SETTING, GemsTraits.STEP_UP, 1)
        );
        ret.add(gem(items, Gems.TANZANITE, MaterialCategories.INTERMEDIATE) // all-rounder
                .mainStatsCommon(768, 28, 15, 50, 1.2f)
                .mainStatsHarvest(harvestTier(Gems.TANZANITE), 9) // diamond
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(3, 8, 5, 2, 5, 10) //18
                .trait(PartTypes.MAIN, Const.Traits.LIGHT, 2)
                .trait(PartTypes.ROD, Const.Traits.SHARP, 2)
                .trait(PartTypes.ROD, Const.Traits.BRITTLE, 3)
                .trait(PartTypes.SETTING, GemsTraits.TWINKLETOES, 4)
        );
        ret.add(gem(items, Gems.OPAL, MaterialCategories.ADVANCED) // speed/armor
                .mainStatsCommon(1024, 36, 10, 60, 1.3f)
                .mainStatsHarvest(harvestTier(Gems.OPAL), 13) // diamond
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(1, 0)
                .mainStatsArmor(3, 8, 5, 2, 4, 8) //18
                .trait(PartTypes.MAIN, GemsTraits.FREEZE_RESISTANT, 4)
                .trait(PartTypes.MAIN, GemsTraits.ENDERBANE, 2)
                .trait(PartTypes.ROD, Const.Traits.BRITTLE, 3)
                .trait(PartTypes.ROD, GemsTraits.ENDERBANE, 4)
                .trait(PartTypes.SETTING, GemsTraits.HASTY, 3)
        );
        ret.add(gem(items, Gems.PEARL, MaterialCategories.ADVANCED) // armor
                .mainStatsCommon(1024, 44, 12, 65, 1.3f)
                .mainStatsHarvest(harvestTier(Gems.PEARL), 8) // diamond
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(3, 8, 6, 3, 12, 16) //20
                .noProperties(PartTypes.SETTING)
                .trait(PartTypes.MAIN, GemsTraits.NEPTUNES_BLESSING, 3)
                .trait(PartTypes.ROD, Const.Traits.AQUATIC, 4)
                .trait(PartTypes.ROD, Const.Traits.BRITTLE, 3)
                .trait(PartTypes.SETTING, GemsTraits.NEPTUNES_BLESSING, 4)
        );

        // Reinforced rods
        ret.add(MaterialBuilder.simple(DataResource.material(SilentGems.getId("reinforced_gold")))
                .crafting(
                        new MaterialCraftingData(
                                Optional.empty(),
                                List.of(MaterialCategories.METAL),
                                List.of(),
                                Map.of(PartTypes.ROD.get(), Ingredient.of(GemsItems.REINFORCED_GOLD_ROD)),
                                true
                        )
                )
                .displayWithDefaultName(0xFDFF70, TextureType.HIGH_CONTRAST)
                .trait(PartTypes.ROD, Const.Traits.MALLEABLE, 4)
        );
        ret.add(MaterialBuilder.simple(DataResource.material(SilentGems.getId("reinforced_silver")))
                .crafting(
                        new MaterialCraftingData(
                                Optional.empty(),
                                List.of(MaterialCategories.METAL),
                                List.of(),
                                Map.of(PartTypes.ROD.get(), Ingredient.of(GemsItems.REINFORCED_SILVER_ROD)),
                                true
                        )
                )
                .displayWithDefaultName(0xCBCCEA, TextureType.HIGH_CONTRAST)
                .trait(PartTypes.ROD, Const.Traits.MALLEABLE, 4)
        );

        return ret;
    }

    private static MaterialBuilder<?> gem(HolderLookup.RegistryLookup<Item> items, Gems gem, IMaterialCategory tierCategory) {
        return MaterialBuilder.simple(DataResource.material(SilentGems.getId(gem.getName())))
                .crafting(
                        items,
                        gem.getItemTag(),
                        MaterialCategories.GEM, tierCategory
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
