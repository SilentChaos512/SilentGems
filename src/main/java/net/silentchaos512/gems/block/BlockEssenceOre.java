/*
 * Silent's Gems -- BlockEssenceOre
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
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ModRecipeHelper;
import net.silentchaos512.lib.block.BlockMetaSubtypes;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

public class BlockEssenceOre extends BlockMetaSubtypes implements ICustomModel, IAddRecipes {
    public enum Type implements IStringSerializable {
        CHAOS, ENDER;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("gem", Type.class);

    public BlockEssenceOre() {
        super(Material.ROCK, Type.values().length);
        setDefaultState(blockState.getBaseState().withProperty(VARIANT, Type.CHAOS));
        setHardness(4.0f);
        setResistance(15.0f);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 3, getDefaultState().withProperty(VARIANT, Type.CHAOS));
        setHarvestLevel("pickaxe", 4, getDefaultState().withProperty(VARIANT, Type.ENDER));
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        ItemStack chaosOre = new ItemStack(this, 1, 0);
        ItemStack chaosEssence = CraftingItems.CHAOS_ESSENCE.getStack();
        GameRegistry.addSmelting(chaosOre, chaosEssence, 0.7f);
        ModRecipeHelper.addSagMillRecipe("ChaosOre", chaosOre, chaosEssence, "cobblestone", 4000);

        ItemStack enderOre = new ItemStack(this, 1, 1);
        ItemStack enderEssence = CraftingItems.ENDER_ESSENCE.getStack();
        GameRegistry.addSmelting(enderOre, enderEssence, 1.0f);
        ModRecipeHelper.addSagMillRecipe("EnderEssenceOre", enderOre, enderEssence, "end_stone", 4000);
    }

    @Override
    public void addOreDict() {
        OreDictionary.registerOre("oreChaos", new ItemStack(this, 1, 0));
        OreDictionary.registerOre("oreEnderEssence", new ItemStack(this, 1, 1));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, Type.values()[MathHelper.clamp(meta, 0, Type.values().length - 1)]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public void registerModels() {
        Item item = Item.getItemFromBlock(this);
        String fullName = SilentGems.RESOURCE_PREFIX + Names.ESSENCE_ORE;
        for (Type type : Type.values()) {
            ModelResourceLocation model = new ModelResourceLocation(fullName, "gem=" + type.getName());
            ModelLoader.setCustomModelResourceLocation(item, type.ordinal(), model);
        }
    }
}
