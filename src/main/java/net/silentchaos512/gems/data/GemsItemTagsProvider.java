package net.silentchaos512.gems.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gear.init.ModTags;
import net.silentchaos512.gems.block.CorruptedBlocks;
import net.silentchaos512.gems.init.GemsItems;
import net.silentchaos512.gems.init.GemsTags;
import net.silentchaos512.gems.init.Registration;
import net.silentchaos512.gems.item.ChaosGemItem;
import net.silentchaos512.gems.item.ChaosOrbItem;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.lib.Gems;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class GemsItemTagsProvider extends ItemTagsProvider {
    public GemsItemTagsProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public String getName() {
        return "Silent's Gems - Item Tags";
    }

    @Override
    protected void registerTags() {
        // Minecraft tags
        copy(BlockTags.SMALL_FLOWERS, ItemTags.FLOWERS);

        // Forge tags
        builder(GemsTags.Items.GEMS_CHAOS, CraftingItems.CHAOS_CRYSTAL);
        for (Gems gem : Gems.values()) {
            getBuilder(gem.getItemTag()).add(gem.getItem());
            getBuilder(gem.getShardTag()).add(gem.getShard());
            copyBlock(gem.getOreTag());
            copyBlock(gem.getBlockTag());
            copyBlock(gem.getGlowroseTag());
        }

        copy(GemsTags.Blocks.ORES_SILVER, GemsTags.Items.ORES_SILVER);
        copy(GemsTags.Blocks.STORAGE_BLOCKS_CHAOS, GemsTags.Items.STORAGE_BLOCKS_CHAOS);
        copy(GemsTags.Blocks.STORAGE_BLOCKS_CHAOS_COAL, GemsTags.Items.STORAGE_BLOCKS_CHAOS_COAL);
        copy(GemsTags.Blocks.STORAGE_BLOCKS_CHAOS_IRON, GemsTags.Items.STORAGE_BLOCKS_CHAOS_IRON);
        copy(GemsTags.Blocks.STORAGE_BLOCKS_SILVER, GemsTags.Items.STORAGE_BLOCKS_SILVER);

        gemBuilder(Tags.Items.GEMS, Gems::getItemTag);
        gemBuilder(Tags.Items.NUGGETS, Gems::getShardTag);
        copy(Tags.Blocks.ORES, Tags.Items.ORES);
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
        copy(GemsTags.Blocks.GLOWROSES, GemsTags.Items.GLOWROSES);

        builder(GemsTags.Items.INGOTS_SILVER, CraftingItems.SILVER_INGOT);
        builder(GemsTags.Items.NUGGETS_SILVER, CraftingItems.SILVER_NUGGET);
        builder(Tags.Items.INGOTS, CraftingItems.SILVER_INGOT);
        builder(Tags.Items.NUGGETS, CraftingItems.SILVER_NUGGET);
        builder(GemsTags.Items.RODS_ORNATE_GOLD, CraftingItems.ORNATE_GOLD_ROD);
        builder(GemsTags.Items.RODS_ORNATE_SILVER, CraftingItems.ORNATE_SILVER_ROD);

        builder(Tags.Items.CROPS, CraftingItems.FLUFFY_PUFF);
        getBuilder(Tags.Items.DUSTS)
                .add(GemsTags.Items.CORRUPTED_DUSTS)
                .add(CraftingItems.CHAOS_DUST.asItem());
        builder(ModTags.Items.PAPER, CraftingItems.FLUFFY_FABRIC);
        getBuilder(Tags.Items.RODS)
                .add(GemsTags.Items.RODS_ORNATE_GOLD, GemsTags.Items.RODS_ORNATE_SILVER);
        getBuilder(Tags.Items.SEEDS)
                .add(GemsItems.FLUFFY_PUFF_SEEDS.asItem());
        getBuilder(Tags.Items.SLIMEBALLS)
                .add(CraftingItems.CORRUPTED_SLIMEBALL.asItem(), CraftingItems.ENDER_SLIMEBALL.asItem());

        // Mod tags
        builder(GemsTags.Items.CHAOS_GEMS, Registration.getItems(ChaosGemItem.class));
        builder(GemsTags.Items.CHAOS_ORBS, Registration.getItems(ChaosOrbItem.class));
        getBuilder(GemsTags.Items.CHARGING_AGENTS ).add(
                GemsTags.Items.CHARGING_AGENT_TIER1,
                GemsTags.Items.CHARGING_AGENT_TIER2,
                GemsTags.Items.CHARGING_AGENT_TIER3);
        getBuilder(GemsTags.Items.CORRUPTED_DUSTS)
                .add(CorruptedBlocks.DIRT.getPile(), CorruptedBlocks.STONE.getPile());

        copy(GemsTags.Blocks.FLUFFY_BLOCKS, GemsTags.Items.FLUFFY_BLOCKS);
        copy(GemsTags.Blocks.GEM_BLOCKS, GemsTags.Items.GEM_BLOCKS);
        gemBuilder(GemsTags.Items.GEMS, Gems::getItemTag);
        gemBuilder(GemsTags.Items.GLOWROSES, gem -> new ItemTags.Wrapper(gem.getGlowroseTag().getId()));
        copy(GemsTags.Blocks.HARDENED_ROCKS, GemsTags.Items.HARDENED_ROCKS);
        copy(GemsTags.Blocks.PEDESTALS, GemsTags.Items.PEDESTALS);
        gemBuilderItem(GemsTags.Items.RETURN_HOME_CHARMS, Gems::getReturnHomeCharm);
        gemBuilder(GemsTags.Items.SHARDS, Gems::getShardTag);
        getBuilder(GemsTags.Items.STEW_FISH)
                .add(Items.COD, Items.SALMON);
        getBuilder(GemsTags.Items.STEW_MEAT)
                .add(Items.BEEF, Items.CHICKEN, Items.MUTTON, Items.PORKCHOP, Items.RABBIT);
        builder(GemsTags.Items.TELEPORTER_CATALYST, CraftingItems.ENDER_CRYSTAL);
        gemBuilderItem(GemsTags.Items.TELEPORTERS, Gems::getTeleporter);
        getBuilder(GemsTags.Items.WISP_ESSENCES)
                .add(Arrays.stream(CraftingItems.values())
                        .filter(item -> item.getName().endsWith("wisp_essence"))
                        .map(IItemProvider::asItem)
                        .toArray(Item[]::new));
    }

    private void builder(Tag<Item> tag, IItemProvider item) {
        builder(tag, Collections.singleton(item));
    }

    private void builder(Tag<Item> tag, Collection<? extends IItemProvider> items) {
        getBuilder(tag).add(items.stream().map(IItemProvider::asItem).toArray(Item[]::new));
    }

    private void gemBuilder(Tag<Item> tag, Function<Gems, Tag<Item>> gemTagGetter) {
        Tag.Builder<Item> builder = getBuilder(tag);
        for (Gems gems : Gems.values()) {
            Tag<Item> itemTag = gemTagGetter.apply(gems);
            builder.add(itemTag);
        }
    }

    private void gemBuilderItem(Tag<Item> tag, Function<Gems, IItemProvider> gemTagGetter) {
        Tag.Builder<Item> builder = getBuilder(tag);
        for (Gems gems : Gems.values()) {
            Item item = gemTagGetter.apply(gems).asItem();
            builder.add(item);
        }
    }

    private void copyBlock(Tag<Block> tag) {
        copy(tag, new ItemTags.Wrapper(tag.getId()));
    }
}
