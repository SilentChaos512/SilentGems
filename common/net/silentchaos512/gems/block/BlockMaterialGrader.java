package net.silentchaos512.gems.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.gui.GuiHandlerSilentGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileMaterialGrader;
import net.silentchaos512.lib.block.BlockContainerSL;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.wit.api.IWitHudInfo;

public class BlockMaterialGrader extends BlockContainerSL implements IWitHudInfo {

  public static final PropertyDirection FACING = PropertyDirection.create("facing",
      EnumFacing.Plane.HORIZONTAL);
  public static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.4, 1.0);

  public BlockMaterialGrader() {

    super(1, SilentGems.MODID, Names.MATERIAL_GRADER, Material.IRON);
    setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    setHardness(3.0f);
    setResistance(100.0f);
    this.isBlockContainer = true;
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    // @formatter:off
    recipes.addShapedOre("material_grader", new ItemStack(this),
        " m ", "i i", "gig",
        'm', ModItems.craftingMaterial.magnifyingGlass,
        'i', ModItems.craftingMaterial.chaosIron,
        'g', "ingotGold");
    // @formatter:on
  }

  public EnumBlockRenderType getRenderType(IBlockState state) {

    return EnumBlockRenderType.MODEL;
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {

    return new TileMaterialGrader();
  }

  @Override
  protected boolean clOnBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
      EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    if (world.isRemote) {
      return true;
    }

    TileEntity tile = world.getTileEntity(pos);
    if (tile instanceof TileMaterialGrader) {
      player.openGui(SilentGems.instance, GuiHandlerSilentGems.ID_MATERIAL_GRADER, world,
          pos.getX(), pos.getY(), pos.getZ());
    }

    return true;
  }

  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX,
      float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {

    IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer);
    return state.withProperty(FACING, placer.getHorizontalFacing());
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
      ItemStack stack) {

    super.onBlockPlacedBy(world, pos, state, placer, stack);
    EnumFacing side = placer.getHorizontalFacing().getOpposite();
    world.setBlockState(pos, state.withProperty(FACING, side), 2);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, new IProperty[] { FACING });
  }

  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {

    return BOUNDING_BOX;
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return BOUNDING_BOX;
  }

  @Override
  public List<String> getWitLines(IBlockState state, BlockPos pos, EntityPlayer player,
      boolean advanced) {

    // TileEntity tileEntity = player.worldObj.getTileEntity(pos);
    // if (tileEntity != null && tileEntity instanceof TileMaterialGrader) {
    // TileMaterialGrader tile = (TileMaterialGrader) tileEntity;
    // return Lists.newArrayList("Charge: " + tile.getField(0));
    // }
    return null;
  }

  @Override
  public boolean isTranslucent(IBlockState state) {

    return false;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {

    return false;
  }

  @Override
  public boolean isFullBlock(IBlockState state) {

    return false;
  }

  @Override
  public boolean isFullCube(IBlockState state) {

    return false;
  }

  @Override
  public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos,
      EnumFacing face) {

    return face == EnumFacing.DOWN;
  }
}
