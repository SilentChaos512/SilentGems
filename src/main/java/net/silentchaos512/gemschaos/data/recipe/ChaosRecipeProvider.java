package net.silentchaos512.gemschaos.data.recipe;

import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.setup.ChaosBlocks;
import net.silentchaos512.gemschaos.setup.ChaosItems;
import net.silentchaos512.lib.util.NameUtils;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ChaosRecipeProvider extends RecipeProvider {
    public ChaosRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        registerMetals(consumer);
    }

    private static void registerMetals(Consumer<IFinishedRecipe> consumer) {
        smeltingAndBlasting(consumer, "chaos_crystal", ChaosBlocks.CHAOS_ORE.get(), ChaosItems.CHAOS_CRYSTAL.get());

        compression(consumer, ChaosBlocks.CHAOS_CRYSTAL_BLOCK, ChaosItems.CHAOS_CRYSTAL, null);
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
                .build(consumer, ChaosMod.getId("blasting/" + id));
        CookingRecipeBuilder.smeltingRecipe(ingredientIn, result, 1.0f, 200)
                .addCriterion("impossible", new ImpossibleTrigger.Instance())
                .build(consumer, ChaosMod.getId("smelting/" + id));
    }

    private static void compression(Consumer<IFinishedRecipe> consumer, IItemProvider block, IItemProvider item, @Nullable IItemProvider nugget) {
        String blockName = NameUtils.fromItem(block).getPath();
        String itemName = NameUtils.fromItem(item).getPath();

        ShapedRecipeBuilder.shapedRecipe(block, 1)
                .patternLine("###")
                .patternLine("###")
                .patternLine("###")
                .key('#', item)
                .addCriterion("has_item", hasItem(item))
                .build(consumer, ChaosMod.getId(itemName + "_from_block"));
        ShapelessRecipeBuilder.shapelessRecipe(item, 9)
                .addIngredient(block)
                .addCriterion("has_item", hasItem(item))
                .build(consumer, ChaosMod.getId(blockName));

        if (nugget != null) {
            String nuggetName = NameUtils.fromItem(nugget).getPath();

            ShapedRecipeBuilder.shapedRecipe(item, 1)
                    .patternLine("###")
                    .patternLine("###")
                    .patternLine("###")
                    .key('#', nugget)
                    .addCriterion("has_item", hasItem(item))
                    .build(consumer, ChaosMod.getId(itemName + "_from_nugget"));
            ShapelessRecipeBuilder.shapelessRecipe(nugget, 9)
                    .addIngredient(item)
                    .addCriterion("has_item", hasItem(item))
                    .build(consumer, ChaosMod.getId(nuggetName));
        }
    }
}
