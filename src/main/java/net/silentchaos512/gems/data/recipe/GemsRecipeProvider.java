package net.silentchaos512.gems.data.recipe;

import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.block.GemLampBlock;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.setup.GemsTags;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.data.recipe.LibRecipeProvider;

import java.util.function.Consumer;

public class GemsRecipeProvider extends LibRecipeProvider {
    public GemsRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, GemsBase.MOD_ID);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        registerGemRecipes(consumer);
        registerMetals(consumer);
        registerFoods(consumer);
        registerMisc(consumer);
    }

    private void registerGemRecipes(Consumer<IFinishedRecipe> consumer) {
        for (Gems gem : Gems.values()) {
            String name = gem.getName();

            smeltingAndBlastingRecipes(consumer, name, gem.getModOresItemTag(), gem.getItem(), 1.0f);
            compressionRecipes(consumer, gem.getBlock(), gem.getItem(), null);

            shapedBuilder(gem.getBricks(), 12)
                    .patternLine("###")
                    .patternLine("#o#")
                    .patternLine("###")
                    .key('#', ItemTags.STONE_BRICKS)
                    .key('o', gem.getItemTag())
                    .addCriterion("has_item", hasItem(gem.getItemTag()))
                    .build(consumer);

            shapedBuilder(gem.getGlass(), 12)
                    .patternLine("###")
                    .patternLine("#o#")
                    .patternLine("###")
                    .key('#', Tags.Items.GLASS_COLORLESS)
                    .key('o', gem.getItemTag())
                    .addCriterion("has_item", hasItem(gem.getItemTag()))
                    .build(consumer);

            shapedBuilder(gem.getLamp(GemLampBlock.State.OFF))
                    .patternLine("rgr")
                    .patternLine("gog")
                    .patternLine("rgr")
                    .key('r', Tags.Items.DUSTS_REDSTONE)
                    .key('g', Tags.Items.DUSTS_GLOWSTONE)
                    .key('o', gem.getItemTag())
                    .addCriterion("has_item", hasItem(gem.getItemTag()))
                    .build(consumer);

            shapelessBuilder(gem.getLamp(GemLampBlock.State.INVERTED_ON))
                    .addIngredient(gem.getLamp(GemLampBlock.State.OFF))
                    .addIngredient(Items.REDSTONE_TORCH)
                    .addCriterion("has_item", hasItem(gem.getItemTag()))
                    .build(consumer);
        }
    }

    private void registerMetals(Consumer<IFinishedRecipe> consumer) {
        smeltingAndBlastingRecipes(consumer, "silver_ingot", GemsBlocks.SILVER_ORE.get(), GemsItems.SILVER_INGOT.get(), 1.0f);
        compressionRecipes(consumer, GemsBlocks.SILVER_BLOCK.get(), GemsItems.SILVER_INGOT.get(), GemsItems.SILVER_NUGGET.get());
    }

    private void registerFoods(Consumer<IFinishedRecipe> consumer) {
        shapedBuilder(GemsItems.POTATO_ON_A_STICK)
                .patternLine(" p")
                .patternLine("/ ")
                .key('p', Items.BAKED_POTATO)
                .key('/', Tags.Items.RODS_WOODEN)
                .addCriterion("has_item", hasItem(Items.BAKED_POTATO))
                .build(consumer);
        shapedBuilder(GemsItems.SUGAR_COOKIE, 8)
                .patternLine(" S ")
                .patternLine("///")
                .patternLine(" S ")
                .key('S', Items.SUGAR)
                .key('/', Items.WHEAT)
                .addCriterion("has_item", hasItem(Items.SUGAR))
                .build(consumer);
        shapedBuilder(GemsItems.IRON_POTATO)
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

    private void registerMisc(Consumer<IFinishedRecipe> consumer) {
        shapedBuilder(GemsItems.GEM_BAG)
                .patternLine("/~/")
                .patternLine("#g#")
                .patternLine("###")
                .key('~', Tags.Items.STRING)
                .key('/', Tags.Items.NUGGETS_GOLD)
                .key('#', ItemTags.WOOL)
                .key('g', Tags.Items.GEMS)
                .build(consumer);

        shapedBuilder(GemsItems.FLOWER_BASKET)
                .patternLine("/~/")
                .patternLine("#g#")
                .patternLine("###")
                .key('~', Tags.Items.STRING)
                .key('/', Tags.Items.NUGGETS_GOLD)
                .key('#', Ingredient.fromItems(Items.SUGAR_CANE, Items.BAMBOO))
                .key('g', GemsTags.Items.GLOWROSES)
                .build(consumer);

        shapedBuilder(GemsItems.SUMMON_KITTY)
                .patternLine("|f|")
                .patternLine("|g|")
                .patternLine("|f|")
                .key('|', Tags.Items.STRING)
                .key('f', GemsTags.Items.STEW_FISH)
                .key('g', GemsTags.Items.GEMS)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS))
                .build(consumer);

        shapedBuilder(GemsItems.SUMMON_PUPPY)
                .patternLine(" m ")
                .patternLine("#g#")
                .patternLine(" m ")
                .key('m', GemsTags.Items.STEW_FISH)
                .key('#', Tags.Items.LEATHER)
                .key('g', GemsTags.Items.GEMS)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS))
                .build(consumer);
    }
}
