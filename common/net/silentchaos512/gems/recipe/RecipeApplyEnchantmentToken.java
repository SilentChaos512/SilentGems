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

    ItemStack tool = null;
    List<ItemStack> tokens = Lists.newArrayList();

    // Find items
    ItemStack stack;
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        if (stack.getItem() == ModItems.enchantmentToken) {
          tokens.add(stack);
        } else {
          if (tool != null) {
            return null;
          }
          tool = stack;
        }
      }
    }

    // Found correct items?
    if (tool == null || tokens.isEmpty()) {
      return null;
    }

    // Apply tokens to tools.
    ItemStack result = tool.copy();
    for (ItemStack token : tokens) {
      if (!ModItems.enchantmentToken.applyTokenToTool(token, result)) {
        return null;
      }
    }

    return result;
  }
}
