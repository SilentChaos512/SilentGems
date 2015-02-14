package silent.gems.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import silent.gems.lib.Strings;

public class TileSG extends TileEntity {

  protected EnumFacing facing;
  protected byte state;
  protected String customName;

  public TileSG() {

    facing = EnumFacing.SOUTH;
    state = 0;
    customName = "";
  }

  public EnumFacing getFacing() {

    return facing;
  }

  public void setFacing(EnumFacing facing) {

    this.facing = facing;
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

    // TODO: Fix if needed.
//    if (nbtTagCompound.hasKey(Strings.NBT_TE_DIRECTION_KEY)) {
//      facing = ForgeDirection.getOrientation(nbtTagCompound
//          .getByte(Strings.NBT_TE_DIRECTION_KEY));
//    }

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

    nbtTagCompound.setByte(Strings.NBT_TE_DIRECTION_KEY, (byte) facing.ordinal());
    nbtTagCompound.setByte(Strings.NBT_TE_STATE_KEY, state);

    if (this.hasCustomName()) {
      nbtTagCompound.setString(Strings.NBT_CUSTOM_NAME, customName);
    }
  }

  @Override
  public String toString() {

    return String
        .format(
            "TileSG Data - Class: %s, xCoord: %d, yCoord: %d, zCoord: %d, customName: '%s', facing: %s, state: %d\n",
            this.getClass().getSimpleName(), pos.getX(), pos.getY(), pos.getZ(), customName, facing, state);
  }
}
