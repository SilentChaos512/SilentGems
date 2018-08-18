package net.silentchaos512.gems.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.gems.item.ItemChaosGem;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.lib.tile.TileInventorySL;

public class TileChaosPlinth extends TileInventorySL implements ITickable, IChaosAccepter {

  public static final int MAX_CHAOS_STORED = 1000000;
  public static final int MAX_RECEIVE = 10000;
  public static final int MAX_ITEM_SEND = MAX_RECEIVE;

  @SyncVariable(name = "Energy")
  protected int chaosStored;

  @Override
  public void update() {

    if (world.isRemote)
      return;

    // Get the Chaos Gem (if any)
    ItemStack stack = getStackInSlot(0);
    if (stack.isEmpty() || !(stack.getItem() instanceof ItemChaosGem))
      return;

    ItemChaosGem item = (ItemChaosGem) stack.getItem();

    // TODO
  }

  @Override
  public int getCharge() {

    return chaosStored;
  }

  @Override
  public int getMaxCharge() {

    return MAX_CHAOS_STORED;
  }

  @Override
  public int getSizeInventory() {

    return 1;
  }

  @Override
  public int receiveCharge(int maxReceive, boolean simulate) {

    // TODO Auto-generated method stub
    return 0;
  }
}
