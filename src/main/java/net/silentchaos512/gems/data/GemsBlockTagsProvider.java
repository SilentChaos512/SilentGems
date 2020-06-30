package net.silentchaos512.gems.data;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.block.pedestal.PedestalBlock;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.init.GemsTags;
import net.silentchaos512.gems.init.Registration;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.lib.block.IBlockProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class GemsBlockTagsProvider extends BlockTagsProvider {
    public GemsBlockTagsProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public String getName() {
        return "Silent's Gems - Block Tags";
    }

    @Override
    protected void registerTags() {
        // Minecraft tags
        builder(BlockTags.CROPS, GemsBlocks.FLUFFY_PUFF_PLANT);
        getBuilder(BlockTags.ENDERMAN_HOLDABLE)
                .add(CorruptedBlocks.STONE.asBlock(), CorruptedBlocks.DIRT.asBlock())
                .add(GemsTags.Blocks.GLOWROSES);
        getBuilder(BlockTags.FLOWER_POTS)
                .add(Registration.getBlocks(FlowerPotBlock.class).toArray(new Block[0]));
        getBuilder(BlockTags.SMALL_FLOWERS).add(GemsTags.Blocks.GLOWROSES);

        // Gem-type blocks
        for (Gems gem : Gems.values()) {
            builder(gem.getOreTag(), gem::getOre);
            builder(gem.getBlockTag(), gem::getBlock);
            builder(gem.getGlowroseTag(), gem::getGlowrose);
        }

        gemBuilder(GemsTags.Blocks.GEM_BLOCKS, Gems::getBlockTag);

        // Other blocks
        builder(GemsTags.Blocks.ORES_SILVER, MiscOres.SILVER);
        builder(GemsTags.Blocks.STORAGE_BLOCKS_CHAOS, MiscBlocks.CHAOS_CRYSTAL);
        builder(GemsTags.Blocks.STORAGE_BLOCKS_CHAOS_COAL, MiscBlocks.CHAOS_IRON);
        builder(GemsTags.Blocks.STORAGE_BLOCKS_CHAOS_IRON, MiscBlocks.CHAOS_IRON);
        builder(GemsTags.Blocks.STORAGE_BLOCKS_SILVER, MiscBlocks.SILVER);

        // Group tags
        Tag.Builder<Block> oresBuilder = getBuilder(Tags.Blocks.ORES)
                .add(GemsBlocks.MULTI_ORE_CLASSIC.get())
                .add(GemsBlocks.MULTI_ORE_DARK.get())
                .add(GemsBlocks.MULTI_ORE_LIGHT.get())
                .add(MiscOres.CHAOS.asBlock())
                .add(MiscOres.ENDER.asBlock())
                .add(GemsTags.Blocks.ORES_SILVER);
        Arrays.stream(Gems.values()).forEach(gem -> oresBuilder.add(gem.getOreTag()));

        Tag.Builder<Block> storageBlocksBuilder = getBuilder(Tags.Blocks.STORAGE_BLOCKS)
                .add(GemsTags.Blocks.STORAGE_BLOCKS_CHAOS)
                .add(GemsTags.Blocks.STORAGE_BLOCKS_CHAOS_COAL)
                .add(GemsTags.Blocks.STORAGE_BLOCKS_CHAOS_IRON)
                .add(GemsTags.Blocks.STORAGE_BLOCKS_SILVER);
        Arrays.stream(Gems.values()).forEach(gem -> storageBlocksBuilder.add(gem.getBlockTag()));

        Tag.Builder<Block> glowrosesBuilder = getBuilder(GemsTags.Blocks.GLOWROSES);
        Arrays.stream(Gems.values()).forEach(gem -> glowrosesBuilder.add(gem.getGlowroseTag()));

        getBuilder(GemsTags.Blocks.FLUFFY_BLOCKS)
                .add(Registration.getBlocks(FluffyBlock.class).toArray(new Block[0]));
        getBuilder(GemsTags.Blocks.HARDENED_ROCKS)
                .add(Registration.getBlocks(HardenedRock.HardenedRockBlock.class).toArray(new Block[0]));

        getBuilder(GemsTags.Blocks.CORRUPTABLE_DIRT)
                .add(Tags.Blocks.DIRT)
                .add(Blocks.GRASS);
        getBuilder(GemsTags.Blocks.CORRUPTABLE_STONE)
                .add(Tags.Blocks.STONE);

        getBuilder(GemsTags.Blocks.PEDESTALS)
                .add(Registration.getBlocks(PedestalBlock.class).toArray(new Block[0]));

        // Supercharger
        getBuilder(GemsTags.Blocks.SUPERCHARGER_PILLAR_CAP)
                .add(GemsTags.Blocks.GEM_BLOCKS, GemsTags.Blocks.STORAGE_BLOCKS_SILVER);
        getBuilder(GemsTags.Blocks.SUPERCHARGER_PILLAR_LEVEL1)
                .add(GemsTags.Blocks.STORAGE_BLOCKS_CHAOS_IRON);
        getBuilder(GemsTags.Blocks.SUPERCHARGER_PILLAR_LEVEL2)
                .add(MiscBlocks.ENRICHED_CHAOS_CRYSTAL.asBlock());
        getBuilder(GemsTags.Blocks.SUPERCHARGER_PILLAR_LEVEL3)
                .add(MiscBlocks.ENDER_CRYSTAL.asBlock());
    }

    private void builder(Tag<Block> tag, IBlockProvider block) {
        builder(tag, Collections.singleton(block));
    }

    private void builder(Tag<Block> tag, Collection<? extends IBlockProvider> blocks) {
        getBuilder(tag).add(blocks.stream().map(IBlockProvider::asBlock).toArray(Block[]::new));
    }

    private void gemBuilder(Tag<Block> tag, Function<Gems, Tag<Block>> gemTagGetter) {
        Tag.Builder<Block> builder = getBuilder(tag);
        Arrays.stream(Gems.values()).map(gemTagGetter).forEach(builder::add);
    }
}
