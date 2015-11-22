package net.silentchaos512.gems.recipe;

import java.util.ArrayList;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.silentchaos512.gems.core.util.InventoryHelper;
import net.silentchaos512.gems.item.ModItems;

public class RecipeToolUpgrade implements IRecipe {

  @Override
  public boolean matches(InventoryCrafting inv, World world) {

    return getCraftingResult(inv) != null;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack tool = null;
    ArrayList<ItemStack> upgrades = new ArrayList<ItemStack>();
    ItemStack stack;
    Item item;
    int meta;

    // Find tool and upgrades
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        item = stack.getItem();
        meta = stack.getItemDamage();
        if (InventoryHelper.isGemTool(stack)) {
          // Tool
          if (tool != null) {
            return null;
          }
          tool = stack;
        } else if (item == ModItems.toolUpgrade) {
          // Upgrade
          upgrades.add(stack);
        } else {
          // Invalid
          return null;
        }
      }
    }

    if (tool != null && !upgrades.isEmpty()) {
      // Apply
      ItemStack result = tool.copy();
      for (ItemStack upgrade : upgrades) {
        result = ModItems.toolUpgrade.applyToTool(result, upgrade);
      }
      return result;
    }

    return null;
  }

  @Override
  public int getRecipeSize() {

    return 9;
  }

  @Override
  public ItemStack getRecipeOutput() {

    // TODO Auto-generated method stub
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
