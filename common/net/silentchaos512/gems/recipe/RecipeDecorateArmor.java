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
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.StackHelper;

public class RecipeDecorateArmor extends RecipeBaseSL {

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    int i;
    int toolRow = 0;
    int toolCol = 0;
    ItemStack stack;
    ItemStack tool = StackHelper.empty();
    int repairValue = 0;

    // Find armor position
    for (int row = 0; row < inv.getWidth(); ++row) {
      for (int col = 0; col < inv.getHeight(); ++col) {
        stack = inv.getStackInRowAndColumn(row, col);
        if (StackHelper.isValid(stack) && stack.getItem() instanceof IArmor) {
          tool = stack;
          toolRow = row;
          toolCol = col;
        }
      }
    }

    // Found armor piece?
    if (StackHelper.isEmpty(tool)) {
      return StackHelper.empty();
    }

    // Check adjacent materials
    ItemStack west = inv.getStackInRowAndColumn(toolRow - 1, toolCol);
    ItemStack north = inv.getStackInRowAndColumn(toolRow, toolCol - 1);
    ItemStack east = inv.getStackInRowAndColumn(toolRow + 1, toolCol);
    ItemStack south = inv.getStackInRowAndColumn(toolRow, toolCol + 1);
    int validPartCount = (StackHelper.isEmpty(west) ? 0 : 1) + (StackHelper.isEmpty(north) ? 0 : 1)
        + (StackHelper.isEmpty(east) ? 0 : 1) + (StackHelper.isEmpty(south) ? 0 : 1);

    if (!checkIsDecorationMaterial(west) || !checkIsDecorationMaterial(north)
        || !checkIsDecorationMaterial(east) || !checkIsDecorationMaterial(south)) {
      return StackHelper.empty();
    }

    // Check for other pieces (invalid for armor) and get all repair values.
    EnumMaterialTier toolTier = ArmorHelper.getArmorTier(tool);
    ToolPart part;
    int partsFound = 0;
    for (i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (StackHelper.isValid(stack) && !(stack.getItem() instanceof IArmor)) {
        part = ToolPartRegistry.fromDecoStack(stack);
        // Invalid part or not a part?
        if (part == null || !(part instanceof ToolPartMain)) {
          return StackHelper.empty();
        }
        // Found a part.
        ++partsFound;
        // Add to total repair value.
        repairValue += part.getRepairAmount(tool, stack);
      }
    }

    // Make sure we got no other parts.
    if (partsFound != validPartCount) {
      return StackHelper.empty();
    }

    ItemStack result = ArmorHelper.decorateArmor(tool, west, north, east, south);

    // Repair.
    ItemHelper.attemptDamageItem(result, -repairValue, SilentGems.random);

    // Recalculate stats.
    ArmorHelper.recalculateStats(result);
    ArmorHelper.incrementStatRedecorated(result, 1);

    // Change the UUID so that rendering cache updates immediately for recipe output.
    result.getTagCompound().removeTag(ToolHelper.NBT_UUID + "Most");
    result.getTagCompound().removeTag(ToolHelper.NBT_UUID + "Least");
    ToolHelper.getUUID(result);

    return result;
  }

  private boolean checkIsDecorationMaterial(ItemStack stack) {

    if (StackHelper.isEmpty(stack)) {
      return true;
    }

    ToolPart part = ToolPartRegistry.fromDecoStack(stack);
    return part != null && part instanceof ToolPartMain;
  }
}
