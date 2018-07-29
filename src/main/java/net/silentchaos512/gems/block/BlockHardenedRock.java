/*
 * Silent's Gems -- BlockHardenedRock
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
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.block.BlockMetaSubtypes;
import net.silentchaos512.lib.registry.ICustomModel;

public class BlockHardenedRock extends BlockMetaSubtypes implements ICustomModel {
    public enum Type implements IStringSerializable {
        STONE, NETHERRACK, END_STONE;

        @Override
        public String getName() {
            return name().toLowerCase();
        }

    }
    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("type", Type.class);

    public BlockHardenedRock() {
        super(Material.ROCK, Type.values().length);
        setHardness(50f);
        setResistance(2000f);
        setDefaultState(blockState.getBaseState().withProperty(VARIANT, Type.STONE));
        setHarvestLevel("pickaxe", 3);
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
        String fullName = SilentGems.RESOURCE_PREFIX + Names.HARDENED_ROCK;
        for (Type type : Type.values()) {
            ModelResourceLocation model = new ModelResourceLocation(fullName, "type=" + type.getName());
            ModelLoader.setCustomModelResourceLocation(item, type.ordinal(), model);
        }
    }
}
