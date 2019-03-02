package net.silentchaos512.gems.lib.urn;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum LidState implements IStringSerializable {
    /**
     * Lidded, lid closed
     */
    CLOSED,
    /**
     * Lidded, lid open
     */
    OPEN,
    /**
     * Lidless (always open)
     */
    NO_LID;

    public boolean isOpen() {
        return this == OPEN || this == NO_LID;
    }

    public boolean hasLid() {
        return this != NO_LID;
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static LidState fromItem(ItemStack stack) {
        return UrnHelper.hasLid(stack) ? CLOSED : NO_LID;
    }
}
