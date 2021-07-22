package net.silentchaos512.gems.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.item.Item;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.gems.block.GemLampBlock;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.setup.Registration;
import net.silentchaos512.gems.util.Gems;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GemsLootTableProvider extends LootTableProvider {
    public GemsLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(BlockLootTables::new, LootParameterSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        map.forEach((p_218436_2_, p_218436_3_) -> LootTableManager.validate(validationtracker, p_218436_2_, p_218436_3_));
    }

    private static final class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {
        @Override
        protected void addTables() {
            for (Gems gem : Gems.values()) {
                registerFortuneDrops(gem.getOre(World.OVERWORLD), gem.getItem());
                registerFortuneDrops(gem.getOre(World.NETHER), gem.getItem());
                registerFortuneDrops(gem.getOre(World.END), gem.getItem());
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
