package net.silentchaos512.gems.compat.jei.altar;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.compat.jei.SilentGemsPlugin;

public class AltarRecipeCategory implements IRecipeCategory {

  public static final String CATEGORY = SilentGems.MODID + ":ChaosAltar";

  public static final int GUI_START_X = 42;
  public static final int GUI_START_Y = 28;
  public static final int GUI_WIDTH = 98;
  public static final int GUI_HEIGHT = 47;

  @Nonnull
  protected final IDrawable background;
  @Nonnull
  protected final IDrawableAnimated arrow;
  @Nonnull
  private final String localizedName = SilentGems.localizationHelper
      .getLocalizedString("jei", "recipe.altar");

  public AltarRecipeCategory(IGuiHelper guiHelper) {

    ResourceLocation backgroundLocation = new ResourceLocation(
        SilentGems.RESOURCE_PREFIX + "textures/gui/ChaosAltar.png");

    // background
    background = SilentGemsPlugin.jeiHelper.getGuiHelper().createDrawable(backgroundLocation,
        GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);

    // arrow
    IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 176, 14, 24, 17);
    arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200,
        IDrawableAnimated.StartDirection.LEFT, false);
  }

//  @Override
//  public void drawAnimations(Minecraft mc) {
//
//    arrow.draw(mc, 38, 6);
//  }

  @Override
  public void drawExtras(Minecraft mc) {

    arrow.draw(mc, 38, 6);
  }

  @Override
  public IDrawable getBackground() {

    return background;
  }

  @Override
  public IDrawable getIcon() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getTitle() {

    return localizedName;
  }

  @Override
  public String getUid() {

    return CATEGORY;
  }

//  @Override
//  public void setRecipe(IRecipeLayout arg0, IRecipeWrapper arg1) {
//
//    // TODO Auto-generated method stub
//
//  }

  @Override
  public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper,
      IIngredients ingredients) {

    recipeLayout.getItemStacks().init(0, true, 55 - GUI_START_X, 34 - GUI_START_Y);
    recipeLayout.getItemStacks().init(1, false, 110 - GUI_START_X, 34 - GUI_START_Y);
    recipeLayout.getItemStacks().init(2, true, 82 - GUI_START_X, 53 - GUI_START_Y);

    // if (recipeWrapper instanceof AltarRecipeJei) {
    // if (ingredients instanceof IngredientsAltar) {
    // AltarRecipeJei wrapper = (AltarRecipeJei) recipeWrapper;
    recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
    recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).get(0));
    recipeLayout.getItemStacks().set(2, ingredients.getInputs(ItemStack.class).get(1));
    // }
  }
}
