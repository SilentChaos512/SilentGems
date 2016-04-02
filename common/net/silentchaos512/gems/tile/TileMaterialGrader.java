package net.silentchaos512.gems.tile;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.lib.Names;

public class TileMaterialGrader extends TileBasicInventory
    implements ISidedInventory, ITickable, IChaosAccepter {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  public static final int ANALYZE_TIME = 60; // 600
  public static final int CHAOS_PER_TICK = 50;
  public static final int MAX_CHARGE = ANALYZE_TIME * CHAOS_PER_TICK;

  protected int chaosStored = 0;
  protected int progress = 0;

  public TileMaterialGrader() {

    super(2, Names.MATERIAL_GRADER);
  }

  @Override
  public int getCharge() {

    return chaosStored;
  }

  @Override
  public int getMaxCharge() {

    return MAX_CHARGE;
  }

  @Override
  public int receiveCharge(int maxReceive, boolean simulate) {

    int amount = Math.min(getMaxCharge() - getCharge(), maxReceive);
    if (!simulate) {
      chaosStored += amount;
    }
    return amount;
  }

  @Override
  public void update() {

    if (!worldObj.isRemote)
      SilentGems.instance.logHelper.debug(chaosStored, progress, getStackInSlot(0), getStackInSlot(1));

    ItemStack input = getStackInSlot(SLOT_INPUT);
    ToolPart part = input != null ? ToolPartRegistry.fromStack(input) : null;

    // Not a part, can't be graded, or already graded?
    if (part == null || !(part instanceof ToolPartMain)
        || EnumMaterialGrade.fromStack(input) != EnumMaterialGrade.NONE) {
      progress = 0;
      return;
    }

    // Not enough energy?
    if (chaosStored < CHAOS_PER_TICK) {
      return;
    }

    // Analyzing material.
    if (progress < ANALYZE_TIME) {
      chaosStored -= CHAOS_PER_TICK;
      ++progress;
    }

    // Grade material if output slot is free.
    if (progress >= ANALYZE_TIME && getStackInSlot(SLOT_OUTPUT) == null) {
      progress = 0;

      // Take one from input stack.
      ItemStack stack = input.copy();
      stack.stackSize = 1;
      --input.stackSize;

      // Assign random grade.
      EnumMaterialGrade.selectRandom(SilentGems.instance.random).setGradeOnStack(stack);

      // Set to output slot, clear input slot if needed.
      setInventorySlotContents(SLOT_OUTPUT, stack);
      if (input.stackSize <= 0) {
        setInventorySlotContents(SLOT_INPUT, null);
      }
    }
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {

    // TODO Auto-generated method stub
    return super.isItemValidForSlot(index, stack);
  }

  @Override
  public int getField(int id) {

    switch (id) { // @formatter:off
      case 0: return chaosStored;
      case 1: return progress;
      default: return 0;
    } // @formatter:on
  }

  @Override
  public void setField(int id, int value) {

    switch (id) { // @formatter:off
      case 0: chaosStored = value;
      case 1: progress = value;
    } // @formatter:on
  }

  @Override
  public int getFieldCount() {

    return 2;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    chaosStored = compound.getInteger("Energy");
    progress = compound.getInteger("Progress");
  }

  @Override
  public void writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    compound.setInteger("Energy", chaosStored);
    compound.setInteger("Progress", progress);
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side) {

    switch (side) {
      case DOWN:
        return new int[] { SLOT_OUTPUT };
      default:
        return new int[] { SLOT_INPUT };
    }
  }

  @Override
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {

    ToolPart part = itemStackIn != null ? ToolPartRegistry.fromStack(itemStackIn) : null;
    return index == SLOT_INPUT && direction != EnumFacing.DOWN && part instanceof ToolPartMain;
  }

  @Override
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {

//    SilentGems.instance.logHelper.debug(index, direction, index == SLOT_OUTPUT && direction == EnumFacing.DOWN);
    return index == SLOT_OUTPUT && direction == EnumFacing.DOWN;
  }
}
