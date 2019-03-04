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

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.item.FluffyPuffSeeds;
import net.silentchaos512.utils.Lazy;

public final class FluffyPuffPlant extends BlockCrops {
    public static final Lazy<FluffyPuffPlant> NORMAL = Lazy.of(() -> new FluffyPuffPlant(false));
    public static final Lazy<FluffyPuffPlant> WILD = Lazy.of(() -> new FluffyPuffPlant(true));

    private final boolean wild;

    private FluffyPuffPlant(boolean wild) {
        super(Properties.create(Material.PLANTS)
                .hardnessAndResistance(0) // was 0.1
                .doesNotBlockMovement()
                .sound(SoundType.PLANT)
        );
        this.wild = wild;
    }

    public IBlockState getMaturePlant() {
        return withAge(getMaxAge());
    }

    @Override
    protected boolean isValidGround(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        if (wild) {
            Block block = state.getBlock();
            return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL;
        }
        return super.isValidGround(state, worldIn, pos);
    }

    @Override
    public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune) {
        drops.add(new ItemStack(getSeedsItem()));
        int age = getAge(state);

        if (age >= 7) {
            int seedCount = 1 + fortune;
            int puffCount = 2 + fortune + RANDOM.nextInt(3);
            // Seeds
            for (int i = 0; i < seedCount; ++i)
                if (RANDOM.nextInt(15) <= age)
                    drops.add(new ItemStack(getSeedsItem()));
            // Puffs
            for (int i = 0; i < puffCount; ++i)
                drops.add(new ItemStack(getCropsItem()));
        }
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return FluffyPuffSeeds.INSTANCE.get();
    }

    @Override
    protected IItemProvider getCropsItem() {
        return CraftingItems.FLUFFY_PUFF;
    }

    @Override
    public EnumPlantType getPlantType(IBlockReader world, BlockPos pos) {
        return EnumPlantType.Crop;
    }
}
