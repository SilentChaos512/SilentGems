package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ItemToolSoul;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.util.StackHelper;


public class RecipeApplyToolSoul extends RecipeBaseSL {

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack tool = StackHelper.empty();
    ItemStack soul = StackHelper.empty();

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      ItemStack stack = inv.getStackInSlot(i);
      // Found a tool?
      if (stack.getItem() instanceof ITool) {
        if (StackHelper.isValid(tool)) {
          return StackHelper.empty();
        }
        tool = stack;
      }
      // Found a soul?
      else if (stack.getItem() instanceof ItemToolSoul) {
        if (StackHelper.isValid(soul)) {
          return StackHelper.empty();
        }
        soul = stack;
      }
    }

    if (StackHelper.isEmpty(tool) || StackHelper.isEmpty(soul)) {
      return StackHelper.empty();
    }

    // Does tool already have a soul?
    if (SoulManager.getSoul(tool) != null) {
      return StackHelper.empty();
    }

    // Is the soul valid?
    ToolSoul toolSoul = ModItems.toolSoul.getSoul(soul);
    if (toolSoul == null) {
      return StackHelper.empty();
    }

    ItemStack result = StackHelper.safeCopy(tool);
    // Have to change UUID to prevent soul duping.
//    result.getTagCompound().removeTag(ToolHelper.NBT_UUID);
    SoulManager.setSoul(result, toolSoul, true);

    // Apply name, if applicable.
    if (soul.hasDisplayName()) {
      String name = soul.getDisplayName();
      toolSoul.setName(name);
      result.setStackDisplayName(name);
    }

    // Recalculate stats and return.
    ToolHelper.recalculateStats(result);
    return result;
  }
}