package net.silentchaos512.gems.api.recipe.altar;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;

public class RecipeChaosAltar {

  public static final List<RecipeChaosAltar> ALL_RECIPES = Lists.newArrayList();

  private ItemStack input;
  private ItemStack output;
  private ItemStack catalyst;
  private int chaosCost;

  public RecipeChaosAltar(ItemStack output, ItemStack input, int chaosCost) {

    this(output, input, chaosCost, null);
  }

  public RecipeChaosAltar(ItemStack output, ItemStack input, int chaosCost, ItemStack catalyst) {

    this.output = output;
    this.input = input;
    this.chaosCost = chaosCost;
    this.catalyst = catalyst;
  }

  public static RecipeChaosAltar getMatchingRecipe(ItemStack inputStack, ItemStack catalystStack) {

    if (inputStack == null)
      return null;

    for (RecipeChaosAltar recipe : ALL_RECIPES)
      if (recipe.matches(inputStack, catalystStack))
        return recipe;

    return null;
  }

  public static RecipeChaosAltar getRecipeByOutput(ItemStack outputStack) {

    if (outputStack == null)
      return null;

    for (RecipeChaosAltar recipe : ALL_RECIPES)
      if (recipe.output.isItemEqual(outputStack))
        return recipe;

    return null;
  }

  public static boolean isValidIngredient(ItemStack inputStack) {

    if (inputStack == null)
      return false;

    for (RecipeChaosAltar recipe : ALL_RECIPES)
      if (recipe.input.isItemEqual(inputStack))
        return true;

    return false;
  }

  public boolean matches(ItemStack inputStack, ItemStack catalystStack) {

    boolean catalystMatch = this.catalyst == null
        || (catalystStack != null && catalystStack.isItemEqual(this.catalyst));
    return inputStack != null && this.input != null && this.output != null
        && inputStack.isItemEqual(this.input) && inputStack.getCount() >= this.input.getCount()
        && catalystMatch;
  }

  public ItemStack getInput() {

    return input.copy();
  }

  public ItemStack getOutput() {

    return output.copy();
  }

  public ItemStack getCatalyst() {

    return catalyst.copy();
  }

  public int getChaosCost() {

    return chaosCost;
  }
}
