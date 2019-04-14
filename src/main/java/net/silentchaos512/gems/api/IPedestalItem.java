package net.silentchaos512.gems.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * An item which can be ticked when placed on a pedestal (example: chaos gems).
 */
public interface IPedestalItem {
    /**
     * Called when the redstone power level changes. Any signal strength is considered powered.
     *
     * @param stack   The item on the pedestal
     * @param world   The world
     * @param pos     The pedestal's position
     * @param powered True if receiving redstone power (any strength)
     * @return True if the pedestal should send an update to clients
     */
    boolean pedestalPowerChange(ItemStack stack, World world, BlockPos pos, boolean powered);

    /**
     * Called each tick when the item is on a pedestal
     *
     * @param stack The item on the pedestal
     * @param world The world
     * @param pos   The pedestal's position
     */
    void pedestalTick(ItemStack stack, World world, BlockPos pos);
}
