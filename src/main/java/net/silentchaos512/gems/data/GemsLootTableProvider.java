package net.silentchaos512.gems.data;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.neoforged.neoforge.data.event.DatapackRegistryGatherer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.silentchaos512.gems.block.GemLampBlock;
import net.silentchaos512.gems.setup.Gems;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsItems;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Loot tables are a reloadable datapack registry as of 26.3, so they are gathered in
 * {@link net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent} instead of being a standalone provider.
 */
public final class GemsLootTableProvider {
    private GemsLootTableProvider() {
    }

    public static DatapackRegistryGatherer gather(DatapackRegistryGatherer gatherer) {
        return gatherer.lootTable(
                new SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK)
        );
    }

    private static final class BlockLootTables extends BlockLootSubProvider {
        private BlockLootTables(LootTableSubProvider.Context output) {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), output);
        }

        @Override
        protected void generate() {
            for (Gems gem : Gems.values()) {
                registerFortuneDrops(gem.getOre(), gem.getItem());
                registerFortuneDrops(gem.getDeepslateOre(), gem.getItem());
                registerFortuneDrops(gem.getNetherOre(), gem.getItem());
                registerFortuneDrops(gem.getEndOre(), gem.getItem());
                dropSelf(gem.getBlock());
                dropSelf(gem.getBricks());
                dropSelf(gem.getTiles());
                dropSelf(gem.getSmallBricks());
                dropSelf(gem.getPolishedStone());
                dropSelf(gem.getSmoothStone());
                dropSelf(gem.getChiseledStone());
                dropSelf(gem.getGlass());
                for (GemLampBlock.State state : GemLampBlock.State.values()) {
                    // Always drop the unpowered version of the lamp, as the others don't have items
                    dropOther(gem.getLamp(state), gem.getLamp(state.withPower(false)));
                }
                dropSelf(gem.getGlowrose());
                dropPottedContents(gem.getPottedGlowrose());
                dropSelf(gem.getTeleporter().get());
                dropSelf(gem.getRedstoneTeleporter().get());
            }

            dropSelf(GemsBlocks.TELEPORTER_ANCHOR.get());
            add(GemsBlocks.CHAOS_ORE.get(), this::createChaosOreDrops);
            add(GemsBlocks.DEEPSLATE_CHAOS_ORE.get(), this::createChaosOreDrops);
            registerFortuneDrops(GemsBlocks.SILVER_ORE.get(), GemsItems.RAW_SILVER.get());
            registerFortuneDrops(GemsBlocks.DEEPSLATE_SILVER_ORE.get(), GemsItems.RAW_SILVER.get());
            dropSelf(GemsBlocks.CHAOS_ESSENCE_BLOCK.get());
            dropSelf(GemsBlocks.SILVER_BLOCK.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return GemsBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toList());
        }

        private void registerFortuneDrops(Block block, Item item) {
            add(block, createOreDrop(block, item));
        }

        private LootTable.Builder createChaosOreDrops(Block block) {
            var fortune = this.enchantments.getOrThrow(Enchantments.FORTUNE);
            return this.createSilkTouchDispatchTable(
                    block,
                    this.applyExplosionDecay(
                            block,
                            LootItem.lootTableItem(GemsItems.CHAOS_ESSENCE.get())
                                    .apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3)))
                                    .apply(ApplyBonusCount.addUniformBonusCount(fortune))
                    )
            );
        }
    }
}
