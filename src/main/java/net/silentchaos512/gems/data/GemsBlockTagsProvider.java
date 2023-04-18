package net.silentchaos512.gems.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsTags;
import net.silentchaos512.gems.util.Gems;

public class GemsBlockTagsProvider extends BlockTagsProvider {
    public GemsBlockTagsProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), GemsBase.MOD_ID, event.getExistingFileHelper());
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
        }

        tag(GemsTags.Blocks.ORES_SILVER).add(GemsBlocks.SILVER_ORE.get(), GemsBlocks.DEEPSLATE_SILVER_ORE.get());
        tag(Tags.Blocks.ORES).addTag(GemsTags.Blocks.ORES_SILVER);

        // Harvesting
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(GemsTags.Blocks.ORES_SILVER)
                .add(GemsBlocks.SILVER_BLOCK.get());
    }
}
