package net.silentchaos512.gems.data.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.GemLampBlock;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.setup.GemsTags;
import net.silentchaos512.gems.setup.Gems;
import net.silentchaos512.lib.data.recipe.LibRecipeProvider;
import net.silentchaos512.lib.util.NameUtils;

public class GemsRecipeProvider extends LibRecipeProvider {
    public GemsRecipeProvider(HolderLookup.Provider registries, RecipeOutput recipeOutput) {
        super(registries, recipeOutput, SilentGems.MOD_ID);
    }

    @Override
    protected void buildRecipes() {
        registerGemRecipes();
        registerMetals();
        registerFoods();
        registerMisc();
    }

    private void registerGemRecipes() {
        for (Gems gem : Gems.values()) {
            String name = gem.getName();

            smeltingAndBlastingRecipes(this.output, name, gem.getModOresItemTag(), gem.getItem(), 1.0f);
            compressionRecipes(this.output, gem.getBlock(), gem.getItem(), null);

            shaped(RecipeCategory.BUILDING_BLOCKS, gem.getBricks(), 12)
                    .pattern("###")
                    .pattern("#o#")
                    .pattern("###")
                    .define('#', Blocks.STONE_BRICKS)
                    .define('o', gem.getItemTag())
                    .unlockedBy("has_item", has(gem.getItemTag()))
                    .save(this.output);

            shaped(RecipeCategory.BUILDING_BLOCKS, gem.getTiles(), 12)
                    .pattern("###")
                    .pattern("#o#")
                    .pattern("###")
                    .define('#', Blocks.DEEPSLATE_TILES)
                    .define('o', gem.getItemTag())
                    .unlockedBy("has_item", has(gem.getItemTag()))
                    .save(this.output);

            shaped(RecipeCategory.BUILDING_BLOCKS, gem.getSmallBricks(), 12)
                    .pattern("###")
                    .pattern("#o#")
                    .pattern("###")
                    .define('#', Blocks.BRICKS)
                    .define('o', gem.getItemTag())
                    .unlockedBy("has_item", has(gem.getItemTag()))
                    .save(this.output);

            shaped(RecipeCategory.BUILDING_BLOCKS, gem.getPolishedStone(), 12)
                    .pattern("###")
                    .pattern("#o#")
                    .pattern("###")
                    .define('#', Blocks.POLISHED_DIORITE)
                    .define('o', gem.getItemTag())
                    .unlockedBy("has_item", has(gem.getItemTag()))
                    .save(this.output);

            shaped(RecipeCategory.BUILDING_BLOCKS, gem.getSmoothStone(), 12)
                    .pattern("###")
                    .pattern("#o#")
                    .pattern("###")
                    .define('#', Blocks.SMOOTH_STONE)
                    .define('o', gem.getItemTag())
                    .unlockedBy("has_item", has(gem.getItemTag()))
                    .save(this.output);

            shaped(RecipeCategory.BUILDING_BLOCKS, gem.getChiseledStone(), 12)
                    .pattern("###")
                    .pattern("#o#")
                    .pattern("###")
                    .define('#', Blocks.CHISELED_STONE_BRICKS)
                    .define('o', gem.getItemTag())
                    .unlockedBy("has_item", has(gem.getItemTag()))
                    .save(this.output);

            shaped(RecipeCategory.BUILDING_BLOCKS, gem.getGlass(), 12)
                    .pattern("###")
                    .pattern("#o#")
                    .pattern("###")
                    .define('#', Tags.Items.GLASS_BLOCKS_COLORLESS)
                    .define('o', gem.getItemTag())
                    .unlockedBy("has_item", has(gem.getItemTag()))
                    .save(this.output);

            shaped(RecipeCategory.BUILDING_BLOCKS, gem.getLamp(GemLampBlock.State.OFF))
                    .pattern("rgr")
                    .pattern("gog")
                    .pattern("rgr")
                    .define('r', Tags.Items.DUSTS_REDSTONE)
                    .define('g', Tags.Items.DUSTS_GLOWSTONE)
                    .define('o', gem.getItemTag())
                    .unlockedBy("has_item", has(gem.getItemTag()))
                    .save(this.output);

            shapeless(RecipeCategory.BUILDING_BLOCKS, gem.getLamp(GemLampBlock.State.INVERTED_ON))
                    .requires(gem.getLamp(GemLampBlock.State.OFF))
                    .requires(Items.REDSTONE_TORCH)
                    .unlockedBy("has_item", has(gem.getItemTag()))
                    .save(this.output);

            shaped(RecipeCategory.BUILDING_BLOCKS, gem.getTeleporter(), 2)
                    .pattern("igi")
                    .pattern("gcg")
                    .pattern(" e ")
                    .define('c', GemsBlocks.CHAOS_ESSENCE_BLOCK)
                    .define('g', gem.getItemTag())
                    .define('e', Tags.Items.ENDER_PEARLS)
                    .define('i', Tags.Items.INGOTS_GOLD)
                    .unlockedBy("has_item", has(GemsItems.CHAOS_ESSENCE))
                    .save(this.output);

            shapeless(RecipeCategory.BUILDING_BLOCKS, gem.getRedstoneTeleporter())
                    .requires(gem.getTeleporter())
                    .requires(Tags.Items.DUSTS_REDSTONE)
                    .unlockedBy("has_item", has(gem.getTeleporter()))
                    .save(this.output);
        }

        // Not a gem thing, but it is related to gem teleporters
        shaped(RecipeCategory.BUILDING_BLOCKS, GemsBlocks.TELEPORTER_ANCHOR, 4)
                .pattern("igi")
                .pattern("gcg")
                .pattern(" e ")
                .define('c', GemsBlocks.CHAOS_ESSENCE_BLOCK)
                .define('i', Tags.Items.INGOTS_GOLD)
                .define('e', Tags.Items.ENDER_PEARLS)
                .define('g', GemsTags.Items.INGOTS_SILVER)
                .unlockedBy("has_item", has(GemsItems.CHAOS_ESSENCE))
                .save(this.output);

        glowroseToDye(this.output, Gems.RUBY, Items.RED_DYE);
        glowroseToDye(this.output, Gems.CARNELIAN, Items.RED_DYE);
        glowroseToDye(this.output, Gems.TOPAZ, Items.ORANGE_DYE);
        glowroseToDye(this.output, Gems.CITRINE, Items.YELLOW_DYE);
        glowroseToDye(this.output, Gems.HELIODOR, Items.YELLOW_DYE);
        glowroseToDye(this.output, Gems.MOLDAVITE, Items.LIME_DYE);
        glowroseToDye(this.output, Gems.PERIDOT, Items.GREEN_DYE);
        glowroseToDye(this.output, Gems.TURQUOISE, Items.CYAN_DYE);
        glowroseToDye(this.output, Gems.KYANITE, Items.LIGHT_BLUE_DYE);
        glowroseToDye(this.output, Gems.SAPPHIRE, Items.BLUE_DYE);
        glowroseToDye(this.output, Gems.IOLITE, Items.PURPLE_DYE);
        glowroseToDye(this.output, Gems.ALEXANDRITE, Items.PURPLE_DYE);
        glowroseToDye(this.output, Gems.AMMOLITE, Items.MAGENTA_DYE);
        glowroseToDye(this.output, Gems.ROSE_QUARTZ, Items.PINK_DYE);
        glowroseToDye(this.output, Gems.BLACK_DIAMOND, Items.BLACK_DYE);
        glowroseToDye(this.output, Gems.WHITE_DIAMOND, Items.WHITE_DYE);
        glowroseToDye(this.output, Gems.AQUAMARINE, Items.LIGHT_BLUE_DYE);
        glowroseToDye(this.output, Gems.GARNET, Items.RED_DYE);
        glowroseToDye(this.output, Gems.OPAL, Items.WHITE_DYE);
        glowroseToDye(this.output, Gems.PEARL, Items.WHITE_DYE);
        glowroseToDye(this.output, Gems.TANZANITE, Items.PURPLE_DYE);
    }

    private void glowroseToDye(RecipeOutput consumer, Gems gem, ItemLike dye) {
        String dyeName = NameUtils.fromItem(dye).getPath();
        String glowroseName = NameUtils.fromItem(gem.getGlowrose()).getPath();
        shapeless(RecipeCategory.MISC, dye, 1)
                .requires(gem.getGlowroseItemTag())
                .unlockedBy("has_item", has(gem.getGlowroseItemTag()))
                .save(consumer, modId(dyeName + "_from_" + glowroseName));
    }

    private void registerMetals() {
        smeltingAndBlastingRecipes(this.output, "silver_ingot", GemsItems.RAW_SILVER.get(), GemsItems.SILVER_INGOT.get(), 1.0f);
        compressionRecipes(this.output, GemsBlocks.SILVER_BLOCK.get(), GemsItems.SILVER_INGOT.get(), GemsItems.SILVER_NUGGET.get());
        shaped(RecipeCategory.MISC, GemsItems.SILVER_ROD, 4)
                .pattern("#")
                .pattern("#")
                .define('#', GemsTags.Items.INGOTS_SILVER)
                .unlockedBy("has_item", has(GemsTags.Items.INGOTS_SILVER))
                .save(this.output);

        compressionRecipes(this.output, GemsBlocks.CHAOS_ESSENCE_BLOCK.get(), GemsItems.CHAOS_ESSENCE.get(), null);

        shaped(RecipeCategory.MISC, GemsItems.REINFORCED_GOLD_ROD, 2)
                .pattern("/")
                .pattern("i")
                .pattern("c")
                .define('/', Tags.Items.INGOTS_GOLD)
                .define('i', Tags.Items.INGOTS_IRON)
                .define('c', GemsItems.CHAOS_ESSENCE)
                .unlockedBy("has_item", has(GemsItems.CHAOS_ESSENCE))
                .save(this.output);
        shaped(RecipeCategory.MISC, GemsItems.REINFORCED_SILVER_ROD, 2)
                .pattern("/")
                .pattern("i")
                .pattern("c")
                .define('/', GemsTags.Items.INGOTS_SILVER)
                .define('i', Tags.Items.INGOTS_IRON)
                .define('c', GemsItems.CHAOS_ESSENCE)
                .unlockedBy("has_item", has(GemsItems.CHAOS_ESSENCE))
                .save(this.output);
    }

    private void registerFoods() {
        shaped(RecipeCategory.FOOD, GemsItems.POTATO_ON_A_STICK)
                .pattern(" p")
                .pattern("/ ")
                .define('p', Items.BAKED_POTATO)
                .define('/', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(Items.BAKED_POTATO))
                .save(this.output);
        shaped(RecipeCategory.FOOD, GemsItems.SUGAR_COOKIE, 8)
                .pattern(" S ")
                .pattern("///")
                .pattern(" S ")
                .define('S', Items.SUGAR)
                .define('/', Items.WHEAT)
                .unlockedBy("has_item", has(Items.SUGAR))
                .save(this.output);
        shaped(RecipeCategory.FOOD, GemsItems.IRON_POTATO)
                .pattern("/#/")
                .pattern("#p#")
                .pattern("/#/")
                .define('/', Tags.Items.GEMS)
                .define('#', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('p', Items.POTATO)
                .unlockedBy("has_item", has(Items.POTATO))
                .save(this.output);

        shapeless(RecipeCategory.FOOD, GemsItems.UNCOOKED_FISHY_STEW)
                .requires(Items.BOWL)
                .requires(GemsTags.Items.STEW_FISH)
                .requires(Items.DRIED_KELP)
                .requires(Items.BROWN_MUSHROOM)
                .unlockedBy("has_item", has(GemsTags.Items.STEW_FISH))
                .save(this.output);
        shapeless(RecipeCategory.FOOD, GemsItems.UNCOOKED_MEATY_STEW)
                .requires(Items.BOWL)
                .requires(GemsTags.Items.STEW_MEAT)
                .requires(Items.POTATO)
                .requires(Items.CARROT)
                .unlockedBy("has_item", has(GemsTags.Items.STEW_MEAT))
                .save(this.output);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(GemsItems.UNCOOKED_FISHY_STEW), RecipeCategory.FOOD, GemsItems.FISHY_STEW, 0.45f, 200)
                .unlockedBy("has_item", has(GemsTags.Items.STEW_FISH))
                .save(this.output);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(GemsItems.UNCOOKED_MEATY_STEW), RecipeCategory.FOOD, GemsItems.MEATY_STEW, 0.45f, 200)
                .unlockedBy("has_item", has(GemsTags.Items.STEW_MEAT))
                .save(this.output);
    }

    private void registerMisc() {
        shapeless(RecipeCategory.MISC, GemsItems.SPARKLING_BONE_MEAL, 8)
                .requires(GemsTags.Items.GEMS)
                .requires(Items.BONE_MEAL, 3)
                .unlockedBy("has_item", has(GemsTags.Items.GEMS))
                .save(this.output);

        shaped(RecipeCategory.TOOLS, GemsItems.TELEPORTER_LINKER)
                .pattern("#")
                .pattern("/")
                .define('#', GemsItems.CHAOS_ESSENCE)
                .define('/', GemsTags.Items.RODS_SILVER)
                .unlockedBy("has_item", has(GemsItems.CHAOS_ESSENCE))
                .save(this.output);

        shaped(RecipeCategory.MISC, GemsItems.SUMMON_KITTY)
                .pattern("|f|")
                .pattern("|g|")
                .pattern("|f|")
                .define('|', Tags.Items.STRINGS)
                .define('f', GemsTags.Items.STEW_FISH)
                .define('g', GemsTags.Items.GEMS)
                .unlockedBy("has_item", has(GemsTags.Items.GEMS))
                .save(this.output);

        shaped(RecipeCategory.MISC, GemsItems.SUMMON_PUPPY)
                .pattern(" m ")
                .pattern("#g#")
                .pattern(" m ")
                .define('m', GemsTags.Items.STEW_FISH)
                .define('#', Tags.Items.LEATHERS)
                .define('g', GemsTags.Items.GEMS)
                .unlockedBy("has_item", has(GemsTags.Items.GEMS))
                .save(this.output);
    }
}
