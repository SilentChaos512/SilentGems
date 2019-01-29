package net.silentchaos512.gems.world.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.AbstractFlowersFeature;
import net.silentchaos512.gems.lib.Gems;

import java.util.*;

public class GlowroseFeature extends AbstractFlowersFeature {
    private final List<Gems> gems = new ArrayList<>();

    public GlowroseFeature(Collection<Gems> gems) {
        this.gems.addAll(gems);
    }

    @Override
    public IBlockState getRandomFlower(Random random, BlockPos pos) {
        int index = random.nextInt(this.gems.size());
        Gems gem = this.gems.get(index);
        return gem.getGlowrose().getDefaultState();
    }
}
