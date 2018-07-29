/*
 * Silent's Gems -- BlockGem
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

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.List;

public class BlockGem extends BlockGemSubtypes implements IAddRecipes {
    public final boolean supercharged;

    public BlockGem(EnumGem.Set set, boolean supercharged) {
        super(set);
        this.supercharged = supercharged;

        setHardness(supercharged ? 7.0f : 3.0f);
        setResistance(supercharged ? 6000000.0F : 30.0f);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", supercharged ? 3 : 1);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        for (int i = 0; i < 16; ++i) {
            EnumGem gem = getGem(i);

            if (supercharged) {
                recipes.addShapedOre(gem.name() + "_block_super", gem.getBlockSuper(), " g ", "gog", " g ",
                        'g', gem.getItemSuperOreName(), 'o', "obsidian");
            } else {
                recipes.addCompression(gem.name() + "_block", gem.getItem(), gem.getBlock(), 9);
            }
        }
    }

    @Override
    public void addOreDict() {
        for (int i = 0; i < 16; ++i) {
            EnumGem gem = getGem(i);
            if (supercharged) {
                OreDictionary.registerOre(gem.getBlockSuperOreName(), gem.getBlockSuper());
            } else {
                OreDictionary.registerOre(gem.getBlockOreName(), gem.getBlock());
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        list.addAll(SilentGems.localizationHelper.getBlockDescriptionLines(Names.GEM_BLOCK));
    }

//    @Override
//    public EnumRarity getRarity(int meta) {
//
//        return supercharged ? EnumRarity.RARE : EnumRarity.COMMON;
//    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        // Does not prevent Withers from breaking. Only the Ender Dragon seems to call this...
        if (supercharged) {
            return entity instanceof EntityPlayer;
        }
        return super.canEntityDestroy(state, world, pos, entity);
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return true;
    }

    @Override
    String getBlockName() {
        return nameForSet(this.getGemSet(), Names.GEM_BLOCK);
    }
}
