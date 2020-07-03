package net.silentchaos512.gems.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.loot.functions.SetSoulFunction;
import net.silentchaos512.gems.loot.modifier.ImperialTraitLootModifier;
import net.silentchaos512.gems.loot.modifier.SkullCollectorTraitLootModifier;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = SilentGems.MOD_ID)
public final class GemsLoot {
    public static final RegistryObject<GlobalLootModifierSerializer<?>> IMPERIAL = register("imperial", ImperialTraitLootModifier.Serializer::new);
    public static final RegistryObject<GlobalLootModifierSerializer<?>> SKULL_COLLECTOR = register("skull_collector", SkullCollectorTraitLootModifier.Serializer::new);

    public static final ResourceLocation RANDOM_GEMS = SilentGems.getId("random_gems");

    //region
    // Maps to max rolls for that table
    private static final Map<ResourceLocation, Integer> ADD_GEMS_TO = ImmutableMap.<ResourceLocation, Integer>builder()
            .put(LootTables.CHESTS_ABANDONED_MINESHAFT, 2)
            .put(LootTables.CHESTS_BURIED_TREASURE, 4)
            .put(LootTables.CHESTS_DESERT_PYRAMID, 3)
            .put(LootTables.CHESTS_END_CITY_TREASURE, 2)
            .put(LootTables.CHESTS_JUNGLE_TEMPLE, 2)
            .put(LootTables.CHESTS_SHIPWRECK_TREASURE, 2)
            .put(LootTables.CHESTS_SIMPLE_DUNGEON, 3)
            .put(LootTables.CHESTS_STRONGHOLD_CROSSING, 4)
            .put(LootTables.CHESTS_WOODLAND_MANSION, 4)
            .build();
    private static final List<ResourceLocation> ADD_RARE_ITEMS_TO = ImmutableList.of(
            LootTables.CHESTS_SIMPLE_DUNGEON,
            LootTables.CHESTS_VILLAGE_VILLAGE_TEMPLE,
            LootTables.CHESTS_NETHER_BRIDGE,
            LootTables.CHESTS_STRONGHOLD_CORRIDOR,
            LootTables.CHESTS_STRONGHOLD_CROSSING,
            LootTables.CHESTS_PILLAGER_OUTPOST
    );
    //endregion

    private GemsLoot() {}

    static void register() {
        LootFunctionManager.registerFunction(SetSoulFunction.SERIALIZER);

        MinecraftForge.EVENT_BUS.addListener(GemsLoot::onLootTableLoad);
    }

    private static <T extends GlobalLootModifierSerializer<?>> RegistryObject<T> register(String name, Supplier<T> serializer) {
        return Registration.LOOT_MODIFIERS.register(name, serializer);
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation tableName = event.getName();
        if (ADD_GEMS_TO.containsKey(tableName)) {
            addGemsToTable(event, ADD_GEMS_TO.get(tableName));
        }
        if (ADD_RARE_ITEMS_TO.contains(tableName)) {
            addRareItemsToTable(event);
        }
    }

    private static void addGemsToTable(LootTableLoadEvent event, int maxRolls) {
        if (hasLootPool(event.getTable(), "silentgems_added_gems")) return;

        SilentGems.LOGGER.info("Add gems to loot table {} ({} rolls)", event.getName(), maxRolls);
        event.getTable().addPool((new LootPool.Builder())
                .name("silentgems_added_gems")
                .rolls(new RandomValueRange(1, maxRolls))
                .addEntry(TableLootEntry.builder(RANDOM_GEMS)
                        .weight(10)
                        .acceptFunction(SetCount.builder(new RandomValueRange(2, 5)))
                )
                .build());
    }

    private static void addRareItemsToTable(LootTableLoadEvent event) {
        if (hasLootPool(event.getTable(), "silentgems_rare_items")) return;

        SilentGems.LOGGER.info("Add 'rare' items to loot table {}", event.getName());
        event.getTable().addPool((new LootPool.Builder())
                .name("silentgems_rare_items")
                .addEntry(ItemLootEntry.builder(GemsItems.CORRUPTING_POWDER)
                        .weight(1)
                        .acceptFunction(SetCount.builder(new RandomValueRange(1, 2)))
                )
                .addEntry(ItemLootEntry.builder(GemsItems.PURIFYING_POWDER)
                        .weight(1)
                        .acceptFunction(SetCount.builder(new RandomValueRange(1, 2)))
                )
                .addEntry(ItemLootEntry.builder(CraftingItems.ENDER_CRYSTAL)
                        .weight(1)
                        .acceptFunction(SetCount.builder(new RandomValueRange(2, 4)))
                )
                .addEntry(EmptyLootEntry.func_216167_a()
                        .weight(3)
                )
                .build()
        );
    }

    @SuppressWarnings("ConstantConditions")
    private static boolean hasLootPool(LootTable table, String poolName) {
        return table.getPool(poolName) != null;
    }
}
