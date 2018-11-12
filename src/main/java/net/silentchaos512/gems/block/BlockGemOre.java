/*
 * Silent's Gems -- BlockGemOre
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ModRecipeHelper;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.Random;

public class BlockGemOre extends BlockGemSubtypes implements IAddRecipes {
    public BlockGemOre(EnumGem.Set set) {
        super(set);
        setHardness(3.0f);
        setResistance(15.0f);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        ItemStack ore, item;
        for (int i = 0; i < 16; ++i) {
            EnumGem gem = getGem(i);
            ore = gem.getOre();
            item = gem.getItem();
            // Smelting
            recipes.addSmelting(ore, item, 0.5f);
            // SAG Mill
            ModRecipeHelper.addSagMillRecipe(gem.getGemName() + "Ore", ore, item,
                    getGemSet() == EnumGem.Set.LIGHT ? "endstone"
                            : getGemSet() == EnumGem.Set.DARK ? "netherrack" : "cobblestone",
                    3000);
        }
    }

    @Override
    public void addOreDict() {
        for (int i = 0; i < 16; ++i) {
            EnumGem gem = getGem(i);
            OreDictionary.registerOre(gem.getOreOreName(), gem.getOre());
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ModItems.gem;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state) + this.getGemSet().startMeta;
    }

    @Override
    String getBlockName() {
        return nameForSet(this.getGemSet(), Names.GEM_ORE);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, damageDropped(state) & 0xF);
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        if (fortune > 0) {
            int j = random.nextInt(fortune + 2) - 1;

            if (j < 0) {
                j = 0;
            }

            return quantityDropped(random) * (j + 1);
        } else {
            return quantityDropped(random);
        }
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        Item item = getItemDropped(world.getBlockState(pos), RANDOM, fortune);
        if (item != Item.getItemFromBlock(this)) {
            return 1 + RANDOM.nextInt(5);
        }
        return 0;
    }
}
