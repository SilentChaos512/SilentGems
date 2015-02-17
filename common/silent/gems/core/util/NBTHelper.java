package silent.gems.core.util;

import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper {

  /**
   * Check that tag compound has X, Y, Z, and D tags, and that Y is not zero.
   * 
   * @param tags
   * @return
   */
  public static boolean hasValidXYZD(NBTTagCompound tags) {

    return tags.hasKey("X") && tags.hasKey("Y") && tags.hasKey("Z") && tags.hasKey("D")
        && tags.getInteger("Y") != 0;
  }

  /**
   * Sets the X, Y, Z, and D tags used by blocks/items such as the Teleporter and Teleporter Linker.
   * 
   * @param tags
   * @param x
   * @param y
   * @param z
   * @param d
   * @return
   */
  public static NBTTagCompound setXYZD(NBTTagCompound tags, int x, int y, int z, int d) {

    tags.setInteger("X", x);
    tags.setInteger("Y", y);
    tags.setInteger("Z", z);
    tags.setInteger("D", d);

    return tags;
  }
}
