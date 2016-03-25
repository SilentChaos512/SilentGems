package net.silentchaos512.gems.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosNode;
import net.silentchaos512.lib.block.BlockSL;

public class BlockChaosNode extends BlockSL implements ITileEntityProvider {

  public BlockChaosNode() {

    super(1, SilentGems.MOD_ID, Names.CHAOS_NODE, Material.barrier);
    setBlockUnbreakable();
    setResistance(6000000.0F);
    translucent = true;

    SilentGems.instance.registry.registerTileEntity(TileChaosNode.class, Names.CHAOS_NODE);
  }

  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {

    return EnumBlockRenderType.INVISIBLE;
  }

  @Override
  public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {

    return NULL_AABB;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {

    return false;
  }

  @Override
  public boolean isFullCube(IBlockState state) {

    return false;
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {

    return new TileChaosNode();
  }
}
