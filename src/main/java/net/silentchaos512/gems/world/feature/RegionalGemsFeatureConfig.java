package net.silentchaos512.gems.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.world.GemsWorldFeatures;

import javax.annotation.Nullable;
import java.util.Random;

public class RegionalGemsFeatureConfig implements IFeatureConfig, IRegionalGemsConfig {
    public static final Codec<RegionalGemsFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Gems.Set.CODEC.fieldOf("gem_set").forGetter(config -> config.gemSet),
                    Codec.INT.fieldOf("size").forGetter(config -> config.size),
                    Codec.INT.fieldOf("region_size").forGetter(config -> config.regionSize),
                    RuleTest.field_237127_c_.fieldOf("target").forGetter(config -> config.target)
            ).apply(instance, (RegionalGemsFeatureConfig::new)));

    protected final Gems.Set gemSet;
    public final int size;
    public final int regionSize;
    public final RuleTest target;

    public RegionalGemsFeatureConfig(Gems.Set gemSet, int size, int regionSize) {
        this(gemSet, size, regionSize, new TagMatchRuleTest(GemsWorldFeatures.getOreGenTargetBlock(gemSet)));
    }

    public RegionalGemsFeatureConfig(Gems.Set gemSet, int size, int regionSize, RuleTest target) {
        this.gemSet = gemSet;
        this.size = size;
        this.regionSize = regionSize;
        this.target = target;
    }

    @Nullable
    public Gems selectGem(ISeedReader world, BlockPos pos, Random random) {
        return selectGem(world, pos, random, gemSet, regionSize);
    }
}
