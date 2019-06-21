package net.silentchaos512.gems.init;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.loot.functions.SetSoulFunction;

import java.util.Map;

@Mod.EventBusSubscriber(modid = SilentGems.MOD_ID)
public final class GemsLoot {
    public static final ResourceLocation RANDOM_GEMS = SilentGems.getId("random_gems");

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

    private GemsLoot() {}

    public static void init() {
        LootFunctionManager.registerFunction(SetSoulFunction.SERIALIZER);

        MinecraftForge.EVENT_BUS.addListener(GemsLoot::onLootTableLoad);
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation tableName = event.getName();
        if (ADD_GEMS_TO.keySet().contains(tableName)) {
            addGemsToTable(event, ADD_GEMS_TO.get(tableName));
        }
    }

    private static void addGemsToTable(LootTableLoadEvent event, int maxRolls) {
        SilentGems.LOGGER.info("Add gems to loot pool {} ({} rolls)", event.getName(), maxRolls);
        event.getTable().addPool((new LootPool.Builder())
                .name("silentgems_added_gems")
                .rolls(new RandomValueRange(1, maxRolls))
                .addEntry(TableLootEntry.func_216171_a(RANDOM_GEMS)
                        .weight(10)
                        .acceptFunction(SetCount.func_215932_a(new RandomValueRange(2, 5)))
                )
                .build());
    }
}
