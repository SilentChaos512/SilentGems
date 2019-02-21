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
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.silentchaos512.gems.block.Glowrose;
import net.silentchaos512.utils.Lazy;

import javax.annotation.Nullable;

public class LuminousFlowerPotBlock extends BlockContainer {
    private static final AxisAlignedBB FLOWER_POT_AABB = new AxisAlignedBB(0.3125, 0.0, 0.3125, 0.6875, 0.375, 0.6875);

    public static final Lazy<LuminousFlowerPotBlock> INSTANCE = Lazy.of(LuminousFlowerPotBlock::new);

    private LuminousFlowerPotBlock() {
        super(Properties.create(Material.CIRCUITS)
                .hardnessAndResistance(1, 30)
                .lightValue(2));
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);

        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemBlock) {
            LuminousFlowerPotTileEntity tile = getTileEntity(worldIn, pos);

            if (tile != null && tile.getFlower().isEmpty()) {
                Block block = Block.getBlockFromItem(heldItem.getItem());

                if (block instanceof Glowrose) {
                    ItemStack flower = new ItemStack(heldItem.getItem(), 1);
                    tile.setFlower(flower);
                    tile.markDirty();
                    worldIn.notifyBlockUpdate(pos, state, state, 3);
                    worldIn.checkLight(pos);
                    player.addStat(StatList.POT_FLOWER);

                    if (player instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) player;
                        CriteriaTriggers.PLACED_BLOCK.trigger(playerMP, pos, heldItem);
                    }

                    if (!player.abilities.isCreativeMode) {
                        heldItem.shrink(1);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int getLightValue(IBlockState state, IWorldReader world, BlockPos pos) {
        LuminousFlowerPotTileEntity tile = getTileEntity(world, pos);
        return tile != null && !tile.getFlower().isEmpty() ? 15 : lightValue;
    }

    @Override
    public boolean propagatesSkylightDown(IBlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos) {
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

//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        return FLOWER_POT_AABB;
//    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos) {
        return super.isValidPosition(state, worldIn, pos);
//                && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
//        if (!world.getBlockState(pos.down()).isSideSolid(world, pos, EnumFacing.UP)) {
//            this.dropBlockAsItem(world, pos, state, 0);
//            world.setBlockToAir(pos);
//        }
    }

    @Override
    public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune) {
        super.getDrops(state, drops, world, pos, fortune);
        LuminousFlowerPotTileEntity te = getTileEntity(world, pos);
        if (te != null && !te.getFlower().isEmpty()) {
            drops.add(te.getFlower());
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest, IFluidState fluid) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack tool) {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.removeBlock(pos);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return new LuminousFlowerPotTileEntity();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return createTileEntity(getDefaultState(), worldIn);
    }
}
