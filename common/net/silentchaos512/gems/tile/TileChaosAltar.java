package net.silentchaos512.gems.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.chunk.Chunk;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.gems.api.energy.IChaosStorage;
import net.silentchaos512.gems.api.recipe.altar.RecipeChaosAltar;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.tile.TileSidedInventorySL;
import net.silentchaos512.lib.util.StackHelper;

public class TileChaosAltar extends TileSidedInventorySL implements ITickable, IChaosAccepter {

  public static final int MAX_CHAOS_STORED = 10000000;
  public static final int MAX_RECEIVE = 100000;
  public static final int MAX_ITEM_SEND = 10000;
  public static final int TRANSMUTE_CHAOS_PER_TICK = 80;

  public static final int[] SLOTS_BOTTOM = { 1 };
  public static final int[] SLOTS_TOP = { 0 };
  public static final int[] SLOTS_SIDE = { 0 };

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  public static final int SLOT_CATALYST = 2;
  public static final int INVENTORY_SIZE = 3;

  protected int chaosStored;
  protected int transmuteProgress = 0;

  @Override
  public void update() {

    if (world.isRemote)
      return;

    ItemStack inputStack = getStackInSlot(SLOT_INPUT);
    if (StackHelper.isEmpty(inputStack))
      return;
    ItemStack outputStack = getStackInSlot(SLOT_OUTPUT);
    ItemStack catalystStack = getStackInSlot(SLOT_CATALYST);

    // Chaos storage item?
    if (inputStack.getItem() instanceof IChaosStorage) {
      // Charge chaos storage items.
      IChaosStorage chaosStorage = (IChaosStorage) inputStack.getItem();
      int amount = chaosStorage.receiveCharge(inputStack, Math.min(chaosStored, MAX_ITEM_SEND), false);
      chaosStored -= amount;

      // Send update?
      if (amount != 0)
        markDirty();

      // Move full items to second slot
      if (chaosStorage.getCharge(inputStack) >= chaosStorage.getMaxCharge(inputStack)) {
        if (StackHelper.isEmpty(outputStack)) {
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
        boolean willFitInOutputSlot = StackHelper.isEmpty(outputStack)
            || (outputStack.isItemEqual(recipe.getOutput()) && StackHelper.getCount(outputStack)
                + StackHelper.getCount(recipe.getOutput()) <= outputStack.getMaxStackSize());

        if (transmuteProgress >= recipe.getChaosCost() && willFitInOutputSlot) {
          // Transmute complete
          transmuteProgress = 0;
          if (StackHelper.isEmpty(outputStack))
            setInventorySlotContents(SLOT_OUTPUT, recipe.getOutput());
          else
            StackHelper.grow(getStackInSlot(SLOT_OUTPUT), StackHelper.getCount(recipe.getOutput()));

          decrStackSize(SLOT_INPUT, StackHelper.getCount(recipe.getInput()));
        }

        if (chaosDrained != 0)
          markDirty();
      } else {
        transmuteProgress = 0;
      }
    }
  }

  public ItemStack getStackToRender() {

    ItemStack stack = getStackInSlot(SLOT_INPUT);
    if (StackHelper.isValid(stack))
      return stack;

    stack = getStackInSlot(SLOT_OUTPUT);
    if (StackHelper.isValid(stack))
      return stack;

    return StackHelper.empty();
  }

  public RecipeChaosAltar getActiveRecipe() {

    return RecipeChaosAltar.getMatchingRecipe(getStackInSlot(SLOT_INPUT),
        getStackInSlot(SLOT_CATALYST));
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {

    super.readFromNBT(tags);
    chaosStored = tags.getInteger("Energy");
    transmuteProgress = tags.getInteger("Progress");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {

    super.writeToNBT(tags);
    tags.setInteger("Energy", chaosStored);
    tags.setInteger("Progress", transmuteProgress);
    return tags;
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("Energy", getCharge());
    tags.setInteger("Progress", transmuteProgress);
    return new SPacketUpdateTileEntity(pos, 1, tags);
  }

  @Override
  public NBTTagCompound getUpdateTag() {

    NBTTagCompound tags = super.getUpdateTag();
    tags.setInteger("Energy", getCharge());
    tags.setInteger("Progress", transmuteProgress);
    return tags;
  }

  @Override
  public void onDataPacket(NetworkManager network, SPacketUpdateTileEntity packet) {

    chaosStored = packet.getNbtCompound().getInteger("Energy");
    transmuteProgress = packet.getNbtCompound().getInteger("Progress");
  }

  @Override
  public void markDirty() {

    super.markDirty();
    Chunk chunk = world.getChunkFromBlockCoords(pos);
    IBlockState state = world.getBlockState(pos);
    world.markAndNotifyBlock(pos, chunk, state, state, 2);
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
        markDirty();
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
