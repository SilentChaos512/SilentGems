package net.silentchaos512.gems.data;

import net.minecraft.data.DataGenerator;
import net.silentchaos512.gear.api.part.PartType;
import net.silentchaos512.gear.data.material.MaterialBuilder;
import net.silentchaos512.gear.data.material.MaterialsProvider;
import net.silentchaos512.gear.gear.material.MaterialCategories;
import net.silentchaos512.gear.gear.part.PartTextureSet;
import net.silentchaos512.gear.util.Const;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.setup.GemsStats;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.gems.util.GemsConst;

import java.util.ArrayList;
import java.util.Collection;

public class GemsMaterialsProvider extends MaterialsProvider {
    public GemsMaterialsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected Collection<MaterialBuilder> getMaterials() {
        Collection<MaterialBuilder> ret = new ArrayList<>();

        ret.add(gem(Gems.RUBY, 3) // durability
                .mainStatsCommon(1024, 34, 12, 30)
                .mainStatsHarvest(2, 6)
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(1, 0)
                .mainStatsArmor(15, 4, 6)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.3f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.IMPERIAL, 2)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 1)
        );
        ret.add(gem(Gems.CARNELIAN, 3) // durability
                .mainStatsCommon(1280, 37, 10, 45)
                .mainStatsHarvest(2, 7)
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(16, 4, 8)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.2f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 3)
        );
        ret.add(gem(Gems.TOPAZ, 3) // speed
                .mainStatsCommon(512, 21, 12, 30)
                .mainStatsHarvest(2, 8)
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(1, 0)
                .mainStatsArmor(15, 4, 6)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.3f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.LUSTROUS, 1)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 2)
        );
        ret.add(gem(Gems.CITRINE, 3) // speed
                .mainStatsCommon(768, 26, 10, 45)
                .mainStatsHarvest(2, 10)
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(16, 4, 8)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.2f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 4)
                .trait(PartType.ADORNMENT, GemsConst.Traits.BOOSTER, 1)
        );
        ret.add(gem(Gems.HELIODOR, 4) // speed
                .mainStatsCommon(1024, 34, 10, 60)
                .mainStatsHarvest(3, 14)
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(1, 0)
                .mainStatsArmor(16, 4, 8)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.2f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.CHIPPING, 4)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 3)
                .trait(PartType.ADORNMENT, Const.Traits.MIGHTY, 4)
        );
        ret.add(gem(Gems.MOLDAVITE, 3) // damage
                .mainStatsCommon(512, 26, 10, 45)
                .mainStatsHarvest(2, 6)
                .mainStatsMelee(4, 0, 0)
                .mainStatsRanged(3, 0)
                .mainStatsArmor(16, 4, 8)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.2f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.SHARP, 3)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 3)
        );
        ret.add(gem(Gems.PERIDOT, 3) // damage
                .mainStatsCommon(512, 21, 12, 30)
                .mainStatsHarvest(2, 6)
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(15, 4, 6)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.3f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.JAGGED, 2)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 3)
                .trait(PartType.ADORNMENT, GemsConst.Traits.HEARTY, 2)
        );
        ret.add(gem(Gems.TURQUOISE, 4) // all-rounder, magic armor
                .mainStatsCommon(1536, 40, 15, 45)
                .mainStatsHarvest(3, 8)
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(15, 6, 20)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.2f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, GemsConst.Traits.BARRIER_JACKET, 1)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 1)
                .trait(PartType.ADORNMENT, GemsConst.Traits.LEAPING, 3)
        );
        ret.add(gem(Gems.KYANITE, 4) // all-rounder
                .mainStatsCommon(1280, 38, 17, 60)
                .mainStatsHarvest(4, 12)
                .mainStatsMelee(4, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(20, 8, 14)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.2f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 1)
        );
        ret.add(gem(Gems.SAPPHIRE, 3) // armor
                .mainStatsCommon(512, 28, 12, 30)
                .mainStatsHarvest(2, 6)
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(1, 0)
                .mainStatsArmor(18, 6, 10)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.3f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.AQUATIC, 2)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 2)
        );
        ret.add(gem(Gems.IOLITE, 3) // armor
                .mainStatsCommon(768, 32, 10, 45)
                .mainStatsHarvest(2, 7)
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(20, 6, 14)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.2f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.CRUSHING, 3)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 2)
                .trait(PartType.ADORNMENT, Const.Traits.CURE_POISON, 1)
        );
        ret.add(gem(Gems.ALEXANDRITE, 3) // all-rounder
                .mainStatsCommon(1024, 29, 15, 45)
                .mainStatsHarvest(3, 9)
                .mainStatsMelee(3, 0, 0)
                .mainStatsRanged(2, 0)
                .mainStatsArmor(18, 5, 10)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.2f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.IMPERIAL, 5)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 1)
                .trait(PartType.ADORNMENT, GemsConst.Traits.CLOAKING, 1)
        );
        ret.add(gem(Gems.AMMOLITE, 4) // armor
                .mainStatsCommon(1024, 40, 12, 60)
                .mainStatsHarvest(3, 8)
                .mainStatsMelee(2, 0, 0)
                .mainStatsRanged(1, 0)
                .mainStatsArmor(22, 12, 16)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.2f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 3)
        );
        ret.add(gem(Gems.ROSE_QUARTZ, 4) // damage
                .mainStatsCommon(1024, 34, 12, 60)
                .mainStatsHarvest(4, 14)
                .mainStatsMelee(6, 0, 0)
                .mainStatsRanged(3, 0)
                .mainStatsArmor(16, 4, 8)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.2f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 3)
        );
        ret.add(gem(Gems.BLACK_DIAMOND, 4) // super
                .mainStatsCommon(1792, 39, 12, 55)
                .mainStatsHarvest(4, 9)
                .mainStatsMelee(4, 0, 0)
                .mainStatsRanged(3, 0)
                .mainStatsArmor(20, 8, 10)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.1f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.ADORNMENT, Const.Traits.CURE_WITHER, 1)
        );
        ret.add(gem(Gems.WHITE_DIAMOND, 4) // super
                .mainStatsCommon(2048, 44, 14, 70)
                .mainStatsHarvest(4, 12)
                .mainStatsMelee(5, 0, 0)
                .mainStatsRanged(3, 0)
                .mainStatsArmor(24, 16, 20)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.1f)
                .noStats(PartType.ROD)
                .noStats(PartType.ADORNMENT)
        );

        return ret;
    }

    private static MaterialBuilder gem(Gems gem, int tier) {
        int color = gem.getColor();
        return new MaterialBuilder(GemsBase.getId(gem.getName()), tier, gem.getItemTag())
                .categories(MaterialCategories.GEM)
                .name(gem.getDisplayName())
                .display(PartType.MAIN, PartTextureSet.HIGH_CONTRAST_WITH_HIGHLIGHT, color)
                .display(PartType.ROD, PartTextureSet.HIGH_CONTRAST, color)
                .displayAdornment(PartTextureSet.HIGH_CONTRAST_WITH_HIGHLIGHT, color);
    }
}
