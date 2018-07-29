/*
 * Silent's Gems -- BlockGemGlass
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
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.RecipeMaker;

public class BlockGemGlass extends BlockGemSubtypes implements IAddRecipes {
    public BlockGemGlass(EnumGem.Set set) {
        super(set, Material.GLASS);
        setHardness(0.3f);
        setSoundType(SoundType.GLASS);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        for (int i = 0; i < 16; ++i) {
            EnumGem gem = getGem(i);
            recipes.addSurroundOre(Names.GEM_GLASS + this.getGemSet().getName() + i, new ItemStack(this, 8, i),
                    gem.getShardOreName(), "blockGlass");
        }
    }

    @Override
    public void addOreDict() {
        for (int i = 0; i < 16; ++i) {
            OreDictionary.registerOre("blockGlass", new ItemStack(this, 1, i));
        }
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        return this == block && blockState != iblockstate || block != this
                && super.shouldSideBeRendered(blockState, blockAccess, pos, side);

    }

    @Override
    String getBlockName() {
        return nameForSet(this.getGemSet(), Names.GEM_GLASS);
    }
}
