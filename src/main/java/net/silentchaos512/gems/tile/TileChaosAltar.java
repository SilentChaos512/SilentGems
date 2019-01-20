package net.silentchaos512.gems.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class TileChaosAltar extends TileEntity implements ITickable, IInventory {
    public static final int MAX_CHAOS_STORED = 10000000;
    public static final int MAX_RECEIVE = 100000;
    public static final int MAX_ITEM_SEND = 10000;
    public static final int TRANSMUTE_CHAOS_PER_TICK = 400;

    public static final int[] SLOTS_BOTTOM = {1};
    public static final int[] SLOTS_TOP = {0};
    public static final int[] SLOTS_SIDE = {0};

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_CATALYST = 2;
    public static final int INVENTORY_SIZE = 3;

//    @SyncVariable(name = "Energy")
    protected int chaosStored;
//    @SyncVariable(name = "Progress")
    protected int transmuteProgress = 0;

    private int updateTimer = 100;

    public TileChaosAltar(TileEntityType<?> tileEntityTypeIn) {
        // FIXME
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
//        if (world.isRemote)
//            return;
//
//        ItemStack inputStack = getStackInSlot(SLOT_INPUT);
//        if (inputStack.isEmpty())
//            return;
//        ItemStack outputStack = getStackInSlot(SLOT_OUTPUT);
//        ItemStack catalystStack = getStackInSlot(SLOT_CATALYST);
//
//        // Chaos storage item?
//        if (inputStack.getItem() instanceof IChaosStorage) {
//            // Charge chaos storage items.
//            IChaosStorage chaosStorage = (IChaosStorage) inputStack.getItem();
//            int amount = chaosStorage.receiveCharge(inputStack, Math.min(chaosStored, MAX_ITEM_SEND),
//                    false);
//            chaosStored -= amount;
//
//            // Send update?
//            if (amount != 0) {
//                sendUpdate();
//                updateTimer = -199;
//            }
//
//            // Move full items to second slot
//            if (chaosStorage.getCharge(inputStack) >= chaosStorage.getMaxCharge(inputStack)) {
//                if (outputStack.isEmpty()) {
//                    setInventorySlotContents(SLOT_OUTPUT, inputStack);
//                    removeStackFromSlot(SLOT_INPUT);
//                }
//            }
//        }
//        // Chaos altar recipe?
//        else {
//            RecipeChaosAltar recipe = RecipeChaosAltar.getMatchingRecipe(inputStack, catalystStack);
//            if (recipe != null) {
//                // Drain Chaos
//                int chaosDrained = Math.min(chaosStored,
//                        Math.min(TRANSMUTE_CHAOS_PER_TICK, recipe.getChaosCost() - transmuteProgress));
//                chaosStored -= chaosDrained;
//
//                // Transmute progress
//                transmuteProgress += chaosDrained;
//                boolean willFitInOutputSlot = outputStack.isEmpty()
//                        || (outputStack.isItemEqual(recipe.getOutput()) && outputStack.getCount()
//                        + recipe.getOutput().getCount() <= outputStack.getMaxStackSize());
//
//                if (transmuteProgress >= recipe.getChaosCost() && willFitInOutputSlot) {
//                    // Transmute complete
//                    transmuteProgress = 0;
//                    if (outputStack.isEmpty())
//                        setInventorySlotContents(SLOT_OUTPUT, recipe.getOutput());
//                    else
//                        getStackInSlot(SLOT_OUTPUT).grow(recipe.getOutput().getCount());
//
//                    decrStackSize(SLOT_INPUT, recipe.getInput().getCount());
//                }
//
//                if (chaosDrained != 0)
//                    sendUpdate();
//            } else {
//                transmuteProgress = 0;
//            }
//        }
//
//        // sendUpdate doesn't always work (during world load?), so we have to do this ugliness to let the client know what
//        // the altar is holding for rendering purposes... Is there a better way?
//        if (updateTimer % 200 == 0) {
//            sendUpdate();
//        }
//
//        ++updateTimer;
    }

    public ItemStack getStackToRender() {
//        ItemStack stack = getStackInSlot(SLOT_INPUT);
//        if (!stack.isEmpty())
//            return stack;
//
//        stack = getStackInSlot(SLOT_OUTPUT);
//        if (!stack.isEmpty())
//            return stack;

        return ItemStack.EMPTY;
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public ITextComponent getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return null;
    }

//    public RecipeChaosAltar getActiveRecipe() {
//        return RecipeChaosAltar.getMatchingRecipe(getStackInSlot(SLOT_INPUT), getStackInSlot(SLOT_CATALYST));
//    }
}
