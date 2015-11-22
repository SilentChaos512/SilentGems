package net.silentchaos512.gems.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.lib.Names;

public class BlockFluffyBlock extends BlockSG {

  public BlockFluffyBlock() {

    super(1, Material.cloth);

    setHardness(0.125f);
    setResistance(3.0f);
    setStepSound(Block.soundTypeCloth);
    setHarvestLevel("", 0);

    setUnlocalizedName(Names.FLUFFY_BLOCK);
  }

  @Override
  public void onFallenUpon(World world, BlockPos pos, Entity entity, float distance) {

    if (distance < 2 || world.isRemote) {
      return;
    }

    // Count the number of fluffy blocks that are stacked up.
    int stackedBlocks = 0;
    for (; world.getBlockState(pos).getBlock() == this; pos = pos.down()) {
      ++stackedBlocks;
    }
    LogHelper.debug(distance + ", " + stackedBlocks);

    // Reduce fall distance by 10 blocks per stacked block
    distance -= Math.min(10 * stackedBlocks, distance);
    // Just changing entity fall distance seems weird in 1.8, so I'll apply the damage here.
    entity.fallDistance = 0f;
    entity.fall(distance, 1f);
  }
}
