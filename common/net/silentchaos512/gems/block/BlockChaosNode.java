package net.silentchaos512.gems.block;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosProvider;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosNode;
import net.silentchaos512.lib.block.BlockSL;
import net.silentchaos512.wit.api.IWitHudInfo;

public class BlockChaosNode extends BlockSL implements ITileEntityProvider, IWitHudInfo {

  public static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);

  public BlockChaosNode() {

    super(1, SilentGems.MOD_ID, Names.CHAOS_NODE, Material.BARRIER);
    setBlockUnbreakable();
    setResistance(6000000.0F);
    translucent = true;
  }

  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {

    return EnumBlockRenderType.INVISIBLE;
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return BOUNDING_BOX;
  }

  @Override
  public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {

    return BOUNDING_BOX;
  }

  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {

    return null;
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

  @Override
  public List<String> getWitLines(IBlockState state, BlockPos pos, EntityPlayer player,
      boolean advanced) {

//    TileEntity tileEntity = player.worldObj.getTileEntity(pos);
//    if (tileEntity != null && tileEntity instanceof TileChaosNode) {
//      TileChaosNode tile = (TileChaosNode) tileEntity;
//      String str = "Chaos: %,d / %,d";
//      str = String.format(str, tile.getCharge(), tile.getMaxCharge());
//      return Lists.newArrayList(str);
//    }
//    return null;
    return null;
  }
}
