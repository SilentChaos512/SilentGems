/*
 * Silent's Gems -- BlockFluffyBlock
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

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.block.BlockMetaSubtypes;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

public class BlockFluffyBlock extends BlockMetaSubtypes implements ICustomModel, IAddRecipes {
    private static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    public BlockFluffyBlock() {
        super(Material.CLOTH, 16);
        setDefaultState(blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
        setHardness(0.8f);
        setResistance(3.0f);
        setSoundType(SoundType.CLOTH);
        setHarvestLevel("", 0);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        ItemStack any = new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            String dyeName = color.getTranslationKey();
            if (dyeName.equals("silver")) {
                dyeName = "lightGray";
            }
            dyeName = "dye" + Character.toUpperCase(dyeName.charAt(0)) + dyeName.substring(1);
            recipes.addSurroundOre("fluffy_block_" + color.ordinal(), new ItemStack(this, 8, color.getMetadata()), dyeName, any);
        }
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float distance) {
        if (distance < 2 || world.isRemote) return;

        // Count the number of fluffy blocks that are stacked up.
        int stackedBlocks = 0;
        while (world.getBlockState(pos).getBlock() == this) {
            pos = pos.down();
            ++stackedBlocks;
        }

        // Reduce fall distance by 10 blocks per stacked block
        distance -= Math.min(10 * stackedBlocks, distance);
        entity.fallDistance = 0f;
        entity.fall(distance, 1f);
    }

    public void onGetBreakSpeed(PlayerEvent.BreakSpeed event) {
        ItemStack mainHand = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
        if (!mainHand.isEmpty() && mainHand.getItem() instanceof ItemShears) {
            int efficiency = EnchantmentHelper.getEfficiencyModifier(event.getEntityPlayer());

            float speed = event.getNewSpeed() * 4;
            if (efficiency > 0) {
                speed += (efficiency * efficiency + 1);
            }

            event.setNewSpeed(speed);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(COLOR).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, COLOR);
    }

    @Override
    public void registerModels() {
        Item item = Item.getItemFromBlock(this);
        String fullName = SilentGems.RESOURCE_PREFIX + Names.FLUFFY_BLOCK;
        for (EnumDyeColor color : EnumDyeColor.values()) {
            ModelResourceLocation model = new ModelResourceLocation(fullName, "color=" + color.getName());
            ModelLoader.setCustomModelResourceLocation(item, color.getMetadata(), model);
        }
    }
}
