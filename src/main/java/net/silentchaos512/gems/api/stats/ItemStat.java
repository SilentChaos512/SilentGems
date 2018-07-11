package net.silentchaos512.gems.api.stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.util.math.MathHelper;

public class ItemStat {

  public static List<ItemStat> ALL_STATS = new ArrayList<>();

  @Getter(value = AccessLevel.PUBLIC)
  protected final String unlocalizedName;
  @Getter(value = AccessLevel.PUBLIC)
  protected final float defaultValue;
  @Getter(value = AccessLevel.PUBLIC)
  protected final float minimumValue;
  @Getter(value = AccessLevel.PUBLIC)
  protected final float maximumValue;

  public ItemStat(String unlocalizedName, float defaultValue, float minValue, float maxValue) {

    this.unlocalizedName = unlocalizedName;
    this.defaultValue = defaultValue;
    this.minimumValue = minValue;
    this.maximumValue = maxValue;

    if (minimumValue > maximumValue) {
      throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
    } else if (defaultValue < minimumValue) {
      throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
    } else if (defaultValue > maximumValue) {
      throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
    }

    ALL_STATS.add(this);
  }

  public float clampValue(float value) {

    value = MathHelper.clamp(value, minimumValue, maximumValue);
    return value;
  }

  public float compute(float baseValue, Collection<ItemStatModifier> modifiers) {

    float f0 = baseValue;

    // Max
    for (ItemStatModifier mod : modifiers) {
      if (mod.getOperation() == ItemStatModifier.Operation.MAX) {
        f0 = Math.max(f0, mod.getAmount());
      }
    }

    // Average (used for mains)
    int count = 0;
    for (ItemStatModifier mod : modifiers) {
      if (mod.getOperation() == ItemStatModifier.Operation.AVERAGE) {
        f0 += mod.getAmount();
        ++count;
      }
    }
    if (count > 0)
      f0 /= count;

    // Additive
    for (ItemStatModifier mod : modifiers)
      if (mod.getOperation() == ItemStatModifier.Operation.ADD)
        f0 += mod.getAmount();

    // Multiplicative
    float f1 = f0;
    for (ItemStatModifier mod : modifiers)
      if (mod.getOperation() == ItemStatModifier.Operation.MULTIPLY)
        f1 *= mod.getAmount();

    // Multiplicative2
//    for (ItemStatModifier mod : modifiers)
//      if (mod.getOperation() == ItemStatModifier.Operation.MULTIPLY2)
//        f1 *= 1.0f + mod.getAmount();

    return clampValue(f1);
  }

  public String toString() {

    return String.format("ItemStat{%s, default=%.2f, min=%.2f, max=%.2f}", unlocalizedName, defaultValue, minimumValue, maximumValue);
  }
}
