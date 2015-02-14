package silent.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.Names;


public class ChaosEssenceBlock extends BlockSG {
  
  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumType.class);
  
  public ChaosEssenceBlock() {
    
    super(EnumType.values().length, Material.iron);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.RAW));
    
    setHardness(4.0f);
    setResistance(6.0f);
    setStepSound(Block.soundTypeMetal);
    
    setHasSubtypes(true);
    setUnlocalizedName(Names.CHAOS_ESSENCE_BLOCK);
  }
  
  public static ItemStack getByType(EnumType type) {
    
    return new ItemStack(SRegistry.getBlock(Names.CHAOS_ESSENCE_BLOCK), 1, type.getMetadata());
  }
  
  @Override
  public void addRecipes() {
    
    Item essence = SRegistry.getItem(Names.CHAOS_ESSENCE);
    
    for (int i = 0; i < EnumType.values().length; ++i) {
      ItemStack small = new ItemStack(essence, 1, i);
      ItemStack big = new ItemStack(this, 1, i);
      RecipeHelper.addCompressionRecipe(small, big, 9);
    }
  }
  
  @Override
  public IBlockState getStateFromMeta(int meta) {
    
    return this.getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
  }
  
  @Override
  public int getMetaFromState(IBlockState state) {
    
    return ((EnumType) state.getValue(VARIANT)).getMetadata();
  }
  
  @Override
  protected BlockState createBlockState() {
    
    return new BlockState(this, new IProperty[] { VARIANT });
  }
  
  public static enum EnumType implements IStringSerializable {

    RAW(0, "raw"),
    REGULAR(1, "regular"),
    REFINED(2, "refined");

    private static final EnumType[] META_LOOKUP = new EnumType[values().length];
    private final int meta;
    private final String name;

    private EnumType(int meta, String name) {

      this.meta = meta;
      this.name = name;
    }

    public int getMetadata() {

      return this.meta;
    }

    @Override
    public String toString() {

      return this.name;
    }

    public static EnumType byMetadata(int meta) {

      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }

    @Override
    public String getName() {

      return this.name;
    }

    static {
      for (int i = 0; i < values().length; ++i) {
        EnumType current = values()[i];
        META_LOOKUP[current.getMetadata()] = current;
      }
    }
  }
}
