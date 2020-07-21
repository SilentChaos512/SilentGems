/*
 * Silent's Gems -- SoulUrnTileEntity
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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.silentchaos512.gems.init.GemsTileEntities;
import net.silentchaos512.gems.item.SoulUrnUpgrades;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.urn.LidState;
import net.silentchaos512.gems.lib.urn.UrnConst;
import net.silentchaos512.gems.lib.urn.UrnUpgrade;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class SoulUrnTileEntity extends LockableLootTileEntity implements ITickableTileEntity, ISidedInventory, IHopper {
    private static final int INVENTORY_ROWS_BASE = 6;
    private static final int INVENTORY_ROWS_UPGRADED = 6; // TODO
    private static final int[] SLOTS_BASE = IntStream.range(0, 9 * INVENTORY_ROWS_BASE).toArray();
    private static final int[] SLOTS_UPGRADED = IntStream.range(0, 9 * INVENTORY_ROWS_UPGRADED).toArray();

    private final LazyOptional<? extends IItemHandler>[] handlers =
            SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    private NonNullList<ItemStack> items;
    private final NonNullList<UrnUpgrade> upgrades = NonNullList.create();
    private int color;
    private Gems gem = null;
    private boolean sizeUpgrade;
    private boolean lidded = true;

    private int transferCooldown = -1;

    public SoulUrnTileEntity() {
        super(GemsTileEntities.SOUL_URN.get());
        this.items = NonNullList.withSize(9 * INVENTORY_ROWS_BASE, ItemStack.EMPTY);
    }

    private void setUpgrades(Collection<UrnUpgrade> list) {
        this.upgrades.clear();
        this.upgrades.addAll(list);

        this.sizeUpgrade = UrnUpgrade.ListHelper.contains(this.upgrades, SoulUrnUpgrades.EXTRA_STORAGE);
        final int inventoryRows = sizeUpgrade ? INVENTORY_ROWS_UPGRADED : INVENTORY_ROWS_BASE;

        NonNullList<ItemStack> newList = NonNullList.withSize(9 * inventoryRows, ItemStack.EMPTY);
        for (int i = 0; i < newList.size() && i < this.items.size(); ++i)
            newList.set(i, this.items.get(i));
        this.items = newList;
    }

    public int getColor() {
        return color;
    }

    @Nullable
    public Gems getGem() {
        return gem;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return sizeUpgrade ? SLOTS_UPGRADED : SLOTS_BASE;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        Block block = Block.getBlockFromItem(itemStackIn.getItem());
        return !(block instanceof SoulUrnBlock || block instanceof ShulkerBoxBlock);
    }

    public boolean tryAddItemToInventory(ItemStack stack) {
        if (this.isFull() || !canInsertItem(0, stack, Direction.UP)) return false;

        for (int slot = 0; slot < this.items.size(); ++slot) {
            ItemStack stackInSlot = this.getStackInSlot(slot);
            if (stackInSlot.isEmpty() || stackInSlot.isItemEqual(stack)) {
                if (!stackInSlot.isEmpty()) {
                    int amountCanFit = Math.min(stack.getCount(), stackInSlot.getMaxStackSize() - stackInSlot.getCount());
                    if (amountCanFit > 0) {
                        stackInSlot.setCount(stackInSlot.getCount() + amountCanFit);
                        stack.setCount(stack.getCount() - amountCanFit);

                        this.setInventorySlotContents(slot, stackInSlot);
                        return true;
                    }
                } else {
                    this.setInventorySlotContents(slot, stack.copy());
                    stack.setCount(0);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items)
            if (!stack.isEmpty())
                return false;
        return true;
    }

    private boolean isFull() {
        for (ItemStack stack : this.items)
            if (!stack.isEmpty() || stack.getCount() < stack.getMaxStackSize())
                return false;
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.silentgems.soul_urn");
    }

    @Override
    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return new SoulUrnContainer(p_213906_1_, p_213906_2_, this);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.items = itemsIn;
    }

    private boolean isOnTransferCooldown() {
        return transferCooldown > 0;
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        LidState lid = world.getBlockState(this.pos).get(SoulUrnBlock.LID);

        // Tick upgrades
        SoulUrnState state = new SoulUrnState(this, lid);
        for (UrnUpgrade upgrade : this.upgrades) {
            upgrade.tickTile(state, this.world, this.pos);
        }

        // Hopper functions
        if (transferCooldown > 0) {
            --transferCooldown;
        }
        if (lid.isOpen() && !isOnTransferCooldown()) {
            transferCooldown = 0;
            updateHopper(() -> HopperTileEntity.pullItems(this));
        }
    }

    //region Hopper-type stuff

    @Override
    public double getXPos() {
        return this.pos.getX() + 0.5;
    }

    @Override
    public double getYPos() {
        return this.pos.getY() + 0.5;
    }

    @Override
    public double getZPos() {
        return this.pos.getZ() + 0.5;
    }

    private boolean updateHopper(Supplier<Boolean> booleanSupplier) {
        if (world != null && !world.isRemote) {
            if (!this.isOnTransferCooldown()) {
                boolean flag = false;
                // TODO: Hopper transfer upgrade, to push items out?
//                if (!this.isEmpty()) {
//                    flag = this.transferItemsOut();
//                }

                if (!this.isFull()) {
                    flag |= booleanSupplier.get();
                }

                if (flag) {
                    transferCooldown = 8;
                    markDirty();
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    //endregion

    //region NBT and data

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        this.loadFromNBT(compound);
        super.read(state, compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        return this.saveToNBT(compound);
    }

    void loadFromNBT(CompoundNBT compound) {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        this.setUpgrades(UrnUpgrade.ListHelper.load(compound));

        if (!this.checkLootAndRead(compound) && compound.contains("Items")) {
            ItemStackHelper.loadAllItems(compound, this.items);
        }

        loadColorFromNBT(compound);
        loadGemFromNBT(compound);
        this.lidded = !compound.contains(UrnConst.NBT_LIDDED) || compound.getBoolean(UrnConst.NBT_LIDDED);
    }

    CompoundNBT saveToNBT(CompoundNBT compound) {
        if (!this.checkLootAndWrite(compound))
            ItemStackHelper.saveAllItems(compound, this.items, false);

        if (this.color != UrnConst.UNDYED_COLOR)
            compound.putInt(UrnConst.NBT_COLOR, this.color);
        if (this.gem != null)
            compound.putString(UrnConst.NBT_GEM, this.gem.getName());
        compound.putBoolean(UrnConst.NBT_LIDDED, this.lidded);

        UrnUpgrade.ListHelper.save(this.upgrades, compound);

        return compound;
    }

    private void loadColorFromNBT(CompoundNBT compound) {
        if (compound.contains(UrnConst.NBT_COLOR)) {
            this.color = compound.getInt(UrnConst.NBT_COLOR);
        }
    }

    private void loadGemFromNBT(CompoundNBT compound) {
        if (compound.contains(UrnConst.NBT_GEM)) {
            this.gem = Gems.fromName(compound.getString(UrnConst.NBT_GEM));
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();

        if (this.color != UrnConst.UNDYED_COLOR)
            tags.putInt(UrnConst.NBT_COLOR, this.color);
        if (this.gem != null)
            tags.putString(UrnConst.NBT_GEM, this.gem.getName());

        UrnUpgrade.ListHelper.save(this.upgrades, tags);

        return tags;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        CompoundNBT tags = pkt.getNbtCompound();

        this.loadColorFromNBT(tags);
        this.loadGemFromNBT(tags);
    }

    //endregion

    public static final class SoulUrnState {
        private final SoulUrnTileEntity tileEntity;
        private final LidState lidState;
        private boolean itemsAbsorbed = false;

        private SoulUrnState(SoulUrnTileEntity tile, LidState lidState) {
            this.tileEntity = tile;
            this.lidState = lidState;
        }

        public SoulUrnTileEntity getTileEntity() {
            return tileEntity;
        }

        public LidState getLidState() {
            return lidState;
        }

        public boolean getItemsAbsorbed() {
            return itemsAbsorbed;
        }

        public void setItemsAbsorbed(boolean value) {
            itemsAbsorbed = value;
        }
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.removed && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.UP)
                return handlers[0].cast();
            if (side == Direction.DOWN)
                return handlers[1].cast();
            return handlers[2].cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
    }
}
