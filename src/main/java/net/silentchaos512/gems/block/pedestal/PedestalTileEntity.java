package net.silentchaos512.gems.block.pedestal;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.silentchaos512.gems.init.ModTileEntities;
import net.silentchaos512.lib.tile.TileSidedInventorySL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PedestalTileEntity extends TileSidedInventorySL implements ITickable {
    private int timer;

    public PedestalTileEntity() {
        super(ModTileEntities.PEDESTAL.type());
    }

    @Override
    public void tick() {
        boolean sendUpdate = false;

        // TODO: chaos gem support

        ++timer;
        if ((timer + this.hashCode()) % 600 == 0) {
            sendUpdate = true;
        }

        if (sendUpdate) {
            sendUpdate();
        }
    }

    public ItemStack getItem() {
        return getStackInSlot(0);
    }

    public void setItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setCount(1);
        setInventorySlotContents(0, copy);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        super.setInventorySlotContents(index, stack);
        sendUpdate();
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getStackInSlot(0).isEmpty();
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = getUpdateTag();
        return new SPacketUpdateTileEntity(pos, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        NBTTagCompound tag = packet.getNbtCompound();
        if (tag.contains("PItem")) {
            setItem(ItemStack.read(tag.getCompound("PItem")));
        } else {
            setItem(ItemStack.EMPTY);
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = super.getUpdateTag();
        ItemStack stack = getItem();
        if (!stack.isEmpty()) {
            tag.put("PItem", stack.write(new NBTTagCompound()));
        }
        return tag;
    }

    @Override
    public ITextComponent getName() {
        return new TextComponentTranslation("container.silentgems.pedestal");
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return null;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, @Nullable EnumFacing direction) {
        return isEmpty();
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return !isEmpty();
    }

    private final LazyOptional<? extends IItemHandler>[] handlers =
            SidedInvWrapper.create(this, EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        if (!this.removed && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == EnumFacing.UP)
                return handlers[0].cast();
            if (side == EnumFacing.DOWN)
                return handlers[1].cast();
            return handlers[2].cast();
        }
        return super.getCapability(cap, side);
    }
}
