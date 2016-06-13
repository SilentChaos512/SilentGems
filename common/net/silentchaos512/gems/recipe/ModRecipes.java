package net.silentchaos512.gems.recipe;

import net.minecraftforge.oredict.RecipeSorter.Category;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.registry.SRegistry;

public class ModRecipes {

  public static void init() {

    SRegistry reg = SilentGems.instance.registry;
    String afterShapeless = "after:minecraft:shapeless";
    try {
      reg.addRecipeHandler(RecipeMultiGemTool.class, "MultiGemTool", Category.SHAPED,
          afterShapeless);
      reg.addRecipeHandler(RecipeMultiGemArmor.class, "MultiGemArmor", Category.SHAPED,
          afterShapeless);
      reg.addRecipeHandler(RecipeDecorateTool.class, "DecorateTool", Category.SHAPED,
          afterShapeless);
      reg.addRecipeHandler(RecipeDecorateArmor.class, "DecorateArmor", Category.SHAPED,
          afterShapeless);
      reg.addRecipeHandler(RecipeApplyEnchantmentToken.class, "ApplyEnchantmentToken",
          Category.SHAPELESS, afterShapeless);
      reg.addRecipeHandler(RecipeNamePlate.class, "NamePlate", Category.SHAPELESS, afterShapeless);
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
