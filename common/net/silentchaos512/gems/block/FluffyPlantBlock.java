package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;

public class FluffyPlantBlock extends BlockCrops implements IHasVariants {

  protected String blockName; // TODO: Is this used?

  public FluffyPlantBlock() {

    setUnlocalizedName(Names.FLUFFY_PLANT);
  }

  @Override
  protected Item getSeed() {

    return ModItems.fluffyPuff;
  }

  @Override
  protected Item getCrop() {

    return ModItems.fluffyPuff;
  }

  @Override
  public int getRenderType() {

    return 3;
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + Names.FLUFFY_PLANT;
  }

  @Override
  public Block setUnlocalizedName(String value) {

    this.blockName = value;
    super.setUnlocalizedName(value);
    return this;
  }

  @Override
  public String[] getVariantNames() {

    String[] result = new String[4];
    for (int i = 0; i < result.length; ++i) {
      result[i] = getFullName() + i;
    }
    return result;
  }

  @Override
  public String getName() {

    return Names.FLUFFY_PLANT;
  }

  @Override
  public String getFullName() {

    return SilentGems.MOD_ID + ":" + Names.FLUFFY_PLANT;
  }
}
