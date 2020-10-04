package net.silentchaos512.gems.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.silentchaos512.gems.lib.Gems;

import java.util.Random;

public class GemGeodeFeature extends Feature<GemGeodeFeatureConfig> {
    public static final GemGeodeFeature INSTANCE = new GemGeodeFeature(GemGeodeFeatureConfig.CODEC);

    public GemGeodeFeature(Codec<GemGeodeFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean func_241855_a(ISeedReader worldIn, ChunkGenerator generator, Random random, BlockPos pos, GemGeodeFeatureConfig config) {
        if (config.target.test(worldIn.getBlockState(pos), random)) {
            float diameterXZ = 2f * random.nextFloat() + 5;
            float diameterY = 3f * random.nextFloat() + 7;
            generateShell(worldIn, random, pos, diameterXZ, diameterY, config);
            generateGems(worldIn, random, pos, diameterXZ, diameterY, config);
            return true;
        }
        return false;
    }

    private void generateShell(IWorld worldIn, Random rand, BlockPos position, float diameterXZ, float diameterY, GemGeodeFeatureConfig config) {
        int xMin = (int) (position.getX() - diameterXZ / 2);
        int xMax = (int) (position.getX() + diameterXZ / 2);
        int yMin = (int) (position.getY() - diameterY / 2);
        int yMax = (int) (position.getY() + diameterY / 2);
        int zMin = (int) (position.getZ() - diameterXZ / 2);
        int zMax = (int) (position.getZ() + diameterXZ / 2);

        for (int x = xMin; x <= xMax; ++x) {
            float dx = x - position.getX();
            for (int y = yMin; y <= yMax; ++y) {
                float dy = y - position.getY();
                for (int z = zMin; z <= zMax; ++z) {
                    float dz = z - position.getZ();
                    if ((dx * dx) / (diameterXZ * diameterXZ) + (dy * dy) / (diameterY * diameterY) + (dz * dz) / (diameterXZ * diameterXZ) <= 0.25f) {
                        BlockPos blockpos = new BlockPos(x, y, z);

                        BlockState state = worldIn.getBlockState(blockpos);
                        if (config.target.test(state, rand) || (config.replaceAir && state.isAir(worldIn, blockpos))) {
                            worldIn.setBlockState(blockpos, config.shellBlock, 2);
                        }
                    }
                }
            }
        }
    }

    private void generateGems(IWorld worldIn, Random rand, BlockPos position, float diameterXZ, float diameterY, GemGeodeFeatureConfig config) {
        diameterXZ *= 0.6f; //GemsConfig.GEODE_FILL_RATIO;
        diameterY *= 0.6f; //GemsConfig.GEODE_FILL_RATIO;
        int xmin = (int) (position.getX() - diameterXZ / 2);
        int xmax = (int) (position.getX() + diameterXZ / 2);
        int ymin = (int) (position.getY() - diameterY / 2);
        int ymax = (int) (position.getY() + diameterY / 2);
        int zmin = (int) (position.getZ() - diameterXZ / 2);
        int zmax = (int) (position.getZ() + diameterXZ / 2);

        int count = 0;
        for (int x = xmin; x <= xmax; ++x) {
            float dx = x - position.getX();
            for (int y = ymin; y <= ymax; ++y) {
                float dy = y - position.getY();
                for (int z = zmin; z <= zmax; ++z) {
                    float dz = z - position.getZ();
                    count = tryPlaceGem(worldIn, rand, position, diameterXZ, diameterY, config, count, x, dx, y, dy, z, dz);
                }
            }
        }
        // SilentGems.logHelper.debug(xmin, xmax, ymin, ymax, zmin, zmax, count);
    }

    private int tryPlaceGem(IWorld worldIn, Random rand, BlockPos position, float diameterXZ, float diameterY, GemGeodeFeatureConfig config, int count, int x, float dx, int y, float dy, int z, float dz) {
        if ((dx * dx) / (diameterXZ * diameterXZ) + (dy * dy) / (diameterY * diameterY) + (dz * dz) / (diameterXZ * diameterXZ) <= 0.25f) {
            // We are in the spawn area.
            if (rand.nextFloat() <= config.gemDensity) {
                ++count;
                BlockPos blockpos = new BlockPos(x, y, z);

                BlockState state = worldIn.getBlockState(blockpos);
                if (state == config.shellBlock) {
                    Gems gem = config.gemSet.selectRandom(rand);
                    worldIn.setBlockState(blockpos, gem.getOre().getDefaultState(), 2);
                }
            }
        }
        return count;
    }
}
