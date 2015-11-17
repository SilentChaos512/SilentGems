package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.lib.Names;

public class BlockFluffyBlock extends BlockSG {

  public BlockFluffyBlock() {

    super(Material.cloth);

    setHardness(0.8f);
    setResistance(3.0f);
    setStepSound(Block.soundTypeCloth);

    setUnlocalizedName(Names.FLUFFY_BLOCK);
  }

  @Override
  public void onFallenUpon(World world, int x, int y, int z, Entity entity, float distance) {

    // Count the number of fluffy blocks that are stacked up.
    int stackedBlocks = 0;
    for (int depth = 0; y - depth > 0 && world.getBlock(x, y - depth, z) == this; ++depth) {
      ++stackedBlocks;
    }

    // Reduce fall distance by 10 blocks per stacked block
    entity.fallDistance -= Math.min(10 * stackedBlocks, distance);
  }
}
