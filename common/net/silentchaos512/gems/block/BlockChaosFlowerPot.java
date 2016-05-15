package net.silentchaos512.gems.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosFlowerPot;
import net.silentchaos512.lib.block.BlockSL;

public class BlockChaosFlowerPot extends BlockSL implements ITileEntityProvider {

  public static final AxisAlignedBB FLOWER_POT_AABB = new AxisAlignedBB(0.3125, 0.0, 0.3125, 0.6875,
      0.375, 0.6875);

  public BlockChaosFlowerPot() {

    super(1, SilentGems.MOD_ID, Names.CHAOS_FLOWER_POT, Material.CIRCUITS);
    setHardness(1.0f);
    setResistance(30.0f);
    lightValue = 2;
  }

  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
      EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX,
      float hitY, float hitZ) {

    if (heldItem != null && heldItem.getItem() instanceof ItemBlock) {
      TileChaosFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);

      if (tileentityflowerpot == null) {
        return false;
      } else if (tileentityflowerpot.getFlowerPotItem() != null) {
        return false;
      } else {
        Block block = Block.getBlockFromItem(heldItem.getItem());

        if (block != ModBlocks.glowRose) {
          return false;
        } else {
          tileentityflowerpot.setFlowerPotData(heldItem.getItem(), heldItem.getMetadata());
          tileentityflowerpot.markDirty();
          worldIn.notifyBlockUpdate(pos, state, state, 3);
          worldIn.checkLight(pos);
          playerIn.addStat(StatList.FLOWER_POTTED);

          if (!playerIn.capabilities.isCreativeMode) {
            --heldItem.stackSize;
          }

          return true;
        }
      }
    } else {
      return false;
    }
  }

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    TileChaosFlowerPot tile = getTileEntity(world, pos);
    return tile != null && tile.getFlowerItemStack() != null ? 15 : lightValue;
  }

  @Override
  public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {

    return 0;
  }

  public TileChaosFlowerPot getTileEntity(IBlockAccess world, BlockPos pos) {

    TileEntity tile = world.getTileEntity(pos);
    if (tile == null || !(tile instanceof TileChaosFlowerPot)) {
      return null;
    }
    return (TileChaosFlowerPot) tile;
  }

  @Override
  public void addRecipes() {

    GameRegistry.addShapedRecipe(new ItemStack(this), "c", "f", 'c',
        ModItems.craftingMaterial.chaosEssenceEnriched, 'f', Items.FLOWER_POT);
  }

  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return FLOWER_POT_AABB;
  }

  public boolean isOpaqueCube(IBlockState state) {

    return false;
  }

  public EnumBlockRenderType getRenderType(IBlockState state) {

    return EnumBlockRenderType.MODEL;
  }

  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT;
  }

  public boolean isFullCube(IBlockState state) {

    return false;
  }

  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {

    return super.canPlaceBlockAt(worldIn, pos)
        && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
  }

  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state,
      Block neighborBlock) {

    if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP)) {
      this.dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    }
  }

  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state,
      EntityPlayer player) {

    super.onBlockHarvested(worldIn, pos, state, player);

    if (player.capabilities.isCreativeMode) {
      TileChaosFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);

      if (tileentityflowerpot != null) {
        tileentityflowerpot.setFlowerPotData((Item) null, 0);
      }
    }
  }

  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state,
      int fortune) {

    List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
    TileChaosFlowerPot te = getTileEntity(world, pos);
    if (te != null && te.getFlowerPotItem() != null) {
      ret.add(te.getFlowerItemStack());
    }
    return ret;
  }

  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
      boolean willHarvest) {

    if (willHarvest)
      return true; // If it will harvest, delay deletion of the block until after getDrops
    return super.removedByPlayer(state, world, pos, player, willHarvest);
  }

  @Override
  public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state,
      TileEntity te, ItemStack tool) {

    super.harvestBlock(world, player, pos, state, te, tool);
    world.setBlockToAir(pos);
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {

    return new TileChaosFlowerPot();
  }
}
