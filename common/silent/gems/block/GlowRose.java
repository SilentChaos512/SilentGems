package silent.gems.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import silent.gems.configuration.Config;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;

public class GlowRose extends BlockSG implements IPlantable {
  
  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

  public GlowRose() {

    super(EnumGem.all().length, Material.plants);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));
    
    this.setHardness(0.0f);
    this.setStepSound(Block.soundTypeGrass);
    this.setTickRandomly(true);
    
    float f = 0.2F;
    this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
    this.lightValue = Config.GLOW_ROSE_LIGHT_LEVEL.value;
    
    setHasGemSubtypes(true);
    setHasSubtypes(true);
    setUnlocalizedName(Names.GLOW_ROSE);
  }

  @Override
  public void addRecipes() {

    Item dyeSG = SRegistry.getItem(Names.DYE);

    // Flowers to dye.
    int k = 2;
    // 0=black
    GameRegistry.addShapelessRecipe(new ItemStack(dyeSG, k, 0), new ItemStack(this, 1,
        EnumGem.ONYX.id));
    // 1=red
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 1), new ItemStack(this, 1,
        EnumGem.RUBY.id));
    // 2=green
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 2), new ItemStack(this, 1,
        EnumGem.EMERALD.id));
    // 3=brown
    // 4=blue
    GameRegistry.addShapelessRecipe(new ItemStack(dyeSG, k, 4), new ItemStack(this, 1,
        EnumGem.SAPPHIRE.id));
    // 5=purple
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 5), new ItemStack(this, 1,
        EnumGem.AMETHYST.id));
    // 6=cyan
    // 7=light gray
    // 8=gray
    // 9=pink
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 9), new ItemStack(this, 1,
        EnumGem.MORGANITE.id));
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 10), new ItemStack(this, 1,
        EnumGem.PERIDOT.id));
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 11), new ItemStack(this, 1,
        EnumGem.HELIODOR.id));
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 12), new ItemStack(this, 1,
        EnumGem.AQUAMARINE.id));
    // 13-magenta
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, k, 14), new ItemStack(this, 1,
        EnumGem.TOPAZ.id));
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

  public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {

    return this.canPlaceBlockOn(world.getBlockState(pos.down(1)).getBlock());
  }

  @Override
  public boolean canPlaceBlockAt(World world, BlockPos pos) {

    return super.canPlaceBlockAt(world, pos)
        && world.getBlockState(pos.down()).getBlock()
            .canSustainPlant(world, pos.down(), net.minecraft.util.EnumFacing.UP, this);
  }

  protected boolean canPlaceBlockOn(Block block) {

    return block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland;
  }

  protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {

    if (!this.canBlockStay(world, pos, state)) {
      this.dropBlockAsItem(world, pos, state, 0);
      world.setBlockState(pos, Blocks.air.getDefaultState(), 3);
    }
  }

  @Override
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {

    return null;
  }

  @Override
  public IBlockState getPlant(net.minecraft.world.IBlockAccess world, BlockPos pos) {

    IBlockState state = world.getBlockState(pos);
    if (state.getBlock() != this) {
      return getDefaultState();
    }
    return state;
  }

  // @Override
  // public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
  //
  // return world.getBlockMetadata(x, y, z);
  // }

  @Override
  public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {

    return EnumPlantType.Plains;
  }

//  @Override
//  public int getRenderType() {
//
//    return 1;
//  }

  @Override
  public boolean isOpaqueCube() {

    return false;
  }
  
  @Override
  public boolean isFullCube() {
    
    return false;
  }

  @Override
  public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state,
      Block neighborBlock) {

    super.onNeighborBlockChange(world, pos, state, neighborBlock);
    this.checkAndDropBlock(world, pos, state);
  }

  // @Override
  // public boolean renderAsNormalBlock() {
  //
  // return false;
  // }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    this.checkAndDropBlock(world, pos, state);
  }
}
