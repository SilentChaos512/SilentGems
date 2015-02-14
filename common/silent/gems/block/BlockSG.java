package silent.gems.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import silent.gems.SilentGems;
import silent.gems.core.registry.IAddRecipe;
import silent.gems.core.registry.IAddThaumcraftStuff;
import silent.gems.core.registry.IHasVariants;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Reference;
import silent.gems.lib.Strings;

public class BlockSG extends Block implements IAddRecipe, IHasVariants, IAddThaumcraftStuff {
  
//  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

  // public IIcon[] icons = null;
  protected int subBlockCount = 1;
  public boolean hasSubtypes = false;
  protected boolean gemSubtypes = false;
  protected String blockName = "null";

  public BlockSG(int subBlockCount, Material material) {

    super(material);

    this.subBlockCount = subBlockCount;
    setHardness(3.0f);
    setResistance(10.0f);
    setStepSound(Block.soundTypeMetal);
    setCreativeTab(SilentGems.tabSilentGems);
    setUnlocalizedName(Strings.RESOURCE_PREFIX + blockName);
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public void addRecipes() {

  }

  @Override
  public void addThaumcraftStuff() {

  }

  @Override
  public int damageDropped(IBlockState state) {

    return hasSubtypes ? this.getMetaFromState(state) : 0;
  }

  public boolean getHasGemSubtypes() {

    return gemSubtypes;
  }
  
  public Block setHasGemSubtypes(boolean value) {

    gemSubtypes = value;
    
//    if (gemSubtypes) {
//      setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));
//    }
    
    return this;
  }

  public boolean getHasSubtypes() {

    return hasSubtypes;
  }

  public Block setHasSubtypes(boolean value) {

    hasSubtypes = value;
    return this;
  }

  // @Override
  // public IIcon getIcon(int side, int meta) {
  //
  // if (hasSubtypes && meta < icons.length) {
  // return icons[meta];
  // }
  // else {
  // return blockIcon;
  // }
  // }

  @Override
  public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {

    if (hasSubtypes) {
      for (int i = 0; i < subBlockCount; ++i) {
        subItems.add(new ItemStack(this, 1, i));
      }
    } else {
      super.getSubBlocks(item, tab, subItems);
    }
  }

  public int getSubBlockCount() {

    return subBlockCount;
  }
  
//  @Override
//  public IBlockState getStateFromMeta(int meta) {
//    
//    if (gemSubtypes) {
//      return this.getDefaultState().withProperty(VARIANT, EnumGem.byId(meta));
//    } else {
//      return super.getStateFromMeta(meta);
//    }
//  }
//  
//  @Override
//  public int getMetaFromState(IBlockState state) {
//    
//    if (gemSubtypes) {
//      return ((EnumGem) state.getValue(VARIANT)).getId();
//    } else {
//      return super.getMetaFromState(state);
//    }
//  }
//  
//  @Override
//  protected BlockState createBlockState() {
//    
//    if (gemSubtypes) {
//      return new BlockState(this, new IProperty[] { VARIANT });
//    } else {
//      return super.createBlockState();
//    }
//  }

  @Override
  public String getName() {

    return blockName;
  }

  @Override
  public String getFullName() {

    return Reference.MOD_ID + ":" + blockName;
  }

  @Override
  public String[] getVariantNames() {

    if (hasSubtypes) {
      String[] names = new String[subBlockCount];
      for (int i = 0; i < names.length; ++i) {
        names[i] = getFullName() + i;
      }
      return names;
    }
    return new String[] { getFullName() };
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + this.blockName;
  }

  // @Override
  // public void registerBlockIcons(IIconRegister reg) {
  //
  // if (gemSubtypes) {
  // registerIconsForGemSubtypes(reg);
  // }
  // else {
  // blockIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + blockName);
  // }
  // }

  // @SideOnly(Side.CLIENT)
  // public void registerIconsForGemSubtypes(IIconRegister reg) {
  //
  // if (icons == null || icons.length != EnumGem.all().length) {
  // icons = new IIcon[EnumGem.all().length];
  // }
  //
  // for (int i = 0; i < EnumGem.all().length; ++i) {
  // icons[i] = reg.registerIcon(Strings.RESOURCE_PREFIX + this.blockName + i);
  // }
  // }

  public Block setUnlocalizedName(String blockName) {

    this.blockName = blockName;
    // setBlockName(blockName);
    return this;
  }
}
