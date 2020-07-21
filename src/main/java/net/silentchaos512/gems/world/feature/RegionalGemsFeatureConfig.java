package net.silentchaos512.gems.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.world.GemsWorldFeatures;

import java.util.Random;

public class RegionalGemsFeatureConfig implements IFeatureConfig {
    public static final Codec<RegionalGemsFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Gems.Set.CODEC.fieldOf("gem_set").forGetter(config -> config.gemSet),
                    Codec.INT.fieldOf("size").forGetter(config -> config.size),
                    Codec.INT.fieldOf("region_size").forGetter(config -> config.regionSize),
                    TagMatchRuleTest.field_237161_a_.fieldOf("target_block").forGetter(config -> config.target)
            ).apply(instance, (RegionalGemsFeatureConfig::new)));

    private final Gems.Set gemSet;
    public final int size;
    public final int regionSize;
    public final TagMatchRuleTest target;

    public RegionalGemsFeatureConfig(Gems.Set gemSet, int size, int regionSize) {
        this(gemSet, size, regionSize, new TagMatchRuleTest(GemsWorldFeatures.getOreGenTargetBlock(gemSet)));
    }

    public RegionalGemsFeatureConfig(Gems.Set gemSet, int size, int regionSize, TagMatchRuleTest target) {
        this.gemSet = gemSet;
        this.size = size;
        this.regionSize = regionSize;
        this.target = target;
    }

    public Gems selectGem(ISeedReader world, BlockPos pos, Random random) {
        int regionSizeNonZero = regionSize > 0 ? regionSize : 1;
        int regionX = pos.getX() / (16 * regionSizeNonZero);
        int regionZ = pos.getZ() / (16 * regionSizeNonZero);
        long regionId = ((long) regionX << 32) + regionZ;
        Random regionRand = new Random(regionId + world.getSeed());

        int gemCount = 4 + regionRand.nextInt(3);
        int index = random.nextInt(gemCount);
        Gems gem = gemSet.selectRandom(regionRand);
        for (int i = 0; i < index; ++i) {
            gem = gemSet.selectRandom(regionRand);
        }
        return gem;
    }
}
