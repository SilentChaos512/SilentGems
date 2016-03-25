package net.silentchaos512.gems.recipe;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartGrip;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.api.tool.part.ToolPartRod;
import net.silentchaos512.gems.api.tool.part.ToolPartTip;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.recipe.RecipeBase;
import net.silentchaos512.lib.util.LogHelper;

public class RecipeDecorateTool extends RecipeBase {

  LogHelper log = SilentGems.instance.logHelper;

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    int i;
    int toolRow = 0;
    int toolCol = 0;
    ItemStack stack;
    ItemStack tool = null;
    int repairValue = 0;

    // Find tool position
    for (int row = 0; row < inv.getWidth(); ++row) {
      for (int col = 0; col < inv.getHeight(); ++col) {
        stack = inv.getStackInRowAndColumn(row, col);
        if (stack != null && stack.getItem() instanceof ITool) {
          tool = stack;
          toolRow = row;
          toolCol = col;
        }
      }
    }

    // Found a tool?
    if (tool == null) {
      return null;
    }

    // Check adjacent materials
    ItemStack west = inv.getStackInRowAndColumn(toolRow - 1, toolCol);
    ItemStack north = inv.getStackInRowAndColumn(toolRow, toolCol - 1);
    ItemStack east = inv.getStackInRowAndColumn(toolRow + 1, toolCol);
    ItemStack south = inv.getStackInRowAndColumn(toolRow, toolCol + 1);

//    log.debug(west, north, east, south);

    if (!checkIsDecorationMaterial(west) || !checkIsDecorationMaterial(north)
        || !checkIsDecorationMaterial(east) || !checkIsDecorationMaterial(south)) {
      log.debug("deco material match fail");
      return null;
    }

    // Check other materials and get all repair values.
    List<ItemStack> otherMats = Lists.newArrayList();
    EnumMaterialTier toolTier = ToolHelper.getToolTier(tool);
    for (i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null && !(stack.getItem() instanceof ITool)) {
        ToolPart part = ToolPartRegistry.fromStack(stack);
        // Invalid part or not a part?
        if (part == null && part instanceof ToolPartMain) {
          return null;
        }
        // Valid for tool tier?
        if (!part.validForToolOfTier(toolTier) && !(part instanceof ToolPartMain)) {
          return null;
        }
        repairValue += part.getRepairAmount(tool);
        otherMats.add(stack);
      }
    }

    ItemStack result = tool.copy();

    result = ToolHelper.decorateTool(tool, west, north, east, south);

    // Other materials
    for (ItemStack other : otherMats) {
      ToolPart part = ToolPartRegistry.fromStack(other);
      EnumMaterialGrade grade = EnumMaterialGrade.fromStack(other);
      if (result != null && part instanceof ToolPartRod) {
        ToolHelper.setRenderPart(result, part, grade, EnumPartPosition.ROD);
      } else if (part instanceof ToolPartGrip) {
        ToolHelper.setRenderPart(result, part, grade, EnumPartPosition.ROD_WOOL);
      } else if (part instanceof ToolPartTip) {
        ToolHelper.setRenderPart(result, part, grade, EnumPartPosition.TIP);
      }
    }

    // Repair.
    log.debug(repairValue);
    result.attemptDamageItem(-repairValue, SilentGems.instance.random);

    // Recalculate stats.
    ToolHelper.recalculateStats(result);

    return result;
  }

  private boolean checkIsDecorationMaterial(ItemStack stack) {

    if (stack == null) {
      return true;
    }

    ToolPart part = ToolPartRegistry.fromStack(stack);
    return part != null;
  }

  private int getRepairValue(ItemStack tool, ItemStack material) {

    if (tool == null || material == null) {
      return 0;
    }
    ToolPart part = ToolPartRegistry.fromStack(material);
    if (part == null) {
      return 0;
    }
    return part.getRepairAmount(tool);
  }
}
