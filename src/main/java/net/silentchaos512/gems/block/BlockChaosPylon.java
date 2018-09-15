/*
 * Silent's Gems -- BlockChaosPylon
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

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.gui.GuiTypes;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.lib.EnumPylonType;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosPylon;
import net.silentchaos512.lib.block.ITileEntityBlock;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.Locale;

public class BlockChaosPylon extends BlockContainer implements ITileEntityBlock, ICustomModel, IAddRecipes {
    public enum VariantType implements IStringSerializable {
        PASSIVE, BURNER;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    private static final PropertyEnum<VariantType> VARIANT = PropertyEnum.create("variant", VariantType.class);
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.25f, 0.0f, 0.25f, 0.75f, 1.0f, 0.75f);

    public BlockChaosPylon() {
        super(Material.IRON);
        setDefaultState(blockState.getBaseState().withProperty(VARIANT, VariantType.PASSIVE));
        setLightLevel(0.25f);
        setLightOpacity(0);
        setHardness(6.0f);
        setResistance(1000.0f);
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileChaosPylon.class;
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        ItemStack pylonPassive = new ItemStack(this, 1, EnumPylonType.PASSIVE.getMeta());
        ItemStack pylonBurner = new ItemStack(this, 1, EnumPylonType.BURNER.getMeta());
        ItemStack chaosCore = CraftingItems.CHAOS_CORE.getStack();

        recipes.addShapedOre("chaos_pylon_passive", pylonPassive,
                "lel", "lol", "ooo",
                'e', chaosCore, 'l', "gemLapis", 'o', "obsidian");
        recipes.addShapedOre("chaos_pylon_burner", pylonBurner,
                " e ", "rpr", "ofo",
                'p', pylonPassive, 'e', chaosCore, 'f', Blocks.FURNACE, 'r', "blockRedstone", 'o', "obsidian");
    }

    @Override
    public void registerModels() {
        Item item = Item.getItemFromBlock(this);
        String fullName = SilentGems.RESOURCE_PREFIX + Names.CHAOS_PYLON;
        for (VariantType type : VariantType.values()) {
            ModelResourceLocation model = new ModelResourceLocation(fullName + type.ordinal(), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, type.ordinal(), model);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        TileChaosPylon toReturn = new TileChaosPylon();
        toReturn.setPylonType(EnumPylonType.getByMeta(meta));
        return toReturn;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        EnumPylonType type = EnumPylonType.getByMeta(getMetaFromState(state));

        if (world.isRemote) {
            return type == EnumPylonType.BURNER;
        }

        if (type == EnumPylonType.BURNER) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileChaosPylon) {
                GuiTypes.BURNER_PYLON.open(player, world, pos);
            }
            return true;
        }

        return false;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        Item item = Item.getItemFromBlock(this);
        for (EnumPylonType type : EnumPylonType.values()) {
            if (type != EnumPylonType.NONE) {
                list.add(new ItemStack(item, 1, type.getMeta()));
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT,
                VariantType.values()[MathHelper.clamp(meta, 0, VariantType.values().length)]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{VARIANT});
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tilePylon = world.getTileEntity(pos);
        if (tilePylon instanceof TileChaosPylon) {
            InventoryHelper.dropInventoryItems(world, pos, (TileChaosPylon) tilePylon);
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED; // I got yo' back!
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState worldIn, IBlockAccess pos, BlockPos state) {
        // return BOUNDING_BOX;
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
        // return super.getBoundingBox(state, source, pos);
    }
}
