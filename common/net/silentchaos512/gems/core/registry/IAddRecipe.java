package net.silentchaos512.gems.core.registry;

/**
 * Blocks and items that implement this will have these two methods called in SRegistry to initialize recipes and ore
 * dictionary entries.
 */
public interface IAddRecipe {

  /**
   * Add any relevant ore dictionary entries in this method.
   */
  void addOreDict();

  /**
   * Add any recipes for this block/item in this method.
   */
  void addRecipes();
}
