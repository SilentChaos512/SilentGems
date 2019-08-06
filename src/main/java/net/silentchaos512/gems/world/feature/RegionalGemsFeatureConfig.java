package net.silentchaos512.gems.world.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.silentchaos512.gems.lib.Gems;

import java.util.Random;
import java.util.function.Predicate;

public class RegionalGemsFeatureConfig implements IFeatureConfig {
    private final Gems.Set gemSet;
    public final int size;
    public final int regionSize;
    public final Predicate<BlockState> target;
    public final Predicate<DimensionType> dimension;

    public RegionalGemsFeatureConfig(Gems.Set gemSet, int size, int regionSize, Predicate<BlockState> target, DimensionType dimension) {
        this(gemSet, size, regionSize, target, d -> d.getId() == dimension.getId());
    }

    public RegionalGemsFeatureConfig(Gems.Set gemSet, int size, int regionSize, Predicate<BlockState> target, Predicate<DimensionType> dimension) {
        this.gemSet = gemSet;
        this.size = size;
        this.regionSize = regionSize;
        this.target = target;
        this.dimension = dimension;
    }

    public Gems selectGem(IWorld world, BlockPos pos, Random random) {
        int regionX = pos.getX() / (16 * regionSize);
        int regionZ = pos.getZ() / (16 * regionSize);
        long regionId = ((long) regionX << 32) + regionZ;
        Random regionRand = new Random(regionId + world.getSeed());

        int gemCount = 2 + regionRand.nextInt(3);
        int index = random.nextInt(gemCount);
        Gems gem = gemSet.selectRandom(regionRand);
        for (int i = 0; i < index; ++i) {
            gem = gemSet.selectRandom(regionRand);
        }
        return gem;
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(
                ops.createString("gem_set"), ops.createString(gemSet.getName()),
                ops.createString("size"), ops.createInt(size),
                ops.createString("region_size"), ops.createInt(regionSize)
        )));
    }

    public static RegionalGemsFeatureConfig deserialize(Dynamic<?> dynamic) {
        Gems.Set gemSet = Gems.Set.deserialize(dynamic);
        int size = dynamic.get("size").asInt(0);
        int regionSize = dynamic.get("region_size").asInt(0);
        int dimensionId = dynamic.get("dimension").asInt(0);
        return new RegionalGemsFeatureConfig(gemSet, size, regionSize, s -> s.getBlock() == Blocks.STONE, d -> d.getId() == DimensionType.OVERWORLD.getId());
    }
}
