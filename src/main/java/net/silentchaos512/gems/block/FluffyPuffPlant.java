/*
 * Silent's Gems -- FluffyPuffPlant
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

import net.minecraft.block.CropsBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.PlantType;
import net.silentchaos512.gems.init.GemsItems;

public class FluffyPuffPlant extends CropsBlock {
    public FluffyPuffPlant() {
        super(Properties.create(Material.PLANTS)
                .tickRandomly()
                .hardnessAndResistance(0) // was 0.1
                .doesNotBlockMovement()
                .sound(SoundType.CROP)
        );
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return GemsItems.FLUFFY_PUFF_SEEDS;
    }

    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return PlantType.CROP;
    }
}
