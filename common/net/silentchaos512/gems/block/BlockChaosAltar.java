package net.silentchaos512.gems.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.lib.block.BlockContainerSL;

public class BlockChaosAltar extends BlockContainerSL {

  public static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.0f, 0.0f, 0.0f, 1.0f, 0.75f,
      1.0f);

  public BlockChaosAltar() {

    super(1, SilentGems.MODID, Names.CHAOS_ALTAR, Material.IRON);
    setHardness(12.0f);
    setResistance(6000.0f);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {

    return new TileChaosAltar();
  }

  @Override
  public void addRecipes() {

    ItemStack result = new ItemStack(this);
    ItemStack chaos = ModItems.craftingMaterial.chaosEssenceEnriched;
    GameRegistry.addRecipe(new ShapedOreRecipe(result, "rer", "dod", "ooo", 'e', chaos, 'r',
        "dustRedstone", 'o', Blocks.OBSIDIAN, 'd', "gemDiamond"));
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    if (world.isRemote) {
      return true;
    }

    TileEntity tile = world.getTileEntity(pos);

    if (tile instanceof TileChaosAltar) {
      player.openGui(SilentGems.instance, GuiHandlerSilentGems.ID_ALTAR, world, pos.getX(),
          pos.getY(), pos.getZ());
    }

    return true;
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    TileEntity tileAltar = world.getTileEntity(pos);

    if (tileAltar != null && tileAltar instanceof TileChaosAltar) {
      InventoryHelper.dropInventoryItems(world, pos, (TileChaosAltar) tileAltar);
      world.updateComparatorOutputLevel(pos, this);
    }

    super.breakBlock(world, pos, state);
  }

  @Override
  public boolean hasComparatorInputOverride(IBlockState state) {

    return true;
  }

  @Override
  public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {

    TileEntity tile = world.getTileEntity(pos);
    if (tile != null && tile instanceof TileChaosAltar) {
      TileChaosAltar altar = (TileChaosAltar) tile;
      float storedRatio = (float) altar.getCharge() / altar.getMaxCharge();
      return (int) (15 * storedRatio);
    }
    return 0;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {

    return false;
  }

  @Override
  public boolean isBlockNormalCube(IBlockState state) {

    return false;
  }

  @Override
  public boolean isFullCube(IBlockState state) {

    return false;
  }

  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {

    return EnumBlockRenderType.MODEL;
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return BOUNDING_BOX;
  }
}
