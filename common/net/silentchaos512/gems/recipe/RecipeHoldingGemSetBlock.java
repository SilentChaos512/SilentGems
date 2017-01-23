package net.silentchaos512.gems.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.item.ItemHoldingGem;
import net.silentchaos512.lib.recipe.RecipeBase;

public class RecipeHoldingGemSetBlock extends RecipeBase {

  @SuppressWarnings("deprecation")
  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack holdingGem = ItemStack.EMPTY;
    ItemStack blockStack = ItemStack.EMPTY;
    ItemStack stack;

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (!stack.isEmpty()) {
        if (stack.getItem() instanceof ItemHoldingGem) {
          if (!holdingGem.isEmpty() || stack.getItemDamage() < stack.getMaxDamage())
            return ItemStack.EMPTY;
          holdingGem = stack;
        } else if (stack.getItem() instanceof ItemBlock) {
          if (!blockStack.isEmpty())
            return ItemStack.EMPTY;
          blockStack = stack;
        }
      }
    }

    if (holdingGem.isEmpty() || blockStack.isEmpty())
      return ItemStack.EMPTY;

    ItemStack result = holdingGem.copy();
    Block block = ((ItemBlock) blockStack.getItem()).block;
    int meta = blockStack.getItemDamage();
    IBlockState state = block.getStateFromMeta(meta);
    ((ItemHoldingGem) result.getItem()).setBlockPlaced(result, state);
    result.setItemDamage(result.getMaxDamage() - 1);
    return result;
  }
}
