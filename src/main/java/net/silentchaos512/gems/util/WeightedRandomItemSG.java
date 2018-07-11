package net.silentchaos512.gems.util;


public class WeightedRandomItemSG extends net.minecraft.util.WeightedRandom.Item {

  private int meta;

  public WeightedRandomItemSG(int itemWeightIn, int meta) {

    super(itemWeightIn);
    this.meta = meta;
  }

  public int getMeta() {

    return meta;
  }
}
