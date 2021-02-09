package net.silentchaos512.gems.data.recipe;

import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.setup.GemsTags;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.data.ExtendedShapedRecipeBuilder;
import net.silentchaos512.lib.util.NameUtils;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class GemsRecipeProvider extends RecipeProvider {
    public GemsRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        registerGemRecipes(consumer);
        registerMetals(consumer);
        registerFoods(consumer);
    }

    private static void registerGemRecipes(Consumer<IFinishedRecipe> consumer) {
        for (Gems gem : Gems.values()) {
            String name = gem.getName();

            smeltingAndBlasting(consumer, name, gem.getModOresItemTag(), gem.getItem());
            compression(consumer, gem.getBlock(), gem.getItem(), null);

            ExtendedShapedRecipeBuilder.vanillaBuilder(gem.getBricks(), 12)
                    .patternLine("###")
                    .patternLine("#o#")
                    .patternLine("###")
                    .key('#', ItemTags.STONE_BRICKS)
                    .key('o', gem.getItemTag())
                    .addCriterion("has_item", hasItem(gem.getItemTag()))
                    .build(consumer);
        }
    }

    private static void registerMetals(Consumer<IFinishedRecipe> consumer) {
        smeltingAndBlasting(consumer, "silver_ingot", GemsBlocks.SILVER_ORE.get(), GemsItems.SILVER_INGOT.get());
        compression(consumer, GemsBlocks.SILVER_BLOCK.get(), GemsItems.SILVER_INGOT.get(), GemsItems.SILVER_NUGGET.get());
    }

    private static void registerFoods(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(GemsItems.POTATO_ON_A_STICK)
                .patternLine(" p")
                .patternLine("/ ")
                .key('p', Items.BAKED_POTATO)
                .key('/', Tags.Items.RODS_WOODEN)
                .addCriterion("has_item", hasItem(Items.BAKED_POTATO))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GemsItems.SUGAR_COOKIE, 8)
                .patternLine(" S ")
                .patternLine("///")
                .patternLine(" S ")
                .key('S', Items.SUGAR)
                .key('/', Items.WHEAT)
                .addCriterion("has_item", hasItem(Items.SUGAR))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GemsItems.IRON_POTATO)
                .patternLine("/#/")
                .patternLine("#p#")
                .patternLine("/#/")
                .key('/', Tags.Items.GEMS)
                .key('#', Tags.Items.STORAGE_BLOCKS_IRON)
                .key('p', Items.POTATO)
                .addCriterion("has_item", hasItem(Items.POTATO))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(GemsItems.UNCOOKED_FISHY_STEW)
                .addIngredient(Items.BOWL)
                .addIngredient(GemsTags.Items.STEW_FISH)
                .addIngredient(Items.DRIED_KELP)
                .addIngredient(Items.BROWN_MUSHROOM)
                .addCriterion("has_item", hasItem(GemsTags.Items.STEW_FISH))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(GemsItems.UNCOOKED_MEATY_STEW)
                .addIngredient(Items.BOWL)
                .addIngredient(GemsTags.Items.STEW_MEAT)
                .addIngredient(Items.POTATO)
                .addIngredient(Items.CARROT)
                .addCriterion("has_item", hasItem(GemsTags.Items.STEW_MEAT))
                .build(consumer);

        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(GemsItems.UNCOOKED_FISHY_STEW), GemsItems.FISHY_STEW, 0.45f, 200)
                .addCriterion("has_item", hasItem(GemsTags.Items.STEW_FISH))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(GemsItems.UNCOOKED_MEATY_STEW), GemsItems.MEATY_STEW, 0.45f, 200)
                .addCriterion("has_item", hasItem(GemsTags.Items.STEW_MEAT))
                .build(consumer);
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

    private static void compression(Consumer<IFinishedRecipe> consumer, IItemProvider block, IItemProvider item, @Nullable IItemProvider nugget) {
        String blockName = NameUtils.fromItem(block).getPath();
        String itemName = NameUtils.fromItem(item).getPath();

        ShapedRecipeBuilder.shapedRecipe(block, 1)
                .patternLine("###")
                .patternLine("###")
                .patternLine("###")
                .key('#', item)
                .addCriterion("has_item", hasItem(item))
                .build(consumer, GemsBase.getId(itemName + "_from_block"));
        ShapelessRecipeBuilder.shapelessRecipe(item, 9)
                .addIngredient(block)
                .addCriterion("has_item", hasItem(item))
                .build(consumer, GemsBase.getId(blockName));

        if (nugget != null) {
            String nuggetName = NameUtils.fromItem(nugget).getPath();

            ShapedRecipeBuilder.shapedRecipe(item, 1)
                    .patternLine("###")
                    .patternLine("###")
                    .patternLine("###")
                    .key('#', nugget)
                    .addCriterion("has_item", hasItem(item))
                    .build(consumer, GemsBase.getId(itemName + "_from_nugget"));
            ShapelessRecipeBuilder.shapelessRecipe(nugget, 9)
                    .addIngredient(item)
                    .addCriterion("has_item", hasItem(item))
                    .build(consumer, GemsBase.getId(nuggetName));
        }
    }
}
