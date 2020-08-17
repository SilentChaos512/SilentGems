package net.silentchaos512.gems.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.RandomChanceWithLooting;
import net.minecraft.loot.functions.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.init.*;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.WispTypes;
import net.silentchaos512.lib.util.NameUtils;

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
    public String getName() {
        return "Silent's Gems - Loot Tables";
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(GiftLootTables::new, LootParameterSets.GIFT),
                Pair.of(EntityLootTables::new, LootParameterSets.ENTITY),
                Pair.of(BlockLootTables::new, LootParameterSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        map.forEach((p_218436_2_, p_218436_3_) -> LootTableManager.func_227508_a_(validationtracker, p_218436_2_, p_218436_3_));
    }

    private static final class GiftLootTables extends net.minecraft.data.loot.GiftLootTables {
        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
            consumer.accept(GemsLootTables.CLASSIC_GEMS, LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .addEntry(TagLootEntry.func_216176_b(GemsTags.Items.GEMS_CLASSIC))));
            consumer.accept(GemsLootTables.DARK_GEMS, LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .addEntry(TagLootEntry.func_216176_b(GemsTags.Items.GEMS_DARK))));
            consumer.accept(GemsLootTables.LIGHT_GEMS, LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .addEntry(TagLootEntry.func_216176_b(GemsTags.Items.GEMS_LIGHT))));
            consumer.accept(GemsLootTables.GEMS, LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .addEntry(TableLootEntry.builder(GemsLootTables.CLASSIC_GEMS)
                                    .weight(15))
                            .addEntry(TableLootEntry.builder(GemsLootTables.DARK_GEMS)
                                    .weight(5))
                            .addEntry(TableLootEntry.builder(GemsLootTables.LIGHT_GEMS)
                                    .weight(1))));

            consumer.accept(GemsLootTables.BAGS_CLASSIC_GEMS, LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .rolls(new RandomValueRange(2, 3))
                            .addEntry(TableLootEntry.builder(GemsLootTables.CLASSIC_GEMS)
                                    .acceptFunction(SetCount.builder(new RandomValueRange(1, 4))))));
            consumer.accept(GemsLootTables.BAGS_DARK_GEMS, LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .rolls(new RandomValueRange(2, 3))
                            .addEntry(TableLootEntry.builder(GemsLootTables.DARK_GEMS)
                                    .acceptFunction(SetCount.builder(new RandomValueRange(1, 4))))));
            consumer.accept(GemsLootTables.BAGS_LIGHT_GEMS, LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .rolls(new RandomValueRange(2, 3))
                            .addEntry(TableLootEntry.builder(GemsLootTables.LIGHT_GEMS)
                                    .acceptFunction(SetCount.builder(new RandomValueRange(1, 4))))));
        }
    }

    private static final class EntityLootTables extends net.minecraft.data.loot.EntityLootTables {
        @Override
        protected void addTables() {
            registerLootTable(GemsEntities.CORRUPTED_SLIME.get(), LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .addEntry(ItemLootEntry.builder(CraftingItems.CORRUPTED_SLIME_BALL)
                                    .acceptCondition(RandomChanceWithLooting.builder(0.15f, 0.05f)))));
            registerLootTable(GemsEntities.ENDER_SLIME.get(), LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .addEntry(ItemLootEntry.builder(CraftingItems.ENDER_SLIME_BALL)
                                    .acceptCondition(RandomChanceWithLooting.builder(0.075f, 0.025f)))));

            registerLootTable(WispTypes.CHAOS.getEntityType(), LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .addEntry(ItemLootEntry.builder(CraftingItems.CHAOS_WISP_ESSENCE)
                                    .acceptFunction(SetCount.builder(new RandomValueRange(0, 2))))));
            registerLootTable(WispTypes.FIRE.getEntityType(), LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .addEntry(ItemLootEntry.builder(CraftingItems.FIRE_WISP_ESSENCE)
                                    .acceptFunction(SetCount.builder(new RandomValueRange(0, 2))))));
            registerLootTable(WispTypes.ICE.getEntityType(), LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .addEntry(ItemLootEntry.builder(CraftingItems.ICE_WISP_ESSENCE)
                                    .acceptFunction(SetCount.builder(new RandomValueRange(0, 2))))));
            registerLootTable(WispTypes.LIGHTNING.getEntityType(), LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .addEntry(ItemLootEntry.builder(CraftingItems.LIGHTNING_WISP_ESSENCE)
                                    .acceptFunction(SetCount.builder(new RandomValueRange(0, 2))))));
            registerLootTable(WispTypes.WATER.getEntityType(), LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .addEntry(ItemLootEntry.builder(CraftingItems.WATER_WISP_ESSENCE)
                                    .acceptFunction(SetCount.builder(new RandomValueRange(0, 2))))));
        }

        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            return ForgeRegistries.ENTITIES.getValues().stream()
                    .filter(type -> SilentGems.MOD_ID.equals(NameUtils.from(type).getNamespace()))
                    .collect(Collectors.toList());
        }
    }

    private static final class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {
        @SuppressWarnings("OverlyLongMethod")
        @Override
        protected void addTables() {
            for (Gems gem : Gems.values()) {
                registerDropSelfLootTable(gem.getBlock());
                registerDropSelfLootTable(gem.getBricks());
                registerDropSelfLootTable(gem.getGlass());
                registerDropSelfLootTable(gem.getGlowrose());
                registerDropSelfLootTable(gem.getLamp(GemLampBlock.State.UNLIT));
                registerDropSelfLootTable(gem.getLamp(GemLampBlock.State.INVERTED_LIT));
                registerDropSelfLootTable(gem.getTeleporter());
                registerDropSelfLootTable(gem.getRedstoneTeleporter());
                registerLootTable(gem.getOre(), droppingItemWithFortune(gem.getOre(), gem.getItem()));
                registerFlowerPot(gem.getPottedGlowrose());
            }

            registerDropSelfLootTable(GemsBlocks.TELEPORTER_ANCHOR.get());

            registerLootTable(GemsBlocks.MULTI_ORE_CLASSIC.get(), block ->
                    droppingWithSilkTouch(block, withExplosionDecay(block, TableLootEntry.builder(GemsLootTables.CLASSIC_GEMS)
                            .acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE)))));
            registerLootTable(GemsBlocks.MULTI_ORE_DARK.get(), block ->
                    droppingWithSilkTouch(block, withExplosionDecay(block, TableLootEntry.builder(GemsLootTables.DARK_GEMS)
                            .acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE)))));
            registerLootTable(GemsBlocks.MULTI_ORE_LIGHT.get(), block ->
                    droppingWithSilkTouch(block, withExplosionDecay(block, TableLootEntry.builder(GemsLootTables.LIGHT_GEMS)
                            .acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE)))));

            for (MiscBlocks block : MiscBlocks.values()) {
                registerDropSelfLootTable(block.asBlock());
            }

            this.registerLootTable(MiscOres.CHAOS.asBlock(), block ->
                    droppingWithSilkTouch(block, withExplosionDecay(block, ItemLootEntry.builder(CraftingItems.CHAOS_CRYSTAL)
                            .acceptFunction(SetCount.builder(RandomValueRange.of(3, 4)))
                            .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE)))));
            this.registerLootTable(MiscOres.ENDER.asBlock(), block ->
                    droppingWithSilkTouch(block, withExplosionDecay(block, ItemLootEntry.builder(CraftingItems.ENDER_CRYSTAL)
                            .acceptFunction(SetCount.builder(RandomValueRange.of(3, 4)))
                            .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE)))));
            registerDropSelfLootTable(MiscOres.SILVER.asBlock());

            for (CorruptedBlocks block : CorruptedBlocks.values()) {
                registerLootTable(block.asBlock(), b ->
                        droppingWithSilkTouchOrRandomly(b, block.getPile(), ConstantRange.of(4)));
            }
            for (HardenedRock block : HardenedRock.values()) {
                registerDropSelfLootTable(block.asBlock());
            }

            registerDropSelfLootTable(GemsBlocks.WHITE_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.ORANGE_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.MAGENTA_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.LIGHT_BLUE_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.YELLOW_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.LIME_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.PINK_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.GRAY_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.LIGHT_GRAY_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.CYAN_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.PURPLE_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.BLUE_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.BROWN_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.GREEN_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.RED_FLUFFY_BLOCK.get());
            registerDropSelfLootTable(GemsBlocks.BLACK_FLUFFY_BLOCK.get());

            registerLootTable(GemsBlocks.SOUL_URN.get(), LootTable.builder()
                    .addLootPool(withSurvivesExplosion(GemsBlocks.SOUL_URN, LootPool.builder()
                            .rolls(ConstantRange.of(1))
                            .addEntry(ItemLootEntry.builder(GemsBlocks.SOUL_URN)
                                    .acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY))
                                    .acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY)
                                            .replaceOperation("Lock", "BlockEntityTag.Lock")
                                            .replaceOperation("LootTable", "BlockEntityTag.LootTable")
                                            .replaceOperation("LootTableSeed", "BlockEntityTag.LootTableSeed")
                                            .replaceOperation("Lidded", "BlockEntityTag.Lidded")
                                            .replaceOperation("Color", "BlockEntityTag.Color")
                                            .replaceOperation("Gem", "BlockEntityTag.Gem")
                                            .replaceOperation("Upgrades", "BlockEntityTag.Upgrades"))
                                    .acceptFunction(SetContents.builderIn()
                                            .addLootEntry(DynamicLootEntry.func_216162_a(ShulkerBoxBlock.CONTENTS)))))));

            registerDropSelfLootTable(GemsBlocks.SUPERCHARGER.get());
            registerDropSelfLootTable(GemsBlocks.TOKEN_ENCHANTER.get());
            registerDropSelfLootTable(GemsBlocks.TRANSMUTATION_ALTAR.get());
            registerDropSelfLootTable(GemsBlocks.PURIFIER.get());

            registerDropSelfLootTable(GemsBlocks.STONE_PEDESTAL.get());
            registerDropSelfLootTable(GemsBlocks.GRANITE_PEDESTAL.get());
            registerDropSelfLootTable(GemsBlocks.DIORITE_PEDESTAL.get());
            registerDropSelfLootTable(GemsBlocks.ANDESITE_PEDESTAL.get());
            registerDropSelfLootTable(GemsBlocks.OBSIDIAN_PEDESTAL.get());

            registerDropSelfLootTable(GemsBlocks.LUMINOUS_FLOWER_POT.get());

            this.registerLootTable(GemsBlocks.FLUFFY_PUFF_PLANT.get(), block -> {
                BlockStateProperty.Builder fluffyPuffMatureCondition = BlockStateProperty.builder(GemsBlocks.FLUFFY_PUFF_PLANT.get())
                        .fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(CropsBlock.AGE, 7));
                return droppingAndBonusWhen(block, CraftingItems.FLUFFY_PUFF.asItem(), GemsItems.FLUFFY_PUFF_SEEDS.get(), fluffyPuffMatureCondition)
                        .addLootPool(LootPool.builder()
                                .addEntry(ItemLootEntry.builder(CraftingItems.FLUFFY_PUFF)
                                        .acceptCondition(fluffyPuffMatureCondition)
                                        .acceptFunction(ApplyBonus.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286f, 3))));
            });
            registerDropping(GemsBlocks.WILD_FLUFFY_PUFF_PLANT.get(), GemsItems.FLUFFY_PUFF_SEEDS);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
        }
    }
}
