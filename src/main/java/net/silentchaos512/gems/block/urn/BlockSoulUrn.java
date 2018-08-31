/*
 * Silent's Gems -- BlockSoulUrn
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

package net.silentchaos512.gems.block.urn;

import com.google.gson.JsonObject;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.gui.GuiHandlerSilentGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.init.ModSounds;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.lib.block.IColoredBlock;
import net.silentchaos512.lib.block.ITileEntityBlock;
import net.silentchaos512.lib.recipe.RecipeJsonHell;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BlockSoulUrn extends BlockContainer implements ITileEntityBlock, IColoredBlock, ICustomModel, IAddRecipes {
    public enum LidState implements IStringSerializable {
        OPEN, CLOSED;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    private static final String NBT_ROOT = "silentgems_soul_urn";
    private static final String NBT_COLOR = "color";
    private static final String NBT_GEM = "gem";

    //@formatter:off
    private static final AxisAlignedBB BOUNDING_BOX_CLOSED = new AxisAlignedBB(
             1/16f,  0/16f,  1/16f,
            15/16f, 15/16f, 15/16f);
    private static final AxisAlignedBB BOUNDING_BOX_OPEN = new AxisAlignedBB(
             1/16f,  0/16f,  1/16f,
            15/16f, 14/16f, 15/16f);
    //@formatter:on

    static final PropertyEnum<LidState> PROPERTY_LID = PropertyEnum.create("lid", LidState.class);
    private static final PropertyEnum<EnumFacing> PROPERTY_FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockSoulUrn() {
        super(Material.CIRCUITS);
        this.setHardness(0.7f);
        this.setResistance(20f);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileSoulUrn();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileSoulUrn.class;
    }

    @Nullable
    public EnumDyeColor getClayColor(ItemStack stack) {
        NBTTagCompound tags = stack.getOrCreateSubCompound(NBT_ROOT);
        if (tags.hasKey(NBT_COLOR)) {
            String str = tags.getString(NBT_COLOR);
            for (EnumDyeColor color : EnumDyeColor.values())
                if (color.getName().equals(str))
                    return color;
        }
        return null;
    }

    public void setClayColor(ItemStack stack, EnumDyeColor color) {
        stack.getOrCreateSubCompound(NBT_ROOT).setString(NBT_COLOR, color.getName());
    }

    @Nullable
    public EnumGem getGem(ItemStack stack) {
        NBTTagCompound tags = stack.getOrCreateSubCompound(NBT_ROOT);
        if (tags.hasKey(NBT_GEM)) {
            String str = tags.getString(NBT_GEM);
            for (EnumGem gem : EnumGem.values())
                if (gem.getName().equals(str))
                    return gem;
        }
        return null;
    }

    public void setGem(ItemStack stack, EnumGem gem) {
        stack.getOrCreateSubCompound(NBT_ROOT).setString(NBT_GEM, gem.getName());
    }

    public ItemStack getStack(@Nullable EnumDyeColor color, @Nullable EnumGem gem) {
        ItemStack stack = new ItemStack(this);
        if (color != null) this.setClayColor(stack, color);
        if (gem != null) this.setGem(stack, gem);
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        EnumDyeColor color = this.getClayColor(stack);
        EnumGem gem = this.getGem(stack);

        if (color != null) {
            String colorName = SilentGems.i18n.translate(color.getTranslationKey());
            tooltip.add(SilentGems.i18n.subText(this, "color", colorName));
        } else {
            String str = SilentGems.i18n.subText(this, "color.uncolored");
            tooltip.add(SilentGems.i18n.subText(this, "color", str));
        }

        if (gem != null) {
            String str = SilentGems.i18n.translatedName(gem.getItem());
            tooltip.add(SilentGems.i18n.subText(this, "gem", str));
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileSoulUrn) {
            TileSoulUrn tileSoulUrn = (TileSoulUrn) tile;
            tileSoulUrn.setDestroyedByCreativePlayer(player.capabilities.isCreativeMode);
            tileSoulUrn.fillWithLoot(player);
        }
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        final boolean playSounds = true;//!SilentGems.instance.isDevBuild();

        if (!worldIn.isRemote) {
            LidState lidState = state.getValue(PROPERTY_LID);

            if (playerIn.isSneaking() || lidState == LidState.CLOSED) {
                // Toggle lid state when sneaking
                worldIn.setBlockState(pos, this.toggleLid(state), 2);
                if (playSounds) {
                    worldIn.playSound(null, pos, ModSounds.SOUL_URN_LID, SoundCategory.BLOCKS, 0.6f,
                            (float) (1.1f + 0.05f * SilentGems.random.nextGaussian()));
                }
            } else if (lidState == LidState.OPEN) {
                // Open inventory if lid is open
                TileEntity tile = worldIn.getTileEntity(pos);
                if (tile instanceof TileSoulUrn) {
                    playerIn.openGui(SilentGems.instance, GuiHandlerSilentGems.GuiType.SOUL_URN.id,
                            worldIn, pos.getX(), pos.getY(), pos.getZ());
                    if (playSounds) {
                        worldIn.playSound(null, pos, ModSounds.SOUL_URN_OPEN, SoundCategory.BLOCKS, 0.6f,
                                (float) (1.1f + 0.05f * SilentGems.random.nextGaussian()));
                    }
                }
            }
        }

        return true;
    }

    private IBlockState toggleLid(IBlockState state) {
        LidState lid = state.getValue(PROPERTY_LID);
        return state.withProperty(PROPERTY_LID, lid == LidState.CLOSED ? LidState.OPEN : LidState.CLOSED);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        EnumFacing side = placer.getHorizontalFacing().getOpposite();
        IBlockState newState = state.withProperty(PROPERTY_FACING, side).withProperty(PROPERTY_LID, LidState.CLOSED);

        worldIn.setBlockState(pos, newState, 2);

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileSoulUrn) {
            TileSoulUrn tileSoulUrn = (TileSoulUrn) tile;
            if (stack.hasDisplayName()) {
                tileSoulUrn.setCustomName(stack.getDisplayName());
            }
            tileSoulUrn.setColor(this.getClayColor(stack));
            tileSoulUrn.setGem(this.getGem(stack));
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileSoulUrn) {
            TileSoulUrn tileSoulUrn = (TileSoulUrn) tile;

            if (!tileSoulUrn.isCleared() && tileSoulUrn.shouldDrop()) {
                ItemStack stack = new ItemStack(this);
                NBTTagCompound compound = new NBTTagCompound();
                NBTTagCompound compound1 = new NBTTagCompound();
                compound.setTag("BlockEntityTag", tileSoulUrn.saveToNBT(compound1));
                stack.setTagCompound(compound);

                if (tileSoulUrn.hasCustomName()) {
                    stack.setStackDisplayName(tileSoulUrn.getName());
                    tileSoulUrn.setCustomName("");
                }

                if (tileSoulUrn.getColor() != null)
                    this.setClayColor(stack, tileSoulUrn.getColor());
                if (tileSoulUrn.getGem() != null)
                    this.setGem(stack, tileSoulUrn.getGem());

                spawnAsEntity(worldIn, pos, stack);
            }

            worldIn.updateComparatorOutputLevel(pos, state.getBlock());
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        ItemStack stack = super.getItem(worldIn, pos, state);
        TileSoulUrn tileSoulUrn = (TileSoulUrn) worldIn.getTileEntity(pos);
        NBTTagCompound compound = tileSoulUrn.saveToNBT(new NBTTagCompound());

        if (!compound.isEmpty())
            stack.setTagInfo("BlockEntityInfo", compound);

        this.setClayColor(stack, tileSoulUrn.getColor());
        this.setGem(stack, tileSoulUrn.getGem());

        return stack;
    }

    @Override
    public IBlockColor getColorHandler() {
        return (state, worldIn, pos, tintIndex) -> {
            if (tintIndex == 0) {
                // Main body/clay color
                if (worldIn != null && pos != null) {
                    TileEntity tile = worldIn.getTileEntity(pos);
                    if (tile instanceof TileSoulUrn) {
                        EnumDyeColor color = ((TileSoulUrn) tile).getColor();
                        if (color != null) return color.getColorValue();
                    }
                }
                // Fallback to plain hardened clay color
                return 0x985F45;
            } else if (tintIndex == 1) {
                // Decorative gem color
                if (worldIn != null && pos != null) {
                    TileEntity tile = worldIn.getTileEntity(pos);
                    if (tile instanceof TileSoulUrn) {
                        EnumGem gem = ((TileSoulUrn) tile).getGem();
                        if (gem != null) return gem.getColor();
                    }
                }
                // Fall through to white if gem is null
            }
            return 0xFFFFFF;
        };
    }

    @Override
    public IItemColor getItemColorHandler() {
        // TODO
        return (stack, tintIndex) -> {
            if (tintIndex == 0) {
                // Main body/clay color
                EnumDyeColor color = this.getClayColor(stack);
                return color != null ? color.getColorValue() : 0x985F45;
            } else if (tintIndex == 1) {
                // Decorative gem color
                EnumGem gem = this.getGem(stack);
                return gem != null ? gem.getColor() : 0xFFFFFF;
            }
            return 0xFFFFFF;
        };
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return blockState.getValue(PROPERTY_LID) == LidState.OPEN ? BOUNDING_BOX_OPEN : BOUNDING_BOX_CLOSED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(PROPERTY_LID) == LidState.OPEN ? BOUNDING_BOX_OPEN : BOUNDING_BOX_CLOSED;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return true;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory((IInventory) worldIn.getTileEntity(pos));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
                .withProperty(PROPERTY_FACING, placer.getHorizontalFacing())
                .withProperty(PROPERTY_LID, LidState.CLOSED);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        // XLFF
        return this.getDefaultState()
                .withProperty(PROPERTY_FACING, EnumFacing.byHorizontalIndex(meta))
                .withProperty(PROPERTY_LID, ((meta & 4) >> 2 == 0 ? LidState.CLOSED : LidState.OPEN));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        // XLFF
        return state.getValue(PROPERTY_FACING).getHorizontalIndex()
                + (state.getValue(PROPERTY_LID) == LidState.OPEN ? 4 : 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PROPERTY_LID, PROPERTY_FACING);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        // Need custom serializer
        recipes.setRecipeSerializer(Item.getItemFromBlock(this), (result, components) -> {
            JsonObject json = RecipeJsonHell.ShapedSerializer.INSTANCE.serialize(result, components);
            json.remove("type");
            json.addProperty("type", "silentgems:soul_urn");
            return json;
        });

//        addRecipe(recipes, null);
//        for (EnumDyeColor color : EnumDyeColor.values())
//            addRecipe(recipes, color);
    }

    private void addRecipe(RecipeMaker recipes, @Nullable EnumDyeColor color) {
        ItemStack clay = color == null ? new ItemStack(Blocks.HARDENED_CLAY)
                : new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, color.getMetadata());
        recipes.addShaped("soul_urn" + (color != null ? "_" + color.getName() : ""), getStack(color, null),
                "cgc", "csc", "ccc",
                'c', clay, 'g', new ItemStack(ModItems.gem, 1, OreDictionary.WILDCARD_VALUE),
                's', new ItemStack(ModItems.soulGem));
    }

    @Override
    public void registerModels() {
        for (LidState lidState : LidState.values()) {
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                IBlockState state = this.getDefaultState()
                        .withProperty(PROPERTY_LID, lidState)
                        .withProperty(PROPERTY_FACING, facing);
                int meta = this.getMetaFromState(state);
                String variant = String.format("facing=%s,lid=%s", facing.getName(), lidState.getName());
                SilentGems.registry.setModel(this, meta, "soul_urn", variant);
            }
        }
    }

    public static class ItemBlockSoulUrn extends ItemBlock {
        private static List<ItemStack> SAMPLE_SUB_ITEMS;

        private final BlockSoulUrn blockSoulUrn;

        public ItemBlockSoulUrn(BlockSoulUrn block) {
            super(block);
            this.blockSoulUrn = block;
            this.setMaxStackSize(1);
        }

        @Override
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
            if (!this.isInCreativeTab(tab)) return;

            // Since their are over 800 possible combinations, let's just show one of each clay color
            if (SAMPLE_SUB_ITEMS == null) {
                SAMPLE_SUB_ITEMS = new ArrayList<>();
                SAMPLE_SUB_ITEMS.add(this.blockSoulUrn.getStack(null, EnumGem.getRandom()));
                for (EnumDyeColor color : EnumDyeColor.values())
                    SAMPLE_SUB_ITEMS.add(this.blockSoulUrn.getStack(color, EnumGem.getRandom()));
            }

            items.addAll(SAMPLE_SUB_ITEMS);
        }
    }
}
