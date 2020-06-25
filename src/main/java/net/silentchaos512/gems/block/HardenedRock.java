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
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.init.Registration;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.registry.BlockRegistryObject;

import java.util.Locale;

public enum HardenedRock implements IBlockProvider {
    STONE, NETHERRACK, END_STONE;

    private BlockRegistryObject<Block> block;

    public static void registerBlocks() {
        for (HardenedRock rock : values()) {
            rock.block = new BlockRegistryObject<>(Registration.BLOCKS.register(rock.getName(), HardenedRockBlock::new));
            Registration.ITEMS.register(rock.getName(), GemsBlocks.defaultItem(rock.block));
        }
    }

    @Override
    public Block asBlock() {
        return block.get();
    }

    public String getName() {
        return "hardened_" + name().toLowerCase(Locale.ROOT);
    }

    public static class HardenedRockBlock extends Block {
        HardenedRockBlock() {
            super(Properties.create(Material.ROCK)
                    .hardnessAndResistance(50, 2000)
                    .harvestTool(ToolType.PICKAXE)
            );
        }

        @Override
        public int getHarvestLevel(BlockState state) {
            return 3;
        }
    }
}
