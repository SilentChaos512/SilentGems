package net.silentchaos512.gems.world.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.silentchaos512.gems.lib.Gems;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RegionalGlowrosesFeatureConfig extends RegionalGemsFeatureConfig {
    public static final Codec<RegionalGlowrosesFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Gems.Set.CODEC.fieldOf("gem_set").forGetter(config -> config.gemSet),
                    Codec.INT.fieldOf("region_size").forGetter(config -> config.regionSize),
                    BlockState.CODEC.listOf().fieldOf("blacklist").forGetter(config -> ImmutableList.copyOf(config.blacklist)),
                    Codec.INT.fieldOf("tries").orElse(128).forGetter(config -> config.tryCount),
                    Codec.INT.fieldOf("xspread").orElse(7).forGetter(config -> config.xSpread),
                    Codec.INT.fieldOf("yspread").orElse(3).forGetter(config -> config.ySpread),
                    Codec.INT.fieldOf("zspread").orElse(7).forGetter(config -> config.zSpread)
            ).apply(instance, RegionalGlowrosesFeatureConfig::new));

    public final Set<BlockState> blacklist = new HashSet<>();
    public final int tryCount;
    public final int xSpread;
    public final int ySpread;
    public final int zSpread;

    @SuppressWarnings("ConstructorWithTooManyParameters")
    public RegionalGlowrosesFeatureConfig(Gems.Set gemSet, int regionSize, Collection<BlockState> blacklist, int tryCount, int xSpread, int ySpread, int zSpread) {
        super(gemSet, 1, regionSize);
        this.blacklist.addAll(blacklist);
        this.tryCount = tryCount;
        this.xSpread = xSpread;
        this.ySpread = ySpread;
        this.zSpread = zSpread;
    }

    public RegionalGlowrosesFeatureConfig(Gems.Set gemSet, int regionSize) {
        this(gemSet, regionSize, ImmutableList.of(), 32, 7, 3, 7);
    }

    public RegionalGlowrosesFeatureConfig(Gems.Set gemSet, int regionSize, int tryCount) {
        this(gemSet, regionSize, ImmutableList.of(), tryCount, 7, 3, 7);
    }
}
