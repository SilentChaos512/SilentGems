package net.silentchaos512.gems.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.silentchaos512.gems.lib.Gems;

import java.util.Random;

public class RegionalGlowrosesFeature extends FlowersFeature<RegionalGlowrosesFeatureConfig> {
    public RegionalGlowrosesFeature(Codec<RegionalGlowrosesFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean func_241855_a(ISeedReader worldIn, ChunkGenerator chunkGenerator, Random random, BlockPos pos, RegionalGlowrosesFeatureConfig config) {
        if (config.regionSize < 1) return false;

        BlockState blockstate = this.getFlowerToPlace(worldIn, random, pos, config);
        int i = 0;

        for (int j = 0; j < this.getFlowerCount(config); ++j) {
            BlockPos blockpos = this.getNearbyPos(random, pos, config);
            if (worldIn.isAirBlock(blockpos) && blockpos.getY() < 255 && blockstate.isValidPosition(worldIn, blockpos) && this.isValidPosition(worldIn, blockpos, config)) {
                worldIn.setBlockState(blockpos, blockstate, 2);
                ++i;
            }
        }

        return i > 0;
    }

    @Override
    public boolean isValidPosition(IWorld world, BlockPos pos, RegionalGlowrosesFeatureConfig config) {
        return !config.blacklist.contains(world.getBlockState(pos));
    }

    @Override
    public int getFlowerCount(RegionalGlowrosesFeatureConfig config) {
        return config.tryCount;
    }

    @Override
    public BlockPos getNearbyPos(Random rand, BlockPos pos, RegionalGlowrosesFeatureConfig config) {
        return pos.add(rand.nextInt(config.xSpread) - rand.nextInt(config.xSpread), rand.nextInt(config.ySpread) - rand.nextInt(config.ySpread), rand.nextInt(config.zSpread) - rand.nextInt(config.zSpread));
    }

    @Deprecated
    @Override
    public BlockState getFlowerToPlace(Random rand, BlockPos pos, RegionalGlowrosesFeatureConfig config) {
        // Not used
        throw new IllegalAccessError("call to incorrect getFlowerToPlace");
    }

    public BlockState getFlowerToPlace(ISeedReader world, Random rand, BlockPos pos, RegionalGlowrosesFeatureConfig config) {
        Gems gem = config.selectGem(world, pos, rand);
        return gem.getGlowrose().getDefaultState();
    }
}
