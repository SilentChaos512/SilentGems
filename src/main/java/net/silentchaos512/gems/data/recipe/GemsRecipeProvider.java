package net.silentchaos512.gems.data.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.block.GemLampBlock;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.setup.GemsTags;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.data.recipe.LibRecipeProvider;
import net.silentchaos512.lib.util.NameUtils;

import java.util.function.Consumer;

public class GemsRecipeProvider extends LibRecipeProvider {
    public GemsRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, GemsBase.MOD_ID);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        registerGemRecipes(consumer);
        registerMetals(consumer);
        registerFoods(consumer);
        registerMisc(consumer);
    }

    private void registerGemRecipes(Consumer<FinishedRecipe> consumer) {
        for (Gems gem : Gems.values()) {
            String name = gem.getName();

            smeltingAndBlastingRecipes(consumer, name, gem.getModOresItemTag(), gem.getItem(), 1.0f);
            compressionRecipes(consumer, gem.getBlock(), gem.getItem(), null);

            shapedBuilder(RecipeCategory.BUILDING_BLOCKS, gem.getBricks(), 12)
                    .pattern("###")
                    .pattern("#o#")
                    .pattern("###")
                    .define('#', ItemTags.STONE_BRICKS)
                    .define('o', gem.getItemTag())
                    .unlockedBy("has_item", has(gem.getItemTag()))
                    .save(consumer);

            shapedBuilder(RecipeCategory.BUILDING_BLOCKS, gem.getGlass(), 12)
                    .pattern("###")
                    .pattern("#o#")
                    .pattern("###")
                    .define('#', Tags.Items.GLASS_COLORLESS)
                    .define('o', gem.getItemTag())
                    .unlockedBy("has_item", has(gem.getItemTag()))
                    .save(consumer);

            shapedBuilder(RecipeCategory.BUILDING_BLOCKS, gem.getLamp(GemLampBlock.State.OFF))
                    .pattern("rgr")
                    .pattern("gog")
                    .pattern("rgr")
                    .define('r', Tags.Items.DUSTS_REDSTONE)
                    .define('g', Tags.Items.DUSTS_GLOWSTONE)
                    .define('o', gem.getItemTag())
                    .unlockedBy("has_item", has(gem.getItemTag()))
                    .save(consumer);

            shapelessBuilder(RecipeCategory.BUILDING_BLOCKS, gem.getLamp(GemLampBlock.State.INVERTED_ON))
                    .requires(gem.getLamp(GemLampBlock.State.OFF))
                    .requires(Items.REDSTONE_TORCH)
                    .unlockedBy("has_item", has(gem.getItemTag()))
                    .save(consumer);
        }

        glowroseToDye(consumer, Gems.RUBY, Items.RED_DYE);
        glowroseToDye(consumer, Gems.CARNELIAN, Items.RED_DYE);
        glowroseToDye(consumer, Gems.TOPAZ, Items.ORANGE_DYE);
        glowroseToDye(consumer, Gems.CITRINE, Items.YELLOW_DYE);
        glowroseToDye(consumer, Gems.HELIODOR, Items.YELLOW_DYE);
        glowroseToDye(consumer, Gems.MOLDAVITE, Items.LIME_DYE);
        glowroseToDye(consumer, Gems.PERIDOT, Items.GREEN_DYE);
        glowroseToDye(consumer, Gems.TURQUOISE, Items.CYAN_DYE);
        glowroseToDye(consumer, Gems.KYANITE, Items.LIGHT_BLUE_DYE);
        glowroseToDye(consumer, Gems.SAPPHIRE, Items.BLUE_DYE);
        glowroseToDye(consumer, Gems.IOLITE, Items.PURPLE_DYE);
        glowroseToDye(consumer, Gems.ALEXANDRITE, Items.PURPLE_DYE);
        glowroseToDye(consumer, Gems.AMMOLITE, Items.MAGENTA_DYE);
        glowroseToDye(consumer, Gems.ROSE_QUARTZ, Items.PINK_DYE);
        glowroseToDye(consumer, Gems.BLACK_DIAMOND, Items.BLACK_DYE);
        glowroseToDye(consumer, Gems.WHITE_DIAMOND, Items.WHITE_DYE);
    }

    private void glowroseToDye(Consumer<FinishedRecipe> consumer, Gems gem, ItemLike dye) {
        String dyeName = NameUtils.fromItem(dye).getPath();
        String glowroseName = NameUtils.fromItem(gem.getGlowrose()).getPath();
        shapelessBuilder(RecipeCategory.MISC, dye, 1)
                .requires(gem.getGlowroseItemTag())
                .save(consumer, modId(dyeName + "_from_" + glowroseName));
    }

    private void registerMetals(Consumer<FinishedRecipe> consumer) {
        smeltingAndBlastingRecipes(consumer, "silver_ingot", GemsItems.RAW_SILVER.get(), GemsItems.SILVER_INGOT.get(), 1.0f);
        compressionRecipes(consumer, GemsBlocks.SILVER_BLOCK.get(), GemsItems.SILVER_INGOT.get(), GemsItems.SILVER_NUGGET.get());
    }

    private void registerFoods(Consumer<FinishedRecipe> consumer) {
        shapedBuilder(RecipeCategory.FOOD, GemsItems.POTATO_ON_A_STICK)
                .pattern(" p")
                .pattern("/ ")
                .define('p', Items.BAKED_POTATO)
                .define('/', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(Items.BAKED_POTATO))
                .save(consumer);
        shapedBuilder(RecipeCategory.FOOD, GemsItems.SUGAR_COOKIE, 8)
                .pattern(" S ")
                .pattern("///")
                .pattern(" S ")
                .define('S', Items.SUGAR)
                .define('/', Items.WHEAT)
                .unlockedBy("has_item", has(Items.SUGAR))
                .save(consumer);
        shapedBuilder(RecipeCategory.FOOD, GemsItems.IRON_POTATO)
                .pattern("/#/")
                .pattern("#p#")
                .pattern("/#/")
                .define('/', Tags.Items.GEMS)
                .define('#', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('p', Items.POTATO)
                .unlockedBy("has_item", has(Items.POTATO))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, GemsItems.UNCOOKED_FISHY_STEW)
                .requires(Items.BOWL)
                .requires(GemsTags.Items.STEW_FISH)
                .requires(Items.DRIED_KELP)
                .requires(Items.BROWN_MUSHROOM)
                .unlockedBy("has_item", has(GemsTags.Items.STEW_FISH))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, GemsItems.UNCOOKED_MEATY_STEW)
                .requires(Items.BOWL)
                .requires(GemsTags.Items.STEW_MEAT)
                .requires(Items.POTATO)
                .requires(Items.CARROT)
                .unlockedBy("has_item", has(GemsTags.Items.STEW_MEAT))
                .save(consumer);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(GemsItems.UNCOOKED_FISHY_STEW), RecipeCategory.FOOD, GemsItems.FISHY_STEW, 0.45f, 200)
                .unlockedBy("has_item", has(GemsTags.Items.STEW_FISH))
                .save(consumer);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(GemsItems.UNCOOKED_MEATY_STEW), RecipeCategory.FOOD, GemsItems.MEATY_STEW, 0.45f, 200)
                .unlockedBy("has_item", has(GemsTags.Items.STEW_MEAT))
                .save(consumer);
    }

    private void registerMisc(Consumer<FinishedRecipe> consumer) {
        shapedBuilder(RecipeCategory.MISC, GemsItems.GEM_BAG)
                .pattern("/~/")
                .pattern("#g#")
                .pattern("###")
                .define('~', Tags.Items.STRING)
                .define('/', Tags.Items.NUGGETS_GOLD)
                .define('#', ItemTags.WOOL)
                .define('g', Tags.Items.GEMS)
                .save(consumer);

        shapedBuilder(RecipeCategory.MISC, GemsItems.FLOWER_BASKET)
                .pattern("/~/")
                .pattern("#g#")
                .pattern("###")
                .define('~', Tags.Items.STRING)
                .define('/', Tags.Items.NUGGETS_GOLD)
                .define('#', Ingredient.of(Items.SUGAR_CANE, Items.BAMBOO))
                .define('g', GemsTags.Items.GLOWROSES)
                .save(consumer);

        shapedBuilder(RecipeCategory.MISC, GemsItems.SOUL_GEM)
                .pattern(" g ")
                .pattern("#d#")
                .pattern(" o ")
                .define('d', Tags.Items.GEMS_DIAMOND)
                .define('o', Items.CHORUS_FRUIT)
                .define('g', GemsTags.Items.GEMS)
                .define('#', GemsTags.Items.INGOTS_SILVER)
                .save(consumer);

        shapedBuilder(RecipeCategory.MISC, GemsItems.SUMMON_KITTY)
                .pattern("|f|")
                .pattern("|g|")
                .pattern("|f|")
                .define('|', Tags.Items.STRING)
                .define('f', GemsTags.Items.STEW_FISH)
                .define('g', GemsTags.Items.GEMS)
                .unlockedBy("has_item", has(GemsTags.Items.GEMS))
                .save(consumer);

        shapedBuilder(RecipeCategory.MISC, GemsItems.SUMMON_PUPPY)
                .pattern(" m ")
                .pattern("#g#")
                .pattern(" m ")
                .define('m', GemsTags.Items.STEW_FISH)
                .define('#', Tags.Items.LEATHER)
                .define('g', GemsTags.Items.GEMS)
                .unlockedBy("has_item", has(GemsTags.Items.GEMS))
                .save(consumer);
    }
}
