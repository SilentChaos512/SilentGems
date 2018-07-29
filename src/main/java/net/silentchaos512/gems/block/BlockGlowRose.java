/*
 * Silent's Gems -- BlockGlowRose
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

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

public class BlockGlowRose extends BlockBush implements ICustomModel, IAddRecipes {
    public BlockGlowRose() {
        setDefaultState(blockState.getBaseState().withProperty(EnumGem.VARIANT_GEM, EnumGem.RUBY));
        setSoundType(SoundType.PLANT);
        lightValue = GemsConfig.GLOW_ROSE_LIGHT_LEVEL;
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        // Flowers to dye.
        // 0=black
        addFlowerToDyeRecipe(recipes, EnumDyeColor.BLACK, EnumGem.ONYX);
        // 1=red
        addFlowerToDyeRecipe(recipes, EnumDyeColor.RED, EnumGem.RUBY);
        addFlowerToDyeRecipe(recipes, EnumDyeColor.RED, EnumGem.GARNET);
        // 2=green
        addFlowerToDyeRecipe(recipes, EnumDyeColor.GREEN, EnumGem.BERYL);
        // 3=brown
        // 4=blue
        addFlowerToDyeRecipe(recipes, EnumDyeColor.BLUE, EnumGem.SAPPHIRE);
        addFlowerToDyeRecipe(recipes, EnumDyeColor.BLUE, EnumGem.IOLITE);
        // 5=purple
        addFlowerToDyeRecipe(recipes, EnumDyeColor.PURPLE, EnumGem.AMETHYST);
        // 6=cyan
        addFlowerToDyeRecipe(recipes, EnumDyeColor.CYAN, EnumGem.INDICOLITE);
        // 7=light gray
        // 8=gray
        // 9=pink
        addFlowerToDyeRecipe(recipes, EnumDyeColor.PINK, EnumGem.MORGANITE);
        // 10=lime
        addFlowerToDyeRecipe(recipes, EnumDyeColor.LIME, EnumGem.PERIDOT);
        // 11=yellow
        addFlowerToDyeRecipe(recipes, EnumDyeColor.YELLOW, EnumGem.HELIODOR);
        // 12=light blue
        addFlowerToDyeRecipe(recipes, EnumDyeColor.LIGHT_BLUE, EnumGem.AQUAMARINE);
        // 13-magenta
        addFlowerToDyeRecipe(recipes, EnumDyeColor.MAGENTA, EnumGem.AGATE);
        // 14-orange
        addFlowerToDyeRecipe(recipes, EnumDyeColor.ORANGE, EnumGem.TOPAZ);
        addFlowerToDyeRecipe(recipes, EnumDyeColor.ORANGE, EnumGem.AMBER);
        // 15-white
        addFlowerToDyeRecipe(recipes, EnumDyeColor.WHITE, EnumGem.OPAL);
    }

    private void addFlowerToDyeRecipe(RecipeMaker recipes, EnumDyeColor dye, EnumGem gem) {

        ItemStack dyeStack;
        ItemStack glowRose = new ItemStack(this, 1, gem.ordinal() & 0xF);

        if (dye == EnumDyeColor.BLACK || dye == EnumDyeColor.BLUE) {
            dyeStack = new ItemStack(ModItems.dye, 2, dye.getDyeDamage());
        } else {
            dyeStack = new ItemStack(Items.DYE, 2, dye.getDyeDamage());
        }

        recipes.addShapeless("glowrose_dye_" + dye.name() + "_" + gem.name(), dyeStack, glowRose);
    }

    @Override
    public void registerModels() {
        Item item = Item.getItemFromBlock(this);
        String fullName = SilentGems.RESOURCE_PREFIX + Names.GLOW_ROSE;
        for (int i = 0; i < 16; ++i)
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(fullName + i, "inventory"));
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        for (int i = 0; i < 16; ++i)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(EnumGem.VARIANT_GEM, EnumGem.values()[meta & 0xF]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(EnumGem.VARIANT_GEM).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, EnumGem.VARIANT_GEM);
    }
}
