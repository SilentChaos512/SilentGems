package net.silentchaos512.gems.recipe;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IAmmoTool;
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
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.recipe.IRecipeSL;
import net.silentchaos512.lib.util.StackHelper;

public class RecipeDecorateTool implements IRecipeSL {

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    int i;
    int toolRow = 0;
    int toolCol = 0;
    ItemStack stack;
    ItemStack tool = StackHelper.empty();
    int repairValue = 0;
    int ammoValue = 0;

    // Find tool position
    for (int row = 0; row < inv.getWidth(); ++row) {
      for (int col = 0; col < inv.getHeight(); ++col) {
        stack = inv.getStackInRowAndColumn(row, col);
        if (StackHelper.isValid(stack) && stack.getItem() instanceof ITool) {
          tool = stack;
          toolRow = row;
          toolCol = col;
        }
      }
    }

    // Found a tool?
    if (StackHelper.isEmpty(tool)) {
      return StackHelper.empty();
    }

    // Check adjacent materials
    ItemStack west = inv.getStackInRowAndColumn(toolRow - 1, toolCol);
    ItemStack north = inv.getStackInRowAndColumn(toolRow, toolCol - 1);
    ItemStack east = inv.getStackInRowAndColumn(toolRow + 1, toolCol);
    ItemStack south = inv.getStackInRowAndColumn(toolRow, toolCol + 1);

    if (!checkIsDecorationMaterial(west) || !checkIsDecorationMaterial(north)
        || !checkIsDecorationMaterial(east) || !checkIsDecorationMaterial(south)) {
      return StackHelper.empty();
    }

    // Check other materials and get all repair values.
    List<ItemStack> otherMats = Lists.newArrayList();
    EnumMaterialTier toolTier = ToolHelper.getToolTier(tool);
    for (i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (StackHelper.isValid(stack) && !(stack.getItem() instanceof ITool)) {
        ToolPart part = ToolPartRegistry.fromStack(stack);
        // Invalid part or not a part?
        if (part == null) {
          return StackHelper.empty();
        }
        // Valid for tool tier?
        if (!part.validForToolOfTier(toolTier) && !(part instanceof ToolPartMain)) {
          return StackHelper.empty();
        }
        int repairAmount = part.getRepairAmount(tool, stack);
        if (repairAmount > 0) {
          repairValue += repairAmount;
          ++ammoValue;
        }
        otherMats.add(stack);
      }
    }

    ItemStack result = StackHelper.safeCopy(tool);

    result = ToolHelper.decorateTool(tool, west, north, east, south);

    // Other materials
    for (ItemStack other : otherMats) {
      ToolPart part = ToolPartRegistry.fromStack(other);
      EnumMaterialGrade grade = EnumMaterialGrade.fromStack(other);
      if (StackHelper.isValid(result) && part instanceof ToolPartRod) {
        ToolHelper.setRenderPart(result, part, grade, EnumPartPosition.ROD);
      } else if (part instanceof ToolPartGrip) {
        ToolHelper.setRenderPart(result, part, grade, EnumPartPosition.ROD_GRIP);
      } else if (part instanceof ToolPartTip) {
        ToolHelper.setRenderPart(result, part, grade, EnumPartPosition.TIP);
      }
    }

    // Tool repair multiplier
    repairValue *= ((ITool) tool.getItem()).getRepairMultiplier();

    // Repair.
    result.attemptDamageItem(-repairValue, SilentGems.instance.random);
    // Restore ammo.
    if (result.getItem() instanceof IAmmoTool && ammoValue > 0) {
      IAmmoTool ammoTool = (IAmmoTool) result.getItem();
      ammoTool.addAmmo(result, ammoValue * GemsConfig.TOMAHAWK_AMMO_PER_MAT);
    }

    // Recalculate stats.
    ToolHelper.recalculateStats(result);
    ToolHelper.incrementStatRedecorated(result, 1);

    return result;
  }

  private boolean checkIsDecorationMaterial(ItemStack stack) {

    if (StackHelper.isEmpty(stack)) {
      return true;
    }

    ToolPart part = ToolPartRegistry.fromStack(stack);
    return part != null;
  }
}
