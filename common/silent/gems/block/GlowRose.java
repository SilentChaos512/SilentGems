package silent.gems.block;

import java.util.List;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import silent.gems.SilentGems;
import silent.gems.configuration.Config;
import silent.gems.core.registry.IAddRecipe;
import silent.gems.core.registry.IHasVariants;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;

public class GlowRose extends BlockBush implements IAddRecipe, IHasVariants {
  
  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

  public GlowRose() {

    super(Material.plants);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));
    this.lightValue = Config.GLOW_ROSE_LIGHT_LEVEL.value;

    setUnlocalizedName(Names.GLOW_ROSE);
  }
  
  @Override
  public String getName() {
    
    return Names.GLOW_ROSE;
  }
  
  @Override
  public String getFullName() {
    
    return SilentGems.MOD_ID + ":" + getName();
  }
  
  @Override
  public String[] getVariantNames() {
    
    String[] names = new String[EnumGem.count()];
    for (int i = 0; i < names.length; ++i) {
      names[i] = getFullName() + i;
    }
    return names;
  }

  @Override
  public void addRecipes() {

    Item dyeSG = SRegistry.getItem(Names.DYE);

    // Flowers to dye.
    int k = 2;
    // 0=black
    GameRegistry.addShapelessRecipe(new ItemStack(dyeSG, k, 0), new ItemStack(this, 1,
        EnumGem.ONYX.getId()));
    // 1=red
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 1), new ItemStack(this, 1,
        EnumGem.RUBY.getId()));
    // 2=green
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 2), new ItemStack(this, 1,
        EnumGem.EMERALD.getId()));
    // 3=brown
    // 4=blue
    GameRegistry.addShapelessRecipe(new ItemStack(dyeSG, k, 4), new ItemStack(this, 1,
        EnumGem.SAPPHIRE.getId()));
    // 5=purple
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 5), new ItemStack(this, 1,
        EnumGem.AMETHYST.getId()));
    // 6=cyan
    // 7=light gray
    // 8=gray
    // 9=pink
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 9), new ItemStack(this, 1,
        EnumGem.MORGANITE.getId()));
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 10), new ItemStack(this, 1,
        EnumGem.PERIDOT.getId()));
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 11), new ItemStack(this, 1,
        EnumGem.HELIODOR.getId()));
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 12), new ItemStack(this, 1,
        EnumGem.AQUAMARINE.getId()));
    // 13-magenta
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 14), new ItemStack(this, 1,
        EnumGem.TOPAZ.getId()));
  }
  
  @Override
  public void addOreDict() {
    
  }
  
  @Override
  public String getUnlocalizedName() {
    
    return "tile." + Names.GLOW_ROSE;
  }
  
  @Override
  public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {
    
    for (int i = 0; i < EnumGem.count(); ++i) {
      subItems.add(new ItemStack(this, 1, i));
    }
  }
  
  @Override
  public int damageDropped(IBlockState state) {

    return this.getMetaFromState(state);
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
