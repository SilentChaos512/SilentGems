package net.silentchaos512.gems.recipe;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ItemEnchantmentToken;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.util.StackHelper;

public class RecipeApplyEnchantmentToken extends RecipeBaseSL {

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack tool = StackHelper.empty();
    List<ItemStack> tokens = Lists.newArrayList();

    for (ItemStack stack : getNonEmptyStacks(inv)) {
      if (stack.getItem() instanceof ItemEnchantmentToken) {
        tokens.add(stack);
      } else {
        if (StackHelper.isValid(tool)) {
          return StackHelper.empty();
        }
        tool = stack;
      }
    }

    // Found correct items?
    if (StackHelper.isEmpty(tool) || tokens.isEmpty()) {
      return StackHelper.empty();
    }

    // Apply tokens to tools.
    ItemStack result = StackHelper.safeCopy(tool);
    for (ItemStack token : tokens) {
      if (!ModItems.enchantmentToken.applyTokenToTool(token, result)) {
        return StackHelper.empty();
      }
    }

    return result;
  }
}
