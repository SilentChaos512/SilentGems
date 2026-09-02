package net.silentchaos512.gems.data.tags;

import net.neoforged.neoforge.common.Tags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsTags;
import net.silentchaos512.gems.setup.Gems;
import net.silentchaos512.lib.data.tag.LibBlockItemTagsProvider;

public abstract class GemsBlockItemTagsProvider {
    protected abstract DirectTagAppender<Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag);

    public void run() {
        for (Gems gem : Gems.values()) {
            tag(gem.getModOresTag(), gem.getModOresItemTag())
                    .add(gem.getOre())
                    .add(gem.getDeepslateOre())
                    .add(gem.getNetherOre())
                    .add(gem.getEndOre());
            tag(gem.getOreTag(), gem.getOreItemTag())
                    .addTag(gem.getModOresTag());
            tag(gem.getBlockTag(), gem.getBlockItemTag())
                    .add(gem.getBlock());
            tag(gem.getGlowroseTag(), gem.getGlowroseItemTag())
                    .add(gem.getGlowrose());

            tag(Tags.Blocks.ORES, Tags.Items.ORES)
                    .addTag(gem.getModOresTag());
            tag(GemsTags.Blocks.GEM_ORES, GemsTags.Items.GEM_ORES)
                    .addTag(gem.getModOresTag());
            tag(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS)
                    .add(gem.getBlock());
            tag(Tags.Blocks.FLOWERS_SMALL, Tags.Items.FLOWERS_SMALL)
                    .addTag(gem.getGlowroseTag());
            tag(GemsTags.Blocks.GLOWROSES, GemsTags.Items.GLOWROSES)
                    .addTag(gem.getGlowroseTag());
        }

        tag(GemsTags.Blocks.ORES_CHAOS, GemsTags.Items.ORES_CHAOS)
                .add(GemsBlocks.CHAOS_ORE.get())
                .add(GemsBlocks.DEEPSLATE_CHAOS_ORE.get());
        tag(GemsTags.Blocks.ORES_SILVER, GemsTags.Items.ORES_SILVER)
                .add(GemsBlocks.SILVER_ORE.get())
                .add(GemsBlocks.DEEPSLATE_SILVER_ORE.get());

        tag(Tags.Blocks.ORES, Tags.Items.ORES)
                .addTag(GemsTags.Blocks.ORES_CHAOS)
                .addTag(GemsTags.Blocks.ORES_SILVER);

        tag(GemsTags.Blocks.STORAGE_BLOCKS_CHAOS, GemsTags.Items.STORAGE_BLOCKS_CHAOS)
                .add(GemsBlocks.CHAOS_ESSENCE_BLOCK.get());
        tag(GemsTags.Blocks.STORAGE_BLOCKS_SILVER, GemsTags.Items.STORAGE_BLOCKS_SILVER)
                .add(GemsBlocks.SILVER_BLOCK.get());

        tag(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS)
                .add(GemsBlocks.CHAOS_ESSENCE_BLOCK.get())
                .add(GemsBlocks.SILVER_BLOCK.get());
    }
}
