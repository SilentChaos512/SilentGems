package silent.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import silent.gems.core.registry.IHasVariants;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.Names;
import silent.gems.lib.Reference;

public class FluffyPlantBlock extends BlockCrops implements IHasVariants {

  // private IIcon[] icons;
  protected String blockName;

  public FluffyPlantBlock() {

    this.setUnlocalizedName(Names.FLUFFY_PLANT);
//    this.setBlockTextureName(Strings.RESOURCE_PREFIX + Names.FLUFFY_PLANT);
  }

  @Override
  protected Item getSeed() {

    return SRegistry.getItem(Names.FLUFFY_SEED);
  }

  @Override
  protected Item getCrop() {

    return SRegistry.getItem(Names.FLUFFY_SEED);
  }

//  @Override
//  public IIcon getIcon(int side, int meta) {
//
//    if (meta < 7) {
//      if (meta == 6) {
//        meta = 5;
//      }
//
//      return this.icons[meta >> 1];
//    } else {
//      return this.icons[3];
//    }
//  }

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

    return Reference.MOD_ID + ":" + Names.FLUFFY_PLANT;
  }

//  @Override
//  public void registerBlockIcons(IIconRegister reg) {
//
//    this.icons = new IIcon[4];
//
//    for (int i = 0; i < this.icons.length; ++i) {
//      this.icons[i] = reg.registerIcon(this.getTextureName() + i);
//    }
//  }
}
