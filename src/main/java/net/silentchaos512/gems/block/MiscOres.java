package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.utils.Lazy;

import java.util.Locale;
import java.util.Random;

public enum MiscOres implements IItemProvider, IStringSerializable {
    CHAOS(CraftingItems.CHAOS_CRYSTAL, 3),
    ENDER(CraftingItems.ENDER_CRYSTAL, 4);

    private final Lazy<MiscOreBlock> block;

    MiscOres(IItemProvider droppedItem, int harvestLevel) {
        block = Lazy.of(() -> new MiscOreBlock(droppedItem, harvestLevel,
                Block.Properties.create(Material.ROCK)
                        .tickRandomly()
                        .hardnessAndResistance(4, 20)));
    }

    public MiscOreBlock getBlock() {
        return block.get();
    }

    @Override
    public Item asItem() {
        return getBlock().asItem();
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT) + "_ore";
    }

    public static class MiscOreBlock extends OreBlockSG {
        private static final int CHAOS_ORE_CHAOS_GENERATED = 200;

        public static final BooleanProperty LIT = BlockStateProperties.LIT;

        MiscOreBlock(IItemProvider droppedItem, int harvestLevel, Properties builder) {
            super(droppedItem, harvestLevel, builder);
            setDefaultState(getDefaultState().with(LIT, false));
        }

        @Override
        public int getExpRandom() {
            return MathHelper.nextInt(RANDOM, 2, 7);
        }

        @Override
        public int getLightValue(IBlockState state, IWorldReader world, BlockPos pos) {
            return state.get(LIT) ? 9 : 0;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onBlockClicked(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player) {
            activate(state, worldIn, pos);
            super.onBlockClicked(state, worldIn, pos, player);
        }

        @Override
        public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
            activate(worldIn.getBlockState(pos), worldIn, pos);
            super.onEntityWalk(worldIn, pos, entityIn);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
            activate(state, worldIn, pos);
            return super.onBlockActivated(state, worldIn, pos, player, hand, side, hitX, hitY, hitZ);
        }

        private static void activate(IBlockState state, World world, BlockPos pos) {
            spawnParticles(world, pos);
            if (!state.get(LIT)) {
                world.setBlockState(pos, state.with(LIT, true));

                if (state.getBlock() == CHAOS.getBlock()) {
                    Chaos.generate(world, CHAOS_ORE_CHAOS_GENERATED, pos);
                }
            }
        }

        @Override
        public void tick(IBlockState state, World worldIn, BlockPos pos, Random random) {
            if (state.get(LIT)) {
                worldIn.setBlockState(pos, state.with(LIT, false), 3);
            }
        }

        @Override
        public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
            if (stateIn.get(LIT)) {
                spawnParticles(worldIn, pos);
            }
        }

        private static void spawnParticles(World world, BlockPos pos) {
            Random random = world.rand;
            for (EnumFacing enumfacing : EnumFacing.values()) {
                BlockPos blockpos = pos.offset(enumfacing);
                if (!world.getBlockState(blockpos).isOpaqueCube(world, blockpos)) {
                    EnumFacing.Axis axis = enumfacing.getAxis();
                    double d1 = axis == EnumFacing.Axis.X ? 0.5D + 0.5625D * (double) enumfacing.getXOffset() : (double) random.nextFloat();
                    double d2 = axis == EnumFacing.Axis.Y ? 0.5D + 0.5625D * (double) enumfacing.getYOffset() : (double) random.nextFloat();
                    double d3 = axis == EnumFacing.Axis.Z ? 0.5D + 0.5625D * (double) enumfacing.getZOffset() : (double) random.nextFloat();
                    world.addParticle(RedstoneParticleData.REDSTONE_DUST, (double) pos.getX() + d1, (double) pos.getY() + d2, (double) pos.getZ() + d3, 0.0D, 0.0D, 0.0D);
                }
            }
        }

        @Override
        protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
            builder.add(LIT);
        }
    }
}
