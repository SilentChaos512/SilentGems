/*
 * Silent's Gems -- HardenedRock
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
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.silentchaos512.utils.Lazy;

import java.util.Locale;

public enum HardenedRock implements IItemProvider, IStringSerializable {
    STONE, NETHERRACK, END_STONE;

    private final Lazy<Block> block;

    public Block getBlock() {
        return block.get();
    }

    @Override
    public Item asItem() {
        return getBlock().asItem();
    }

    @Override
    public String getName() {
        return "hardened_" + name().toLowerCase(Locale.ROOT);
    }

    HardenedRock() {
        block = Lazy.of(HardenedRockBlock::new);
    }

    public static class HardenedRockBlock extends Block {
        HardenedRockBlock() {
            super(Properties.create(Material.ROCK)
                    .hardnessAndResistance(50, 2000));
        }

        @Override
        public int getHarvestLevel(IBlockState state) {
            return 3;
        }
    }
}
