package net.silentchaos512.gemschaos.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.gemschaos.setup.ChaosBlocks;
import net.silentchaos512.gemschaos.setup.ChaosItems;
import net.silentchaos512.gemschaos.setup.ChaosRegistration;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ChaosLootTableProvider extends LootTableProvider {
    public ChaosLootTableProvider(DataGenerator dataGeneratorIn) {
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
            this.registerLootTable(ChaosBlocks.CHAOS_ORE.get(), block ->
                    droppingWithSilkTouch(block, withExplosionDecay(block, ItemLootEntry.builder(ChaosItems.CHAOS_CRYSTAL)
                            .acceptFunction(SetCount.builder(RandomValueRange.of(3, 4)))
                            .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE)))));

            registerDropSelfLootTable(ChaosBlocks.CHAOS_CRYSTAL_BLOCK.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ChaosRegistration.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
        }
    }
}
