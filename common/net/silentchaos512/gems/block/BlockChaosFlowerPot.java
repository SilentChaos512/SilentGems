package net.silentchaos512.gems.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
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
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosFlowerPot;
import net.silentchaos512.lib.block.BlockSL;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.StackHelper;

public class BlockChaosFlowerPot extends BlockSL implements ITileEntityProvider {

  public static final AxisAlignedBB FLOWER_POT_AABB = new AxisAlignedBB(0.3125, 0.0, 0.3125, 0.6875,
      0.375, 0.6875);

  public IRecipe recipe;

  public BlockChaosFlowerPot() {

    super(1, SilentGems.MODID, Names.CHAOS_FLOWER_POT, Material.CIRCUITS);
    setHardness(1.0f);
    setResistance(30.0f);
    lightValue = 2;
  }

  @Override
  protected boolean clOnBlockActivated(World worldIn, BlockPos pos, IBlockState state,
      EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

    ItemStack heldItem = playerIn.getHeldItem(hand);

    if (StackHelper.isValid(heldItem) && heldItem.getItem() instanceof ItemBlock) {
      TileChaosFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);

      if (tileentityflowerpot == null) {
        return false;
      } else if (StackHelper.isValid(tileentityflowerpot.getFlowerItemStack())) {
        return false;
      } else {
        Block block = Block.getBlockFromItem(heldItem.getItem());

        if (block != ModBlocks.glowRose) {
          return false;
        } else {
          ItemStack flower = new ItemStack(heldItem.getItem(), 1, heldItem.getItemDamage());
          tileentityflowerpot.setFlowerItemStack(flower);
          tileentityflowerpot.markDirty();
          worldIn.notifyBlockUpdate(pos, state, state, 3);
          worldIn.checkLight(pos);
          playerIn.addStat(StatList.FLOWER_POTTED);

          if (!playerIn.capabilities.isCreativeMode) {
            StackHelper.shrink(heldItem, 1);
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
    return tile != null && StackHelper.isValid(tile.getFlowerItemStack()) ? 15 : lightValue;
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
  public void addRecipes(RecipeMaker recipes) {

    recipe = recipes.addShaped("chaos_flower_pot", new ItemStack(this), "c", "f", 'c',
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

  @Override
  protected void clOnNeighborChanged(IBlockState state, World world, BlockPos pos, Block block) {

    if (!world.getBlockState(pos.down()).isSideSolid(world, pos, EnumFacing.UP)) {
      this.dropBlockAsItem(world, pos, state, 0);
      world.setBlockToAir(pos);
    }
  }

  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state,
      EntityPlayer player) {

    super.onBlockHarvested(worldIn, pos, state, player);

    if (player.capabilities.isCreativeMode) {
      TileChaosFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);

//      if (tileentityflowerpot != null) {
//        tileentityflowerpot.setFlowerItemStack(ItemStack.EMPTY);
//      }
    }
  }

  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state,
      int fortune) {

    List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
    TileChaosFlowerPot te = getTileEntity(world, pos);
    if (te != null && StackHelper.isValid(te.getFlowerItemStack())) {
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
