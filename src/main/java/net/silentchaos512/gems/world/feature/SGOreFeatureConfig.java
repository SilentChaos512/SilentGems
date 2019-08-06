package net.silentchaos512.gems.world.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.common.Tags;

import java.util.function.Predicate;

public class SGOreFeatureConfig implements IFeatureConfig {
    public final Predicate<BlockState> target;
    public final int size;
    public final BlockState state;
    public final Predicate<DimensionType> dimension;

    public SGOreFeatureConfig(BlockState state, int size, Predicate<BlockState> target, DimensionType dimension) {
        this(state, size, target, d -> d.getId() == dimension.getId());
    }

    public SGOreFeatureConfig(BlockState state, int size, Predicate<BlockState> target, Predicate<DimensionType> dimension) {
        this.state = state;
        this.size = size;
        this.target = target;
        this.dimension = dimension;
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(
                ops.createString("size"), ops.createInt(this.size),
                ops.createString("state"), BlockState.serialize(ops, this.state).getValue()
        )));
    }

    public static SGOreFeatureConfig deserialize(Dynamic<?> dynamic) {
        int size = dynamic.get("size").asInt(0);
        BlockState state = dynamic.get("state").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
        // TODO: How to handle target? What even is this Dynamic stuff anyway?
        return new SGOreFeatureConfig(state, size, s -> s.isIn(Tags.Blocks.STONE), DimensionType.OVERWORLD);
    }
}
