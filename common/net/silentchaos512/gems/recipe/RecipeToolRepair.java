package net.silentchaos512.gems.recipe;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.silentchaos512.gems.item.ModItems;

public class RecipeToolRepair implements IRecipe {

  @Override
  public boolean matches(InventoryCrafting inv, World world) {

    return getCraftingResult(inv) != null;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack brokenTool = null;
    ItemStack stack;
    Item item;

    // Search for broken tool.
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        item = stack.getItem();
        if (item == ModItems.brokenTool) {
          if (brokenTool != null) {
            return null;
          } else {
            brokenTool = stack;
          }
        }
      }
    }

    // Get tool from broken tool.
    if (brokenTool == null) {
      return null;
    }
    ItemStack tool = ModItems.brokenTool.getTool(brokenTool);
    if (tool == null) {
      return null;
    }

    // Check materials.
    List materials = Lists.newArrayList();
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        if (ModItems.brokenTool.isValidRepairMaterial(tool, stack)) {
          materials.add(stack);
        } else if (stack != brokenTool) {
          return null;
        }
      }
    }

    if (materials.isEmpty()) {
      return null;
    }

    int maxDamage = tool.getItem().getMaxDamage(tool);
    int repairAmount = ModItems.brokenTool.getTotalRepairValue(tool, materials);
    tool.setItemDamage(maxDamage - repairAmount);

    return tool;
  }

  @Override
  public int getRecipeSize() {

    return 9;
  }

  @Override
  public ItemStack getRecipeOutput() {

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