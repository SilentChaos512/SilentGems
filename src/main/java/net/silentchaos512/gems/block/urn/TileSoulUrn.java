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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.lib.EnumGem;

import javax.annotation.Nullable;

@Getter
public class TileSoulUrn extends TileEntityLockableLoot implements ITickable, ISidedInventory, IHopper {
    private NonNullList<ItemStack> items;
    @Setter
    @Nullable
    private EnumDyeColor color;
    @Setter
    @Nullable
    private EnumGem gem;
    @Setter
    private boolean destroyedByCreativePlayer;
    private boolean cleared;
    @Getter(value = AccessLevel.NONE)
    private int ticksExisted;

    public TileSoulUrn() {
        this(null, null);
    }

    public TileSoulUrn(@Nullable EnumDyeColor color, @Nullable EnumGem gem) {
        this.items = NonNullList.withSize(9, ItemStack.EMPTY);
        this.color = color;
        this.gem = gem;
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
    public void update() {
        if (this.world.isRemote)
            return;

        // TODO: TileSoulUrn#update
        ++this.ticksExisted;
        if (ticksExisted % 30 == 0) {
            BlockSoulUrn.LidState lidState = ModBlocks.soulUrn.getStateFromMeta(this.getBlockMetadata()).getValue(BlockSoulUrn.PROPERTY_LID);
            if (lidState == BlockSoulUrn.LidState.OPEN) {
                this.updateHopper();
            }
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
    public String getName() {
        return this.hasCustomName() ? this.customName : "container." + SilentGems.MODID + ".soul_urn";
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.loadFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        return this.saveToNBT(compound);
    }

    public void loadFromNBT(NBTTagCompound compound) {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound) && compound.hasKey("Items", 9))
            ItemStackHelper.loadAllItems(compound, this.items);
        if (compound.hasKey("CustomName", 8))
            this.customName = compound.getString("CustomName");

        loadColorFromNBT(compound);

        loadGemFromNBT(compound);
    }

    public NBTTagCompound saveToNBT(NBTTagCompound compound) {
        if (!this.checkLootAndWrite(compound))
            ItemStackHelper.saveAllItems(compound, this.items, false);
        if (this.hasCustomName())
            compound.setString("CustomName", this.customName);
        if (!compound.hasKey("Lock") && this.isLocked())
            this.getLockCode().toNBT(compound);

        if (this.color != null)
            compound.setString("UrnColor", this.color.getName());
        if (this.gem != null)
            compound.setString("UrnGem", this.gem.getName());

        return compound;
    }

    private void loadColorFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("UrnColor")) {
            String str = compound.getString("UrnColor");
            for (EnumDyeColor color : EnumDyeColor.values()) {
                if (color.getName().equals(str)) {
                    this.color = color;
                    break;
                }
            }
        }
    }

    private void loadGemFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("UrnGem")) {
            String str = compound.getString("UrnGem");
            for (EnumGem gem : EnumGem.values()) {
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

    public boolean shouldDrop() {
        return !this.isDestroyedByCreativePlayer() || !this.isEmpty() || this.hasCustomName() || this.lootTable != null;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        // Prevents inventory loss when opening/closing lid or rotating
        return oldState.getBlock() != newState.getBlock();
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tags = super.getUpdateTag();

        if (this.color != null)
            tags.setString("UrnColor", this.color.getName());
        if (this.gem != null)
            tags.setString("UrnGem", this.gem.getName());

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
        if (this.world != null & !this.world.isRemote) {
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
            for (EntityItem entityitem : TileEntityHopper.getCaptureItems(this.getWorld(), this.getXPos(), this.getYPos(), this.getZPos())) {
                if (TileEntityHopper.putDropInInventoryAllSlots(null, this, entityitem)) {
                    return true;
                }
            }
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
}
