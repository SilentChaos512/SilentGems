package net.silentchaos512.gems.util;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.GemLamp;
import net.silentchaos512.gems.block.MiscBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.init.ModTags;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.item.Foods;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.lib.util.recipe.RecipeGenerator;
import net.silentchaos512.lib.util.recipe.RecipeGenerator.ShapedBuilder;
import net.silentchaos512.lib.util.recipe.RecipeGenerator.ShapelessBuilder;
import net.silentchaos512.lib.util.recipe.RecipeGenerator.SmeltingBuilder;

/**
 * Generates recipe JSON files. This does not add the recipes in-game. The files need to be copied
 * to the correct directory for them to load. They end up in 'run/output'.
 */
public final class RecipeGen {
    private RecipeGen() { throw new IllegalAccessError("Utility class"); }

    @SuppressWarnings("OverlyLongMethod")
    public static void generateRecipes() {
        for (Gems gem : Gems.values()) {
            String set = gem.getSet().getName();
            // Block <--> Gem
            RecipeGenerator.compress9(name(gem.getName() + "_block"), name(set + "_gem_block"),
                    gem.getBlock(), gem.getItem());
            // Gem <--> Shard
            RecipeGenerator.compress9(name(gem.getName()), name(set + "_gem"),
                    gem.getItem(), gem.getShard());
            // Bricks
            RecipeGenerator.create(name(gem.getName() + "_bricks"), ShapedBuilder
                    .create(new ItemStack(gem.getBricks(), 5))
                    .group(name(set + "_gem_bricks"))
                    .layout("/#/", "###", "/#/")
                    .key('/', gem.getShardTag())
                    .key('#', Blocks.STONE_BRICKS));
            // Glass
            RecipeGenerator.create(name(gem.getName() + "_glass"), ShapedBuilder
                    .create(new ItemStack(gem.getGlass(), 5))
                    .group(name(set + "_gem_glass"))
                    .layout("/#/", "###", "/#/")
                    .key('/', gem.getShardTag())
                    .key('#', Blocks.GLASS));
            // Lamps
            RecipeGenerator.create(name(gem.getName() + "_lamp"), ShapedBuilder
                    .create(gem.getLamp(GemLamp.State.UNLIT))
                    .group(set + "_gem_lamp")
                    .layout("rgr", "g#g", "rgr")
                    .key('r', Tags.Items.DUSTS_REDSTONE)
                    .key('g', Tags.Items.DUSTS_GLOWSTONE)
                    .key('#', gem.getItemTag()));
            RecipeGenerator.create(name(gem.getName() + "_lamp_inverted"), ShapelessBuilder
                    .create(gem.getLamp(GemLamp.State.INVERTED_UNLIT))
                    .group(name(set + "_inverted_lamp"))
                    .ingredient(gem.getLamp(GemLamp.State.UNLIT))
                    .ingredient(Blocks.REDSTONE_TORCH));
        }

        // Misc storage blocks
        for (MiscBlocks misc : MiscBlocks.values()) {
            RecipeGenerator.compress9(name(misc.getName()), misc, misc.getStoredItem());
        }

        // Crafting Items
        // Chaos Crystal <--> Shard
        RecipeGenerator.compress9(name(CraftingItems.CHAOS_CRYSTAL),
                CraftingItems.CHAOS_CRYSTAL,
                CraftingItems.CHAOS_CRYSTAL_SHARD);
        // Ender Crystal <--> Shard
        RecipeGenerator.compress9(name(CraftingItems.ENDER_CRYSTAL),
                CraftingItems.ENDER_CRYSTAL,
                CraftingItems.ENDER_CRYSTAL_SHARD);
        // Chaos Iron Unfired
        RecipeGenerator.create(name(CraftingItems.CHAOS_IRON_UNFIRED), ShapedBuilder
                .create(CraftingItems.CHAOS_IRON_UNFIRED)
                .layout("d", "i", "d")
                .key('d', CraftingItems.CHAOS_DUST)
                .key('i', Tags.Items.INGOTS_IRON));
        // Chaos Coal
        RecipeGenerator.create(name(CraftingItems.CHAOS_COAL), ShapedBuilder
                .create(CraftingItems.CHAOS_COAL, 8)
                .layout("###", "#c#", "###")
                .key('#', Items.COAL)
                .key('c', CraftingItems.CHAOS_CRYSTAL));
        RecipeGenerator.create(name(CraftingItems.CHAOS_COAL), ShapedBuilder
                .create(CraftingItems.CHAOS_COAL, 6)
                .layout("###", "#c#", "###")
                .key('#', Items.CHARCOAL)
                .key('c', CraftingItems.CHAOS_CRYSTAL));
        // Enriched Chaos Crystal
        RecipeGenerator.create(name(CraftingItems.ENRICHED_CHAOS_CRYSTAL), ShapedBuilder
                .create(CraftingItems.ENRICHED_CHAOS_CRYSTAL)
                .layout("gcg", "cqc", "gcg")
                .key('g', Tags.Items.DUSTS_GLOWSTONE)
                .key('c', ModTags.Items.GEMS_CHAOS)
                .key('q', Tags.Items.GEMS_QUARRTZ));
        // Fluffy Fabric
        RecipeGenerator.compress4(name(CraftingItems.FLUFFY_FABRIC),
                CraftingItems.FLUFFY_FABRIC,
                CraftingItems.FLUFFY_PUFF);
        // Gilded String
        RecipeGenerator.create(name(CraftingItems.GILDED_STRING), ShapedBuilder
                .create(CraftingItems.GILDED_STRING, 3)
                .layout("#|#", "#|#", "#|#")
                .key('#', Tags.Items.NUGGETS_GOLD)
                .key('|', Items.STRING));
        // Iron Potato
        RecipeGenerator.create(name(CraftingItems.IRON_POTATO), ShapedBuilder
                .create(CraftingItems.IRON_POTATO)
                .layout("i#i", "#p#", "i#i")
                .key('i', CraftingItems.CHAOS_IRON)
                .key('#', Tags.Blocks.STORAGE_BLOCKS_IRON)
                .key('p', Items.POTATO));
        // Mystery Goo
        RecipeGenerator.create(name(CraftingItems.MYSTERY_GOO), ShapedBuilder
                .create(CraftingItems.MYSTERY_GOO)
                .layout("k#k", "#a#", "k#k")
                .key('k', Blocks.KELP)
                .key('#', Blocks.MOSSY_COBBLESTONE)
                .key('a', Items.APPLE));
        // Ornate Rods
        RecipeGenerator.create(name(CraftingItems.ORNATE_GOLD_ROD), ShapedBuilder
                .create(CraftingItems.ORNATE_GOLD_ROD, 4)
                .group(name("ornate_rods"))
                .layout("#", "c", "#")
                .key('#', Tags.Items.INGOTS_GOLD)
                .key('c', CraftingItems.ENRICHED_CHAOS_CRYSTAL));
        RecipeGenerator.create(name(CraftingItems.ORNATE_SILVER_ROD), ShapedBuilder
                .create(CraftingItems.ORNATE_SILVER_ROD, 4)
                .group(name("ornate_rods"))
                .layout("#", "c", "#")
                .key('#', "forge:ingots/silver")
                .key('c', CraftingItems.ENRICHED_CHAOS_CRYSTAL));

        // Foods
        RecipeGenerator.create(name(Foods.CANDY_CANE), ShapedBuilder
                .create(Foods.CANDY_CANE)
                .layout("ss", "rs", " s")
                .key('s', Items.SUGAR)
                .key('r', Tags.Items.DYES_RED));
        RecipeGenerator.create(name(Foods.POTATO_ON_A_STICK), ShapedBuilder
                .create(Foods.POTATO_ON_A_STICK)
                .layout(" p", "/ ")
                .key('p', Items.BAKED_POTATO)
                .key('/', Tags.Items.RODS_WOODEN));
        RecipeGenerator.create(name(Foods.SECRET_DONUT), ShapedBuilder
                .create(Foods.SECRET_DONUT, 6)
                .layout("///", "/m/", "///")
                .key('/', Items.WHEAT)
                .key('m', Blocks.RED_MUSHROOM));
        RecipeGenerator.create(name(Foods.SUGAR_COOKIE), ShapedBuilder
                .create(Foods.SUGAR_COOKIE.getItem(), 8)
                .layout(" s ", "///", " s ")
                .key('s', Items.SUGAR)
                .key('/', Items.WHEAT));
        RecipeGenerator.create(name(Foods.UNCOOKED_FISHY_STEW), ShapelessBuilder
                .create(Foods.UNCOOKED_FISHY_STEW)
                .ingredient(Items.BOWL)
                .ingredient(ModTags.Items.STEW_FISH)
                .ingredient(Items.DRIED_KELP)
                .ingredient(Blocks.BROWN_MUSHROOM));
        RecipeGenerator.create(name(Foods.UNCOOKED_MEATY_STEW), ShapelessBuilder
                .create(Foods.UNCOOKED_MEATY_STEW)
                .ingredient(Items.BOWL)
                .ingredient(ModTags.Items.STEW_MEAT)
                .ingredient(Items.POTATO)
                .ingredient(Items.CARROT));
        RecipeGenerator.create(name(Foods.FISHY_STEW), SmeltingBuilder
                .create(Foods.FISHY_STEW)
                .ingredient(Foods.UNCOOKED_FISHY_STEW)
                .experience(0.45f));
        RecipeGenerator.create(name(Foods.MEATY_STEW), SmeltingBuilder
                .create(Foods.MEATY_STEW)
                .ingredient(Foods.UNCOOKED_MEATY_STEW)
                .experience(0.45f));

        // Pet Summoners
        RecipeGenerator.create(name("summon_kitty"), ShapedBuilder
                .create(ModItems.summonKitty)
                .layout("|f|", "|g|", "|f|")
                .key('|', Items.STRING)
                .key('f', ModTags.Items.STEW_FISH)
                .key('g', ModTags.Items.MOD_GEMS));
        RecipeGenerator.create(name("summon_puppy"), ShapedBuilder
                .create(ModItems.summonPuppy)
                .layout(" m ", "#g#", " m ")
                .key('#', Items.LEATHER)
                .key('m', ModTags.Items.STEW_MEAT)
                .key('g', ModTags.Items.MOD_GEMS));
    }

    private static ResourceLocation name(String name) {
        return new ResourceLocation(SilentGems.MOD_ID, name);
    }

    private static ResourceLocation name(IStringSerializable thing) {
        return new ResourceLocation(SilentGems.MOD_ID, thing.getName());
    }
}
