package net.silentchaos512.gems.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.silentchaos512.gems.lib.Strings;

public class TileSG extends TileEntity {

  protected byte state;
  protected String customName;

  public TileSG() {

    state = 0;
    customName = "";
  }

  public short getState() {

    return state;
  }

  public void setState(byte state) {

    this.state = state;
  }

  public boolean hasCustomName() {

    return customName != null && customName.length() > 0;
  }

  public String getCustomName() {

    return customName;
  }

  public void setCustomName(String customName) {

    this.customName = customName;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbtTagCompound) {

    super.readFromNBT(nbtTagCompound);

    if (nbtTagCompound.hasKey(Strings.NBT_TE_STATE_KEY)) {
      state = nbtTagCompound.getByte(Strings.NBT_TE_STATE_KEY);
    }

    if (nbtTagCompound.hasKey(Strings.NBT_CUSTOM_NAME)) {
      customName = nbtTagCompound.getString(Strings.NBT_CUSTOM_NAME);
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound nbtTagCompound) {

    super.writeToNBT(nbtTagCompound);

    nbtTagCompound.setByte(Strings.NBT_TE_STATE_KEY, state);

    if (this.hasCustomName()) {
      nbtTagCompound.setString(Strings.NBT_CUSTOM_NAME, customName);
    }
  }

  @Override
  public String toString() {

    return String.format(
        "TileSG Data - Class: %s, xCoord: %d, yCoord: %d, zCoord: %d, customName: '%s', state: %d\n",
        this.getClass().getSimpleName(), pos.getX(), pos.getY(), pos.getZ(), customName, state);
  }
}
