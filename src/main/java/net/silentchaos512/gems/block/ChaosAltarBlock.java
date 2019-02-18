/*
 * Silent's Gems -- ChaosAltarBlock
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

package net.silentchaos512.gems.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.gems.client.gui.GuiTypes;
import net.silentchaos512.gems.tile.TileChaosAltar;

import javax.annotation.Nullable;

public class ChaosAltarBlock extends BlockContainer {
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.0f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f);

    public ChaosAltarBlock() {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(12, 6000));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        // FIXME
        return new TileChaosAltar(null);
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileChaosAltar)
            GuiTypes.ALTAR.open(player, world, pos);
        return true;
    }

    @Override
    public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean isMoving) {
        TileEntity tileAltar = world.getTileEntity(pos);
        if (tileAltar instanceof TileChaosAltar) {
//            InventoryHelper.dropInventoryItems(world, pos, (TileChaosAltar) tileAltar);
            world.updateComparatorOutputLevel(pos, this);
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
//        TileEntity tile = world.getTileEntity(pos);
//        if (tile instanceof TileChaosAltar) {
//            TileChaosAltar altar = (TileChaosAltar) tile;
//            float storedRatio = (float) altar.getCharge() / altar.getMaxCharge();
//            return (int) (15 * storedRatio);
//        }
        return 0;
    }

//    @Override
//    public boolean isOpaqueCube(IBlockState state) {
//        return false;
//    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

//    @Override
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        return BOUNDING_BOX;
//    }
}
