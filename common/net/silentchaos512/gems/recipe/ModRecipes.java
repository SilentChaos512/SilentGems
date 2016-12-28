package net.silentchaos512.gems.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.SilentGemsAPI;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.SRegistry;

public class ModRecipes {

  public static void init() {

    // Chaos Essence creation.
    SilentGemsAPI.addAltarRecipe(ModItems.craftingMaterial.chaosEssence,
        ModItems.craftingMaterial.getStack(Names.CHAOS_ESSENCE_SHARD, 4), 240000,
        new ItemStack(Items.DIAMOND));

    // Light <--> Dark gem conversion.
    ItemStack slimeBall = new ItemStack(Items.SLIME_BALL);
    ItemStack magmaCream = new ItemStack(Items.MAGMA_CREAM);
    for (int i = 0; i < 16; ++i) {
      EnumGem light = EnumGem.values()[i];
      EnumGem dark = EnumGem.values()[i + 16];
      ItemStack lightShards = new ItemStack(ModItems.gemShard, 6, light.ordinal());
      ItemStack darkShards = new ItemStack(ModItems.gemShard, 6, dark.ordinal());
      SilentGemsAPI.addAltarRecipe(darkShards, light.getItem(), 80000, magmaCream);
      SilentGemsAPI.addAltarRecipe(lightShards, dark.getItem(), 80000, slimeBall);
    }

    // Recipe handlers.
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
      reg.addRecipeHandler(RecipeChaosGemUpgrade.class, "ChaosGemUpgrade", Category.SHAPELESS, dep);
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
