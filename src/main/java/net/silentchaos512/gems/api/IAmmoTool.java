package net.silentchaos512.gems.api;

import net.minecraft.item.ItemStack;

public interface IAmmoTool {
    int getAmmo(ItemStack tool);

    int getMaxAmmo(ItemStack tool);

    void addAmmo(ItemStack tool, int amount);
}
