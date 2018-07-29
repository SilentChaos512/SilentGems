/*
 * Silent's Gems -- BlockChaosPlinth
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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.item.ItemChaosGem;
import net.silentchaos512.gems.tile.TileChaosPlinth;
import net.silentchaos512.lib.block.ITileEntityBlock;
import net.silentchaos512.lib.util.StackHelper;

public class BlockChaosPlinth extends BlockContainer implements ITileEntityBlock {
    protected BlockChaosPlinth() {
        super(Material.ROCK);
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileChaosPlinth.class;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileChaosPlinth();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        TileEntity t = world.getTileEntity(pos);

        if (t instanceof TileChaosPlinth) {
            TileChaosPlinth tile = (TileChaosPlinth) t;
            ItemStack currentGem = tile.getStackInSlot(0);

            if (StackHelper.isValid(heldItem) && heldItem.getItem() instanceof ItemChaosGem) {
                player.setHeldItem(hand, currentGem);
                tile.setInventorySlotContents(0, heldItem);
                return true;
            } else if (StackHelper.isValid(currentGem) && StackHelper.isEmpty(heldItem)) {
                // TODO
            }
        }

        return false;
    }
}
