package net.silentchaos512.gems.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public interface IBlockPlacer {

  public IBlockState getBlockPlaced(ItemStack stack);

  public int getRemainingBlocks(ItemStack stack);
}
