package net.silentchaos512.gems.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.item.ItemBlockPlacer;
import net.silentchaos512.gems.item.ItemHoldingGem;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.util.StackHelper;

public class RecipeHoldingGemSetBlock extends RecipeBaseSL {

  @Override
  public boolean matches(InventoryCrafting inv, World world) {

    int countGem = 0;
    int countBlock = 0;

    for (ItemStack stack : getNonEmptyStacks(inv)) {
      // Empty holding gem
      Item item = stack.getItem();
      if (item instanceof ItemHoldingGem && ((ItemBlockPlacer) item).getRemainingBlocks(stack) == 0)
        ++countGem;
      // Block item
      else if (item instanceof ItemBlock)
        ++countBlock;
      // Invalid
      else
        return false;
    }

    return countGem == 1 && countBlock == 1;
  }

  @SuppressWarnings("deprecation")
  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack holdingGem = StackHelper.empty();
    ItemStack blockStack = StackHelper.empty();

    for (ItemStack stack : getNonEmptyStacks(inv)) {
      if (stack.getItem() instanceof ItemHoldingGem)
        holdingGem = stack;
      else if (stack.getItem() instanceof ItemBlock)
        blockStack = stack;
    }

    if (StackHelper.isEmpty(holdingGem) || StackHelper.isEmpty(blockStack))
      return StackHelper.empty();

    ItemStack result = StackHelper.safeCopy(holdingGem);
    Block block = ((ItemBlock) blockStack.getItem()).getBlock();
    int meta = blockStack.getItemDamage();
    IBlockState state = block.getStateFromMeta(meta);
    ItemHoldingGem itemHoldingGem = (ItemHoldingGem) result.getItem();
    itemHoldingGem.setBlockPlaced(result, state);
    itemHoldingGem.setRemainingBlocks(result, 1);
    return result;
  }
}
