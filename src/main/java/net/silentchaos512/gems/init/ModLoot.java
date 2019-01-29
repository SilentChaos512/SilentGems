package net.silentchaos512.gems.init;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.silentchaos512.gems.SilentGems;

import java.util.Map;

public final class ModLoot {
    public static final ResourceLocation ENDER_SLIME = get("ender_slime");
    public static final ResourceLocation RANDOM_GEMS = get("random_gems");

    // Maps to max rolls for that table
    private static final Map<ResourceLocation, Integer> ADD_GEMS_TO = ImmutableMap.<ResourceLocation, Integer>builder()
            .put(LootTableList.CHESTS_ABANDONED_MINESHAFT, 2)
            .put(LootTableList.CHESTS_BURIED_TREASURE, 4)
            .put(LootTableList.CHESTS_DESERT_PYRAMID, 3)
            .put(LootTableList.CHESTS_END_CITY_TREASURE, 2)
            .put(LootTableList.CHESTS_JUNGLE_TEMPLE, 2)
            .put(LootTableList.CHESTS_SHIPWRECK_TREASURE, 2)
            .put(LootTableList.CHESTS_SIMPLE_DUNGEON, 3)
            .put(LootTableList.CHESTS_STRONGHOLD_CROSSING, 4)
            .put(LootTableList.CHESTS_WOODLAND_MANSION, 4)
            .build();

    private ModLoot() {}

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(ModLoot::onLootTableLoad);

        LootTableList.register(ENDER_SLIME);
        LootTableList.register(RANDOM_GEMS);
    }

    private static ResourceLocation get(String resourcePath) {
        return new ResourceLocation(SilentGems.MOD_ID, resourcePath);
    }

    private static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation tableName = event.getName();
        if (ADD_GEMS_TO.keySet().contains(tableName)) {
            addGemsToTable(event.getTable(), ADD_GEMS_TO.get(tableName));
        }
    }

    private static void addGemsToTable(LootTable table, int maxRolls) {
        LootEntryTable entryTable = new LootEntryTable(RANDOM_GEMS, 10, 0, new LootCondition[0], "random_gems");
        table.addPool(new LootPool(
                new LootEntry[]{entryTable},
                new LootCondition[0],
                new RandomValueRange(1, maxRolls),
                new RandomValueRange(0, 1),
                "silentgems_random_gems_added"
        ));
    }
}
