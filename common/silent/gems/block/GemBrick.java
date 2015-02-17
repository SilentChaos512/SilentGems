package silent.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import silent.gems.core.util.RecipeHelper;
import silent.gems.item.ModItems;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;

public class GemBrick extends BlockSG {
  
  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

  public GemBrick(String name) {

    super(EnumGem.count(), Material.rock);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));

    setHardness(3.0f);
    setResistance(30.0f);
    setStepSound(Block.soundTypeStone);
    
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(name);
  }

  @Override
  public void addRecipes() {
    
    ItemStack bricks = new ItemStack(Blocks.stonebrick);
    Item gem;
    
    if (this.blockName.equals(Names.GEM_BRICK_COATED)) {
      gem = ModItems.gem;
    } else {
      gem = ModItems.gemShard;
    }
    
    for (int i = 0; i < EnumGem.count(); ++i) {
      ItemStack gemStack = new ItemStack(gem, 1, i);
      ItemStack result = new ItemStack(this, 8, i);
      RecipeHelper.addSurround(result, gemStack, bricks);
    }
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
}
