package net.silentchaos512.gems.recipe;

import javax.annotation.Nonnull;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.tool.part.ArmorPartFrame;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.api.tool.part.ToolPartRod;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.lib.collection.ItemStackList;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.util.StackHelper;

public class RecipeGemArrow extends RecipeBaseSL {

  @Override
  public @Nonnull ItemStack getRecipeOutput() {

    return new ItemStack(ModItems.arrow);
  }

  protected boolean partTiersMatch(InventoryCrafting inv) {

    EnumMaterialTier tier = null;

    // Check mains
    for (ItemStack stack : getMaterials(inv)) {
      ToolPart part = ToolPartRegistry.fromStack(stack);
      if (tier == null) {
        tier = part.getTier();
      } else if (tier != part.getTier()) {
        return false;
      }
    }

    // No mains found?
    if (tier == null) {
      return false;
    }

    // Check rod
    ItemStack rod = getRod(inv);
    if (StackHelper.isValid(rod))
      return ToolPartRegistry.fromStack(rod).validForToolOfTier(tier);

    return true;
  }

  @Override
  public boolean matches(InventoryCrafting inv, World world) {

    if (inv.getSizeInventory() < 9) {
      return false;
    }

    for (int col = 0; col < 3; ++col) {
      ItemStack stack = inv.getStackInRowAndColumn(col, 0);
      if (stack.getItem() == Items.FLINT) {
        // No flint arrows for now, it would conflict with vanilla recipe.
        return false;
      }

      if (StackHelper.isValid(stack)) {
        ItemStack rod = inv.getStackInRowAndColumn(col, 1);
        ItemStack fletching = inv.getStackInRowAndColumn(col, 2);
        ToolPart part1 = ToolPartRegistry.fromStack(stack);
        ToolPart part2 = ToolPartRegistry.fromStack(rod);
        // ToolPart part3 = ToolPartRegistry.fromStack(fletching);

        return part1 instanceof ToolPartMain && part2 instanceof ToolPartRod
            && fletching.getItem() == Items.FEATHER; // TODO: fletching
      }
    }

    return false;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    if (!partTiersMatch(inv)) {
      return StackHelper.empty();
    }

    ItemStack rod = getRod(inv);
    ItemStackList materials = getMaterials(inv);
    ItemStack[] array = materials.toArray(new ItemStack[materials.size()]);

    return ModItems.arrow.construct(rod, array[0]);
  }

  protected ItemStackList getMaterials(InventoryCrafting inv) {

    ItemStackList list = ItemStackList.create();
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      ItemStack stack = inv.getStackInSlot(i);
      ToolPart part = ToolPartRegistry.fromStack(stack);
      if (part != null && !part.isBlacklisted(stack) && part instanceof ToolPartMain) {
        list.add(stack);
      }
    }
    return list;
  }

  protected ItemStack getRod(InventoryCrafting inv) {

    ItemStack rod = StackHelper.empty();
    for (ItemStack stack : getNonEmptyStacks(inv)) {
      ToolPart part = ToolPartRegistry.fromStack(stack);
      if (part != null && !part.isBlacklisted(stack) && part instanceof ToolPartRod) {
        if (StackHelper.isEmpty(rod)) {
          rod = stack;
        } else if (!rod.isItemEqual(stack)) {
          return StackHelper.empty();
        }
      }
    }
    return rod;
  }
}
