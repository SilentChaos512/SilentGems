/*
 * Silent's Gems -- BlockChaosFlowerPot
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

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.tile.TileChaosFlowerPot;
import net.silentchaos512.lib.block.ITileEntityBlock;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.wit.api.IWitHudInfo;

import java.util.ArrayList;
import java.util.List;

public class BlockChaosFlowerPot extends Block implements ITileEntityProvider, ITileEntityBlock, IAddRecipes, IWitHudInfo {
    private static final AxisAlignedBB FLOWER_POT_AABB = new AxisAlignedBB(0.3125, 0.0, 0.3125, 0.6875, 0.375, 0.6875);
    @Deprecated
    public IRecipe recipe;

    public BlockChaosFlowerPot() {
        super(Material.CIRCUITS);
        setHardness(1.0f);
        setResistance(30.0f);
        lightValue = 2;
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileChaosFlowerPot.class;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);

        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemBlock) {
            TileChaosFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);

            if (tileentityflowerpot == null) {
                return false;
            } else if (!tileentityflowerpot.getFlowerItemStack().isEmpty()) {
                return false;
            } else {
                Block block = Block.getBlockFromItem(heldItem.getItem());

                if (block != ModBlocks.glowRose) {
                    return false;
                } else {
                    ItemStack flower = new ItemStack(heldItem.getItem(), 1, heldItem.getItemDamage());
                    tileentityflowerpot.setFlowerItemStack(flower);
                    tileentityflowerpot.markDirty();
                    worldIn.notifyBlockUpdate(pos, state, state, 3);
                    worldIn.checkLight(pos);
                    playerIn.addStat(StatList.FLOWER_POTTED);

                    if (playerIn instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) playerIn, pos, heldItem);

                    if (!playerIn.capabilities.isCreativeMode) {
                        heldItem.shrink(1);
                    }

                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileChaosFlowerPot tile = getTileEntity(world, pos);
        return tile != null && !tile.getFlowerItemStack().isEmpty() ? 15 : lightValue;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    public TileChaosFlowerPot getTileEntity(IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileChaosFlowerPot)) {
            return null;
        }
        return (TileChaosFlowerPot) tile;
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        recipe = recipes.addShaped("chaos_flower_pot", new ItemStack(this), "c", "f", 'c',
                ModItems.craftingMaterial.chaosEssenceEnriched, 'f', Items.FLOWER_POT);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FLOWER_POT_AABB;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos)
                && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!world.getBlockState(pos.down()).isSideSolid(world, pos, EnumFacing.UP)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
        TileChaosFlowerPot te = getTileEntity(world, pos);
        if (te != null && !te.getFlowerItemStack().isEmpty()) {
            drops.add(te.getFlowerItemStack());
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack tool) {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileChaosFlowerPot();
    }

    @Override
    public List<String> getWitLines(IBlockState state, BlockPos pos, EntityPlayer player, boolean advanced) {
        List<String> list = new ArrayList<>();
        TileEntity te = player.world.getTileEntity(pos);
        if (te instanceof TileChaosFlowerPot) {
            int flowerId = ((TileChaosFlowerPot) te).getFlowerId();
            if (flowerId >= 0 && flowerId < 16) {
                String key = "tile.silentgems.glowrose" + flowerId + ".name";
                list.add(TextFormatting.GRAY + SilentGems.i18n.translate(key));
            }
        }
        return list;
    }
}
