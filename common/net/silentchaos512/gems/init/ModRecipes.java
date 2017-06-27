package net.silentchaos512.gems.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.silentchaos512.gems.api.SilentGemsAPI;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.recipe.RecipeApplyEnchantmentToken;
import net.silentchaos512.gems.recipe.RecipeChaosGemUpgrade;
import net.silentchaos512.gems.recipe.RecipeDecorateArmor;
import net.silentchaos512.gems.recipe.RecipeDecorateTool;
import net.silentchaos512.gems.recipe.RecipeHoldingGemSetBlock;
import net.silentchaos512.gems.recipe.RecipeMultiGemArmor;
import net.silentchaos512.gems.recipe.RecipeMultiGemBow;
import net.silentchaos512.gems.recipe.RecipeMultiGemShield;
import net.silentchaos512.gems.recipe.RecipeMultiGemTool;
import net.silentchaos512.gems.recipe.RecipeNamePlate;
import net.silentchaos512.lib.registry.IRegistrationHandler;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.registry.SRegistry;

public class ModRecipes implements IRegistrationHandler<IRecipe> {

  @Override
  public void registerAll(SRegistry reg) {

    RecipeMaker recipes = reg.recipes;

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
    recipes.addRecipe("multipart_tool", new RecipeMultiGemTool());
    recipes.addRecipe("multipart_shield", new RecipeMultiGemShield());
    recipes.addRecipe("multipart_bow", new RecipeMultiGemBow());
    recipes.addRecipe("multipart_armor", new RecipeMultiGemArmor());
    recipes.addRecipe("decorate_tool", new RecipeDecorateTool());
    recipes.addRecipe("decorate_armor", new RecipeDecorateArmor());
    recipes.addRecipe("apply_enchantment_token", new RecipeApplyEnchantmentToken());
    recipes.addRecipe("chaos_gem_upgrade", new RecipeChaosGemUpgrade());
    recipes.addRecipe("name_plate_use", new RecipeNamePlate());
    recipes.addRecipe("holding_gem_set_block", new RecipeHoldingGemSetBlock());
  }
}
