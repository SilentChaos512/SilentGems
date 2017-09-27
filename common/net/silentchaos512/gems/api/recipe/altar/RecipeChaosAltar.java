package net.silentchaos512.gems.api.recipe.altar;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.silentchaos512.lib.util.StackHelper;

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

    if (StackHelper.isEmpty(inputStack))
      return null;

    for (RecipeChaosAltar recipe : ALL_RECIPES)
      if (recipe.matches(inputStack, catalystStack))
        return recipe;

    return null;
  }

  public static RecipeChaosAltar getRecipeByOutput(ItemStack outputStack) {

    if (StackHelper.isEmpty(outputStack))
      return null;

    for (RecipeChaosAltar recipe : ALL_RECIPES)
      if (recipe.output.isItemEqual(outputStack))
        return recipe;

    return null;
  }

  public static boolean isValidIngredient(ItemStack inputStack) {

    if (StackHelper.isEmpty(inputStack))
      return false;

    for (RecipeChaosAltar recipe : ALL_RECIPES)
      if (recipe.input.isItemEqual(inputStack))
        return true;

    return false;
  }

  public boolean matches(ItemStack inputStack, ItemStack catalystStack) {

    boolean catalystMatch = (StackHelper.isEmpty(catalystStack) && StackHelper.isEmpty(this.catalyst))
        || (StackHelper.isValid(catalystStack) && catalystStack.isItemEqual(this.catalyst));
    return StackHelper.isValid(inputStack) && StackHelper.isValid(this.input)
        && StackHelper.isValid(this.output) && inputStack.isItemEqual(this.input)
        && StackHelper.getCount(inputStack) >= StackHelper.getCount(this.input) && catalystMatch;
  }

  public ItemStack getInput() {

    return StackHelper.safeCopy(input);
  }

  public ItemStack getOutput() {

    return StackHelper.safeCopy(output);
  }

  public ItemStack getCatalyst() {

    return StackHelper.safeCopy(catalyst);
  }

  public int getChaosCost() {

    return chaosCost;
  }
}
