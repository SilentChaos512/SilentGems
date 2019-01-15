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

import java.util.Locale;

public final class HardenedRock extends Block {
    public enum Type {
        STONE, NETHERRACK, END_STONE;

        private final HardenedRock block;

        Type() {
            this.block = new HardenedRock();
        }

        public HardenedRock getBlock() {
            return block;
        }

        public String getName() {
            return "hardened_" + name().toLowerCase(Locale.ROOT);
        }
    }

    private HardenedRock() {
        super(Builder.create(Material.ROCK)
                .hardnessAndResistance(50, 2000));
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return 3;
    }
}
