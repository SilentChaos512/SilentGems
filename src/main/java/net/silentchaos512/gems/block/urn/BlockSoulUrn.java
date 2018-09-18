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

import net.minecraft.block.BlockCauldron;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.gui.GuiTypes;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.init.ModSounds;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.urn.UrnConst;
import net.silentchaos512.gems.lib.urn.UrnHelper;
import net.silentchaos512.gems.lib.urn.UrnUpgrade;
import net.silentchaos512.lib.block.IColoredBlock;
import net.silentchaos512.lib.block.ITileEntityBlock;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.util.DyeHelper;
import net.silentchaos512.lib.util.MathUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BlockSoulUrn extends BlockContainer implements ITileEntityBlock, IColoredBlock, ICustomModel, IAddRecipes {
    public enum LidState implements IStringSerializable {
        CLOSED, OPEN, NO_LID;

        public boolean isOpen() {
            return this == OPEN || this == NO_LID;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
    private static final AxisAlignedBB BOUNDING_BOX_CLOSED = MathUtils.boundingBoxByPixels(1, 0, 1, 15, 15, 15);

    private static final AxisAlignedBB BOUNDING_BOX_OPEN = MathUtils.boundingBoxByPixels(1, 0, 1, 15, 14, 15);
    static final PropertyEnum<LidState> PROPERTY_LID = PropertyEnum.create("lid", LidState.class);

    private static final PropertyEnum<EnumFacing> PROPERTY_FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockSoulUrn() {
        super(Material.ROCK);
        this.setHardness(5f);
        this.setResistance(20f);
        this.setHarvestLevel("pickaxe", 1);
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

    public ItemStack getStack(int color, @Nullable EnumGem gem) {
        ItemStack stack = new ItemStack(this);
        if (color != UrnConst.UNDYED_COLOR) UrnHelper.setClayColor(stack, color);
        if (gem != null) UrnHelper.setGem(stack, gem);
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        int color = UrnHelper.getClayColor(stack);
        EnumGem gem = UrnHelper.getGem(stack);

        if (color != UrnConst.UNDYED_COLOR) {
            tooltip.add(SilentGems.i18n.subText(this, "color", String.format("#%06X", color)));
        } else {
            String str = SilentGems.i18n.subText(this, "color.uncolored");
            tooltip.add(SilentGems.i18n.subText(this, "color", str));
        }

        if (gem != null) {
            String str = SilentGems.i18n.translatedName(gem.getItem());
            tooltip.add(SilentGems.i18n.subText(this, "gem", str));
        }

        if (KeyTracker.isControlDown()) {
            tooltip.add(TextFormatting.YELLOW + SilentGems.i18n.subText(this, "upgrades", ""));
            List<UrnUpgrade> upgrades = UrnUpgrade.ListHelper.load(stack);
            for (UrnUpgrade upgrade : upgrades) {
                String upgradeName = SilentGems.i18n.translate(upgrade.getTranslationKey());
                tooltip.add(SilentGems.i18n.subText(this, "upgrade_list", upgradeName));
            }
        } else {
            String pressCtrl = TextFormatting.DARK_GRAY + SilentGems.i18n.miscText("pressCtrl");
            tooltip.add(TextFormatting.YELLOW + SilentGems.i18n.subText(this, "upgrades", pressCtrl));
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
        if (!worldIn.isRemote) {
            LidState lid = state.getValue(PROPERTY_LID);

            if (lid != LidState.NO_LID && (playerIn.isSneaking() || !lid.isOpen())) {
                // Toggle lid state when sneaking or if closed
                worldIn.setBlockState(pos, toggleLid(state), 2);
                worldIn.playSound(null, pos, ModSounds.SOUL_URN_LID, SoundCategory.BLOCKS, 0.6f,
                        (float) (1.1f + 0.05f * SilentGems.random.nextGaussian()));
            } else {
                // Open inventory if lid is open (or there is no lid)
                TileEntity tile = worldIn.getTileEntity(pos);
                if (tile instanceof TileSoulUrn) {
                    GuiTypes.SOUL_URN.open(playerIn, worldIn, pos);
                    worldIn.playSound(null, pos, ModSounds.SOUL_URN_OPEN, SoundCategory.BLOCKS, 0.6f,
                            (float) (1.1f + 0.05f * SilentGems.random.nextGaussian()));
                }
            }
        }

        return true;
    }

    private static IBlockState toggleLid(IBlockState state) {
        LidState lid = state.getValue(PROPERTY_LID);
        if (lid == LidState.NO_LID) return state;
        return state.withProperty(PROPERTY_LID, lid == LidState.CLOSED ? LidState.OPEN : LidState.CLOSED);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        EnumFacing side = placer.getHorizontalFacing().getOpposite();
        IBlockState newState = state.withProperty(PROPERTY_FACING, side)
                .withProperty(PROPERTY_LID, UrnHelper.isLidless(stack) ? LidState.NO_LID : LidState.CLOSED);

        worldIn.setBlockState(pos, newState, 2);

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileSoulUrn) {
            TileSoulUrn tileSoulUrn = (TileSoulUrn) tile;
            if (stack.hasDisplayName()) {
                tileSoulUrn.setCustomName(stack.getDisplayName());
            }

            tileSoulUrn.setColorAndGem(UrnHelper.getClayColor(stack), UrnHelper.getGem(stack));

            NBTTagCompound tagCompound = stack.getOrCreateSubCompound(UrnConst.NBT_ROOT);
            tileSoulUrn.setUpgrades(UrnUpgrade.ListHelper.load(tagCompound));
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileSoulUrn) {
            TileSoulUrn tileSoulUrn = (TileSoulUrn) tile;

            if (!tileSoulUrn.isCleared() && tileSoulUrn.shouldDrop()) {
                ItemStack stack = new ItemStack(this);
                if (state.getValue(PROPERTY_LID) == LidState.NO_LID) {
                    UrnHelper.setLidless(stack, true);
                }

                NBTTagCompound compound = new NBTTagCompound();
                NBTTagCompound compound1 = new NBTTagCompound();
                compound.setTag(UrnConst.NBT_ROOT, tileSoulUrn.saveToNBT(compound1));
                stack.setTagCompound(compound);

                if (tileSoulUrn.hasCustomName()) {
                    stack.setStackDisplayName(tileSoulUrn.getName());
                    tileSoulUrn.setCustomName("");
                }

                if (tileSoulUrn.getColor() != UrnConst.UNDYED_COLOR)
                    UrnHelper.setClayColor(stack, tileSoulUrn.getColor());
                if (tileSoulUrn.getGem() != null)
                    UrnHelper.setGem(stack, tileSoulUrn.getGem());

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
        if (tileSoulUrn != null) {
            NBTTagCompound compound = tileSoulUrn.saveToNBT(new NBTTagCompound());

            if (!compound.isEmpty())
                stack.setTagInfo("BlockEntityInfo", compound);

            UrnHelper.setClayColor(stack, tileSoulUrn.getColor());
            if (tileSoulUrn.getGem() != null)
                UrnHelper.setGem(stack, tileSoulUrn.getGem());
        }

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
                        return ((TileSoulUrn) tile).getColor();
                    }
                }
                // Fallback to plain hardened clay color
                return UrnConst.UNDYED_COLOR;
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
        return (stack, tintIndex) -> {
            if (tintIndex == 0) {
                // Main body/clay color
                return UrnHelper.getClayColor(stack);
            } else if (tintIndex == 1) {
                // Decorative gem color
                EnumGem gem = UrnHelper.getGem(stack);
                return gem != null ? gem.getColor() : 0xFFFFFF;
            }
            return 0xFFFFFF;
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return blockState.getValue(PROPERTY_LID).isOpen() ? BOUNDING_BOX_OPEN : BOUNDING_BOX_CLOSED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(PROPERTY_LID).isOpen() ? BOUNDING_BOX_OPEN : BOUNDING_BOX_CLOSED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isTranslucent(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory((IInventory) worldIn.getTileEntity(pos));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
                .withProperty(PROPERTY_FACING, placer.getHorizontalFacing())
                .withProperty(PROPERTY_LID, meta == 0 ? LidState.CLOSED : LidState.NO_LID);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        // LLFF
        return this.getDefaultState()
                .withProperty(PROPERTY_FACING, EnumFacing.byHorizontalIndex(meta))
                .withProperty(PROPERTY_LID, lidStateFromMeta(meta));
    }

    private static LidState lidStateFromMeta(int meta) {
        meta = meta >> 2;
        if (meta < 0 || meta >= LidState.values().length) return LidState.NO_LID;
        return LidState.values()[meta];
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        // LLFF
        return state.getValue(PROPERTY_FACING).getHorizontalIndex()
                + (state.getValue(PROPERTY_LID).ordinal() << 2);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PROPERTY_LID, PROPERTY_FACING);
    }

//    @Override
//    public void addRecipes(RecipeMaker recipes) {
//        // Need custom serializer
//        recipes.setRecipeSerializer(Item.getItemFromBlock(this), (result, components) -> {
//            JsonObject json = RecipeJsonHell.ShapedSerializer.INSTANCE.serialize(result, components);
//            json.remove("type");
//            json.addProperty("type", "silentgems:soul_urn");
//            return json;
//        });
//
//        addRecipe(recipes, null);
//        for (EnumDyeColor color : EnumDyeColor.values())
//            addRecipe(recipes, color);
//    }

//    private void addRecipe(RecipeMaker recipes, @Nullable EnumDyeColor color) {
//        ItemStack clay = color == null ? new ItemStack(Blocks.HARDENED_CLAY)
//                : new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, color.getMetadata());
//
//        recipes.addShaped("soul_urn" + (color != null ? "_" + color.getName() : ""),
//                getStack(color == null ? UrnConst.UNDYED_COLOR : color.getColorValue(), null),
//                "cgc", "csc", "ccc",
//                'c', clay,
//                'g', new ItemStack(ModItems.gem, 1, OreDictionary.WILDCARD_VALUE),
//                's', new ItemStack(ModItems.soulGem));
//    }

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
            this.setMaxDamage(0);
        }

        @Override
        public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            IBlockState state = worldIn.getBlockState(pos);
            // Cauldrons can remove dye color
            if (state.getBlock() == Blocks.CAULDRON) {
                int waterLevel = state.getValue(BlockCauldron.LEVEL);
                if (waterLevel > 0) {
                    UrnHelper.setClayColor(player.getHeldItem(hand), UrnConst.UNDYED_COLOR);
                    Blocks.CAULDRON.setWaterLevel(worldIn, pos, state, waterLevel - 1);
                    return EnumActionResult.SUCCESS;
                }

                return EnumActionResult.PASS;
            }

            return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }

        @Override
        public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
            super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
            // TODO: Tick upgrades
        }

        @Override
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
            if (!this.isInCreativeTab(tab)) return;

            // Since their are over 800 possible combinations, let's just show one of each clay color
            if (SAMPLE_SUB_ITEMS == null) {
                SAMPLE_SUB_ITEMS = new ArrayList<>();

                SAMPLE_SUB_ITEMS.add(this.blockSoulUrn.getStack(UrnConst.UNDYED_COLOR, EnumGem.getRandom()));
                for (EnumDyeColor color : EnumDyeColor.values())
                    SAMPLE_SUB_ITEMS.add(this.blockSoulUrn.getStack(DyeHelper.getColor(color), EnumGem.getRandom()));
            }

            items.addAll(SAMPLE_SUB_ITEMS);
        }

        @Override
        public String getTranslationKey(ItemStack stack) {
            return super.getTranslationKey(stack)
                    + (stack.getItemDamage() >> 2 == LidState.NO_LID.ordinal() ? "_no_lid" : "");
        }
    }
}
