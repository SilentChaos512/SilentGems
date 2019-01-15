package net.silentchaos512.gems.util;


@Deprecated // Can be replaced with base class?
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
