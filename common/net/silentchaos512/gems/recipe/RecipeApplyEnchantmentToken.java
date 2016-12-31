package net.silentchaos512.gems.recipe;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.lib.recipe.RecipeBase;

public class RecipeApplyEnchantmentToken extends RecipeBase {

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack tool = ItemStack.EMPTY;
    List<ItemStack> tokens = Lists.newArrayList();

    // Find items
    ItemStack stack;
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (!stack.isEmpty()) {
        if (stack.getItem() == ModItems.enchantmentToken) {
          tokens.add(stack);
        } else {
          if (!tool.isEmpty()) {
            return ItemStack.EMPTY;
          }
          tool = stack;
        }
      }
    }

    // Found correct items?
    if (tool.isEmpty() || tokens.isEmpty()) {
      return ItemStack.EMPTY;
    }

    // Apply tokens to tools.
    ItemStack result = tool.copy();
    for (ItemStack token : tokens) {
      if (!ModItems.enchantmentToken.applyTokenToTool(token, result)) {
        return ItemStack.EMPTY;
      }
    }

    return result;
  }
}
