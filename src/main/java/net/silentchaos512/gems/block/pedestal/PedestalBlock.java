package net.silentchaos512.gems.block.pedestal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.lib.util.PlayerUtils;

public class PedestalBlock extends BlockContainer {
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

    public PedestalBlock() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(4, 5)
                .sound(SoundType.STONE)
        );
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new PedestalTileEntity();
    }

    @Override
    public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
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
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
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
                        // setItem makes a copy, no need to do so here
                        pedestal.setItem(inHand);
                        inHand.shrink(1);
                    }
                }
            }
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
