package net.silentchaos512.gems.compat.jei.altar;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;


public class AltarRecipeHandler implements IRecipeHandler<AltarRecipeJei> {

//  @Override
//  public String getRecipeCategoryUid() {
//
//    return AltarRecipeCategory.CATEGORY;
//  }

  @Override
  public String getRecipeCategoryUid(AltarRecipeJei arg0) {

    return AltarRecipeCategory.CATEGORY;
  }

  @Override
  public Class<AltarRecipeJei> getRecipeClass() {

    return AltarRecipeJei.class;
  }

  @Override
  public IRecipeWrapper getRecipeWrapper(AltarRecipeJei arg0) {

    return arg0;
  }

  @Override
  public boolean isRecipeValid(AltarRecipeJei arg0) {

    return true;
  }

}
