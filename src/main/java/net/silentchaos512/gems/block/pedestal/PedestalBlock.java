package net.silentchaos512.gems.block.pedestal;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.lib.util.PlayerUtils;

public class PedestalBlock extends Block {
    private static final VoxelShape SHAPE_PILLAR = Block.makeCuboidShape(5, 2, 5, 11, 14, 11);
    private static final VoxelShape SHAPE_BOTTOM1 = Block.makeCuboidShape(1, 0, 1, 15, 1, 15);
    private static final VoxelShape SHAPE_BOTTOM2 = Block.makeCuboidShape(3, 1, 3, 13, 2, 13);
    private static final VoxelShape SHAPE_TOP1 = Block.makeCuboidShape(1, 15, 1, 15, 16, 15);
    private static final VoxelShape SHAPE_TOP2 = Block.makeCuboidShape(3, 14, 3, 13, 15, 13);
    private static final VoxelShape SHAPE = createShape(SHAPE_PILLAR, SHAPE_BOTTOM1, SHAPE_BOTTOM2, SHAPE_TOP1, SHAPE_TOP2);

    private static VoxelShape createShape(VoxelShape... shapes) {
        VoxelShape shape = shapes[0];
        for (int i = 1; i < shapes.length; ++i) {
            shape = VoxelShapes.or(shape, shapes[i]);
        }
        return shape;
    }

    public PedestalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PedestalTileEntity();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof IInventory) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tile);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof PedestalTileEntity) {
                // Remove/place item
                PedestalTileEntity pedestal = (PedestalTileEntity) tile;
                ItemStack stack = pedestal.getStackInSlot(0);
                if (!stack.isEmpty()) {
                    // Remove
                    PlayerUtils.giveItem(player, stack);
                    pedestal.setItem(ItemStack.EMPTY);
                } else {
                    // Place
                    ItemStack inHand = player.getHeldItemMainhand();
                    if (!inHand.isEmpty()) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, inHand);
                        // setItem makes a copy, no need to do so here
                        pedestal.setItem(inHand);
                        inHand.shrink(1);
                    }
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
