package net.silentchaos512.gems.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosPylon;

public class BlockChaosPylon extends BlockSG implements ITileEntityProvider {

  public BlockChaosPylon() {

    super(Material.iron);
    this.setUnlocalizedName(Names.CHAOS_PYLON);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {

    return new TileChaosPylon();
  }
}
