package net.silentchaos512.gems.recipe;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.api.tool.part.ToolPartRod;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.lib.recipe.RecipeBase;

public class RecipeMultiGemTool extends RecipeBase {

  public static final String RECIPE_SWORD = "h;h;r";
  public static final String RECIPE_KATANA = "hh;h ;r ";
  public static final String RECIPE_SCEPTER = " h ;hrh;hrh";
  public static final String RECIPE_TOMAHAWK = "hhh;hr ; r ";
  public static final String RECIPE_PICKAXE = "hhh; r ; r ";
  public static final String RECIPE_SHOVEL = "h;r;r";
  public static final String RECIPE_AXE = "hh;hr; r";
  public static final String RECIPE_HOE = "hh; r; r";
  public static final String RECIPE_SICKLE = " h;hh;r ";

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    // 5 part head
    if (matchesRecipe(inv, RECIPE_SCEPTER))
      return ModItems.scepter.constructTool(getRodType(inv), getGems(inv));

    // 4 part head
    else if (matchesRecipe(inv, RECIPE_TOMAHAWK))
      return ModItems.tomahawk.constructTool(getRodType(inv), getGems(inv));

    // 3 part head
    else if (matchesRecipe(inv, RECIPE_PICKAXE))
      return ModItems.pickaxe.constructTool(getRodType(inv), getGems(inv));
    else if (matchesRecipe(inv, RECIPE_AXE))
      return ModItems.axe.constructTool(getRodType(inv), getGems(inv));
    else if (matchesRecipe(inv, RECIPE_SICKLE))
      return ModItems.sickle.constructTool(getRodType(inv), getGems(inv));
    else if (matchesRecipe(inv, RECIPE_KATANA))
      return ModItems.katana.constructTool(getRodType(inv), getGems(inv));

    // 2 part head
    else if (matchesRecipe(inv, RECIPE_SWORD))
      return ModItems.sword.constructTool(getRodType(inv), getGems(inv));
    else if (matchesRecipe(inv, RECIPE_HOE))
      return ModItems.hoe.constructTool(getRodType(inv), getGems(inv));

    // 1 part head
    else if (matchesRecipe(inv, RECIPE_SHOVEL))
      return ModItems.shovel.constructTool(getRodType(inv), getGems(inv));

    return null;
  }

  private boolean matchesRecipe(InventoryCrafting inv, String recipe) {

    String[] lines = recipe.split(";");
    if (lines.length != 3) {
      throw new IllegalArgumentException("Malformed recipe (requires exactly three lines!)");
    }

    int height = 3;
    int width = 0;
    for (String line : lines) {
      width = Math.max(width, line.length());
    }

    for (int i = 0; i <= 3 - width; ++i) {
      for (int j = 0; j <= 3 - height; ++j) {
        if (checkMatch(inv, i, j, width, height, true, recipe)) {
          return true;
        }
        if (checkMatch(inv, i, j, width, height, false, recipe)) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean checkMatch(InventoryCrafting inv, int posX, int posY, int recipeWidth,
      int recipeHeight, boolean mirror, String recipe) {

    int headInRecipe = 0;
    int headFound = 0;
    ToolPart firstRod = null;

    // Recipe array has to be re-arranged for some reason.
    int[] order = new int[] { 0, 3, 6, 1, 4, 7, 2, 5, 8 };
    char[] chars = new char[recipeWidth * recipeHeight];
    recipe = recipe.replaceAll(";", "");
    for (int i : order) {
      if (i < chars.length) {
        chars[i] = recipe.charAt(i);
        if (chars[i] == 'h')
          ++headInRecipe;
      }
    }

    EnumMaterialTier tier = null;

    ItemStack stack;
    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 3; ++j) {
        int k = i - posX;
        int l = j - posY;
        char c = ' ';

        if (k >= 0 && l >= 0 && k < recipeWidth && l < recipeHeight)
          c = mirror ? chars[recipeWidth - k - 1 + l * recipeWidth] : chars[k + l * recipeWidth];

        stack = inv.getStackInRowAndColumn(i, j);

        // Check for excess things.
        if (c == ' ' && stack != null)
          return false;

        ToolPart part = getPartInSlot(inv, i, j);

        // Check part
        if (part != null && c != ' ' && !part.isBlacklisted(stack)) {
          if (tier == null)
            tier = part.getTier();
          // Make sure tiers are compatible.
          if (tier != part.getTier()) {
            // Some parts support different tiers.
            if (!part.validForToolOfTier(tier))
              return false;
          }

          // Count head parts.
          if (part instanceof ToolPartMain)
            ++headFound;

          // Check rods.
          if (part instanceof ToolPartRod) {
            if (firstRod == null)
              firstRod = part;
            if (firstRod != part)
              return false;
          }

          if (c == 'h' && !(part instanceof ToolPartMain))
            return false;
          else if (c == 'r' && !(part instanceof ToolPartRod))
            return false;
        } else if (part == null && c != ' ') {
          return false;
        }
      }
    }

    return headInRecipe == headFound;
  }

  private ToolPart getPartInSlot(InventoryCrafting inv, int row, int column) {

    ItemStack stack = inv.getStackInRowAndColumn(row, column);
    if (stack != null) {
      ToolPart part = ToolPartRegistry.fromStack(stack);
      if (part != null) {
        return part;
      }
    }
    return null;
  }

  private ItemStack getRodType(InventoryCrafting inv) {

    ItemStack firstRod = null;
    ItemStack stack = null;
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      ToolPart part = ToolPartRegistry.fromStack(stack);
      if (part != null && part instanceof ToolPartRod) {
        if (firstRod == null) {
          firstRod = stack;
        }
        if (!firstRod.isItemEqual(stack)) {
          return null;
        }
      }
    }
    return firstRod;
  }

  private ItemStack[] getGems(InventoryCrafting inv) {

    List<ItemStack> list = Lists.newArrayList();
    ItemStack stack;
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      ToolPart part = ToolPartRegistry.fromStack(stack);
      if (part != null && part instanceof ToolPartMain) {
        list.add(stack);
      }
    }
    return list.toArray(new ItemStack[list.size()]);
  }

  @Override
  public int getRecipeSize() {

    return 10;
  }
}
