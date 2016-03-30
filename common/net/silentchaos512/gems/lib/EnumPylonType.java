package net.silentchaos512.gems.lib;

import net.minecraft.util.IStringSerializable;

public enum EnumPylonType implements IStringSerializable {

  NONE(-1, 0),
  PASSIVE(0, 10),
  BURNER(1, 100);

  private final int meta;
  private final int generationRate;

  private EnumPylonType(int meta, int generationRate) {

    this.meta = meta;
    this.generationRate = generationRate;
  }

  public static EnumPylonType getByMeta(int meta) {

    for (EnumPylonType type : EnumPylonType.values()) {
      if (type.meta == meta) {
        return type;
      }
    }
    return NONE;
  }

  public int getChaosGenerationRate() {

    return generationRate;
  }

  public int getMeta() {

    return meta;
  }

  @Override
  public String getName() {

    return name().toLowerCase();
  }
}
