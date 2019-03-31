/*
 * Silent's Gems -- TileSoulUrn
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
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModTileEntities;
import net.silentchaos512.gems.item.SoulUrnUpgrades;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.urn.LidState;
import net.silentchaos512.gems.lib.urn.UrnConst;
import net.silentchaos512.gems.lib.urn.UrnUpgrade;

import javax.annotation.Nullable;
import java.util.List;

public class TileSoulUrn extends TileEntityLockableLoot implements ITickable, ISidedInventory, IHopper {
    private static final int INVENTORY_ROWS_BASE = 3;
    private static final int INVENTORY_ROWS_UPGRADED = 6;

    private NonNullList<ItemStack> items;
    private NonNullList<UrnUpgrade> upgrades = NonNullList.create();
    private int color;
    private Gems gem = null;
    private boolean destroyedByCreativePlayer;
    private boolean cleared;

    private int ticksExisted;

    public TileSoulUrn() {
        super(ModTileEntities.SOUL_URN.type());
        this.items = NonNullList.withSize(9 * INVENTORY_ROWS_BASE, ItemStack.EMPTY);
    }

    void setColorAndGem(int color, @Nullable Gems gem) {
        this.color = color;
        this.gem = gem;
    }

    void setUpgrades(List<UrnUpgrade> list) {
        this.upgrades.clear();
        this.upgrades.addAll(list);

        int inventoryRows = INVENTORY_ROWS_BASE;
        if (UrnUpgrade.ListHelper.contains(this.upgrades, SoulUrnUpgrades.EXTRA_STORAGE))
            inventoryRows = INVENTORY_ROWS_UPGRADED;

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

    public void setDestroyedByCreativePlayer(boolean destroyedByCreativePlayer) {
        this.destroyedByCreativePlayer = destroyedByCreativePlayer;
    }

    public boolean isCleared() {
        return cleared;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        // TODO
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        Block block = Block.getBlockFromItem(itemStackIn.getItem());
        return !(block instanceof BlockSoulUrn || block instanceof BlockShulkerBox);
    }

    public boolean tryAddItemToInventory(ItemStack stack) {
        if (this.isFull() || !canInsertItem(0, stack, EnumFacing.UP)) return false;

        for (int slot = 0; slot < this.items.size(); ++slot) {
            ItemStack stackInSlot = this.getStackInSlot(slot);
            if (stackInSlot.isEmpty() || stackInSlot.isItemEqual(stack)) {
                if (!stackInSlot.isEmpty()) {
                    int amountCanFit = Math.min(stack.getCount(), stackInSlot.getMaxStackSize() - stackInSlot.getCount());
//                    SilentGems.logHelper.debug("{}, {}, {}", amountCanFit, stack.getCount(), stackInSlot.getCount());
                    stackInSlot.setCount(stackInSlot.getCount() + amountCanFit);
                    stack.setCount(stack.getCount() - amountCanFit);

                    this.setInventorySlotContents(slot, stackInSlot);
                } else {
                    this.setInventorySlotContents(slot, stack.copy());
                    stack.setCount(0);
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
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
    public void tick() {
        if (this.world.isRemote)
            return;

        LidState lid = world.getBlockState(this.pos).get(BlockSoulUrn.LID);
        ++this.ticksExisted;

        // Tick upgrades
        SoulUrnState state = new SoulUrnState(this, lid);
        for (UrnUpgrade upgrade : this.upgrades) {
            upgrade.tickTile(state, this.world, this.pos);
        }

        // Hopper functions
        if (!state.itemsAbsorbed && lid.isOpen() && ticksExisted % 30 == 0) {
            this.updateHopper();
        }
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerSoulUrn(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return SilentGems.RESOURCE_PREFIX + "soul_urn";
    }

    @Override
    public ITextComponent getName() {
        return new TextComponentTranslation("container." + SilentGems.MOD_ID + ".soul_urn");
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return this.hasCustomName() ? this.customName : this.getName();
    }

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        this.loadFromNBT(compound);
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);
        return this.saveToNBT(compound);
    }

    public void loadFromNBT(NBTTagCompound compound) {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound) && compound.contains("Items"))
            ItemStackHelper.loadAllItems(compound, this.items);
        if (compound.contains("CustomName"))
            this.customName = new TextComponentString(compound.getString("CustomName"));

        loadColorFromNBT(compound);
        loadGemFromNBT(compound);

        this.setUpgrades(UrnUpgrade.ListHelper.load(compound));
    }

    public NBTTagCompound saveToNBT(NBTTagCompound compound) {
        if (!this.checkLootAndWrite(compound))
            ItemStackHelper.saveAllItems(compound, this.items, false);
        if (this.hasCustomName())
            compound.putString("CustomName", this.customName.getFormattedText());
        if (!compound.contains("Lock") && this.isLocked())
            this.getLockCode().write(compound);

        if (this.color != UrnConst.UNDYED_COLOR)
            compound.putInt(UrnConst.NBT_COLOR, this.color);
        if (this.gem != null)
            compound.putString(UrnConst.NBT_GEM, this.gem.getName());

        UrnUpgrade.ListHelper.save(this.upgrades, compound);

        return compound;
    }

    private void loadColorFromNBT(NBTTagCompound compound) {
        if (compound.contains(UrnConst.NBT_COLOR)) {
            this.color = compound.getInt(UrnConst.NBT_COLOR);
        }
    }

    private void loadGemFromNBT(NBTTagCompound compound) {
        if (compound.contains(UrnConst.NBT_GEM)) {
            String str = compound.getString(UrnConst.NBT_GEM);
            for (Gems gem : Gems.values()) {
                if (gem.getName().equals(str)) {
                    this.gem = gem;
                    break;
                }
            }
        }
    }

    @Override
    public void clear() {
        this.cleared = true;
        super.clear();
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        // TODO
    }

    public boolean shouldDrop() {
        return !this.destroyedByCreativePlayer || !this.isEmpty() || this.hasCustomName() || this.lootTable != null;
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
//        // Prevents inventory loss when opening/closing lid or rotating
//        return oldState.getBlock() != newState.getBlock();
//    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tags = super.getUpdateTag();

        if (this.color != UrnConst.UNDYED_COLOR)
            tags.putInt(UrnConst.NBT_COLOR, this.color);
        if (this.gem != null)
            tags.putString(UrnConst.NBT_GEM, this.gem.getName());

        UrnUpgrade.ListHelper.save(this.upgrades, tags);

        return tags;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tags = pkt.getNbtCompound();

        this.loadColorFromNBT(tags);
        this.loadGemFromNBT(tags);
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

    private boolean updateHopper() {
        if (this.world != null && !this.world.isRemote) {
            boolean flag = false;

            // TODO: Hopper upgrade?
//            if (!this.isEmpty()) {
//                flag = this.transferItemsOut();
//            }

            if (!this.isFull()) {
                flag = this.pullItems() || flag;
            }

            if (flag) {
                this.markDirty();
                return true;
            }
        }
        return false;
    }

    private boolean pullItems() {
        Boolean ret = net.minecraftforge.items.VanillaInventoryCodeHooks.extractHook(this);
        if (ret != null) return ret;
        IInventory iinventory = TileEntityHopper.getSourceInventory(this);

        if (iinventory != null) {
            EnumFacing enumfacing = EnumFacing.DOWN;

            if (isInventoryEmpty(iinventory, enumfacing)) {
                return false;
            }

            if (iinventory instanceof ISidedInventory) {
                ISidedInventory isidedinventory = (ISidedInventory) iinventory;
                int[] aint = isidedinventory.getSlotsForFace(enumfacing);

                for (int i : aint) {
                    if (pullItemFromSlot(this, iinventory, i, enumfacing)) {
                        return true;
                    }
                }
            } else {
                int j = iinventory.getSizeInventory();

                for (int k = 0; k < j; ++k) {
                    if (pullItemFromSlot(this, iinventory, k, enumfacing)) {
                        return true;
                    }
                }
            }
        } else {
//            for (EntityItem entityitem : TileEntityHopper.getCaptureItems(this.getWorld(), this.getXPos(), this.getYPos(), this.getZPos())) {
//                if (TileEntityHopper.putDropInInventoryAllSlots(null, this, entityitem)) {
//                    return true;
//                }
//            }
        }

        return false;
    }

    private static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            ISidedInventory isidedinventory = (ISidedInventory) inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int i : aint) {
                if (!isidedinventory.getStackInSlot(i).isEmpty()) {
                    return false;
                }
            }
        } else {
            int j = inventoryIn.getSizeInventory();

            for (int k = 0; k < j; ++k) {
                if (!inventoryIn.getStackInSlot(k).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean pullItemFromSlot(IHopper hopper, IInventory inventoryIn, int index, EnumFacing direction) {
        ItemStack itemstack = inventoryIn.getStackInSlot(index);

        if (!itemstack.isEmpty() && canExtractItemFromSlot(inventoryIn, itemstack, index, direction)) {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = TileEntityHopper.putStackInInventoryAllSlots(inventoryIn, hopper, inventoryIn.decrStackSize(index, 1), null);

            if (itemstack2.isEmpty()) {
                inventoryIn.markDirty();
                return true;
            }

            inventoryIn.setInventorySlotContents(index, itemstack1);
        }

        return false;
    }

    private static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
        return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory) inventoryIn).canExtractItem(index, stack, side);
    }

    //endregion

    public static final class SoulUrnState {
        private final TileSoulUrn tileEntity;
        private final LidState lidState;
        private boolean itemsAbsorbed = false;

        private SoulUrnState(TileSoulUrn tile, LidState lidState) {
            this.tileEntity = tile;
            this.lidState = lidState;
        }

        public TileSoulUrn getTileEntity() {
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
}
