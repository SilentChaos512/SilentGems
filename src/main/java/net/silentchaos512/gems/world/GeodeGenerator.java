package net.silentchaos512.gems.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.feature.MinableFeature;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gems.lib.Gems;

import java.util.Random;
import java.util.function.Predicate;

public class GeodeGenerator extends MinableFeature {
    private final Gems.Set gemSet;
    private final Block shellBlock;
    private final Predicate<Block> predicate;

    public GeodeGenerator(Block shell, Gems.Set gemSet) {
        this(shell, gemSet, Tags.Blocks.STONE::contains);
    }

    public GeodeGenerator(Block shell, Gems.Set gemSet, Predicate<Block> predicate) {
        this.gemSet = gemSet;
        this.shellBlock = shell;
        this.predicate = predicate;
    }

    @Override
    public boolean place(IWorld world, IChunkGenerator<? extends IChunkGenSettings> generator, Random random, BlockPos pos, MinableConfig config) {
        float diameterXZ = 2f * random.nextFloat() + 5;
        float diameterY = 3f * random.nextFloat() + 7;
        generateShell(world, random, pos, diameterXZ, diameterY);
        generateGems(world, random, pos, diameterXZ, diameterY);
        return true;
    }

    public void generateShell(IWorld worldIn, Random rand, BlockPos position, float diameterXZ, float diameterY) {
        int xmin = (int) (position.getX() - diameterXZ / 2);
        int xmax = (int) (position.getX() + diameterXZ / 2);
        int ymin = (int) (position.getY() - diameterY / 2);
        int ymax = (int) (position.getY() + diameterY / 2);
        int zmin = (int) (position.getZ() - diameterXZ / 2);
        int zmax = (int) (position.getZ() + diameterXZ / 2);

        for (int x = xmin; x <= xmax; ++x) {
            float dx = x - position.getX();
            for (int y = ymin; y <= ymax; ++y) {
                float dy = y - position.getY();
                for (int z = zmin; z <= zmax; ++z) {
                    float dz = z - position.getZ();
                    if ((dx * dx) / (diameterXZ * diameterXZ) + (dy * dy) / (diameterY * diameterY) + (dz * dz) / (diameterXZ * diameterXZ) <= 0.25f) {
                        BlockPos blockpos = new BlockPos(x, y, z);

                        IBlockState state = worldIn.getBlockState(blockpos);
//                        if (state.getBlock().isReplaceableOreGen(state, worldIn, blockpos, this.predicate)) {
                        if (this.predicate.test(state.getBlock())) {
                            worldIn.setBlockState(blockpos, shellBlock.getDefaultState(), 2);
                        }
                    }
                }
            }
        }
        // SilentGems.logHelper.debug(xmin, xmax, ymin, ymax, zmin, zmax, count);
    }

    public void generateGems(IWorld worldIn, Random rand, BlockPos position, float diameterXZ, float diameterY) {
        Predicate<Block> shellPredicate = block -> block == shellBlock;

        diameterXZ *= 0.6f; //GemsConfig.GEODE_FILL_RATIO;
        diameterY *= 0.6f; //GemsConfig.GEODE_FILL_RATIO;
        int xmin = (int) (position.getX() - diameterXZ / 2);
        int xmax = (int) (position.getX() + diameterXZ / 2);
        int ymin = (int) (position.getY() - diameterY / 2);
        int ymax = (int) (position.getY() + diameterY / 2);
        int zmin = (int) (position.getZ() - diameterXZ / 2);
        int zmax = (int) (position.getZ() + diameterXZ / 2);

        Block block = gemSet.getMultiOre();

        int count = 0;
        for (int x = xmin; x <= xmax; ++x) {
            float dx = x - position.getX();
            for (int y = ymin; y <= ymax; ++y) {
                float dy = y - position.getY();
                for (int z = zmin; z <= zmax; ++z) {
                    float dz = z - position.getZ();
                    count = tryPlaceGem(worldIn, rand, position, diameterXZ, diameterY, shellPredicate, block, count, x, dx, y, dy, z, dz);
                }
            }
        }
        // SilentGems.logHelper.debug(xmin, xmax, ymin, ymax, zmin, zmax, count);
    }

    private int tryPlaceGem(IWorld worldIn, Random rand, BlockPos position, float diameterXZ, float diameterY, Predicate<Block> shellPredicate, Block block, int count, int x, float dx, int y, float dy, int z, float dz) {
        if ((dx * dx) / (diameterXZ * diameterXZ) + (dy * dy) / (diameterY * diameterY) + (dz * dz) / (diameterXZ * diameterXZ) <= 0.25f) {
            // We are in the spawn area.
            if (rand.nextFloat() <= 0.75f /*GemsConfig.GEODE_GEM_DENSITY*/) {
                ++count;
                BlockPos blockpos = new BlockPos(x, y, z);

                IBlockState state = worldIn.getBlockState(blockpos);
//                if (state.getBlock().isReplaceableOreGen(state, worldIn, blockpos, shellPredicate)) {
                if (shellPredicate.test(state.getBlock())) {
                    Gems gem = gemSet.selectRandom(rand);
                    worldIn.setBlockState(blockpos, gem.getOre().getDefaultState(), 2);

                    // Make sure the geode is sealed (in case of intersection with a cave, for example.)
                    if (true /*GemsConfig.GEODE_SEAL_BREAKS*/) {
                        for (EnumFacing facing : EnumFacing.values()) {
                            BlockPos offset = position.offset(facing);
                            IBlockState adjacent = worldIn.getBlockState(offset);
                            if (worldIn.isAirBlock(offset) || adjacent != shellBlock && adjacent.getBlock() != block) {
                                worldIn.setBlockState(offset, shellBlock.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }
        }
        return count;
    }
}
