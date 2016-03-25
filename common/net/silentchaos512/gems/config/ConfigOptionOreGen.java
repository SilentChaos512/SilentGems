package net.silentchaos512.gems.config;

import java.util.Random;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.Configuration;

public class ConfigOptionOreGen extends ConfigOption {

  public static final float VEIN_COUNT_MIN = 0.0f;
  public static final float VEIN_COUNT_MAX = 1000.0f;
  public static final int VEIN_SIZE_MIN = 0;
  public static final int VEIN_SIZE_MAX = 1000;
  public static final int Y_MIN = 0;
  public static final int Y_MAX = 255;

  public final String name;
  public float veinCount;
  public int veinSize;
  public int minY;
  public int maxY;
  public final int dimension;

  public ConfigOptionOreGen(String name, int dimension, float veinCount,
      int veinSize, int minY, int maxY) {

    this.name = name;
    this.dimension = dimension;
    this.veinCount = veinCount;
    this.veinSize = veinSize;
    this.minY = minY;
    this.maxY = maxY;
  }

  public int getVeinCount(Random random) {

    float diff = veinCount - (int) veinCount;
    return (int) veinCount + (random.nextFloat() > diff ? 1 : 0);
  }

  @Override
  public ConfigOption loadValue(Configuration c, String category) {

    return loadValue(c, category + c.CATEGORY_SPLITTER + name,
        "World generation for " + name);
  }

  @Override
  public ConfigOption loadValue(Configuration c, String category, String comment) {

    c.setCategoryComment(category, comment);
    veinCount = (float) c.get(category, "Vein Count", veinCount).getDouble();
    veinSize = c.get(category, "Vein Size", veinSize).getInt();
    minY = c.get(category, "Min Y", minY).getInt();
    maxY = c.get(category, "Max Y", maxY).getInt();
    return this.validate();
  }

  @Override
  public ConfigOption validate() {

    veinCount = MathHelper.clamp_float(veinCount, VEIN_COUNT_MIN, VEIN_COUNT_MAX);
    veinSize = MathHelper.clamp_int(veinSize, VEIN_SIZE_MIN, VEIN_SIZE_MAX);
    minY = MathHelper.clamp_int(minY, Y_MIN, Y_MAX);
    maxY = MathHelper.clamp_int(maxY, Y_MIN, Y_MAX);
    return this;
  }
}
