package net.silentchaos512.gems.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;

public class GemsItemTagsProvider extends ItemTagsProvider {
    public GemsItemTagsProvider(DataGenerator generatorIn, GemsBlockTagsProvider blocks) {
        super(generatorIn, blocks);
    }

    @Override
    public String getName() {
        return "Silent's Gems - Item Tags";
    }

    @Override
    protected void registerTags() {
        // Minecraft tags
        copy(BlockTags.SMALL_FLOWERS, ItemTags.FLOWERS);

        getBuilder(ItemTags.field_232908_Z_)
                .func_240534_a_(Arrays.stream(Gems.values()).map(Gems::getItem).toArray(Item[]::new))
                .func_240534_a_(
                        CraftingItems.SILVER_INGOT.asItem(),
                        CraftingItems.CHAOS_IRON_INGOT.asItem(),
                        CraftingItems.CHAOS_GOLD_INGOT.asItem(),
                        CraftingItems.CHAOS_SILVER_INGOT.asItem()
                );

        // Forge tags
        builder(GemsTags.Items.GEMS_CHAOS, CraftingItems.CHAOS_CRYSTAL);
        for (Gems gem : Gems.values()) {
            getBuilder(gem.getItemTag()).func_240532_a_(gem.getItem());
            getBuilder(gem.getShardTag()).func_240532_a_(gem.getShard());
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

        builder(Tags.Items.CROPS, CraftingItems.FLUFFY_PUFF);
        getBuilder(Tags.Items.DUSTS)
                .func_240531_a_(GemsTags.Items.CORRUPTED_DUSTS)
                .func_240532_a_(CraftingItems.CHAOS_DUST.asItem());
        builder(ModTags.Items.PAPER, CraftingItems.FLUFFY_FABRIC);
        getBuilder(Tags.Items.SEEDS)
                .func_240532_a_(GemsItems.FLUFFY_PUFF_SEEDS.asItem());
        getBuilder(Tags.Items.SLIMEBALLS)
                .func_240534_a_(CraftingItems.CORRUPTED_SLIME_BALL.asItem(), CraftingItems.ENDER_SLIME_BALL.asItem());

        // Mod tags
        builder(GemsTags.Items.CHAOS_GEMS, Registration.getItems(ChaosGemItem.class));
        builder(GemsTags.Items.CHAOS_ORBS, Registration.getItems(ChaosOrbItem.class));
        builder(GemsTags.Items.CHARGING_AGENT_TIER1, CraftingItems.CHARGING_AGENT);
        builder(GemsTags.Items.CHARGING_AGENT_TIER2, CraftingItems.SUPER_CHARGING_AGENT);
        builder(GemsTags.Items.CHARGING_AGENT_TIER3, CraftingItems.ULTRA_CHARGING_AGENT);
        getBuilder(GemsTags.Items.CHARGING_AGENTS)
                .func_240531_a_(GemsTags.Items.CHARGING_AGENT_TIER1)
                .func_240531_a_(GemsTags.Items.CHARGING_AGENT_TIER2)
                .func_240531_a_(GemsTags.Items.CHARGING_AGENT_TIER3);
        getBuilder(GemsTags.Items.CORRUPTED_DUSTS)
                .func_240534_a_(CorruptedBlocks.DIRT.getPile(), CorruptedBlocks.STONE.getPile());

        copy(GemsTags.Blocks.FLUFFY_BLOCKS, GemsTags.Items.FLUFFY_BLOCKS);
        copy(GemsTags.Blocks.GEM_BLOCKS, GemsTags.Items.GEM_BLOCKS);
        gemBuilder(GemsTags.Items.GEMS, Gems::getItemTag);
        gemBuilder(GemsTags.Items.GLOWROSES, Gems::getGlowroseItemTag);
        copy(GemsTags.Blocks.HARDENED_ROCKS, GemsTags.Items.HARDENED_ROCKS);
        copy(GemsTags.Blocks.PEDESTALS, GemsTags.Items.PEDESTALS);
        gemBuilderItem(GemsTags.Items.RETURN_HOME_CHARMS, Gems::getReturnHomeCharm);
        gemBuilder(GemsTags.Items.SHARDS, Gems::getShardTag);
        getBuilder(GemsTags.Items.STEW_FISH)
                .func_240534_a_(Items.COD, Items.SALMON);
        getBuilder(GemsTags.Items.STEW_MEAT)
                .func_240534_a_(Items.BEEF, Items.CHICKEN, Items.MUTTON, Items.PORKCHOP, Items.RABBIT);
        copy(GemsTags.Blocks.HARDENED_ROCKS, GemsTags.Items.HARDENED_ROCKS);
        copy(GemsTags.Blocks.SUPERCHARGER_PILLAR_CAP, GemsTags.Items.SUPERCHARGER_PILLAR_CAP);
        copy(GemsTags.Blocks.SUPERCHARGER_PILLAR_LEVEL1, GemsTags.Items.SUPERCHARGER_PILLAR_LEVEL1);
        copy(GemsTags.Blocks.SUPERCHARGER_PILLAR_LEVEL2, GemsTags.Items.SUPERCHARGER_PILLAR_LEVEL2);
        copy(GemsTags.Blocks.SUPERCHARGER_PILLAR_LEVEL3, GemsTags.Items.SUPERCHARGER_PILLAR_LEVEL3);
        builder(GemsTags.Items.TELEPORTER_CATALYST, CraftingItems.ENDER_CRYSTAL);
        gemBuilderItem(GemsTags.Items.TELEPORTERS, Gems::getTeleporter);
        getBuilder(GemsTags.Items.WISP_ESSENCES)
                .func_240534_a_(Arrays.stream(CraftingItems.values())
                        .filter(item -> item.getName().endsWith("wisp_essence"))
                        .map(IItemProvider::asItem)
                        .toArray(Item[]::new));

        gemSetBuilder(GemsTags.Items.GEMS_CLASSIC, Gems.Set.CLASSIC, Gems::getItemTag);
        gemSetBuilder(GemsTags.Items.GEMS_DARK, Gems.Set.DARK, Gems::getItemTag);
        gemSetBuilder(GemsTags.Items.GEMS_LIGHT, Gems.Set.LIGHT, Gems::getItemTag);
    }

    private Builder<Item> getBuilder(ITag.INamedTag<Item> tag) {
        return func_240522_a_(tag);
    }

    private void copy(ITag.INamedTag<Block> block, ITag.INamedTag<Item> item) {
        func_240521_a_(block, item);
    }

    private void builder(ITag.INamedTag<Item> tag, IItemProvider item) {
        builder(tag, Collections.singleton(item));
    }

    private void builder(ITag.INamedTag<Item> tag, Collection<? extends IItemProvider> items) {
        getBuilder(tag).func_240534_a_(items.stream().map(IItemProvider::asItem).toArray(Item[]::new));
    }

    private void gemBuilder(ITag.INamedTag<Item> tag, Function<Gems, ITag.INamedTag<Item>> gemTagGetter) {
        Builder<Item> builder = getBuilder(tag);
        for (Gems gems : Gems.values()) {
            ITag.INamedTag<Item> itemTag = gemTagGetter.apply(gems);
            builder.func_240531_a_(itemTag);
        }
    }

    private void gemBuilderItem(ITag.INamedTag<Item> tag, Function<Gems, IItemProvider> gemTagGetter) {
        Builder<Item> builder = getBuilder(tag);
        for (Gems gems : Gems.values()) {
            Item item = gemTagGetter.apply(gems).asItem();
            builder.func_240532_a_(item);
        }
    }

    private void gemSetBuilder(ITag.INamedTag<Item> tag, Gems.Set set, Function<Gems, ITag.INamedTag<Item>> gemTagGetter) {
        Builder<Item> builder = getBuilder(tag);
        set.iterator().forEachRemaining(gem -> builder.func_240531_a_(gemTagGetter.apply(gem)));
    }

    private void copyBlock(ITag.INamedTag<Block> tag) {
        copy(tag, ItemTags.makeWrapperTag(tag.getName().toString()));
    }

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    @Override
    public void act(DirectoryCache cache) {
        // Temp fix that removes the broken safety check
        this.tagToBuilder.clear();
        this.registerTags();
        this.tagToBuilder.forEach((p_240524_4_, p_240524_5_) -> {
            JsonObject jsonobject = p_240524_5_.serialize();
            Path path = this.makePath(p_240524_4_);
            if (path == null)
                return; //Forge: Allow running this data provider without writing it. Recipe provider needs valid tags.

            try {
                String s = GSON.toJson((JsonElement) jsonobject);
                String s1 = HASH_FUNCTION.hashUnencodedChars(s).toString();
                if (!Objects.equals(cache.getPreviousHash(path), s1) || !Files.exists(path)) {
                    Files.createDirectories(path.getParent());

                    try (BufferedWriter bufferedwriter = Files.newBufferedWriter(path)) {
                        bufferedwriter.write(s);
                    }
                }

                cache.recordHash(path, s1);
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't save tags to {}", path, ioexception);
            }

        });
    }
}
