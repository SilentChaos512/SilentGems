package net.silentchaos512.gems.api.energy;

/**
 * Represents a tile entity that can receive Chaos energy. Certain tiles like Nodes and Pylons will try to send their
 * Chaos to Accepters.
 * 
 * @author SilentChaos512
 *
 */
public interface IChaosAccepter extends IChaosHandler {

  /**
   * Called by things that try to send Chaos to the Accepter.
   * 
   * @param maxReceive
   *          The maximum amount to send to the Accepter.
   * @param simulate
   *          If true, the Chaos will not actually be received.
   * @return The amount of Chaos that is (or would be) received.
   */
  int receiveCharge(int maxReceive, boolean simulate);
}
