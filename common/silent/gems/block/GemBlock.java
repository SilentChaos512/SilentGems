package silent.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import silent.gems.SilentGems;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;

public class GemBlock extends BlockSG {
  
  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

  public GemBlock() {

    super(EnumGem.count(), Material.iron);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));

    setHardness(3.0f);
    setResistance(30.0f);
    setStepSound(Block.soundTypeMetal);
    
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(Names.GEM_BLOCK);
  }
  
  @Override
  public IBlockState getStateFromMeta(int meta) {
    
    return this.getDefaultState().withProperty(VARIANT, EnumGem.byId(meta));
  }
  
  @Override
  public int getMetaFromState(IBlockState state) {
    
    return ((EnumGem) state.getValue(VARIANT)).getId();
  }
  
  @Override
  protected BlockState createBlockState() {
    
    return new BlockState(this, new IProperty[] { VARIANT });
  }

  @Override
  public void addRecipes() {
    
    for (int i = 0; i < EnumGem.count(); ++i) {
      ItemStack gem = EnumGem.values()[i].getItem();
      ItemStack block = EnumGem.values()[i].getBlock();
      RecipeHelper.addCompressionRecipe(gem, block, 9);
    }
  }
}
