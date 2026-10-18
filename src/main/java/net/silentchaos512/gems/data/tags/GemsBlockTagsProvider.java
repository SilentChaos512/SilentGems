package net.silentchaos512.gems.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsTags;
import net.silentchaos512.gems.setup.Gems;

import java.util.concurrent.CompletableFuture;

public class GemsBlockTagsProvider extends BlockTagsProvider {
    public GemsBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, SilentGems.MOD_ID);
    }

    @Override
    protected DirectTagAppender<Block> tag(TagKey<Block> tag) {
        return new DirectTagAppender<>(super.tag(tag), block -> BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (Gems gem : Gems.values()) {
            tag(gem.getModOresTag()).add(gem.getOre(), gem.getDeepslateOre(), gem.getNetherOre(), gem.getEndOre());
            tag(gem.getOreTag()).addTag(gem.getModOresTag());
            tag(gem.getBlockTag()).add(gem.getBlock());
            tag(gem.getGlowroseTag()).add(gem.getGlowrose());

            // Group tags
            tag(GemsTags.Blocks.GEM_ORES).addTag(gem.getModOresTag());
            tag(GemsTags.Blocks.GLOWROSES).addTag(gem.getGlowroseTag());
            tag(Tags.Blocks.ORES).addTag(gem.getOreTag());
            tag(Tags.Blocks.STORAGE_BLOCKS).add(gem.getBlock());

            tag(BlockTags.FLOWERS).addTag(gem.getGlowroseTag());

            // Harvesting
            tag(BlockTags.NEEDS_IRON_TOOL)
                    .add(gem.getOre(), gem.getDeepslateOre());
            tag(BlockTags.NEEDS_DIAMOND_TOOL)
                    .add(gem.getNetherOre());
            tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                    .add(gem.getEndOre());
            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .addTag(gem.getModOresTag())
                    .add(gem.getBlock())
                    .add(gem.getBricks());

            // Harvest tiers
            gem.generateIncorrectForToolTag(this::tag);
        }

        tag(GemsTags.Blocks.ORES_CHAOS).add(GemsBlocks.CHAOS_ORE.get(), GemsBlocks.DEEPSLATE_CHAOS_ORE.get());
        tag(GemsTags.Blocks.ORES_SILVER).add(GemsBlocks.SILVER_ORE.get(), GemsBlocks.DEEPSLATE_SILVER_ORE.get());
        tag(Tags.Blocks.ORES)
                .addTag(GemsTags.Blocks.ORES_CHAOS)
                .addTag(GemsTags.Blocks.ORES_SILVER);

        tag(GemsTags.Blocks.STORAGE_BLOCKS_CHAOS).add(GemsBlocks.CHAOS_ESSENCE_BLOCK.get());
        tag(GemsTags.Blocks.STORAGE_BLOCKS_SILVER).add(GemsBlocks.SILVER_BLOCK.get());
        tag(Tags.Blocks.STORAGE_BLOCKS)
                .add(GemsBlocks.CHAOS_ESSENCE_BLOCK.get())
                .add(GemsBlocks.SILVER_BLOCK.get());

        // Harvesting

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(GemsTags.Blocks.ORES_CHAOS)
                .addTag(GemsTags.Blocks.ORES_SILVER)
                .add(GemsBlocks.CHAOS_ESSENCE_BLOCK.get())
                .add(GemsBlocks.SILVER_BLOCK.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .addTag(GemsTags.Blocks.ORES_CHAOS);
        tag(BlockTags.NEEDS_IRON_TOOL)
                .addTag(GemsTags.Blocks.ORES_SILVER);
    }
}
