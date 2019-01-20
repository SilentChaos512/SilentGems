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

import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.silentchaos512.gems.init.ModItems;

public class FluffyPuffPlant extends BlockCrops {
    public FluffyPuffPlant() {
        super(Builder.create(Material.PLANTS)
                .hardnessAndResistance(0)); // was 0.1
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

//    @Override
//    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
//        ItemStack heldItem = player.getHeldItem(hand);
//        if (!heldItem.isEmpty() && heldItem.getItem() == ModItems.sickle)
//            return false;
//
//        // Right-click to harvest
//        List<ItemStack> drops = Lists.newArrayList();
//        int age = state.getValue(AGE);
//
//        // Get drops if mature
//        if (age >= 7) {
//            for (int i = 0; i < 3; ++i)
//                if (i == 0 || RANDOM.nextInt(15) <= age)
//                    drops.add(new ItemStack(getCrop(), 1, damageDropped(state)));
//
//            // Soul gem drops (since they normally only drop when breaking blocks).
//            if (SilentGems.random.nextFloat() < 0.025f) {
//                drops.add(ModItems.soulGem.getStack("FluffyPuff"));
//            }
//
//            // Reset to newly planted state
//            world.setBlockState(pos, getDefaultState());
//        }
//
//        // Spawn items in world
//        for (ItemStack stack : drops) {
//            spawnAsEntity(world, pos, stack);
//        }
//
//        return !drops.isEmpty();
//    }



    @Override
    protected IItemProvider getSeedsItem() {
        return ModItems.fluffyPuffSeeds;
    }

    @Override
    protected IItemProvider getCropsItem() {
        return ModItems.fluffyPuff;
    }

    @Override
    public EnumPlantType getPlantType(IBlockReader world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

//    @Override
//    public void registerModels() {
//        Item item = Item.getItemFromBlock(this);
//        String fullName = SilentGems.RESOURCE_PREFIX + Names.FLUFFY_PUFF_PLANT;
//        for (int i = 0; i < 4; ++i) {
//            ModelResourceLocation model = new ModelResourceLocation(fullName + i, "inventory");
//            ModelLoader.setCustomModelResourceLocation(item, i, model);
//        }
//    }
}
