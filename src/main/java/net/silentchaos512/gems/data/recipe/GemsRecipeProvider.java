package net.silentchaos512.gems.data.recipe;

import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.util.NameUtils;

import java.util.function.Consumer;

public class GemsRecipeProvider extends RecipeProvider {
    public GemsRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        registerGemRecipes(consumer);
    }

    private static void registerGemRecipes(Consumer<IFinishedRecipe> consumer) {
        for (Gems gem : Gems.values()) {
            String name = gem.getName();

            smeltingAndBlasting(consumer, name, gem.getModOresItemTag(), gem.getItem());
            compression(consumer, gem.getBlock(), gem.getItem());
        }
    }

    private static void smeltingAndBlasting(Consumer<IFinishedRecipe> consumer, String id, IItemProvider ingredientItem, IItemProvider result) {
        Ingredient ingredientIn = Ingredient.fromItems(ingredientItem);
        smeltingAndBlasting(consumer, id, ingredientIn, result);
    }

    private static void smeltingAndBlasting(Consumer<IFinishedRecipe> consumer, String id, ITag<Item> ingredientTag, IItemProvider result) {
        Ingredient ingredientIn = Ingredient.fromTag(ingredientTag);
        smeltingAndBlasting(consumer, id, ingredientIn, result);
    }

    private static void smeltingAndBlasting(Consumer<IFinishedRecipe> consumer, String id, Ingredient ingredientIn, IItemProvider result) {
        CookingRecipeBuilder.blastingRecipe(ingredientIn, result, 1.0f, 100)
                .addCriterion("impossible", new ImpossibleTrigger.Instance())
                .build(consumer, GemsBase.getId("blasting/" + id));
        CookingRecipeBuilder.smeltingRecipe(ingredientIn, result, 1.0f, 200)
                .addCriterion("impossible", new ImpossibleTrigger.Instance())
                .build(consumer, GemsBase.getId("smelting/" + id));
    }

    private static void compression(Consumer<IFinishedRecipe> consumer, IItemProvider block, IItemProvider item) {
        compression(consumer, block, item, NameUtils.fromItem(item).getPath());
    }

    private static void compression(Consumer<IFinishedRecipe> consumer, IItemProvider block, IItemProvider item, String path) {
        ShapedRecipeBuilder.shapedRecipe(block, 1)
                .patternLine("###")
                .patternLine("###")
                .patternLine("###")
                .key('#', item)
                .addCriterion("has_item", hasItem(item))
                .build(consumer, GemsBase.getId(path + "_from_block"));
        ShapelessRecipeBuilder.shapelessRecipe(item, 9)
                .addIngredient(block)
                .addCriterion("has_item", hasItem(item))
                .build(consumer, GemsBase.getId(path + "_block"));
    }
}
