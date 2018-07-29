/*
 * Silent's Gems -- BlockGemOreMulti
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

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.lib.block.BlockOreSL;

import java.util.Random;

public class BlockGemOreMulti extends BlockOreSL {
    private final EnumGem.Set gemSet;

    public BlockGemOreMulti(EnumGem.Set gemSet) {
        super(ModItems.gem, 2, 1, 1, 1, 5);
        this.gemSet = gemSet;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Random rand = SilentGems.random;
        EnumGem gem = this.gemSet.selectRandom(rand);
        int count = quantityDropped(state, fortune, rand);
        for (int i = 0; i < count; ++i) {
            drops.add(gem.getItem());
        }
    }
}
