package silent.gems.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.SilentGems;
import silent.gems.core.registry.IAddRecipe;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;

public class GemOre extends BlockSG {
  
  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

  public GemOre() {

    super(EnumGem.all().length, Material.rock);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));
    
    setHardness(3.0f);
    setResistance(5.0f);
    setStepSound(Block.soundTypeStone);
    setHarvestLevel("pickaxe", 2);

    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(Names.GEM_ORE);
  }

  @Override
  public void addOreDict() {

    OreDictionary.registerOre("oreRuby", new ItemStack(this, 1, EnumGem.RUBY.id));
    OreDictionary.registerOre("oreGarnet", new ItemStack(this, 1, EnumGem.GARNET.id));
    OreDictionary.registerOre("oreTopaz", new ItemStack(this, 1, EnumGem.TOPAZ.id));
    OreDictionary.registerOre("oreHeliodor", new ItemStack(this, 1, EnumGem.HELIODOR.id));
    OreDictionary.registerOre("orePeridot", new ItemStack(this, 1, EnumGem.PERIDOT.id));
    OreDictionary.registerOre("oreEmerald", new ItemStack(this, 1, EnumGem.EMERALD.id));
    OreDictionary.registerOre("oreAquamarine", new ItemStack(this, 1, EnumGem.AQUAMARINE.id));
    OreDictionary.registerOre("oreSapphire", new ItemStack(this, 1, EnumGem.SAPPHIRE.id));
    OreDictionary.registerOre("oreIolite", new ItemStack(this, 1, EnumGem.IOLITE.id));
    OreDictionary.registerOre("oreAmethyst", new ItemStack(this, 1, EnumGem.AMETHYST.id));
    OreDictionary.registerOre("oreMorganite", new ItemStack(this, 1, EnumGem.MORGANITE.id));
    OreDictionary.registerOre("oreOnyx", new ItemStack(this, 1, EnumGem.ONYX.id));
  }

  @Override
  public void addRecipes() {

    for (int i = 0; i < EnumGem.all().length; ++i) {
      GameRegistry.addSmelting(new ItemStack(this, 1, i), EnumGem.all()[i].getItem(), 0.5f);
    }
  }

//  @Override
//  public void addThaumcraftStuff() {
//
//     for (int i = 0; i < EnumGem.all().length; ++i) {
//     ThaumcraftApi.addSmeltingBonus(new ItemStack(this, 1, i), new ItemStack(EnumGem.all()[i]
//     .getShard().getItem(), 0, i));
//     }
//  }

  @Override
  public int damageDropped(IBlockState state) {

    return ((EnumGem) state.getValue(VARIANT)).getId();
  }
  
  @Override
  public void getSubBlocks(Item item, CreativeTabs tab, List list) {
    
    for (int i = 0; i < EnumGem.all().length; ++i) {
      list.add(new ItemStack(item, 1, EnumGem.all()[i].getId()));
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

  @Override
  public int getExpDrop(IBlockAccess world, BlockPos pos, int fortune) {

    if (this.getItemDropped(world.getBlockState(pos), RANDOM, fortune) != Item
        .getItemFromBlock(this)) {
      return 1 + RANDOM.nextInt(5);
    }
    return 0;
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {

    return SRegistry.getItem(Names.GEM_ITEM);
  }

  @Override
  public int quantityDropped(Random random) {

    return 1;
  }

  @Override
  public int quantityDroppedWithBonus(int fortune, Random random) {

    if (fortune > 0) {
      int j = random.nextInt(fortune + 2) - 1;

      if (j < 0) {
        j = 0;
      }

      return quantityDropped(random) * (j + 1);
    } else {
      return quantityDropped(random);
    }
  }
}
