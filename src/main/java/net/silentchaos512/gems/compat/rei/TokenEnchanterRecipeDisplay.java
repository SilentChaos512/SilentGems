package net.silentchaos512.gems.compat.rei;

public class TokenEnchanterRecipeDisplay /*implements RecipeDisplay*/ {
    /*private final TokenEnchanterRecipe recipe;

    public TokenEnchanterRecipeDisplay(TokenEnchanterRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public Optional<IRecipe> getRecipe() {
        return Optional.empty();
    }

    public TokenEnchanterRecipe getTokenRecipe() {
        return recipe;
    }

    @Override
    public List<List<ItemStack>> getInput() {
        List<List<ItemStack>> lists = new ArrayList<>();
        lists.add(Arrays.asList(recipe.getToken().getMatchingStacks()));
        recipe.getIngredients().forEach((ingredient, count) ->
                lists.add(getStackList(ingredient, count)));
        return lists;
    }

    private static List<ItemStack> getStackList(Ingredient ingredient, int count) {
        List<ItemStack> list = new ArrayList<>();
        for (ItemStack stack : ingredient.getMatchingStacks()) {
            ItemStack copy = stack.copy();
            copy.setCount(count);
            list.add(copy);
        }
        return list;
    }

    @Override
    public List<ItemStack> getOutput() {
        return Collections.singletonList(recipe.getResult());
    }

    @Override
    public ResourceLocation getRecipeCategory() {
        return ReiPluginGems.TOKEN_ENCHANTING;
    }*/
}
