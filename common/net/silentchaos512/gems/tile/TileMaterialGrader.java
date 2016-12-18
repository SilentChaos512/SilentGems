package net.silentchaos512.gems.tile;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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

  static final String NBT_ENERGY = "Energy";
  static final String NBT_PROGRESS = "Progress";

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  public static final int ANALYZE_TIME = 600;
  public static final int CHAOS_PER_TICK = 50;
  public static final int MAX_CHARGE = ANALYZE_TIME * CHAOS_PER_TICK * 10;

  protected int chaosStored = 0;
  protected int progress = 0;
  protected boolean requireClientSync = false;

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
    if (!simulate && amount > 0 && !worldObj.isRemote) {
      chaosStored += amount;
      requireClientSync = true;
    }
    return amount;
  }

  @Override
  public void update() {

    // if (worldObj.getTotalWorldTime() % 40 == 0)
    // SilentGems.logHelper.debug(worldObj.isRemote, chaosStored, getCharge());

    if (worldObj.isRemote)
      return;

    ItemStack input = getStackInSlot(SLOT_INPUT);
    ToolPart part = input != null ? ToolPartRegistry.fromStack(input) : null;

    // Is input (if anything) a grade-able part?
    if (part != null && part instanceof ToolPartMain
        && EnumMaterialGrade.fromStack(input) == EnumMaterialGrade.NONE) {
      // Analyze, if we have enough energy.
      if (chaosStored >= CHAOS_PER_TICK) {
        // Analyzing material.
        if (progress < ANALYZE_TIME) {
          chaosStored -= CHAOS_PER_TICK;
          ++progress;
          requireClientSync = true;
        }

        // Grade material if output slot is free.
        if (progress >= ANALYZE_TIME && getStackInSlot(SLOT_OUTPUT) == null) {
          progress = 0;

          // Take one from input stack.
          ItemStack stack = input.copy();
          stack.stackSize = 1;
          --input.stackSize;

          // Assign random grade.
          EnumMaterialGrade.selectRandom(SilentGems.random).setGradeOnStack(stack);

          // Set to output slot, clear input slot if needed.
          setInventorySlotContents(SLOT_OUTPUT, stack);
          if (input.stackSize <= 0) {
            setInventorySlotContents(SLOT_INPUT, null);
          }

          requireClientSync = true;
        }
      }
    } else {
      progress = 0;
    }

    // Send update to client?
    if (requireClientSync) {
      IBlockState state = worldObj.getBlockState(pos);
      worldObj.notifyBlockUpdate(pos, state, state, 3);
      requireClientSync = false;
    }
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
      case 0: chaosStored = value; break;
      case 1: progress = value; break;
    } // @formatter:on
  }

  @Override
  public int getFieldCount() {

    return 2;
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger(NBT_ENERGY, getCharge());
    tags.setInteger(NBT_PROGRESS, progress);
    return new SPacketUpdateTileEntity(pos, getBlockMetadata(), tags);
  }

  @Override
  public NBTTagCompound getUpdateTag() {

    NBTTagCompound tags = super.getUpdateTag();
    tags.setInteger(NBT_ENERGY, getCharge());
    tags.setInteger(NBT_PROGRESS, progress);
    return tags;
  }

  @Override
  public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {

    super.onDataPacket(net, pkt);
    NBTTagCompound tags = pkt.getNbtCompound();
    chaosStored = tags.getInteger(NBT_ENERGY);
    progress = tags.getInteger(NBT_PROGRESS);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    chaosStored = compound.getInteger(NBT_ENERGY);
    progress = compound.getInteger(NBT_PROGRESS);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    compound.setInteger(NBT_ENERGY, chaosStored);
    compound.setInteger(NBT_PROGRESS, progress);
    return compound;
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
  public boolean isItemValidForSlot(int index, ItemStack stack) {

    if (index == SLOT_INPUT) {
      if (stack == null) {
        return false;
      }

      ToolPart part = ToolPartRegistry.fromStack(stack);
      EnumMaterialGrade grade = EnumMaterialGrade.fromStack(stack);
      if (part != null && part instanceof ToolPartMain && grade == EnumMaterialGrade.NONE) {
        return true;
      }

      return false;
    }

    return true;
  }

  @Override
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {

    return isItemValidForSlot(index, itemStackIn);
  }

  @Override
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {

    return true;
  }

  public List<String> getDebugLines() {

    List<String> list = Lists.newArrayList();

    list.add(String.format("Chaos: %,d / %,d", getCharge(), getMaxCharge()));
    list.add(String.format("progress = %d", progress));
    list.add(String.format("ANALYZE_TIME = %d", ANALYZE_TIME));
    list.add(String.format("CHAOS_PER_TICK = %d", CHAOS_PER_TICK));

    return list;
  }
}
