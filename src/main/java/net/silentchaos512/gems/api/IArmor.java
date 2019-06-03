package net.silentchaos512.gems.api;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.lib.EnumToolType;

public interface IArmor extends IGearItem {
    ItemStack constructArmor(ItemStack frame, ItemStack... materials);

    @Override
    default EnumToolType getToolType() {
        return EnumToolType.ARMOR;
    }
}
