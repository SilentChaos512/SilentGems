/*
 * Silent's Gems -- BlockMisc
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.block.BlockMetaSubtypes;
import net.silentchaos512.lib.item.ItemBlockMetaSubtypes;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.Locale;

public class BlockMisc extends BlockMetaSubtypes implements ICustomModel, IAddRecipes {
    public enum Type implements IStringSerializable {
        CHAOS_ESSENCE, CHAOS_ESSENCE_ENRICHED, CHAOS_ESSENCE_CRYSTALLIZED, CHAOS_COAL, CHAOS_IRON, ENDER_ESSENCE;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public int getMetadata() {
            return ordinal();
        }
    }
    private static final PropertyEnum<Type> VARIANT = PropertyEnum.create("type", Type.class);

    public BlockMisc() {
        super(Material.IRON, Type.values().length);
        setHardness(3.0f);
        setResistance(30.0f);
        setDefaultState(blockState.getBaseState().withProperty(VARIANT, Type.CHAOS_ESSENCE));
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        ItemStack chaosCoal = getStack(Type.CHAOS_COAL, 1);
        ItemStack chaosEssence = getStack(Type.CHAOS_ESSENCE, 1);
        ItemStack chaosEssenceCrystallized = getStack(Type.CHAOS_ESSENCE_CRYSTALLIZED, 1);
        ItemStack chaosEssenceEnriched = getStack(Type.CHAOS_ESSENCE_ENRICHED, 1);
        ItemStack chaosIron = getStack(Type.CHAOS_IRON, 1);
        ItemStack enderEssence = getStack(Type.ENDER_ESSENCE, 1);

        recipes.addCompression("chaos_essence_block", CraftingItems.CHAOS_ESSENCE.getStack(), chaosEssence, 9);
        recipes.addCompression("chaos_essence_enriched_block", CraftingItems.ENRICHED_CHAOS_ESSENCE.getStack(), chaosEssenceEnriched, 9);
        recipes.addCompression("chaos_essence_crystallized_block", CraftingItems.CRYSTALLIZED_CHAOS_ESSENCE.getStack(), chaosEssenceCrystallized, 9);
        recipes.addCompression("chaos_coal_block", CraftingItems.CHAOS_COAL.getStack(), chaosCoal, 9);
        recipes.addCompression("chaos_iron_block", CraftingItems.CHAOS_IRON.getStack(), chaosIron, 9);
        recipes.addCompression("ender_essence_block", CraftingItems.ENDER_ESSENCE.getStack(), enderEssence, 9);
    }

    public ItemStack getStack(Type type, int count) {
        return new ItemStack(this, count, type.ordinal());
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        IBlockState enderBlock = this.getStateFromMeta(Type.ENDER_ESSENCE.ordinal());
        // Levitation effect from ender essence blocks
        if (entityIn instanceof EntityLivingBase && worldIn.getBlockState(pos) == enderBlock) {
            int stackedBlocks = 1;
            IBlockState state = worldIn.getBlockState(pos.down(stackedBlocks));
            // Stack up to 5 blocks, each increasing the levitation level
            while (state == enderBlock && stackedBlocks < 5) {
                state = worldIn.getBlockState(pos.down(++stackedBlocks));
            }
            ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 100, stackedBlocks - 1));
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        meta = MathHelper.clamp(meta, 0, Type.values().length - 1);
        return this.getDefaultState().withProperty(VARIANT, Type.values()[meta]);
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
        String fullName = SilentGems.RESOURCE_PREFIX + Names.MISC_BLOCK;
        for (Type type : Type.values()) {
            ModelResourceLocation model = new ModelResourceLocation(fullName, "type=" + type.getName());
            ModelLoader.setCustomModelResourceLocation(item, type.ordinal(), model);
        }
    }

    public static class ItemBlock extends ItemBlockMetaSubtypes {
        public ItemBlock(BlockMisc block) {
            super(block);
        }

        @Override
        public int getItemBurnTime(ItemStack itemStack) {
            if (itemStack.getItem() == this && itemStack.getItemDamage() == Type.CHAOS_COAL.ordinal())
                return 10 * GemsConfig.BURN_TIME_CHAOS_COAL;
            return 0;
        }

        @Override
        public EnumRarity getRarity(ItemStack stack) {
            switch (Type.values()[MathHelper.clamp(stack.getItemDamage(), 0, Type.values().length - 1)]) {
                case CHAOS_ESSENCE_CRYSTALLIZED:
                    return EnumRarity.EPIC;
                case CHAOS_ESSENCE_ENRICHED:
                    return EnumRarity.RARE;
                default:
                    return EnumRarity.COMMON;
            }
        }
    }
}



