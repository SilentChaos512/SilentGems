package net.silentchaos512.gems.core.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class DimensionalPosition {

  public final int x, y, z, d;

  public DimensionalPosition(int x, int y, int z, int d) {

    this.x = x;
    this.y = y;
    this.z = z;
    this.d = d;
  }

  public static DimensionalPosition fromNBT(NBTTagCompound tags) {

    return new DimensionalPosition(tags.getInteger("X"), tags.getInteger("Y"), tags.getInteger("Z"),
        tags.getInteger("D"));
  }

  public BlockPos toBlockPos() {

    return new BlockPos(x, y, z);
  }
}
