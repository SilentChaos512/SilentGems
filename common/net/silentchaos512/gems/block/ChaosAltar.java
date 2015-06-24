package net.silentchaos512.gems.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.silentchaos512.gems.tile.TileChaosAltar;

public class ChaosAltar extends BlockSG implements ITileEntityProvider {

  public ChaosAltar() {
    
    super(Material.iron);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int p_149915_2_) {

    return new TileChaosAltar();
  }
}
