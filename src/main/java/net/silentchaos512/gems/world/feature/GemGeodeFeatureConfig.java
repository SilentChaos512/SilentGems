package net.silentchaos512.gems.world.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.silentchaos512.gems.lib.Gems;

import java.util.function.Predicate;

public class GemGeodeFeatureConfig implements IFeatureConfig {
    public final Gems.Set gemSet;
    public final BlockState shellBlock;
    public final Predicate<BlockState> target;
    public final boolean replaceAir = true;
    public final float gemDensity = 0.75f;

    public GemGeodeFeatureConfig(Gems.Set gemSet, BlockState shellBlock, Predicate<BlockState> target) {
        this.gemSet = gemSet;
        this.shellBlock = shellBlock;
        this.target = target;
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(
                ops.createString("gem_set"), ops.createString(gemSet.getName()),
                ops.createString("shell_block"), BlockState.serialize(ops, shellBlock).getValue()
        )));
    }

    public static GemGeodeFeatureConfig deserialize(Dynamic<?> dynamic) {
        String setName = dynamic.get("gem_set").asString("classic");
        Gems.Set gemSet = "light".equals(setName) ? Gems.Set.LIGHT : "dark".equals(setName) ? Gems.Set.DARK : Gems.Set.CLASSIC;
        BlockState shellBlock = dynamic.get("shell_block").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
        // TODO: How to handle target? What even is this Dynamic stuff anyway?
        return new GemGeodeFeatureConfig(gemSet, shellBlock, s -> s.getBlock() == Blocks.STONE);
    }
}
