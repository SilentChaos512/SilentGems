package net.silentchaos512.gems.api.recipe.altar;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;

public class RecipeChaosAltar {

  public static final List<RecipeChaosAltar> ALL_RECIPES = Lists.newArrayList();

  private ItemStack input;
  private ItemStack output;
  private int chaosCost;

  public RecipeChaosAltar(ItemStack output, ItemStack input, int chaosCost) {

    this.output = output;
    this.input = input;
    this.chaosCost = chaosCost;
  }

  public static RecipeChaosAltar getMatchingRecipe(ItemStack inputStack) {

    if (inputStack == null) return null;

    for (RecipeChaosAltar recipe : ALL_RECIPES)
      if (recipe.matches(inputStack))
        return recipe;

    return null;
  }

  public static RecipeChaosAltar getRecipeByOutput(ItemStack outputStack) {

    if (outputStack == null) return null;

    for (RecipeChaosAltar recipe : ALL_RECIPES)
      if (recipe.output.isItemEqual(outputStack))
        return recipe;

    return null;
  }

  public static boolean isValidIngredient(ItemStack inputStack) {

    if (inputStack == null) return false;

    for (RecipeChaosAltar recipe : ALL_RECIPES)
      if (recipe.input.isItemEqual(inputStack))
        return true;

    return false;
  }

  public boolean matches(ItemStack inputStack) {

    return inputStack != null && this.input != null && this.output != null
        && inputStack.isItemEqual(this.input);
  }

  public ItemStack getInput() {

    return input.copy();
  }

  public ItemStack getOutput() {

    return output.copy();
  }

  public int getChaosCost() {

    return chaosCost;
  }
}
