package net.silentchaos512.gems.api.energy;

/**
 * Represents a tile entity that can provide Chaos to other tiles.
 * 
 * @author SilentChaos512
 *
 */
public interface IChaosProvider extends IChaosHandler {

  /**
   * Remove Chaos from the Provider.
   * 
   * @param maxExtract
   *          The maximum amount to be removed.
   * @param simulate
   *          If true, the Chaos will not actually be extracted.
   * @return The amount of Chaos that is (or would be) extracted.
   */
  int extractEnergy(int maxExtract, boolean simulate);
}
