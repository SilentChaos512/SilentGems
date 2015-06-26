package net.silentchaos512.gems.core.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom.Item;

public class WeightedRandomItemSG extends Item {

  private int meta;

  public WeightedRandomItemSG(int weight, int meta) {

    super(weight);
    this.meta = meta;
  }

  public int getMeta() {
    
    return this.meta;
  }
}
