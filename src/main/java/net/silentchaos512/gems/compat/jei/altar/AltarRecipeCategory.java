package net.silentchaos512.gems.compat.jei.altar;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;

import javax.annotation.Nonnull;

public class AltarRecipeCategory implements IRecipeCategory {
    public static final String CATEGORY = SilentGems.MODID + ":ChaosAltar";
    private static final int GUI_START_X = 42;
    private static final int GUI_START_Y = 28;
    private static final int GUI_WIDTH = 98;
    private static final int GUI_HEIGHT = 47;

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final IDrawableAnimated arrow;
    @Nonnull
    private final String localizedName = SilentGems.i18n.translate("jei", "recipe.altar");

    public AltarRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation backgroundLocation = new ResourceLocation(SilentGems.RESOURCE_PREFIX + "textures/gui/ChaosAltar.png");

        // background
        background = guiHelper.createDrawable(backgroundLocation, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);

        // arrow
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 176, 14, 24, 17);
        arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public String getUid() {
        return CATEGORY;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 55 - GUI_START_X, 34 - GUI_START_Y);
        recipeLayout.getItemStacks().init(1, false, 110 - GUI_START_X, 34 - GUI_START_Y);
        recipeLayout.getItemStacks().init(2, true, 82 - GUI_START_X, 53 - GUI_START_Y);

        recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
        recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).get(0));
        recipeLayout.getItemStacks().set(2, ingredients.getInputs(ItemStack.class).get(1));
    }

    @Override
    public String getModName() {
        return SilentGems.MOD_NAME;
    }
}
