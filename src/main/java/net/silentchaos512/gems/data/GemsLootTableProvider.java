package net.silentchaos512.gems.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.gems.block.GemLampBlock;
import net.silentchaos512.gems.setup.GemsBlocks;
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
        map.forEach((p_218436_2_, p_218436_3_) -> LootTableManager.validateLootTable(validationtracker, p_218436_2_, p_218436_3_));
    }

    private static final class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {
        @Override
        protected void addTables() {
            for (Gems gem : Gems.values()) {
                registerLootTable(gem.getOre(World.OVERWORLD), droppingItemWithFortune(gem.getOre(World.OVERWORLD), gem.getItem()));
                registerLootTable(gem.getOre(World.THE_NETHER), droppingItemWithFortune(gem.getOre(World.THE_NETHER), gem.getItem()));
                registerLootTable(gem.getOre(World.THE_END), droppingItemWithFortune(gem.getOre(World.THE_END), gem.getItem()));
                registerDropSelfLootTable(gem.getBlock());
                registerDropSelfLootTable(gem.getBricks());
                registerDropSelfLootTable(gem.getGlass());
                for (GemLampBlock.State state : GemLampBlock.State.values()) {
                    // Always drop the unpowered version of the lamp, as the others don't have items
                    registerDropping(gem.getLamp(state), gem.getLamp(state.withPower(false)));
                }
                registerDropSelfLootTable(gem.getGlowrose());
                registerFlowerPot(gem.getPottedGlowrose());
            }

            registerDropSelfLootTable(GemsBlocks.SILVER_ORE.get());
            registerDropSelfLootTable(GemsBlocks.SILVER_BLOCK.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
        }
    }
}
