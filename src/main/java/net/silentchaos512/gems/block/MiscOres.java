package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.ModList;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.utils.Lazy;

import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

public enum MiscOres implements IBlockProvider, IStringSerializable {
    CHAOS(() -> new MiscOreBlock(CraftingItems.CHAOS_CRYSTAL, 3, Block.Properties.create(Material.ROCK).tickRandomly().hardnessAndResistance(4, 20)) {
        @Override
        public int getExpRandom() {
            return MathHelper.nextInt(RANDOM, 2, 7);
        }
    }),
    ENDER(() -> new MiscOreBlock(CraftingItems.ENDER_CRYSTAL, ModList.get().isLoaded("silentgear") ? 4 : 3, Block.Properties.create(Material.ROCK).tickRandomly().hardnessAndResistance(4, 20)) {
        @Override
        public int getExpRandom() {
            return MathHelper.nextInt(RANDOM, 4, 8);
        }
    }),
    SILVER(() -> new OreBlockSG(null, 2, Block.Properties.create(Material.ROCK).hardnessAndResistance(3, 12)) {
        @Override
        public int getExpRandom() {
            return 0;
        }
    });

    private final Lazy<OreBlockSG> block;

    MiscOres(Supplier<OreBlockSG> blockSupplier) {
        this.block = Lazy.of(blockSupplier);
    }

    public OreBlockSG getBlock() {
        return this.block.get();
    }

    @Override
    public Block asBlock() {
        return getBlock();
    }

    @Override
    public Item asItem() {
        return asBlock().asItem();
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT) + "_ore";
    }

    public static abstract class MiscOreBlock extends OreBlockSG {
        private static final int CHAOS_ORE_CHAOS_GENERATED = 500;

        static final BooleanProperty LIT = BlockStateProperties.LIT;

        MiscOreBlock(IItemProvider droppedItem, int harvestLevel, Properties builder) {
            super(droppedItem, harvestLevel, builder.harvestTool(ToolType.PICKAXE));
            setDefaultState(getDefaultState().with(LIT, false));
        }

        @Override
        public int getLightValue(BlockState state, IEnviromentBlockReader world, BlockPos pos) {
            return state.get(LIT) ? 9 : 0;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
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
        public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
            activate(state, worldIn, pos);
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }

        private static void activate(BlockState state, World world, BlockPos pos) {
            spawnParticles(world, pos);
            if (!state.get(LIT)) {
                world.setBlockState(pos, state.with(LIT, true));

                if (state.getBlock() == CHAOS.asBlock()) {
                    Chaos.generate(world, CHAOS_ORE_CHAOS_GENERATED, pos);
                }
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
            if (state.get(LIT)) {
                worldIn.setBlockState(pos, state.with(LIT, false), 3);
            }
        }

        @Override
        public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
            if (stateIn.get(LIT)) {
                spawnParticles(worldIn, pos);
            }
        }

        private static void spawnParticles(World world, BlockPos pos) {
            Random random = world.rand;
            for (Direction direction : Direction.values()) {
                BlockPos blockpos = pos.offset(direction);
                if (!world.getBlockState(blockpos).isOpaqueCube(world, blockpos)) {
                    Direction.Axis axis = direction.getAxis();
                    double d1 = axis == Direction.Axis.X ? 0.5D + 0.5625D * (double) direction.getXOffset() : (double) random.nextFloat();
                    double d2 = axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double) direction.getYOffset() : (double) random.nextFloat();
                    double d3 = axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double) direction.getZOffset() : (double) random.nextFloat();
                    world.addParticle(RedstoneParticleData.REDSTONE_DUST, (double) pos.getX() + d1, (double) pos.getY() + d2, (double) pos.getZ() + d3, 0.0D, 0.0D, 0.0D);
                }
            }
        }

        @Override
        protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
            builder.add(LIT);
        }
    }
}
