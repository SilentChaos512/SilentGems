package net.silentchaos512.gems.world;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.EnumGem.Set;

import java.util.Random;

public class GeodeGenerator extends WorldGenMinable {
    private final EnumGem.Set gemSet;
    private final IBlockState shellBlock;
    private Predicate<IBlockState> predicate;

    public GeodeGenerator(IBlockState shell, EnumGem.Set gemSet) {
        super(shell, 30);
        this.gemSet = gemSet;
        this.shellBlock = shell;
        this.predicate = BlockMatcher.forBlock(Blocks.STONE);
    }

    public GeodeGenerator(IBlockState shell, EnumGem.Set gemSet, Predicate<IBlockState> predicate) {
        super(shell, 30, predicate);
        this.gemSet = gemSet;
        this.shellBlock = shell;
        this.predicate = predicate;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        float diameterXZ = 2f * rand.nextFloat() + 5;
        float diameterY = 3f * rand.nextFloat() + 7;
        generateShell(worldIn, rand, position, diameterXZ, diameterY);
        generateGems(worldIn, rand, position, diameterXZ, diameterY);
        return true;
    }

    public void generateShell(World worldIn, Random rand, BlockPos position, float diameterXZ, float diameterY) {
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
                    if ((dx * dx) / (diameterXZ * diameterXZ) + (dy * dy) / (diameterY * diameterY) + (dz * dz) / (diameterXZ * diameterXZ) <= 0.25f) {
                        ++count;
                        BlockPos blockpos = new BlockPos(x, y, z);

                        IBlockState state = worldIn.getBlockState(blockpos);
                        if (state.getBlock().isReplaceableOreGen(state, worldIn, blockpos, this.predicate)) {
                            worldIn.setBlockState(blockpos, this.shellBlock, 2);
                        }
                    }
                }
            }
        }
        // SilentGems.logHelper.debug(xmin, xmax, ymin, ymax, zmin, zmax, count);
    }

    public void generateGems(World worldIn, Random rand, BlockPos position, float diameterXZ, float diameterY) {
        Predicate shellPredicate = BlockMatcher.forBlock(shellBlock.getBlock());

        diameterXZ *= GemsConfig.GEODE_FILL_RATIO;
        diameterY *= GemsConfig.GEODE_FILL_RATIO;
        int xmin = (int) (position.getX() - diameterXZ / 2);
        int xmax = (int) (position.getX() + diameterXZ / 2);
        int ymin = (int) (position.getY() - diameterY / 2);
        int ymax = (int) (position.getY() + diameterY / 2);
        int zmin = (int) (position.getZ() - diameterXZ / 2);
        int zmax = (int) (position.getZ() + diameterXZ / 2);

        Block block = gemSet == Set.LIGHT ? ModBlocks.gemOreLight
                : gemSet == Set.DARK ? ModBlocks.gemOreDark : ModBlocks.gemOre;

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

    private int tryPlaceGem(World worldIn, Random rand, BlockPos position, float diameterXZ, float diameterY, Predicate shellPredicate, Block block, int count, int x, float dx, int y, float dy, int z, float dz) {
        if ((dx * dx) / (diameterXZ * diameterXZ) + (dy * dy) / (diameterY * diameterY) + (dz * dz) / (diameterXZ * diameterXZ) <= 0.25f) {
            // We are in the spawn area.
            if (rand.nextFloat() <= GemsConfig.GEODE_GEM_DENSITY) {
                ++count;
                BlockPos blockpos = new BlockPos(x, y, z);

                IBlockState state = worldIn.getBlockState(blockpos);
                if (state.getBlock().isReplaceableOreGen(state, worldIn, blockpos, shellPredicate)) {
                    EnumGem gem = EnumGem.values()[rand.nextInt(16)];
                    IBlockState stateToPlace = block.getDefaultState().withProperty(EnumGem.VARIANT_GEM,
                            gem);
                    worldIn.setBlockState(blockpos, stateToPlace, 2);

                    // Make sure the geode is sealed (in case of intersection with a cave, for example.)
                    if (GemsConfig.GEODE_SEAL_BREAKS) {
                        for (EnumFacing facing : EnumFacing.values()) {
                            BlockPos offset = position.offset(facing);
                            IBlockState adjacent = worldIn.getBlockState(offset);
                            if (worldIn.isAirBlock(offset) || adjacent != shellBlock && adjacent.getBlock() != block) {
                                worldIn.setBlockState(offset, shellBlock);
                            }
                        }
                    }
                }
            }
        }
        return count;
    }
}
