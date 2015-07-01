package net.silentchaos512.gems.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.silentchaos512.gems.core.util.InventoryHelper;
import net.silentchaos512.gems.item.EnchantToken;

public class EnchantToolRecipe implements IRecipe {
  
  private ItemStack getResult(ItemStack tool, List<ItemStack> tokens) {
    
    if (tool == null || tokens == null || tokens.isEmpty()) {
      return null;
    }
    
    ItemStack result = tool.copy();
    
    for (ItemStack token : tokens) {
      if (EnchantToken.capApplyTokenToTool(token, result)) {
        EnchantToken.enchantTool(token, result);
      } else {
        return null;
      }
    }
    
    return result;
  }

  @Override
  public boolean matches(InventoryCrafting inventorycrafting, World world) {

    ItemStack stack = null;
    ItemStack tool = null;
    ArrayList<ItemStack> tokens = new ArrayList<ItemStack>();

    // Count valid ingredients and look for invalid
    for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
      stack = inventorycrafting.getStackInSlot(i);
      if (stack != null) {
        if (InventoryHelper.isTool(stack) || stack.getItem() instanceof ItemArmor) {
          tool = stack;
        } else if (stack.getItem() instanceof EnchantToken) {
          tokens.add(stack);
        } else {
          // Invalid item
          return false;
        }
      }
    }
    
    return this.getResult(tool, tokens) != null;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {

    ItemStack stack = null;
    ItemStack tool = null;
    ArrayList<ItemStack> tokens = new ArrayList<ItemStack>();

    // Find ingredients.
    for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
      stack = inventorycrafting.getStackInSlot(i);
      if (stack != null) {
        if (InventoryHelper.isTool(stack) || stack.getItem() instanceof ItemArmor) {
          tool = stack;
        } else if (stack.getItem() instanceof EnchantToken) {
          tokens.add(stack);
        }
      }
    }

    return this.getResult(tool, tokens);
  }

  @Override
  public int getRecipeSize() {

    return 9;
  }

  @Override
  public ItemStack getRecipeOutput() {

    return null;
  }

}
