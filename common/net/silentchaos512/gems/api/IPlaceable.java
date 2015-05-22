package net.silentchaos512.gems.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface IPlaceable {

  public Block getBlockPlaced(ItemStack stack);
}
