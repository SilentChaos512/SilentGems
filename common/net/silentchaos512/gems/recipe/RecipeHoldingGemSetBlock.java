package net.silentchaos512.gems.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.item.ItemHoldingGem;
import net.silentchaos512.lib.recipe.IRecipeSL;
import net.silentchaos512.lib.util.StackHelper;

public class RecipeHoldingGemSetBlock implements IRecipeSL {

  @SuppressWarnings("deprecation")
  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack holdingGem = StackHelper.empty();
    ItemStack blockStack = StackHelper.empty();
    ItemStack stack;

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (StackHelper.isValid(stack)) {
        if (stack.getItem() instanceof ItemHoldingGem) {
          if (StackHelper.isValid(holdingGem) || stack.getItemDamage() < stack.getMaxDamage())
            return StackHelper.empty();
          holdingGem = stack;
        } else if (stack.getItem() instanceof ItemBlock) {
          if (StackHelper.isValid(blockStack))
            return StackHelper.empty();
          blockStack = stack;
        }
      }
    }

    if (StackHelper.isEmpty(holdingGem) || StackHelper.isEmpty(blockStack))
      return StackHelper.empty();

    ItemStack result = StackHelper.safeCopy(holdingGem);
    Block block = ((ItemBlock) blockStack.getItem()).block;
    int meta = blockStack.getItemDamage();
    IBlockState state = block.getStateFromMeta(meta);
    ((ItemHoldingGem) result.getItem()).setBlockPlaced(result, state);
    result.setItemDamage(result.getMaxDamage() - 1);
    return result;
  }
}
