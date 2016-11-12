package net.silentchaos512.gems.recipe;

import net.minecraftforge.oredict.RecipeSorter.Category;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.SilentGemsAPI;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.lib.registry.SRegistry;

public class ModRecipes {

  public static void init() {

    SilentGemsAPI.addAltarRecipe(ModItems.craftingMaterial.chaosEssence,
        ModItems.craftingMaterial.chaosEssenceShard, 100000, null);

    SRegistry reg = SilentGems.instance.registry;
    String dep = "after:minecraft:shapeless";
    try {
      reg.addRecipeHandler(RecipeMultiGemTool.class, "MultiGemTool", Category.SHAPED, dep);
      reg.addRecipeHandler(RecipeMultiGemShield.class, "MultiGemShield", Category.SHAPED, dep);
      reg.addRecipeHandler(RecipeMultiGemBow.class, "MultiGemBow", Category.SHAPED, dep);
      reg.addRecipeHandler(RecipeMultiGemArmor.class, "MultiGemArmor", Category.SHAPED, dep);
      reg.addRecipeHandler(RecipeDecorateTool.class, "DecorateTool", Category.SHAPED, dep);
      reg.addRecipeHandler(RecipeDecorateArmor.class, "DecorateArmor", Category.SHAPED, dep);
      reg.addRecipeHandler(RecipeApplyEnchantmentToken.class, "ApplyEnchantmentToken",
          Category.SHAPELESS, dep);
      reg.addRecipeHandler(RecipeNamePlate.class, "NamePlate", Category.SHAPELESS, dep);
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
