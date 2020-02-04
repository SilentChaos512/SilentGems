package net.silentchaos512.gems.block.pedestal;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.gems.api.IPedestalItem;
import net.silentchaos512.gems.capability.PedestalItemCapability;
import net.silentchaos512.gems.init.GemsTileEntities;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;

import javax.annotation.Nullable;

public class PedestalTileEntity extends LockableSidedInventoryTileEntity implements ITickableTileEntity {
    private int timer;
    private boolean poweredLastTick;

    public PedestalTileEntity() {
        super(GemsTileEntities.PEDESTAL, 1);
    }

    @Override
    public void tick() {
        if (world == null) return;

        ItemStack stack = getItem();
        boolean sendUpdate = false;

        // Tick items with pedestal support
        if (!stack.isEmpty()) {
            IPedestalItem pedestalItem = getPedestalItemCap(stack);

            if (pedestalItem != null) {
                final boolean powered = this.world.isBlockPowered(this.pos);
                if (powered != this.poweredLastTick) {
                    // Redstone power change
                    sendUpdate = pedestalItem.pedestalPowerChange(stack, this.world, this.pos, powered);
                    this.poweredLastTick = powered;
                }

                pedestalItem.pedestalTick(stack, this.world, this.pos);
            }
        }

        ++timer;
        if ((timer + this.hashCode()) % 600 == 0) {
            sendUpdate = true;
        }

        if (sendUpdate) {
            sendUpdate();
        }
    }

    @Nullable
    private static IPedestalItem getPedestalItemCap(ItemStack stack) {
        LazyOptional<IPedestalItem> lazyOptional = stack.getCapability(PedestalItemCapability.INSTANCE);
        if (lazyOptional.isPresent())
            return lazyOptional.orElseThrow(IllegalThreadStateException::new);
        if (stack.getItem() instanceof IPedestalItem)
            return (IPedestalItem) stack.getItem();
        return null;
    }

    public ItemStack getItem() {
        return getStackInSlot(0);
    }

    public void setItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setCount(1);
        setInventorySlotContents(0, copy);
    }

    private void sendUpdate() {
        if (world != null) {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
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
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = getUpdateTag();
        return new SUpdateTileEntityPacket(pos, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        CompoundNBT tag = packet.getNbtCompound();
        if (tag.contains("PItem")) {
            setItem(ItemStack.read(tag.getCompound("PItem")));
        } else {
            setItem(ItemStack.EMPTY);
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        ItemStack stack = getItem();
        if (!stack.isEmpty()) {
            tag.put("PItem", stack.write(new CompoundNBT()));
        }
        return tag;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("");
    }

    @Override
    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return null;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, @Nullable Direction direction) {
        return isEmpty();
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return !isEmpty();
    }
}
