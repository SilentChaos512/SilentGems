package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.lib.recipe.RecipeBase;

public class RecipeMultiGemShield extends RecipeBase {

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    //@formatter:off
    ItemStack[] materials = {
        inv.getStackInRowAndColumn(0, 0),
        inv.getStackInRowAndColumn(2, 0),
        inv.getStackInRowAndColumn(1, 2) };
    ItemStack[] wood = {
        inv.getStackInRowAndColumn(1, 0),
        inv.getStackInRowAndColumn(0, 1),
        inv.getStackInRowAndColumn(2, 1) };
    ItemStack[] empty = {
        inv.getStackInRowAndColumn(0, 2),
        inv.getStackInRowAndColumn(2, 2) };
    ItemStack rod = inv.getStackInRowAndColumn(1, 1);
    //@formatter:on

    // Make sure bottom corners are empty.
    for (ItemStack stack : empty)
      if (stack != null)
        return null;

    // Check for wood.
    for (ItemStack stack : wood) {
      if (stack == null)
        return null;

      boolean isWood = false;
      for (int id : OreDictionary.getOreIDs(stack)) {
        if (OreDictionary.getOreName(id).equals("plankWood")) {
          isWood = true;
          break;
        }
      }

      if (!isWood)
        return null;
    }

    // Check parts (gems)
    ToolPart[] parts = new ToolPart[3];
    int i = -1;
    for (ItemStack stack : materials) {
      if (stack == null)
        return null;

      ToolPart part = ToolPartRegistry.fromStack(stack);
      if (part == null || part.isBlacklisted(stack))
        return null;
      parts[++i] = part;
    }

    // Determine tier and make sure all parts are the same tier.
    EnumMaterialTier targetTier = parts[0].getTier();
    for (ToolPart part : parts)
      if (part.getTier() != targetTier)
        return null;

    // Check rod and make sure tier matches.
    if (rod == null)
      return null;
    ToolPart partRod = ToolPartRegistry.fromStack(rod);
    if (partRod == null || !partRod.validForToolOfTier(targetTier))
      return null;

    // Recipe correct. Make the shield.
    return ModItems.shield.constructTool(rod, materials);
  }

  @Override
  public int getRecipeSize() {

    return 10;
  }
}
