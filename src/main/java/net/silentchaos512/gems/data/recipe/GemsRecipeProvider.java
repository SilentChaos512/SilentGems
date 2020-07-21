package net.silentchaos512.gems.data.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.CorruptedBlocks;
import net.silentchaos512.gems.block.GemLampBlock;
import net.silentchaos512.gems.block.MiscBlocks;
import net.silentchaos512.gems.block.MiscOres;
import net.silentchaos512.gems.crafting.ingredient.SoulGemIngredient;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.init.GemsItems;
import net.silentchaos512.gems.init.GemsRecipeInit;
import net.silentchaos512.gems.init.GemsTags;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.item.ModFoods;
import net.silentchaos512.gems.item.SoulUrnUpgrades;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.soul.Soul;
import net.silentchaos512.lib.data.ExtendedShapedRecipeBuilder;
import net.silentchaos512.lib.util.NameUtils;
import net.silentchaos512.utils.Color;

import java.nio.file.Path;
import java.util.function.Consumer;

public class GemsRecipeProvider extends RecipeProvider {
    public GemsRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public String getName() {
        return "Silent's Gems - Recipes";
    }

    @Override
    protected void saveRecipeAdvancement(DirectoryCache cache, JsonObject advancementJson, Path pathIn) {
        // Skip all recipe advancements for now...
//        super.saveRecipeAdvancement(cache, advancementJson, pathIn);
    }

    @SuppressWarnings("OverlyLongMethod")
    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        registerCraftingItemsAndRandoms(consumer);
        registerGemRecipes(consumer);
        registerSpecialRecipes(consumer);
        registerTokenEnchanting(consumer);
        registerFluffyStuff(consumer);

        smeltingAndBlasting(consumer, "chaos_ore", MiscOres.CHAOS, CraftingItems.CHAOS_CRYSTAL);
        smeltingAndBlasting(consumer, "ender_ore", MiscOres.ENDER, CraftingItems.ENDER_CRYSTAL);
        smeltingAndBlasting(consumer, "silver_ore", MiscOres.SILVER, CraftingItems.SILVER_INGOT);

        pedestal(consumer, GemsBlocks.ANDESITE_PEDESTAL, Ingredient.fromItems(Items.ANDESITE));
        pedestal(consumer, GemsBlocks.GRANITE_PEDESTAL, Ingredient.fromItems(Items.GRANITE));
        pedestal(consumer, GemsBlocks.DIORITE_PEDESTAL, Ingredient.fromItems(Items.DIORITE));
        pedestal(consumer, GemsBlocks.STONE_PEDESTAL, Ingredient.fromItems(Items.STONE));
        pedestal(consumer, GemsBlocks.OBSIDIAN_PEDESTAL, Ingredient.fromTag(Tags.Items.OBSIDIAN));

        soulUrn(consumer, Blocks.TERRACOTTA, 0x985F45, "");
        soulUrn(consumer, Blocks.WHITE_TERRACOTTA, 0xD1B1A1, "white");
        soulUrn(consumer, Blocks.ORANGE_TERRACOTTA, 0xA05325, "orange");
        soulUrn(consumer, Blocks.MAGENTA_TERRACOTTA, 0x95576C, "magenta");
        soulUrn(consumer, Blocks.LIGHT_BLUE_TERRACOTTA, 0x706C8A, "light_blue");
        soulUrn(consumer, Blocks.YELLOW_TERRACOTTA, 0xB98423, "yellow");
        soulUrn(consumer, Blocks.LIME_TERRACOTTA, 0x677534, "lime");
        soulUrn(consumer, Blocks.PINK_TERRACOTTA, 0xA04D4E, "pink");
        soulUrn(consumer, Blocks.GRAY_TERRACOTTA, 0x392A24, "gray");
        soulUrn(consumer, Blocks.LIGHT_GRAY_TERRACOTTA, 0x876A61, "light_gray");
        soulUrn(consumer, Blocks.CYAN_TERRACOTTA, 0x565A5B, "cyan");
        soulUrn(consumer, Blocks.PURPLE_TERRACOTTA, 0x764556, "purple");
        soulUrn(consumer, Blocks.BLUE_TERRACOTTA, 0x4A3B5B, "blue");
        soulUrn(consumer, Blocks.BROWN_TERRACOTTA, 0x4D3224, "brown");
        soulUrn(consumer, Blocks.GREEN_TERRACOTTA, 0x4B522A, "green");
        soulUrn(consumer, Blocks.RED_TERRACOTTA, 0x8E3C2E, "red");
        soulUrn(consumer, Blocks.BLACK_TERRACOTTA, 0x251610, "black");

        ShapedRecipeBuilder.shapedRecipe(GemsItems.CHAOS_POTATO)
                .patternLine(" / ")
                .patternLine("/#/")
                .patternLine(" / ")
                .key('/', CraftingItems.CHAOS_CRYSTAL_SHARD)
                .key('#', Items.POTATO)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS_CHAOS))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GemsItems.FRAGILE_CHAOS_ORB)
                .patternLine(" / ")
                .patternLine("/#/")
                .patternLine(" / ")
                .key('/', Tags.Items.GLASS)
                .key('#', GemsTags.Items.GEMS_CHAOS)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS_CHAOS))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GemsItems.REFINED_CHAOS_ORB)
                .patternLine("/")
                .patternLine("#")
                .patternLine("/")
                .key('/', CraftingItems.ENRICHED_CHAOS_CRYSTAL)
                .key('#', GemsItems.FRAGILE_CHAOS_ORB)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS_CHAOS))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(GemsItems.PERFECT_CHAOS_ORB)
                .addIngredient(GemsItems.REFINED_CHAOS_ORB)
                .addIngredient(GemsItems.SOUL_GEM)
                .addIngredient(GemsItems.PURIFYING_POWDER)
                .addIngredient(GemsItems.PURIFYING_POWDER)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS_CHAOS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(CraftingItems.CHARGING_AGENT, 2)
                .patternLine("#g#")
                .patternLine("#l#")
                .patternLine("#g#")
                .key('#', GemsTags.Items.GEMS_CHAOS)
                .key('g', Tags.Items.DUSTS_GLOWSTONE)
                .key('l', Tags.Items.GEMS_LAPIS)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS_CHAOS))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(CraftingItems.SUPER_CHARGING_AGENT, 2)
                .patternLine("#f#")
                .patternLine("aea")
                .patternLine("#f#")
                .key('#', GemsTags.Items.GEMS_CHAOS)
                .key('f', Items.CHORUS_FRUIT)
                .key('a', CraftingItems.CHARGING_AGENT)
                .key('e', Items.ENDER_EYE)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS_CHAOS))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(CraftingItems.ULTRA_CHARGING_AGENT, 2)
                .patternLine("#e#")
                .patternLine("ana")
                .patternLine("#e#")
                .key('#', GemsTags.Items.GEMS_CHAOS)
                .key('e', CraftingItems.ENDER_CRYSTAL)
                .key('a', CraftingItems.SUPER_CHARGING_AGENT)
                .key('n', CraftingItems.NETHER_STAR_SHARD)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS_CHAOS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(GemsItems.CHAOS_METER)
                .patternLine("#c#")
                .patternLine("#/#")
                .patternLine("d d")
                .key('#', Tags.Items.INGOTS_GOLD)
                .key('c', CraftingItems.ENRICHED_CHAOS_CRYSTAL)
                .key('/', Tags.Items.INGOTS_IRON)
                .key('d', GemsTags.Items.CORRUPTED_DUSTS)
                .addCriterion("has_item", hasItem(GemsTags.Items.CORRUPTED_DUSTS))
                .build(consumer);

        for (CorruptedBlocks block : CorruptedBlocks.values()) {
            ShapedRecipeBuilder.shapedRecipe(block)
                    .patternLine("##")
                    .patternLine("##")
                    .key('#', block.getPile())
                    .addCriterion("has_item", hasItem(block.getPile()))
                    .build(consumer);
        }

        ShapedRecipeBuilder.shapedRecipe(GemsBlocks.LUMINOUS_FLOWER_POT)
                .patternLine("c")
                .patternLine("#")
                .key('c', CraftingItems.ENRICHED_CHAOS_CRYSTAL)
                .key('#', Items.FLOWER_POT)
                .addCriterion("has_item", hasItem(CraftingItems.ENRICHED_CHAOS_CRYSTAL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(GemsBlocks.PURIFIER)
                .patternLine("ada")
                .patternLine("clc")
                .patternLine("###")
                .key('a', GemsItems.PURIFYING_POWDER)
                .key('d', Tags.Items.GEMS_DIAMOND)
                .key('c', CraftingItems.CHAOS_IRON_INGOT)
                .key('l', Items.GLOWSTONE)
                .key('#', Tags.Items.OBSIDIAN)
                .addCriterion("has_item", hasItem(CraftingItems.ENRICHED_CHAOS_CRYSTAL))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GemsBlocks.SUPERCHARGER)
                .patternLine("ada")
                .patternLine("clc")
                .patternLine("###")
                .key('a', GemsTags.Items.INGOTS_SILVER)
                .key('d', Tags.Items.GEMS_DIAMOND)
                .key('c', CraftingItems.CHAOS_IRON_INGOT)
                .key('l', Tags.Items.STORAGE_BLOCKS_LAPIS)
                .key('#', Tags.Items.OBSIDIAN)
                .addCriterion("has_item", hasItem(CraftingItems.CHAOS_IRON_INGOT))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GemsBlocks.TOKEN_ENCHANTER)
                .patternLine("oeo")
                .patternLine("clc")
                .patternLine("###")
                .key('o', CraftingItems.BLANK_TOKEN)
                .key('e', Tags.Items.GEMS_EMERALD)
                .key('c', CraftingItems.CHAOS_IRON_INGOT)
                .key('l', Tags.Items.STORAGE_BLOCKS_LAPIS)
                .key('#', Tags.Items.OBSIDIAN)
                .addCriterion("has_item", hasItem(CraftingItems.CHAOS_IRON_INGOT))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GemsBlocks.TRANSMUTATION_ALTAR)
                .patternLine("cdc")
                .patternLine("cgc")
                .patternLine("###")
                .key('c', CraftingItems.ENRICHED_CHAOS_CRYSTAL)
                .key('d', Tags.Items.GEMS_DIAMOND)
                .key('g', Items.GLOWSTONE)
                .key('#', Tags.Items.OBSIDIAN)
                .addCriterion("has_item", hasItem(CraftingItems.ENRICHED_CHAOS_CRYSTAL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(GemsBlocks.TELEPORTER_ANCHOR, 4)
                .patternLine("cec")
                .patternLine(" # ")
                .patternLine("cec")
                .key('c', CraftingItems.ENRICHED_CHAOS_CRYSTAL)
                .key('e', Tags.Items.ENDER_PEARLS)
                .key('#', Tags.Items.STORAGE_BLOCKS_IRON)
                .addCriterion("has_item", hasItem(CraftingItems.ENRICHED_CHAOS_CRYSTAL))
                .build(consumer, SilentGems.getId("teleporter/anchor"));
        ShapedRecipeBuilder.shapedRecipe(GemsItems.TELEPORTER_LINKER)
                .patternLine("#")
                .patternLine("/")
                .patternLine("/")
                .key('#', CraftingItems.ENRICHED_CHAOS_CRYSTAL)
                .key('/', Tags.Items.INGOTS_GOLD)
                .addCriterion("has_item", hasItem(CraftingItems.ENRICHED_CHAOS_CRYSTAL))
                .build(consumer);

        ExtendedShapedRecipeBuilder.builder(GemsRecipeInit.GEAR_SOUL_RECIPE.get(), GemsItems.GEAR_SOUL)
                .patternLine(" # ")
                .patternLine("#s#")
                .patternLine(" # ")
                .key('#', GemsItems.SOUL_GEM)
                .key('s', CraftingItems.SOUL_SHELL)
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(GemsItems.GEM_BAG)
                .patternLine("~~~")
                .patternLine("#g#")
                .patternLine("###")
                .key('~', CraftingItems.GILDED_STRING)
                .key('#', ItemTags.WOOL)
                .key('g', Tags.Items.GEMS)
                .addCriterion("has_item", hasItem(Tags.Items.GEMS))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GemsItems.GLOWROSE_BASKET)
                .patternLine("~~~")
                .patternLine("#g#")
                .patternLine("###")
                .key('~', CraftingItems.GILDED_STRING)
                .key('#', Ingredient.fromItems(Items.SUGAR_CANE, Items.BAMBOO))
                .key('g', GemsTags.Items.GLOWROSES)
                .addCriterion("has_item", hasItem(GemsTags.Items.GLOWROSES))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(GemsItems.GLOWROSE_FERTILIZER, 4)
                .patternLine("###")
                .patternLine("#/#")
                .patternLine("###")
                .key('#', Items.BONE_MEAL)
                .key('/', GemsTags.Items.GEMS_CHAOS)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS_CHAOS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(GemsItems.SUMMON_KITTY)
                .patternLine("|f|")
                .patternLine("|g|")
                .patternLine("|f|")
                .key('|', Tags.Items.STRING)
                .key('f', GemsTags.Items.STEW_FISH)
                .key('g', GemsTags.Items.GEMS)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GemsItems.SUMMON_PUPPY)
                .patternLine(" m ")
                .patternLine("#g#")
                .patternLine(" m ")
                .key('m', GemsTags.Items.STEW_FISH)
                .key('#', Tags.Items.LEATHER)
                .key('g', GemsTags.Items.GEMS)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS))
                .build(consumer);
    }

    private void registerCraftingItemsAndRandoms(Consumer<IFinishedRecipe> consumer) {
        compression(consumer, CraftingItems.CHAOS_CRYSTAL, CraftingItems.CHAOS_CRYSTAL_SHARD);
        compression(consumer, CraftingItems.ENDER_CRYSTAL, CraftingItems.ENDER_CRYSTAL_SHARD);
        compression(consumer, CraftingItems.SILVER_INGOT, CraftingItems.SILVER_NUGGET);

        compression(consumer, MiscBlocks.CHAOS_COAL, CraftingItems.CHAOS_COAL, "storage_blocks/chaos_coal");
        compression(consumer, MiscBlocks.CHAOS_CRYSTAL, CraftingItems.CHAOS_CRYSTAL, "storage_blocks/chaos_crystal");
        compression(consumer, MiscBlocks.ENRICHED_CHAOS_CRYSTAL, CraftingItems.ENRICHED_CHAOS_CRYSTAL, "storage_blocks/enriched_chaos_crystal");
        compression(consumer, MiscBlocks.ENDER_CRYSTAL, CraftingItems.ENDER_CRYSTAL, "storage_blocks/ender_crystal");
        compression(consumer, MiscBlocks.SILVER, CraftingItems.SILVER_INGOT, "storage_blocks/silver");
        compression(consumer, MiscBlocks.CHAOS_IRON, CraftingItems.CHAOS_IRON_INGOT, "storage_blocks/chaos_iron");
        compression(consumer, MiscBlocks.CHAOS_GOLD, CraftingItems.CHAOS_GOLD_INGOT, "storage_blocks/chaos_gold");
        compression(consumer, MiscBlocks.CHAOS_SILVER, CraftingItems.CHAOS_SILVER_INGOT, "storage_blocks/chaos_silver");

        chaosMetalDust(consumer, CraftingItems.CHAOS_IRON_DUST, CraftingItems.CHAOS_IRON_INGOT, Tags.Items.INGOTS_IRON);
        chaosMetalDust(consumer, CraftingItems.CHAOS_GOLD_DUST, CraftingItems.CHAOS_GOLD_INGOT, Tags.Items.INGOTS_GOLD);
        chaosMetalDust(consumer, CraftingItems.CHAOS_SILVER_DUST, CraftingItems.CHAOS_SILVER_INGOT, GemsTags.Items.INGOTS_SILVER);

        ShapedRecipeBuilder.shapedRecipe(CraftingItems.BLANK_TOKEN, 12)
                .patternLine("///")
                .patternLine("clc")
                .patternLine("///")
                .key('/', Tags.Items.INGOTS_GOLD)
                .key('c', GemsTags.Items.GEMS_CHAOS)
                .key('l', Tags.Items.GEMS_LAPIS)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS_CHAOS))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(CraftingItems.CHAOS_DUST, 2)
                .addIngredient(GemsTags.Items.GEMS_CHAOS)
                .addIngredient(GemsTags.Items.GEMS_CHAOS)
                .addCriterion("has_item", hasItem(GemsTags.Items.GEMS_CHAOS))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(CraftingItems.GILDED_STRING, 3)
                .patternLine("#|#")
                .patternLine("#|#")
                .patternLine("#|#")
                .key('#', Tags.Items.NUGGETS_GOLD)
                .key('|', Tags.Items.STRING)
                .addCriterion("has_item", hasItem(Tags.Items.NUGGETS_GOLD))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(CraftingItems.MYSTERY_GOO)
                .patternLine("k#k")
                .patternLine("#a#")
                .patternLine("k#k")
                .key('k', Items.KELP)
                .key('#', Items.MOSSY_COBBLESTONE)
                .key('a', Items.APPLE)
                .addCriterion("has_item", hasItem(Items.MOSSY_COBBLESTONE))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Items.NETHER_STAR)
                .patternLine("###")
                .patternLine("###")
                .patternLine("###")
                .key('#', CraftingItems.NETHER_STAR_SHARD)
                .addCriterion("has_item", hasItem(Items.NETHER_STAR))
                .build(consumer, SilentGems.getId("nether_star"));
        ShapedRecipeBuilder.shapedRecipe(CraftingItems.RUNE_SLATE, 2)
                .patternLine("#/#")
                .patternLine("/o/")
                .patternLine("#/#")
                .key('#', Tags.Items.STONE)
                .key('/', CraftingItems.ENRICHED_CHAOS_CRYSTAL)
                .key('o', GemsTags.Items.INGOTS_SILVER)
                .addCriterion("has_item", hasItem(CraftingItems.ENRICHED_CHAOS_CRYSTAL))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(CraftingItems.SOUL_SHELL, 2)
                .patternLine(" # ")
                .patternLine("#o#")
                .patternLine(" # ")
                .key('#', Tags.Items.GLASS)
                .key('o', Tags.Items.GEMS_DIAMOND)
                .addCriterion("has_item", hasItem(Tags.Items.GEMS_DIAMOND))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(Items.SLIME_BALL)
                .addIngredient(Tags.Items.SLIMEBALLS)
                .addCriterion("has_item", hasItem(Tags.Items.SLIMEBALLS))
                .build(consumer, SilentGems.getId("slime_ball"));

        // Foods
        ShapedRecipeBuilder.shapedRecipe(ModFoods.POTATO_ON_A_STICK)
                .patternLine(" p")
                .patternLine("/ ")
                .key('p', Items.BAKED_POTATO)
                .key('/', Tags.Items.RODS_WOODEN)
                .addCriterion("has_item", hasItem(Items.BAKED_POTATO))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModFoods.SUGAR_COOKIE, 8)
                .patternLine(" S ")
                .patternLine("///")
                .patternLine(" S ")
                .key('S', Items.SUGAR)
                .key('/', Items.WHEAT)
                .addCriterion("has_item", hasItem(Items.SUGAR))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModFoods.CANDY_CANE, 8)
                .patternLine("ss")
                .patternLine("rs")
                .patternLine(" s")
                .key('s', Items.SUGAR)
                .key('r', Tags.Items.DYES_RED)
                .addCriterion("has_item", hasItem(Items.SUGAR))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModFoods.IRON_POTATO)
                .patternLine("/#/")
                .patternLine("#p#")
                .patternLine("/#/")
                .key('/', CraftingItems.CHAOS_IRON_INGOT)
                .key('#', Tags.Items.STORAGE_BLOCKS_IRON)
                .key('p', Items.POTATO)
                .addCriterion("has_item", hasItem(Items.POTATO))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModFoods.SECRET_DONUT, 6)
                .patternLine("///")
                .patternLine("/m/")
                .patternLine("///")
                .key('/', Items.WHEAT)
                .key('m', Items.RED_MUSHROOM)
                .addCriterion("has_item", hasItem(Items.RED_MUSHROOM))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModFoods.UNCOOKED_FISHY_STEW)
                .addIngredient(Items.BOWL)
                .addIngredient(GemsTags.Items.STEW_FISH)
                .addIngredient(Items.DRIED_KELP)
                .addIngredient(Items.BROWN_MUSHROOM)
                .addCriterion("has_item", hasItem(GemsTags.Items.STEW_FISH))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModFoods.UNCOOKED_MEATY_STEW)
                .addIngredient(Items.BOWL)
                .addIngredient(GemsTags.Items.STEW_MEAT)
                .addIngredient(Items.POTATO)
                .addIngredient(Items.CARROT)
                .addCriterion("has_item", hasItem(GemsTags.Items.STEW_MEAT))
                .build(consumer);

        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModFoods.UNCOOKED_FISHY_STEW), ModFoods.FISHY_STEW, 0.45f, 200)
                .addCriterion("has_item", hasItem(GemsTags.Items.STEW_FISH))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModFoods.UNCOOKED_MEATY_STEW), ModFoods.MEATY_STEW, 0.45f, 200)
                .addCriterion("has_item", hasItem(GemsTags.Items.STEW_MEAT))
                .build(consumer);
    }

    private void registerGemRecipes(Consumer<IFinishedRecipe> consumer) {
        for (Gems gem : Gems.values()) {
            String name = gem.getName();
            smeltingAndBlasting(consumer, NameUtils.from(gem.getOre()).getPath(), gem.getOreItemTag(), gem.getItem());
            compression(consumer, gem.getItem(), gem.getShard(), "gems/" + name);
            compression(consumer, gem.getBlock(), gem.getItem(), "gems/" + name + "_block");
            ShapedRecipeBuilder.shapedRecipe(gem.getBricks(), 5)
                    .setGroup(gem.getSet().getName() + "_gem_bricks")
                    .patternLine("/#/")
                    .patternLine("###")
                    .patternLine("/#/")
                    .key('/', gem.getShardTag())
                    .key('#', Items.STONE_BRICKS)
                    .addCriterion("has_item", hasItem(Items.STONE_BRICKS))
                    .build(consumer, SilentGems.getId("bricks/" + name));
            ShapedRecipeBuilder.shapedRecipe(gem.getGlass(), 5)
                    .setGroup(gem.getSet().getName() + "_gem_glass")
                    .patternLine("/#/")
                    .patternLine("###")
                    .patternLine("/#/")
                    .key('/', gem.getShardTag())
                    .key('#', Tags.Items.GLASS)
                    .addCriterion("has_item", hasItem(Tags.Items.GLASS))
                    .build(consumer, SilentGems.getId("glass/" + name));
            ShapedRecipeBuilder.shapedRecipe(gem.getReturnHomeCharm())
                    .setGroup(gem.getSet().getName() + "_return_home_charms")
                    .patternLine(" / ")
                    .patternLine("/g/")
                    .patternLine("#o#")
                    .key('/', CraftingItems.GILDED_STRING)
                    .key('g', gem.getItemTag())
                    .key('#', GemsTags.Items.GEMS_CHAOS)
                    .key('o', Tags.Items.ENDER_PEARLS)
                    .addCriterion("has_item", hasItem(GemsTags.Items.GEMS_CHAOS))
                    .build(consumer, "return_home_charm/" + name);
            ShapedRecipeBuilder.shapedRecipe(gem.getLamp(GemLampBlock.State.UNLIT))
                    .setGroup(gem.getSet().getName() + "_gem_lamps")
                    .patternLine("rgr")
                    .patternLine("g#g")
                    .patternLine("rgr")
                    .key('r', Tags.Items.DUSTS_REDSTONE)
                    .key('g', Tags.Items.DUSTS_GLOWSTONE)
                    .key('#', gem.getItemTag())
                    .addCriterion("has_item", hasItem(Tags.Items.DUSTS_GLOWSTONE))
                    .build(consumer, "lamp/" + name);
            ShapelessRecipeBuilder.shapelessRecipe(gem.getLamp(GemLampBlock.State.INVERTED_LIT))
                    .setGroup(gem.getSet().getName() + "_inverted_gem_lamps")
                    .addIngredient(gem.getLamp(GemLampBlock.State.UNLIT))
                    .addIngredient(Items.REDSTONE_TORCH)
                    .addCriterion("has_item", hasItem(Tags.Items.DUSTS_GLOWSTONE))
                    .build(consumer, "lamp/" + name + "_inverted");
            ShapedRecipeBuilder.shapedRecipe(gem.getTeleporter(), 2)
                    .setGroup(gem.getSet().getName() + "_gem_teleporters")
                    .patternLine("cec")
                    .patternLine(" # ")
                    .patternLine("cec")
                    .key('c', CraftingItems.ENRICHED_CHAOS_CRYSTAL)
                    .key('e', GemsTags.Items.TELEPORTER_CATALYST)
                    .key('#', gem.getBlockItemTag())
                    .addCriterion("has_item", hasItem(GemsTags.Items.TELEPORTER_CATALYST))
                    .build(consumer, SilentGems.getId("teleporter/standard/" + name));
            ShapelessRecipeBuilder.shapelessRecipe(gem.getRedstoneTeleporter(), 1)
                    .setGroup(gem.getSet().getName() + "_gem_redstone_teleporters")
                    .addIngredient(gem.getTeleporter())
                    .addIngredient(Tags.Items.DUSTS_REDSTONE)
                    .addCriterion("has_item", hasItem(GemsTags.Items.TELEPORTER_CATALYST))
                    .build(consumer, SilentGems.getId("teleporter/redstone/" + name));
        }
    }

    private void smeltingAndBlasting(Consumer<IFinishedRecipe> consumer, String id, IItemProvider ingredientItem, IItemProvider result) {
        Ingredient ingredientIn = Ingredient.fromItems(ingredientItem);
        smeltingAndBlasting(consumer, id, ingredientIn, result);
    }

    private void smeltingAndBlasting(Consumer<IFinishedRecipe> consumer, String id, ITag<Item> ingredientTag, IItemProvider result) {
        Ingredient ingredientIn = Ingredient.fromTag(ingredientTag);
        smeltingAndBlasting(consumer, id, ingredientIn, result);
    }

    private void smeltingAndBlasting(Consumer<IFinishedRecipe> consumer, String id, Ingredient ingredientIn, IItemProvider result) {
        CookingRecipeBuilder.blastingRecipe(ingredientIn, result, 1.0f, 100)
                .addCriterion("impossible", new ImpossibleTrigger.Instance())
                .build(consumer, SilentGems.getId("blasting/" + id));
        CookingRecipeBuilder.smeltingRecipe(ingredientIn, result, 1.0f, 200)
                .addCriterion("impossible", new ImpossibleTrigger.Instance())
                .build(consumer, SilentGems.getId("smelting/" + id));
    }

    private void compression(Consumer<IFinishedRecipe> consumer, IItemProvider large, IItemProvider small) {
        compression(consumer, large, small, NameUtils.fromItem(large).getPath());
    }

    private void compression(Consumer<IFinishedRecipe> consumer, IItemProvider large, IItemProvider small, String path) {
        ShapedRecipeBuilder.shapedRecipe(large, 1)
                .patternLine("###")
                .patternLine("###")
                .patternLine("###")
                .key('#', small)
                .addCriterion("has_item", hasItem(large))
                .build(consumer, SilentGems.getId(path + "_compress"));
        ShapelessRecipeBuilder.shapelessRecipe(small, 9)
                .addIngredient(large)
                .addCriterion("has_item", hasItem(large))
                .build(consumer, SilentGems.getId(path + "_decompress"));
    }

    private void chaosMetalDust(Consumer<IFinishedRecipe> consumer, IItemProvider dust, IItemProvider ingot, ITag<Item> baseMetal) {
        ShapedRecipeBuilder.shapedRecipe(dust, 1)
                .patternLine("#")
                .patternLine("/")
                .patternLine("#")
                .key('#', CraftingItems.CHAOS_DUST)
                .key('/', baseMetal)
                .addCriterion("has_item", hasItem(baseMetal))
                .build(consumer);
        smeltingAndBlasting(consumer, NameUtils.fromItem(ingot).getPath(), dust, ingot);
    }

    private void pedestal(Consumer<IFinishedRecipe> consumer, IItemProvider pedestal, Ingredient material) {
        ShapedRecipeBuilder.shapedRecipe(pedestal)
                .patternLine("#/#")
                .patternLine(" # ")
                .patternLine("###")
                .key('#', material)
                .key('/', CraftingItems.ENRICHED_CHAOS_CRYSTAL)
                .addCriterion("has_item", hasItem(CraftingItems.ENRICHED_CHAOS_CRYSTAL))
                .build(consumer);
    }

    private void soulUrn(Consumer<IFinishedRecipe> consumer, IItemProvider terracotta, int color, String suffix) {
        ExtendedShapedRecipeBuilder.builder(GemsRecipeInit.SOUL_URN.get(), GemsBlocks.SOUL_URN)
                .addExtraData(json -> json.addProperty("urn_clay_color", Color.format(color)))
                .setGroup("silentgems:soul_urns")
                .patternLine("#g#")
                .patternLine("#s#")
                .patternLine("###")
                .key('#', terracotta)
                .key('g', GemsTags.Items.GEMS)
                .key('s', GemsItems.SOUL_GEM)
                .build(consumer, SilentGems.getId("soul_urn" + (!suffix.isEmpty() ? "_" + suffix : "")));
    }

    private static void registerSpecialRecipes(Consumer<IFinishedRecipe> consumer) {
        customRecipe(consumer, GemsRecipeInit.APPLY_CHAOS_RUNE);
        customRecipe(consumer, GemsRecipeInit.APPLY_ENCHANTMENT_TOKEN);
        customRecipe(consumer, GemsRecipeInit.MODIFY_SOUL_URN);
    }

    private static void customRecipe(Consumer<IFinishedRecipe> consumer, RegistryObject<SpecialRecipeSerializer<?>> serializer) {
        CustomRecipeBuilder.customRecipe(serializer.get()).build(consumer, serializer.getId().toString());
    }

    private void registerTokenEnchanting(Consumer<IFinishedRecipe> consumer) {
        for (Gems gem : Gems.values()) {
            TokenEnchantingRecipeBuilder.builder(gem.getChaosGem(), 1, 3333, 300)
                    .token(gem.getBlockItemTag())
                    .addIngredient(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 10)
                    .build(consumer, idTokenEnchanting("chaos_gem/" + gem.getName()));
        }

        for (SpawnEggItem spawnEggItem : SpawnEggItem.getEggs()) {
            EntityType<?> type = spawnEggItem.getType(new CompoundNBT());
            if (Soul.canHaveSoulGem(type)) {
                ResourceLocation typeId = NameUtils.from(type);
                TokenEnchantingRecipeBuilder.builder(spawnEggItem, 1, 1000, 200)
                        .token(Items.EGG)
                        .addIngredient(SoulGemIngredient.of(typeId), 4)
                        .addIngredient(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 4)
                        .build(consumer, idTokenEnchanting("spawn_egg/" + typeId.getPath()));
            }
        }

        TokenEnchantingRecipeBuilder.builder(CraftingItems.URN_UPGRADE_BASE, 4, 100, 20)
                .token(Blocks.TERRACOTTA)
                .addIngredient(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 1)
                .build(consumer, idTokenEnchanting("urn/upgrade_base"));
        TokenEnchantingRecipeBuilder.builder(SoulUrnUpgrades.PLANTER, 1, 500, 100)
                .token(CraftingItems.URN_UPGRADE_BASE)
                .addIngredient(Blocks.DIRT, 10)
                .addIngredient(Items.BONE_MEAL, 30)
                .addIngredient(GemsTags.Items.GLOWROSES, 2)
                .build(consumer, idTokenEnchanting("urn/planter_upgrade"));
        TokenEnchantingRecipeBuilder.builder(SoulUrnUpgrades.VACUUM, 1, 500, 100)
                .token(CraftingItems.URN_UPGRADE_BASE)
                .addIngredient(Items.ENDER_EYE, 2)
                .addIngredient(Items.HOPPER, 1)
                .build(consumer, idTokenEnchanting("urn/vacuum_upgrade"));

        TokenEnchantingRecipeBuilder.builder(CraftingItems.CHAOS_COAL, 8, 250, 20)
                .token(GemsTags.Items.GEMS_CHAOS)
                .addIngredient(Items.COAL, 8)
                .build(consumer, idTokenEnchanting("chaos_coal"));
        TokenEnchantingRecipeBuilder.builder(CraftingItems.CHAOS_COAL, 6, 250, 20)
                .token(GemsTags.Items.GEMS_CHAOS)
                .addIngredient(Items.CHARCOAL, 8)
                .build(consumer, idTokenEnchanting("chaos_coal2"));
        TokenEnchantingRecipeBuilder.builder(GemsItems.CORRUPTING_POWDER, 4, 50, 20)
                .token(CraftingItems.CHAOS_DUST)
                .addIngredient(GemsTags.Items.CORRUPTED_DUSTS, 2)
                .addIngredient(Tags.Items.DUSTS_REDSTONE, 2)
                .build(consumer);
        TokenEnchantingRecipeBuilder.builder(CraftingItems.ENDER_FROST, 1, 150, 20)
                .token(Blocks.BLUE_ICE)
                .addIngredient(CraftingItems.ENDER_CRYSTAL, 4)
                .addIngredient(Tags.Items.GEMS_LAPIS, 8)
                .build(consumer);
        TokenEnchantingRecipeBuilder.builder(CraftingItems.ENDER_SLIME_CRYSTAL, 1, 250, 100)
                .token(Tags.Items.GEMS_DIAMOND)
                .addIngredient(CraftingItems.ENDER_SLIME_BALL, 6)
                .addIngredient(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 2)
                .build(consumer);
        TokenEnchantingRecipeBuilder.builder(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 1, 150, 20)
                .token(Tags.Items.GEMS_QUARTZ)
                .addIngredient(GemsTags.Items.GEMS_CHAOS, 4)
                .addIngredient(Tags.Items.DUSTS_GLOWSTONE, 4)
                .build(consumer);
        TokenEnchantingRecipeBuilder.builder(CraftingItems.MAGMA_CREAM_CRYSTAL, 1, 250, 100)
                .token(Tags.Items.GEMS_DIAMOND)
                .addIngredient(Items.MAGMA_CREAM, 6)
                .addIngredient(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 2)
                .build(consumer);
        TokenEnchantingRecipeBuilder.builder(GemsItems.PURIFYING_POWDER, 4, 50, 20)
                .token(CraftingItems.CHAOS_DUST)
                .addIngredient(GemsTags.Items.CORRUPTED_DUSTS, 2)
                .addIngredient(Tags.Items.DUSTS_GLOWSTONE, 2)
                .build(consumer);
        TokenEnchantingRecipeBuilder.builder(CraftingItems.SLIME_CRYSTAL, 1, 250, 100)
                .token(Tags.Items.GEMS_DIAMOND)
                .addIngredient(Items.SLIME_BALL, 6)
                .addIngredient(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 2)
                .build(consumer);
    }

    private static ResourceLocation idTokenEnchanting(String path) {
        return SilentGems.getId("token_enchanting/" + path);
    }

    private void registerFluffyStuff(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(CraftingItems.FLUFFY_FABRIC)
                .patternLine("##")
                .patternLine("##")
                .key('#', CraftingItems.FLUFFY_PUFF)
                .addCriterion("has_item", hasItem(CraftingItems.FLUFFY_PUFF))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(CraftingItems.FLUFFY_PUFF, 4)
                .addIngredient(CraftingItems.FLUFFY_FABRIC)
                .addCriterion("has_item", hasItem(CraftingItems.FLUFFY_PUFF))
                .build(consumer, SilentGems.getId("fluffy_puff_from_fabric"));
        ShapedRecipeBuilder.shapedRecipe(GemsBlocks.WHITE_FLUFFY_BLOCK)
                .patternLine("##")
                .patternLine("##")
                .key('#', CraftingItems.FLUFFY_FABRIC)
                .addCriterion("has_item", hasItem(CraftingItems.FLUFFY_PUFF))
                .build(consumer, SilentGems.getId("fluffy_block_base"));
        ShapelessRecipeBuilder.shapelessRecipe(CraftingItems.FLUFFY_FABRIC, 4)
                .addIngredient(GemsTags.Items.FLUFFY_BLOCKS)
                .addCriterion("has_item", hasItem(CraftingItems.FLUFFY_PUFF))
                .build(consumer, SilentGems.getId("fluffy_fabric_from_block"));

        dyeFluffyBlock(consumer, GemsBlocks.WHITE_FLUFFY_BLOCK, Tags.Items.DYES_WHITE);
        dyeFluffyBlock(consumer, GemsBlocks.ORANGE_FLUFFY_BLOCK, Tags.Items.DYES_ORANGE);
        dyeFluffyBlock(consumer, GemsBlocks.MAGENTA_FLUFFY_BLOCK, Tags.Items.DYES_MAGENTA);
        dyeFluffyBlock(consumer, GemsBlocks.LIGHT_BLUE_FLUFFY_BLOCK, Tags.Items.DYES_LIGHT_BLUE);
        dyeFluffyBlock(consumer, GemsBlocks.YELLOW_FLUFFY_BLOCK, Tags.Items.DYES_YELLOW);
        dyeFluffyBlock(consumer, GemsBlocks.LIME_FLUFFY_BLOCK, Tags.Items.DYES_LIME);
        dyeFluffyBlock(consumer, GemsBlocks.PINK_FLUFFY_BLOCK, Tags.Items.DYES_PINK);
        dyeFluffyBlock(consumer, GemsBlocks.GRAY_FLUFFY_BLOCK, Tags.Items.DYES_GRAY);
        dyeFluffyBlock(consumer, GemsBlocks.LIGHT_GRAY_FLUFFY_BLOCK, Tags.Items.DYES_LIGHT_GRAY);
        dyeFluffyBlock(consumer, GemsBlocks.CYAN_FLUFFY_BLOCK, Tags.Items.DYES_CYAN);
        dyeFluffyBlock(consumer, GemsBlocks.PURPLE_FLUFFY_BLOCK, Tags.Items.DYES_PURPLE);
        dyeFluffyBlock(consumer, GemsBlocks.BLUE_FLUFFY_BLOCK, Tags.Items.DYES_BLUE);
        dyeFluffyBlock(consumer, GemsBlocks.BROWN_FLUFFY_BLOCK, Tags.Items.DYES_BROWN);
        dyeFluffyBlock(consumer, GemsBlocks.GREEN_FLUFFY_BLOCK, Tags.Items.DYES_GREEN);
        dyeFluffyBlock(consumer, GemsBlocks.RED_FLUFFY_BLOCK, Tags.Items.DYES_RED);
        dyeFluffyBlock(consumer, GemsBlocks.BLACK_FLUFFY_BLOCK, Tags.Items.DYES_BLACK);

        ShapelessRecipeBuilder.shapelessRecipe(GemsItems.FLUFFY_PUFF_SEEDS)
                .addIngredient(CraftingItems.FLUFFY_PUFF)
                .addCriterion("has_item", hasItem(CraftingItems.FLUFFY_PUFF))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Items.FEATHER)
                .patternLine(" ##")
                .patternLine("## ")
                .patternLine("#  ")
                .key('#', CraftingItems.FLUFFY_PUFF)
                .addCriterion("has_item", hasItem(CraftingItems.FLUFFY_PUFF))
                .build(consumer, SilentGems.getId("fluffy_feather"));
        ShapelessRecipeBuilder.shapelessRecipe(Items.STRING)
                .addIngredient(CraftingItems.FLUFFY_PUFF)
                .addIngredient(CraftingItems.FLUFFY_PUFF)
                .addCriterion("has_item", hasItem(CraftingItems.FLUFFY_PUFF))
                .build(consumer, SilentGems.getId("fluffy_string"));
        ShapedRecipeBuilder.shapedRecipe(Items.WHITE_WOOL)
                .patternLine("###")
                .patternLine("# #")
                .patternLine("###")
                .key('#', CraftingItems.FLUFFY_PUFF)
                .addCriterion("has_item", hasItem(CraftingItems.FLUFFY_PUFF))
                .build(consumer, SilentGems.getId("fluffy_wool"));
    }

    private void dyeFluffyBlock(Consumer<IFinishedRecipe> consumer, IItemProvider block, ITag<Item> dye) {
        ShapedRecipeBuilder.shapedRecipe(block, 8)
                .patternLine("###")
                .patternLine("#d#")
                .patternLine("###")
                .key('#', GemsTags.Items.FLUFFY_BLOCKS)
                .key('d', dye)
                .addCriterion("has_item", hasItem(GemsBlocks.WHITE_FLUFFY_BLOCK))
                .build(consumer);
    }
}
