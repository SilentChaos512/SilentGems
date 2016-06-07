package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.armor.ItemArmorFrame;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.lib.recipe.RecipeBase;

public class RecipeMultiGemArmor extends RecipeBase {

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    // Get the middle stack, which determines the armor type.
    ItemStack centerStack = inv.getStackInRowAndColumn(1, 1);
    if (centerStack == null || centerStack.getItem() == null
        || !(centerStack.getItem() instanceof ItemArmorFrame))
      return null;

    // Determine the target tier and output item.
    ItemArmorFrame item = (ItemArmorFrame) centerStack.getItem();
    Item outputItem = item.getOutputItem(centerStack);
    EnumMaterialTier targetTier = item.getTier(centerStack);

    // Get armor parts. Check tiers match (getGems checks all materials are same tier, or it returns null).
    ItemStack[] stacks = getGems(inv);

    if (stacks == null || EnumMaterialTier.fromStack(stacks[0]) != targetTier)
      return null;

    return ArmorHelper.constructArmor(outputItem, stacks);
  }

  public ItemStack[] getGems(InventoryCrafting inv) {

    // Get materials from slots.
    ItemStack[] stacks = new ItemStack[4];
    stacks[0] = inv.getStackInRowAndColumn(0, 1); // West
    stacks[1] = inv.getStackInRowAndColumn(1, 0); // North
    stacks[2] = inv.getStackInRowAndColumn(2, 1); // East
    stacks[3] = inv.getStackInRowAndColumn(1, 2); // South

    // Make sure all are same tier.
    EnumMaterialTier tier = EnumMaterialTier.fromStack(stacks[0]);
    for (int i = 1; i < stacks.length; ++i) {
      if (tier == null || tier != EnumMaterialTier.fromStack(stacks[i])) {
        return null;
      }
    }

    return stacks;
  }
}
