package net.silentchaos512.gems.compat.jei;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.block.purifier.PurifierBlock;

import java.util.Arrays;
import java.util.Collections;

public class PurifierRecipeCategoryJei implements IRecipeCategory<PurifierRecipeCategoryJei.Recipe> {
    private static final int GUI_START_X = 0;
    private static final int GUI_START_Y = 165;
    private static final int GUI_WIDTH = 25;
    private static final int GUI_HEIGHT = 25;

    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public PurifierRecipeCategoryJei(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(SilentGemsPlugin.GUI_TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(new ItemStack(PurifierBlock.INSTANCE.get()));
        localizedName = I18n.format("category.silentgems.purifier");
    }

    @Override
    public ResourceLocation getUid() {
        return SilentGemsPlugin.PURIFIER;
    }

    @Override
    public Class<? extends Recipe> getRecipeClass() {
        return Recipe.class;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(Recipe recipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(Collections.singletonList(recipe.ingredient));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, Recipe recipe, IIngredients iIngredients) {
        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();
        itemStacks.init(0, true, 4, 4);
        itemStacks.set(0, Arrays.asList(recipe.ingredient.getMatchingStacks()));
    }

    public static class Recipe {
        private final Ingredient ingredient;

        public Recipe(Ingredient ingredient) {
            this.ingredient = ingredient;
        }
    }
}
