package net.silentchaos512.gems.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.silentchaos512.gems.SilentGems;

import java.util.List;

public final class ModLoot {
    public static final ResourceLocation ENDER_SLIME = get("ender_slime");
    public static final ResourceLocation RANDOM_GEMS = get("random_gems");

    private static final List<ResourceLocation> ADD_GEMS_TO = ImmutableList.of(
            LootTableList.CHESTS_ABANDONED_MINESHAFT,
            LootTableList.CHESTS_BURIED_TREASURE,
            LootTableList.CHESTS_DESERT_PYRAMID,
            LootTableList.CHESTS_END_CITY_TREASURE,
            LootTableList.CHESTS_JUNGLE_TEMPLE,
            LootTableList.CHESTS_SHIPWRECK_TREASURE,
            LootTableList.CHESTS_STRONGHOLD_CROSSING,
            LootTableList.CHESTS_WOODLAND_MANSION
    );

    private ModLoot() {}

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(ModLoot::onLootTableLoad);

        LootTableList.register(ENDER_SLIME);
        LootTableList.register(RANDOM_GEMS);
    }

    private static ResourceLocation get(String resourcePath) {
        return new ResourceLocation(SilentGems.MOD_ID, resourcePath);
    }

    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (ADD_GEMS_TO.contains(event.getName())) {
            LootPool main = event.getTable().getPool("main");
            //noinspection ConstantConditions -- claims main cannot be null, lies!
            if (main != null) {
                main.addEntry(new LootEntryTable(RANDOM_GEMS, 7, 0, new LootCondition[0], "silentgems_random_gems"));
            }
        }
    }
}
