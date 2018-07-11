package net.silentchaos512.gems.api.energy;

/**
 * Represents a tile entity that holds Chaos energy.
 * 
 * @author SilentChaos512
 *
 */
public interface IChaosHandler {

  /**
   * @return The current amount of Chaos in the tile entity.
   */
  public int getCharge();

  /**
   * @return The maximum amount of Chaos the tile entity can hold.
   */
  public int getMaxCharge();
}
