/*
 * Silent's Gems -- SoulUrnBlock
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

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.gems.init.GemsSounds;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.urn.LidState;
import net.silentchaos512.gems.lib.urn.UrnConst;
import net.silentchaos512.gems.lib.urn.UrnHelper;
import net.silentchaos512.gems.lib.urn.UrnUpgrade;
import net.silentchaos512.lib.client.key.InputUtils;
import net.silentchaos512.utils.Color;
import net.silentchaos512.utils.Lazy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SoulUrnBlock extends ContainerBlock {
    public static final Lazy<SoulUrnBlock> INSTANCE = Lazy.of(SoulUrnBlock::new);

    private static final VoxelShape SHAPE_CLOSED = Block.makeCuboidShape(1, 0, 1, 15, 15, 15);
    private static final VoxelShape SHAPE_OPEN = Block.makeCuboidShape(1, 0, 1, 15, 14, 15);

    static final EnumProperty<LidState> LID = EnumProperty.create("lid", LidState.class);

    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public SoulUrnBlock() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(5, 40));
        this.setDefaultState(this.getDefaultState()
                .with(FACING, Direction.SOUTH)
                .with(LID, LidState.CLOSED));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, LID);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (context.replacingClickedOnBlock()) {
            BlockState currentState = context.getWorld().getBlockState(context.getPos());
            return toggleLid(currentState);
        }
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new SoulUrnTileEntity();
    }

    public ItemStack getStack(int color, @Nullable Gems gem) {
        ItemStack stack = new ItemStack(this);
        if (color != UrnConst.UNDYED_COLOR) UrnHelper.setClayColor(stack, color);
        if (gem != null) UrnHelper.setGem(stack, gem);
        return stack;
    }

    private static BlockState toggleLid(BlockState state) {
        LidState lid = state.get(LID);
        if (lid == LidState.NO_LID) return state;
        return state.with(LID, lid == LidState.CLOSED ? LidState.OPEN : LidState.CLOSED);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof SoulUrnTileEntity) {
            SoulUrnTileEntity soulUrn = (SoulUrnTileEntity) tile;
            if (!worldIn.isRemote && player.isCreative() && !soulUrn.isEmpty()) {
                ItemStack itemstack = getStack(soulUrn.getColor(), soulUrn.getGem());
                CompoundNBT tags = soulUrn.saveToNBT(new CompoundNBT());
                if (!tags.isEmpty()) {
                    itemstack.setTagInfo(UrnConst.NBT_ROOT, tags);
                }

                if (soulUrn.hasCustomName()) {
                    itemstack.setDisplayName(soulUrn.getCustomName());
                }

                ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), itemstack);
                itementity.setDefaultPickupDelay();
                worldIn.addEntity(itementity);
            } else {
                soulUrn.fillWithLoot(player);
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        TileEntity tile = builder.get(LootParameters.BLOCK_ENTITY);
        if (tile instanceof SoulUrnTileEntity) {
            IInventory soulUrn = (SoulUrnTileEntity) tile;
            builder = builder.withDynamicDrop(ShulkerBoxBlock.field_220169_b, (context, consumer) -> {
                for (int i = 0; i < soulUrn.getSizeInventory(); ++i) {
                    consumer.accept(soulUrn.getStackInSlot(i));
                }
            });
        }

        return super.getDrops(state, builder);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            LidState lid = state.get(LID);

            if (lid != LidState.NO_LID && (player.isSneaking() || !lid.isOpen())) {
                // Toggle lid state when sneaking or if closed
                worldIn.setBlockState(pos, toggleLid(state), 2);
                GemsSounds.SOUL_URN_LID.play(worldIn, pos);
            } else {
                // Open inventory if lid is open (or there is no lid)
                TileEntity tile = worldIn.getTileEntity(pos);
                if (tile instanceof SoulUrnTileEntity) {
                    player.openContainer((INamedContainerProvider) tile);
                    GemsSounds.SOUL_URN_OPEN.play(worldIn, pos);
                }
            }
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction side = placer != null ? placer.getHorizontalFacing().getOpposite() : Direction.SOUTH;
        LidState lid = LidState.fromItem(stack);
        BlockState newState = state.with(FACING, side).with(LID, lid);

        worldIn.setBlockState(pos, newState, 2);

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof SoulUrnTileEntity) {
            SoulUrnTileEntity tileSoulUrn = (SoulUrnTileEntity) tile;
            if (stack.hasDisplayName()) {
                tileSoulUrn.setCustomName(stack.getDisplayName());
            }
            tileSoulUrn.loadFromNBT(stack.getOrCreateChildTag(UrnConst.NBT_ROOT));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof SoulUrnTileEntity) {
                worldIn.updateComparatorOutputLevel(pos, state.getBlock());
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        ItemStack stack = super.getPickBlock(state, target, world, pos, player);

        SoulUrnTileEntity tileSoulUrn = (SoulUrnTileEntity) world.getTileEntity(pos);
        if (tileSoulUrn != null) {
            CompoundNBT tag = tileSoulUrn.saveToNBT(new CompoundNBT());
            if (!tag.isEmpty()) {
                stack.setTagInfo("BlockEntityInfo", tag);
            }
        }

        return stack;
    }

    @SuppressWarnings("deprecation")
    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory((IInventory) worldIn.getTileEntity(pos));
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(LID).isOpen() ? SHAPE_OPEN : SHAPE_CLOSED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public static int getBlockColor(BlockState state, @Nullable IEnviromentBlockReader world, @Nullable BlockPos pos, int tintIndex) {
        if (tintIndex == 0) {
            // Main body/clay color
            if (world != null && pos != null) {
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof SoulUrnTileEntity) {
                    return ((SoulUrnTileEntity) tile).getColor();
                }
            }
            // Fallback to plain hardened clay color
            return UrnConst.UNDYED_COLOR;
        } else if (tintIndex == 1) {
            // Decorative gem color
            if (world != null && pos != null) {
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof SoulUrnTileEntity) {
                    Gems gem = ((SoulUrnTileEntity) tile).getGem();
                    if (gem != null) return gem.getColor();
                }
            }
            // Fall through to white if gem is null
        }
        return 0xFFFFFF;
    }

    public static int getItemColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 0) {
            // Main body/clay color
            return UrnHelper.getClayColor(stack);
        } else if (tintIndex == 1) {
            // Decorative gem color
            Gems gem = UrnHelper.getGem(stack);
            return gem != null ? gem.getColor() : 0xFFFFFF;
        }
        return 0xFFFFFF;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        int color = UrnHelper.getClayColor(stack);
        Gems gem = UrnHelper.getGem(stack);

        tooltip.add(color != UrnConst.UNDYED_COLOR
                ? translate("color", Color.format(color))
                : translate("color", translate("color.uncolored")));

        if (gem != null) {
            tooltip.add(translate("gem", gem.getItemStack().getDisplayName()));
        }

        if (InputUtils.isControlDown()) {
            tooltip.add(translate("upgrades").applyTextStyle(TextFormatting.YELLOW));
            List<UrnUpgrade> upgrades = UrnUpgrade.ListHelper.load(stack);
            for (UrnUpgrade upgrade : upgrades) {
                tooltip.add(translate("upgrade_list", upgrade.getDisplayName()));
            }
        } else {
            ITextComponent pressCtrl = new TranslationTextComponent("misc.silentgems.pressCtrl").applyTextStyle(TextFormatting.DARK_GRAY);
            tooltip.add(translate("upgrades", pressCtrl).applyTextStyle(TextFormatting.YELLOW));

            addInventoryInformation(stack, worldIn, tooltip, flagIn);
        }
    }

    private static void addInventoryInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        // Just copied from ShulkerBoxBlock
        CompoundNBT tags = stack.getChildTag(UrnConst.NBT_ROOT);
        if (tags != null) {
            if (tags.contains("LootTable", 8)) {
                tooltip.add(new StringTextComponent("???????"));
            }

            if (tags.contains("Items", 9)) {
                NonNullList<ItemStack> list = NonNullList.withSize(27, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(tags, list);
                int i = 0;
                int j = 0;

                for (ItemStack itemstack : list) {
                    if (!itemstack.isEmpty()) {
                        ++j;
                        if (i <= 4) {
                            ++i;
                            ITextComponent text = itemstack.getDisplayName().deepCopy();
                            text.appendText(" x").appendText(String.valueOf(itemstack.getCount()));
                            tooltip.add(text);
                        }
                    }
                }

                if (j - i > 0) {
                    tooltip.add((new TranslationTextComponent("container.shulkerBox.more", j - i)).applyTextStyle(TextFormatting.ITALIC));
                }
            }
        }
    }

    private static ITextComponent translate(String key, Object... args) {
        return new TranslationTextComponent("block.silentgems.soul_urn." + key, args);
    }

    public static class SoulUrnBlockItem extends BlockItem {
        private static List<ItemStack> SAMPLE_SUB_ITEMS;

        private final SoulUrnBlock blockSoulUrn;

        public SoulUrnBlockItem(SoulUrnBlock block) {
            super(block, new Item.Properties()
                    .maxStackSize(1)
                    .group(GemsItemGroups.BLOCKS));

            this.blockSoulUrn = block;
        }

        @Override
        public ActionResultType onItemUse(ItemUseContext context) {
            BlockState state = context.getWorld().getBlockState(context.getPos());
            // Cauldrons can remove dye color
            if (state.getBlock() == Blocks.CAULDRON) {
                int waterLevel = state.get(CauldronBlock.LEVEL);
                if (waterLevel > 0) {
                    UrnHelper.setClayColor(context.getItem(), UrnConst.UNDYED_COLOR);
                    ((CauldronBlock) Blocks.CAULDRON).setWaterLevel(context.getWorld(), context.getPos(), state, waterLevel - 1);
                    return ActionResultType.SUCCESS;
                }

                return ActionResultType.PASS;
            }

            return super.onItemUse(context);
        }

        @Override
        public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
            super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
            // TODO: Tick upgrades
        }

        @Override
        public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
            if (!this.isInGroup(group)) return;

            // Just show one of each clay color
            if (SAMPLE_SUB_ITEMS == null) {
                SAMPLE_SUB_ITEMS = new ArrayList<>();

                SAMPLE_SUB_ITEMS.add(this.blockSoulUrn.getStack(UrnConst.UNDYED_COLOR, Gems.selectRandom()));
                for (DyeColor color : DyeColor.values()) {
                    SAMPLE_SUB_ITEMS.add(this.blockSoulUrn.getStack(color.func_196057_c(), Gems.selectRandom()));
                }
            }

            items.addAll(SAMPLE_SUB_ITEMS);
        }

//        @Override
//        public String getTranslationKey(ItemStack stack) {
//            return super.getTranslationKey(stack)
//                    + (stack.getItemDamage() >> 2 == LidState.NO_LID.ordinal() ? "_no_lid" : "");
//        }
    }
}
