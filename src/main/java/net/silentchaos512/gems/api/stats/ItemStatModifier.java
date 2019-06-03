package net.silentchaos512.gems.api.stats;

import lombok.AccessLevel;
import lombok.Getter;

public class ItemStatModifier {
    public enum Operation {
        AVERAGE, ADD, MULTIPLY, MAX
    }

    @Getter(AccessLevel.PUBLIC)
    private final String id;
    @Getter(AccessLevel.PUBLIC)
    private final float amount;
    @Getter(AccessLevel.PUBLIC)
    private final Operation operation;

    public ItemStatModifier(String id, float amount, Operation operation) {
        this.id = id;
        this.amount = amount;
        this.operation = operation;
    }
}