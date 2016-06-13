package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.lib.recipe.RecipeBase;

public class RecipeDecorateArmor extends RecipeBase {

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    int i;
    int toolRow = 0;
    int toolCol = 0;
    ItemStack stack;
    ItemStack tool = null;
    int repairValue = 0;

    // Find armor position
    for (int row = 0; row < inv.getWidth(); ++row) {
      for (int col = 0; col < inv.getHeight(); ++col) {
        stack = inv.getStackInRowAndColumn(row, col);
        if (stack != null && stack.getItem() instanceof IArmor) {
          tool = stack;
          toolRow = row;
          toolCol = col;
        }
      }
    }

    // Found armor piece?
    if (tool == null) {
      return null;
    }

    // Check adjacent materials
    ItemStack west = inv.getStackInRowAndColumn(toolRow - 1, toolCol);
    ItemStack north = inv.getStackInRowAndColumn(toolRow, toolCol - 1);
    ItemStack east = inv.getStackInRowAndColumn(toolRow + 1, toolCol);
    ItemStack south = inv.getStackInRowAndColumn(toolRow, toolCol + 1);
    int validPartCount = (west == null ? 0 : 1) + (north == null ? 0 : 1) + (east == null ? 0 : 1)
        + (south == null ? 0 : 1);

    if (!checkIsDecorationMaterial(west) || !checkIsDecorationMaterial(north)
        || !checkIsDecorationMaterial(east) || !checkIsDecorationMaterial(south)) {
      return null;
    }

    // Check for other pieces (invalid for armor) and get all repair values.
    EnumMaterialTier toolTier = ArmorHelper.getArmorTier(tool);
    ToolPart part;
    int partsFound = 0;
    for (i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null && !(stack.getItem() instanceof IArmor)) {
        part = ToolPartRegistry.fromStack(stack);
        // Invalid part or not a part?
        if (part == null || !(part instanceof ToolPartMain)) {
          return null;
        }
        // Found a part.
        ++partsFound;
        // Add to total repair value.
        repairValue += part.getRepairAmount(tool);
      }
    }

    // Make sure we got no other parts.
    if (partsFound != validPartCount) {
      return null;
    }

    ItemStack result = ArmorHelper.decorateArmor(tool, west, north, east, south);

    // Repair.
    result.attemptDamageItem(-repairValue, SilentGems.random);

    // Recalculate stats.
    ArmorHelper.recalculateStats(result);
    ArmorHelper.incrementStatRedecorated(result, 1);

    return result;
  }

  private boolean checkIsDecorationMaterial(ItemStack stack) {

    if (stack == null) {
      return true;
    }

    ToolPart part = ToolPartRegistry.fromStack(stack);
    return part != null && part instanceof ToolPartMain;
  }
}
