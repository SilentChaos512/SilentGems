package net.silentchaos512.gems.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.gems.api.energy.IChaosStorage;
import net.silentchaos512.gems.api.recipe.altar.RecipeChaosAltar;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.lib.tile.TileSidedInventorySL;

public class TileChaosAltar extends TileSidedInventorySL implements ITickable, IChaosAccepter {

  public static final int MAX_CHAOS_STORED = 10000000;
  public static final int MAX_RECEIVE = 100000;
  public static final int MAX_ITEM_SEND = 10000;
  public static final int TRANSMUTE_CHAOS_PER_TICK = 400;

  public static final int[] SLOTS_BOTTOM = { 1 };
  public static final int[] SLOTS_TOP = { 0 };
  public static final int[] SLOTS_SIDE = { 0 };

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  public static final int SLOT_CATALYST = 2;
  public static final int INVENTORY_SIZE = 3;

  @SyncVariable(name = "Energy")
  protected int chaosStored;
  @SyncVariable(name = "Progress")
  protected int transmuteProgress = 0;

  private int updateTimer = 100;

  @Override
  public void update() {

    if (world.isRemote)
      return;

    ItemStack inputStack = getStackInSlot(SLOT_INPUT);
    if (inputStack.isEmpty())
      return;
    ItemStack outputStack = getStackInSlot(SLOT_OUTPUT);
    ItemStack catalystStack = getStackInSlot(SLOT_CATALYST);

    // Chaos storage item?
    if (inputStack.getItem() instanceof IChaosStorage) {
      // Charge chaos storage items.
      IChaosStorage chaosStorage = (IChaosStorage) inputStack.getItem();
      int amount = chaosStorage.receiveCharge(inputStack, Math.min(chaosStored, MAX_ITEM_SEND),
          false);
      chaosStored -= amount;

      // Send update?
      if (amount != 0) {
        sendUpdate();
        updateTimer = -199;
      }

      // Move full items to second slot
      if (chaosStorage.getCharge(inputStack) >= chaosStorage.getMaxCharge(inputStack)) {
        if (outputStack.isEmpty()) {
          setInventorySlotContents(SLOT_OUTPUT, inputStack);
          removeStackFromSlot(SLOT_INPUT);
        }
      }
    }
    // Chaos altar recipe?
    else {
      RecipeChaosAltar recipe = RecipeChaosAltar.getMatchingRecipe(inputStack, catalystStack);
      if (recipe != null) {
        // Drain Chaos
        int chaosDrained = Math.min(chaosStored,
            Math.min(TRANSMUTE_CHAOS_PER_TICK, recipe.getChaosCost() - transmuteProgress));
        chaosStored -= chaosDrained;

        // Transmute progress
        transmuteProgress += chaosDrained;
        boolean willFitInOutputSlot = outputStack.isEmpty()
            || (outputStack.isItemEqual(recipe.getOutput()) && outputStack.getCount()
                + recipe.getOutput().getCount() <= outputStack.getMaxStackSize());

        if (transmuteProgress >= recipe.getChaosCost() && willFitInOutputSlot) {
          // Transmute complete
          transmuteProgress = 0;
          if (outputStack.isEmpty())
            setInventorySlotContents(SLOT_OUTPUT, recipe.getOutput());
          else
            getStackInSlot(SLOT_OUTPUT).grow(recipe.getOutput().getCount());

          decrStackSize(SLOT_INPUT, recipe.getInput().getCount());
        }

        if (chaosDrained != 0)
          sendUpdate();
      } else {
        transmuteProgress = 0;
      }
    }

    // sendUpdate doesn't always work (during world load?), so we have to do this ugliness to let the client know what
    // the altar is holding for rendering purposes... Is there a better way?
    if (updateTimer % 200 == 0) {
      sendUpdate();
    }

    ++updateTimer;
  }

  public ItemStack getStackToRender() {

    ItemStack stack = getStackInSlot(SLOT_INPUT);
    if (!stack.isEmpty())
      return stack;

    stack = getStackInSlot(SLOT_OUTPUT);
    if (!stack.isEmpty())
      return stack;

    return ItemStack.EMPTY;
  }

  public RecipeChaosAltar getActiveRecipe() {

    return RecipeChaosAltar.getMatchingRecipe(getStackInSlot(SLOT_INPUT),
        getStackInSlot(SLOT_CATALYST));
  }

  @Override
  public int getField(int id) {

    switch (id) {
      case 0:
        return chaosStored;
      case 1:
        return transmuteProgress;
      case 2:
        RecipeChaosAltar recipe = RecipeChaosAltar.getMatchingRecipe(getStackInSlot(SLOT_INPUT),
            getStackInSlot(SLOT_CATALYST));
        return recipe == null ? -1 : RecipeChaosAltar.ALL_RECIPES.indexOf(recipe);
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {

    // TODO Auto-generated method stub

  }

  @Override
  public int getFieldCount() {

    return 3;
  }

  @Override
  public String getName() {

    return Names.CHAOS_ALTAR;
  }

  @Override
  public int getCharge() {

    return chaosStored;
  }

  @Override
  public int getMaxCharge() {

    return MAX_CHAOS_STORED;
  }

  @Override
  public int receiveCharge(int maxReceive, boolean simulate) {

    int received = Math.min(getMaxCharge() - getCharge(), maxReceive);
    if (!simulate) {
      chaosStored += received;
      if (received != 0) {
        sendUpdate();
      }
    }
    return received;
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side) {

    switch (side) {
      case DOWN:
        return SLOTS_BOTTOM;
      case UP:
        return SLOTS_TOP;
      default:
        return SLOTS_SIDE;
    }
  }

  @Override
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {

    return isItemValidForSlot(index, itemStackIn);
  }

  @Override
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {

    return direction == EnumFacing.DOWN;
  }

  @Override
  public int getSizeInventory() {

    return INVENTORY_SIZE;
  }
}
