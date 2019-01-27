/*
 * Silent's Gems -- GemOre
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

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.Gems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class GemOre extends BlockOre {
    private final Gems gem;

    public GemOre(Gems gem) {
        super(Block.Builder.create(Material.ROCK)
                .hardnessAndResistance(3, 15));
        this.gem = gem;
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return 2;
    }

    @Override
    public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
        return gem.getItem();
    }

//    @Override
//    public int getItemsToDropCount(IBlockState state, int fortune, World worldIn, BlockPos pos, Random random) {
//        if (fortune > 0) {
//            int bonus = random.nextInt(fortune + 2) - 1;
//            if (bonus < 0) bonus = 0;
//            return bonus + 1;
//        } else {
//            return 1;
//        }
//    }


    @Override
    public int getItemsToDropCount(IBlockState state, int fortune, World worldIn, BlockPos pos, Random random) {
        return super.getItemsToDropCount(state, fortune, worldIn, pos, random);
    }

    @Override
    public int getExpDrop(IBlockState state, IWorldReader reader, BlockPos pos, int fortune) {
        World world = reader instanceof World ? (World) reader : null;
        if (world == null || this.getItemDropped(state, world, pos, fortune) != this) {
            return MathHelper.nextInt(RANDOM, 1, 5);
        }
        return 0;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(gem.getSet().getDisplayName());
    }
}
