package net.silentchaos512.gems.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;
import net.silentchaos512.gems.block.GemLampBlock;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.setup.Registration;
import net.silentchaos512.gems.util.Gems;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GemsLootTableProvider extends LootTableProvider {
    public GemsLootTableProvider(DataGenerator gen) {
        super(gen.getPackOutput(), Collections.emptySet(), VanillaLootTableProvider.create(gen.getPackOutput()).getTables());
    }

    @Override
    public List<SubProviderEntry> getTables() {
        return ImmutableList.of(
                new SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        map.forEach((p_218436_2_, p_218436_3_) -> LootTables.validate(validationtracker, p_218436_2_, p_218436_3_));
    }

    private static final class BlockLootTables extends BlockLootSubProvider {
        protected BlockLootTables() {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
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
                dropSelf(gem.getGlass());
                for (GemLampBlock.State state : GemLampBlock.State.values()) {
                    // Always drop the unpowered version of the lamp, as the others don't have items
                    dropOther(gem.getLamp(state), gem.getLamp(state.withPower(false)));
                }
                dropSelf(gem.getGlowrose());
                dropPottedContents(gem.getPottedGlowrose());
            }

            registerFortuneDrops(GemsBlocks.SILVER_ORE.get(), GemsItems.RAW_SILVER.get());
            registerFortuneDrops(GemsBlocks.DEEPSLATE_SILVER_ORE.get(), GemsItems.RAW_SILVER.get());
            dropSelf(GemsBlocks.SILVER_BLOCK.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
        }

        private void registerFortuneDrops(Block block, Item item) {
            add(block, createOreDrop(block, item));
        }
    }
}
