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

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.PlantType;
import net.silentchaos512.gems.init.GemsItems;
import net.silentchaos512.utils.Lazy;

public final class FluffyPuffPlant extends CropsBlock {
    public static final Lazy<FluffyPuffPlant> NORMAL = Lazy.of(() -> new FluffyPuffPlant(false));
    public static final Lazy<FluffyPuffPlant> WILD = Lazy.of(() -> new FluffyPuffPlant(true));

    private final boolean wild;

    private FluffyPuffPlant(boolean wild) {
        super(Properties.create(Material.PLANTS)
                .tickRandomly()
                .hardnessAndResistance(0) // was 0.1
                .doesNotBlockMovement()
                .sound(SoundType.CROP)
        );
        this.wild = wild;
    }

    public BlockState getMaturePlant() {
        return withAge(getMaxAge());
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        if (wild) {
            Block block = state.getBlock();
            return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL;
        }
        return super.isValidGround(state, worldIn, pos);
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return GemsItems.fluffyPuffSeeds;
    }

    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return PlantType.Crop;
    }
}
