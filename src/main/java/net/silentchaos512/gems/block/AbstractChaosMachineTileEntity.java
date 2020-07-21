package net.silentchaos512.gems.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraftforge.items.ItemHandlerHelper;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;

import javax.annotation.Nullable;

public abstract class AbstractChaosMachineTileEntity<R extends IRecipe<?>> extends LockableSidedInventoryTileEntity implements ITickableTileEntity {
    protected int progress;
    protected int processTime;
    protected int chaosGenerated;
    protected int chaosBuffer;

    protected final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return AbstractChaosMachineTileEntity.this.progress;
                case 1:
                    return AbstractChaosMachineTileEntity.this.processTime;
                case 2:
                    // Chaos generated lower bytes
                    return AbstractChaosMachineTileEntity.this.chaosGenerated & 0xFFFF;
                case 3:
                    // Chaos generated upper bytes
                    return (AbstractChaosMachineTileEntity.this.chaosGenerated >> 16) & 0xFFFF;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    progress = value;
                    break;
                case 1:
                    processTime = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    protected AbstractChaosMachineTileEntity(TileEntityType<?> typeIn, int inventorySize) {
        super(typeIn, inventorySize);
    }

    protected abstract int[] getOutputSlots();

    @Nullable
    protected abstract R getRecipe();

    protected abstract int getChaosGenerated(R recipe);

    protected abstract int getProcessTime(R recipe);

    protected abstract ItemStack getProcessResult(R recipe);

    protected void sendUpdate() {
        if (world == null) return;
        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state, 3);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    protected void setInactiveState() {
        if (world == null) return;
        if (progress > 0) {
            progress = 0;
            sendUpdate();
        }
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        R recipe = getRecipe();
        if (recipe != null && canMachineRun(recipe)) {
            // Process
            ++progress;
            processTime = getProcessTime(recipe);
            chaosGenerated = getChaosGenerated(recipe);
            chaosBuffer += this.chaosGenerated;

            if (progress >= processTime) {
                // Create result
                storeResultItem(getProcessResult(recipe));
                consumeIngredients(recipe);

                if (getRecipe() == null) {
                    // Nothing left to process
                    setInactiveState();
                } else {
                    // Continue processing next output
                    progress = 0;
                }
            }
        } else {
            setNeutralState();
        }

        if (this.chaosBuffer > 0 && this.world.getGameTime() % 20 == 0) {
            Chaos.generate(this.world, this.chaosBuffer, this.pos);
            this.chaosBuffer = 0;
        }
    }

    private void setNeutralState() {
        boolean update = progress > 0 || chaosGenerated > 0;
        progress = 0;
        chaosGenerated = 0;
        if (update) {
            sendUpdate();
        }
    }

    private boolean canMachineRun(R recipe) {
        return world != null && hasRoomForOutputItem(getProcessResult(recipe));
    }

    private boolean hasRoomForOutputItem(ItemStack stack) {
        for (int i : getOutputSlots()) {
            ItemStack output = getStackInSlot(i);
            if (canItemsStack(stack, output)) {
                return true;
            }
        }
        return false;
    }

    protected static boolean canItemsStack(ItemStack a, ItemStack b) {
        // Determine if the item stacks can be merged
        if (a.isEmpty() || b.isEmpty()) return true;
        return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
    }

    private void storeResultItem(ItemStack stack) {
        // Merge the item into any output slot it can fit in
        for (int i : getOutputSlots()) {
            ItemStack output = getStackInSlot(i);
            if (canItemsStack(stack, output)) {
                if (output.isEmpty()) {
                    setInventorySlotContents(i, stack);
                } else {
                    output.setCount(output.getCount() + stack.getCount());
                }
                return;
            }
        }
    }

    protected abstract void consumeIngredients(R recipe);

    @Override
    public void read(BlockState state, CompoundNBT tags) {
        super.read(state, tags);
        readData(tags);
    }

    private void readData(CompoundNBT tags) {
        this.progress = tags.getInt("Progress");
        this.processTime = tags.getInt("ProcessTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        super.write(tags);
        writeData(tags);
        return tags;
    }

    private void writeData(CompoundNBT tags) {
        tags.putInt("Progress", this.progress);
        tags.putInt("ProcessTime", this.processTime);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        CompoundNBT tags = packet.getNbtCompound();
        readData(tags);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        writeData(tags);
        return tags;
    }
}
