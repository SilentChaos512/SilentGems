/*
 * Silent's Gems -- BlockFluffyPuffPlant
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

import com.google.common.collect.Lists;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.util.StackHelper;

import java.util.List;

public class BlockFluffyPuffPlant extends BlockCrops implements ICustomModel {
    public BlockFluffyPuffPlant() {
        setHardness(0.1f);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(getSeed()));
        int age = getAge(state);

        if (age >= 7) {
            int seedCount = 1 + fortune;
            int puffCount = 2 + fortune + SilentGems.random.nextInt(3);
            // Seeds
            for (int i = 0; i < seedCount; ++i)
                if (SilentGems.random.nextInt(15) <= age)
                    drops.add(new ItemStack(getSeed()));
            // Puffs
            for (int i = 0; i < puffCount; ++i)
                drops.add(new ItemStack(getCrop()));
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (StackHelper.isValid(heldItem) && heldItem.getItem() == ModItems.sickle)
            return false;

        // Right-click to harvest
        List<ItemStack> drops = Lists.newArrayList();
        int age = state.getValue(AGE);

        // Get drops if mature
        if (age >= 7) {
            for (int i = 0; i < 3; ++i)
                if (i == 0 || RANDOM.nextInt(15) <= age)
                    drops.add(new ItemStack(getCrop(), 1, damageDropped(state)));

            // Soul gem drops (since they normally only drop when breaking blocks).
            if (SilentGems.random.nextFloat() < 0.025f) {
                drops.add(ModItems.soulGem.getStack("FluffyPuff"));
            }

            // Reset to newly planted state
            world.setBlockState(pos, getDefaultState());
        }

        // Spawn items in world
        for (ItemStack stack : drops) {
            spawnAsEntity(world, pos, stack);
        }

        return !drops.isEmpty();
    }

    @Override
    protected Item getSeed() {
        return ModItems.fluffyPuffSeeds;
    }

    @Override
    protected Item getCrop() {
        return ModItems.fluffyPuff;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public void registerModels() {
        Item item = Item.getItemFromBlock(this);
        String fullName = SilentGems.RESOURCE_PREFIX + Names.FLUFFY_PUFF_PLANT;
        for (int i = 0; i < 4; ++i) {
            ModelResourceLocation model = new ModelResourceLocation(fullName + i, "inventory");
            ModelLoader.setCustomModelResourceLocation(item, i, model);
        }
    }
}
