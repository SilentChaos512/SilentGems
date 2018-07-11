package net.silentchaos512.gems.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ItemSoulGem;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.util.StackHelper;

public class RecipeToolSoul extends RecipeBaseSL {

  @Override
  public boolean matches(InventoryCrafting inv, World world) {

    if (ModItems.toolSoul == null || ModItems.toolSoul.recipe == null) {
      return false;
    }
    return ModItems.toolSoul.recipe.matches(inv, null);
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    if (!matches(inv, null)) {
      return StackHelper.empty();
    }

    ItemStack result = new ItemStack(ModItems.toolSoul);
    ToolSoul soul = ToolSoul.construct(getSoulGems(inv));
    ModItems.toolSoul.setSoul(result, soul);

    return result;
  }

  private ItemSoulGem.Soul[] getSoulGems(InventoryCrafting inv) {

    List<ItemSoulGem.Soul> list = new ArrayList<>();

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      ItemStack stack = inv.getStackInSlot(i);
      if (StackHelper.isValid(stack) && stack.getItem() instanceof ItemSoulGem) {
        list.add(((ItemSoulGem) stack.getItem()).getSoul(stack));
      }
    }

    return list.toArray(new ItemSoulGem.Soul[list.size()]);
  }
}