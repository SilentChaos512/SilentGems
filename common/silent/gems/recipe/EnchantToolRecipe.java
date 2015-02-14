package silent.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import silent.gems.core.util.InventoryHelper;
import silent.gems.item.EnchantToken;

public class EnchantToolRecipe implements IRecipe {

  @Override
  public boolean matches(InventoryCrafting inventorycrafting, World world) {

    int numTools = 0;
    int numTokens = 0;

    ItemStack stack, token = null, tool = null;

    // Count valid ingredients and look for invalid
    for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
      stack = inventorycrafting.getStackInSlot(i);
      if (stack != null) {
        if (InventoryHelper.isGemTool(stack)) {
          ++numTools;
          tool = stack;
        } else if (stack.getItem() instanceof EnchantToken) {
          ++numTokens;
          token = stack;
        } else {
          // Invalid item
          return false;
        }
      }
    }

    return numTools <= 1 && numTokens <= 1 && token != null && tool != null
        && EnchantToken.capApplyTokenToTool(token, tool);
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {

    ItemStack tool = null, token = null, s = null;

    // Find ingredients.
    for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
      s = inventorycrafting.getStackInSlot(i);
      if (s != null) {
        if (InventoryHelper.isGemTool(s)) {
          tool = s;
        } else if (s.getItem() instanceof EnchantToken) {
          token = s;
        }
      }
    }

    if (tool == null || token == null) {
      return null;
    }

    ItemStack result = tool.copy();

    if (EnchantToken.capApplyTokenToTool(token, tool)) {
      EnchantToken.enchantTool(token, result);
    }

    return result;
  }

  @Override
  public int getRecipeSize() {

    // What's this?
    return 2;
  }

  @Override
  public ItemStack getRecipeOutput() {

    // What's this?
    return null;
  }

  @Override
  public ItemStack[] getRemainingItems(InventoryCrafting inv) {

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      ItemStack stack = inv.getStackInSlot(i);
      if (stack != null) {
        --stack.stackSize;
        if (stack.stackSize <= 0) {
          stack = null;
        }
        inv.setInventorySlotContents(i, stack);
      }
    }
    return new ItemStack[] {};
  }
}
