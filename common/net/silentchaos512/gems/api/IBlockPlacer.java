package net.silentchaos512.gems.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * An item that places blocks. Implementing on an item will make them work with tool's 'right-click to place' ability.
 * 
 * @author SilentChaos512
 *
 */
public interface IBlockPlacer {

  public IBlockState getBlockPlaced(ItemStack stack);

  public int getRemainingBlocks(ItemStack stack);
}
