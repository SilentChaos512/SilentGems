/*
 * Silent's Gems -- BlockGemSubtypes
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

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.lib.block.BlockMetaSubtypes;
import net.silentchaos512.lib.registry.ICustomModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public abstract class BlockGemSubtypes extends BlockMetaSubtypes implements ICustomModel {
    @Nullable
    private final EnumGem.Set gemSet;

    BlockGemSubtypes(EnumGem.Set set) {
        this(set, Material.ROCK);
    }

    BlockGemSubtypes(@Nullable EnumGem.Set set, Material material) {
        super(material, 16);
        this.gemSet = set;
        if (set != null)
            setDefaultState(this.blockState.getBaseState().withProperty(EnumGem.VARIANT_GEM, EnumGem.RUBY));
    }

    abstract String getBlockName();

    public EnumGem getGem(int meta) {
        if (meta < 0 || meta > 15) {
            return EnumGem.RUBY;
        }
        return EnumGem.values()[meta + this.getGemSet().startMeta];
    }

    @Nonnull
    public EnumGem.Set getGemSet() {
        return this.gemSet == null ? EnumGem.Set.CLASSIC : this.gemSet;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getGem(getMetaFromState(state)).ordinal() & 0xF;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(EnumGem.VARIANT_GEM, EnumGem.values()[meta & 0xF]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(EnumGem.VARIANT_GEM).ordinal() & 0xF;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, EnumGem.VARIANT_GEM);
    }

    @Override
    public void registerModels() {
        Item item = Item.getItemFromBlock(this);
        String fullName = SilentGems.RESOURCE_PREFIX + getBlockName();
        for (int i = 0; i < 16; ++i) {
            EnumGem gem = EnumGem.values()[i];
            ModelResourceLocation model = new ModelResourceLocation(fullName, "gem=" + gem.getName());
            ModelLoader.setCustomModelResourceLocation(item, i, model);
        }
    }

    static String nameForSet(EnumGem.Set set, String baseName) {
        switch (set) {
            case CLASSIC:
                return baseName;
            case DARK:
                return baseName + "dark";
            case LIGHT:
                return baseName + "light";
            default:
                return baseName + "unknown";
        }
    }
}
