package net.silentchaos512.gems.data;

import net.minecraft.data.DataGenerator;
import net.silentchaos512.gear.api.part.PartType;
import net.silentchaos512.gear.api.stats.ItemStats;
import net.silentchaos512.gear.data.material.MaterialBuilder;
import net.silentchaos512.gear.data.material.MaterialsProvider;
import net.silentchaos512.gear.gear.material.MaterialCategories;
import net.silentchaos512.gear.gear.part.PartTextureSet;
import net.silentchaos512.gear.util.Const;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.setup.GemsStats;
import net.silentchaos512.gems.util.Gems;

import java.util.ArrayList;
import java.util.Collection;

public class GemsMaterialsProvider extends MaterialsProvider {
    public GemsMaterialsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected Collection<MaterialBuilder> getMaterials() {
        Collection<MaterialBuilder> ret = new ArrayList<>();

        ret.add(gem(Gems.RUBY)
                .stat(PartType.MAIN, ItemStats.DURABILITY, 1024)
                .stat(PartType.MAIN, ItemStats.ARMOR_DURABILITY, 24)
                .stat(PartType.MAIN, ItemStats.ENCHANTABILITY, 10)
                .stat(PartType.MAIN, ItemStats.HARVEST_LEVEL, 2)
                .stat(PartType.MAIN, ItemStats.HARVEST_SPEED, 8)
                .stat(PartType.MAIN, ItemStats.MELEE_DAMAGE, 5)
                .stat(PartType.MAIN, ItemStats.MAGIC_DAMAGE, 2)
                .stat(PartType.MAIN, ItemStats.ATTACK_SPEED, -0.2f)
                .stat(PartType.MAIN, ItemStats.ARMOR, 14)
                .stat(PartType.MAIN, ItemStats.ARMOR_TOUGHNESS, 2)
                .stat(PartType.MAIN, ItemStats.MAGIC_ARMOR, 4)
                .stat(PartType.MAIN, ItemStats.RANGED_DAMAGE, 1)
                .stat(PartType.MAIN, ItemStats.RANGED_SPEED, -0.2f)
                .stat(PartType.MAIN, ItemStats.RARITY, 30)
                .stat(PartType.MAIN, GemsStats.CHARGEABILITY.get(), 1.1f)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.MAIN, Const.Traits.BRITTLE, 1)
        );
        ret.add(gem(Gems.CARNELIAN)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.TOPAZ)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.CITRINE)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.HELIODOR)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.MOLDAVITE)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.PERIDOT)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.TURQUOISE)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.KYANITE)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.SAPPHIRE)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.IOLITE)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.ALEXANDRITE)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.AMMOLITE)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.ROSE_QUARTZ)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.BLACK_DIAMOND)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );
        ret.add(gem(Gems.WHITE_DIAMOND)
                .noStats(PartType.MAIN)
                .noStats(PartType.ADORNMENT)
        );

        return ret;
    }

    private static MaterialBuilder gem(Gems gem) {
        int color = gem.getColor();
        return new MaterialBuilder(GemsBase.getId(gem.getName()), 3, gem.getItemTag())
                .categories(MaterialCategories.GEM)
                .name(gem.getDisplayName())
                .display(PartType.MAIN, PartTextureSet.HIGH_CONTRAST_WITH_HIGHLIGHT, color)
                .display(PartType.ROD, PartTextureSet.HIGH_CONTRAST, color)
                .displayAdornment(PartTextureSet.HIGH_CONTRAST_WITH_HIGHLIGHT, color);
    }
}
