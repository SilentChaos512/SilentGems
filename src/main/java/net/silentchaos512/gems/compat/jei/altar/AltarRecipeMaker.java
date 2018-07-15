package net.silentchaos512.gems.compat.jei.altar;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.silentchaos512.gems.api.recipe.altar.RecipeChaosAltar;

public class AltarRecipeMaker {

    @Nonnull
    public static List<AltarRecipeJei> getRecipes() {
        List<AltarRecipeJei> recipes = new ArrayList<>();

        for (RecipeChaosAltar recipe : RecipeChaosAltar.ALL_RECIPES)
            recipes.add(new AltarRecipeJei(recipe));

        return recipes;
    }
}
