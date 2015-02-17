package silent.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import silent.gems.SilentGems;
import silent.gems.core.registry.IHasVariants;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.Names;

public class FluffyPlantBlock extends BlockCrops implements IHasVariants {

  protected String blockName;

  public FluffyPlantBlock() {

    this.setUnlocalizedName(Names.FLUFFY_PLANT);
  }

  @Override
  protected Item getSeed() {

    return SRegistry.getItem(Names.FLUFFY_SEED);
  }

  @Override
  protected Item getCrop() {

    return SRegistry.getItem(Names.FLUFFY_SEED);
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
