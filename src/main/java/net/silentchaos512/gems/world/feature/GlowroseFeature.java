package net.silentchaos512.gems.world.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.AbstractFlowersFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.lib.Gems;

import java.util.*;

public class GlowroseFeature extends AbstractFlowersFeature {
    private final List<Gems> gems = new ArrayList<>();

    public GlowroseFeature(Collection<Gems> gems) {
        this.gems.addAll(gems);
    }

    @Override
    public boolean place(IWorld world, IChunkGenerator<? extends IChunkGenSettings> generator, Random random, BlockPos pos, NoFeatureConfig config) {
        IBlockState flower = this.getRandomFlower(random, pos);
        int numberPlaced = 0;
        int maxCount = GemsConfig.COMMON.glowroseMaxPlaceCount.get();
        int tryCount = GemsConfig.COMMON.glowroseSpawnTryCount.get();

        // Same as super, but fewer iterations
        for(int j = 0; j < tryCount && numberPlaced < maxCount; ++j) {
            BlockPos pos1 = pos.add(
                    random.nextInt(8) - random.nextInt(8),
                    random.nextInt(4) - random.nextInt(4),
                    random.nextInt(8) - random.nextInt(8)
            );
            if (world.isAirBlock(pos1) && pos1.getY() < 255 && flower.isValidPosition(world, pos1)) {
                world.setBlockState(pos1, flower, 2);
                ++numberPlaced;
            }
        }

        return numberPlaced > 0;
    }

    @Override
    public IBlockState getRandomFlower(Random random, BlockPos pos) {
        int index = random.nextInt(this.gems.size());
        Gems gem = this.gems.get(index);
        return gem.getGlowrose().getDefaultState();
    }
}
