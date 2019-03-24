package net.silentchaos512.gems.compat.rei;

import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.crafting.altar.AltarRecipe;

import java.util.*;

public class AltarRecipeDisplay implements RecipeDisplay {
    private final AltarRecipe recipe;

    public AltarRecipeDisplay(AltarRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public Optional getRecipe() {
        return Optional.empty();
    }

    public AltarRecipe getAltarRecipe() {
        return this.recipe;
    }

    @Override
    public List<List<ItemStack>> getInput() {
        List<List<ItemStack>> lists = new ArrayList<>();
        lists.add(Arrays.asList(recipe.getInput().getMatchingStacks()));
        lists.add(Arrays.asList(recipe.getCatalyst().getMatchingStacks()));
        return lists;
    }

    @Override
    public List<ItemStack> getOutput() {
        return Collections.singletonList(recipe.getResult());
    }

    @Override
    public ResourceLocation getRecipeCategory() {
        return ReiPluginGems.ALTAR_TRANSMUTATION;
    }
}
