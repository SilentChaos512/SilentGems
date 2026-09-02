package net.silentchaos512.gems.data.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.GemLampBlock;
import net.silentchaos512.gems.setup.Gems;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.setup.GemsTags;
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

        glowroseToDye(this.output, Gems.RUBY, dye("red"));
        glowroseToDye(this.output, Gems.CARNELIAN, dye("red"));
        glowroseToDye(this.output, Gems.TOPAZ, dye("orange"));
        glowroseToDye(this.output, Gems.CITRINE, dye("yellow"));
        glowroseToDye(this.output, Gems.HELIODOR, dye("yellow"));
        glowroseToDye(this.output, Gems.MOLDAVITE, dye("lime"));
        glowroseToDye(this.output, Gems.PERIDOT, dye("green"));
        glowroseToDye(this.output, Gems.TURQUOISE, dye("cyan"));
        glowroseToDye(this.output, Gems.KYANITE, dye("light_blue"));
        glowroseToDye(this.output, Gems.SAPPHIRE, dye("blue"));
        glowroseToDye(this.output, Gems.IOLITE, dye("purple"));
        glowroseToDye(this.output, Gems.ALEXANDRITE, dye("purple"));
        glowroseToDye(this.output, Gems.AMMOLITE, dye("magenta"));
        glowroseToDye(this.output, Gems.ROSE_QUARTZ, dye("pink"));
        glowroseToDye(this.output, Gems.BLACK_DIAMOND, dye("black"));
        glowroseToDye(this.output, Gems.WHITE_DIAMOND, dye("white"));
        glowroseToDye(this.output, Gems.AQUAMARINE, dye("light_blue"));
        glowroseToDye(this.output, Gems.GARNET, dye("red"));
        glowroseToDye(this.output, Gems.OPAL, dye("white"));
        glowroseToDye(this.output, Gems.PEARL, dye("white"));
        glowroseToDye(this.output, Gems.TANZANITE, dye("purple"));
    }

    private static Item dye(String color) {
        return BuiltInRegistries.ITEM.get(Identifier.withDefaultNamespace(color + "_dye")).orElseThrow().value();
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

        cookingRecipes("fishy_stew", GemsItems.UNCOOKED_FISHY_STEW, GemsItems.FISHY_STEW, 0.45f, 200);
        cookingRecipes("meaty_stew", GemsItems.UNCOOKED_MEATY_STEW, GemsItems.MEATY_STEW, 0.45f, 200);
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
