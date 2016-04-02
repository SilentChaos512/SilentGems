package net.silentchaos512.gems.api.energy;

import net.minecraft.item.ItemStack;

/**
 * An item that stores Chaos. Nodes and Pylons sending Chaos to players will fill IChaosStorage items.
 * 
 * @author SilentChaos512
 *
 */
public interface IChaosStorage {

  /**
   * @param stack
   * @return The current amount of Chaos stored.
   */
  int getCharge(ItemStack stack);

  /**
   * @param stack
   * @return The maximum amount of Chaos that can be stored.
   */
  int getMaxCharge(ItemStack stack);

  /**
   * Used to send Chaos to the item.
   * 
   * @param stack
   * @param maxReceive
   *          The maximum amount to receive.
   * @param simulate
   *          If true, the Chaos will not actually be received.
   * @return The amount of Chaos that is (or would be) received.
   */
  int receiveCharge(ItemStack stack, int maxReceive, boolean simulate);

  /**
   * Used to pull Chaos from the item.
   * 
   * @param stack
   * @param maxExtract
   *          The maximum amount to extract.
   * @param simulate
   *          If true, the Chaos will not actually be extracted.
   * @return The amount of Chaos that is (or would be) extracted.
   */
  int extractCharge(ItemStack stack, int maxExtract, boolean simulate);
}
