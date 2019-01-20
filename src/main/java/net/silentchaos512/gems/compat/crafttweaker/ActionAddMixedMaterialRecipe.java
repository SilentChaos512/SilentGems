//package net.silentchaos512.gems.compat.crafttweaker;
//
//import crafttweaker.mc1120.recipes.MCRecipeManager.ActionBaseAddRecipe;
//import net.minecraft.item.Item;
//import net.minecraft.item.crafting.IRecipe;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.common.registry.ForgeRegistries;
//import net.silentchaos512.gems.api.lib.EnumMaterialTier;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ActionAddMixedMaterialRecipe extends ActionBaseAddRecipe {
//
//  static int lastIndex = -1;
//
//  Item toolItem;
//  EnumMaterialTier tier;
//  Object[] recipe;
//  int nameIndex;
//
//  public ActionAddMixedMaterialRecipe(Item toolItem, EnumMaterialTier tier, Object... recipe) {
//
//    this.toolItem = toolItem;
//    this.tier = tier;
//    this.recipe = recipe;
//    nameIndex = ++lastIndex;
//  }
//
//  @Override
//  public void apply() {
//
//    // Add the real recipe.
//    IRecipe irecipe = new RecipeMixedMaterialItem(tier, toolItem, recipe);
//    irecipe.setRegistryName(new ResourceLocation("crafttweaker", calculateName()));
//    ForgeRegistries.RECIPES.register(irecipe);
//
//    // Also add example recipes.
//    List<EnumMaterialTier> tiers = new ArrayList<>();
//    if (tier == null) {
//      for (EnumMaterialTier t : EnumMaterialTier.values()) {
//        tiers.add(t);
//      }
//    } else {
//      tiers.add(tier);
//    }
//
//    List<String> recipeLines = new ArrayList<>();
//    int index = 0;
//    while (index < recipe.length && recipe[index] instanceof String) {
//      recipeLines.add((String) recipe[index++]);
//    }
//    List<Object> extraParams = new ArrayList<>();
//    for (; index < recipe.length; ++index) {
//      extraParams.add(recipe[index]);
//    }
//
//    EnumMaterialTier[] arrayTiers = tiers.toArray(new EnumMaterialTier[0]);
//    String[] arrayLines = recipeLines.toArray(new String[0]);
//    Object[] arrayExtraParams = extraParams.toArray();
//
//    if (toolItem instanceof ITool)
//      ToolHelper.addExampleRecipe(toolItem, arrayTiers, arrayLines, arrayExtraParams);
//  }
//
//  @Override
//  public String describe() {
//
//    return "Adding custom tool recipes for " + toolItem;
//  }
//
//  @Override
//  public String calculateName() {
//
//    return "multipart_custom" + nameIndex;
//  }
//
//}
