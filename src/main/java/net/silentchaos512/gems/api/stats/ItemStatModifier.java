package net.silentchaos512.gems.api.stats;

import lombok.AccessLevel;
import lombok.Getter;

public class ItemStatModifier {

  public static enum Operation {

    AVERAGE, ADD, MULTIPLY, MAX
  }

  @Getter(value = AccessLevel.PUBLIC)
  private final String id;
  @Getter(value = AccessLevel.PUBLIC)
  private final float amount;
  @Getter(value = AccessLevel.PUBLIC)
  private final Operation operation;

  public ItemStatModifier(String id, float amount, Operation operation) {

    this.id = id;
    this.amount = amount;
    this.operation = operation;
  }
}