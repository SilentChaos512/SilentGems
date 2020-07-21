/*
 * Silent's Gems -- LuminousFlowerPotBlock
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.silentchaos512.gems.block.flowerpot;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.silentchaos512.gems.block.GlowroseBlock;

import javax.annotation.Nullable;

public class LuminousFlowerPotBlock extends Block {
    private static final VoxelShape SHAPE = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);

    public LuminousFlowerPotBlock() {
        super(Properties.create(Material.MISCELLANEOUS)
                .hardnessAndResistance(1, 30)
                .setLightLevel(state -> 2));
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(hand);

        if (!heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem) {
            LuminousFlowerPotTileEntity tile = getTileEntity(worldIn, pos);

            if (tile != null && tile.getFlower().isEmpty()) {
                Block block = Block.getBlockFromItem(heldItem.getItem());

                if (block instanceof GlowroseBlock) {
                    ItemStack flower = new ItemStack(heldItem.getItem(), 1);
                    tile.setFlower(flower);
                    tile.markDirty();
                    worldIn.notifyBlockUpdate(pos, state, state, 3);
//                    worldIn.checkLight(pos);
                    player.addStat(Stats.POT_FLOWER);

                    if (player instanceof ServerPlayerEntity) {
                        ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
                        CriteriaTriggers.PLACED_BLOCK.trigger(playerMP, pos, heldItem);
                    }

                    if (!player.abilities.isCreativeMode) {
                        heldItem.shrink(1);
                    }

                    return ActionResultType.SUCCESS;
                }
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        LuminousFlowerPotTileEntity tile = getTileEntity(world, pos);
        return tile != null && !tile.getFlower().isEmpty() ? 15 : 2;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }

    @Nullable
    public static LuminousFlowerPotTileEntity getTileEntity(IBlockReader world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof LuminousFlowerPotTileEntity)) {
            return null;
        }
        return (LuminousFlowerPotTileEntity) tile;
    }

//    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
//        return FLOWER_POT_AABB;
//    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return super.isValidPosition(state, worldIn, pos);
//                && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
    }

//    @Override
//    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
//        if (!world.getBlockState(pos.down()).isSideSolid(world, pos, EnumFacing.UP)) {
//            this.dropBlockAsItem(world, pos, state, 0);
//            world.setBlockToAir(pos);
//        }
//    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new LuminousFlowerPotTileEntity();
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
