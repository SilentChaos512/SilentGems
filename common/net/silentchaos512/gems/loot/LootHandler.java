package net.silentchaos512.gems.loot;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.event.LootTableLoadEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;

public class LootHandler {

  public static void init(LootTableLoadEvent event) {

    if (event.getName().equals(LootTableList.CHESTS_SPAWN_BONUS_CHEST)) {
      LootPool main = event.getTable().getPool("main");
      if (main != null) {
        main.addEntry(new LootEntryItem(Items.FLINT, 30, 0, count(6, 12), new LootCondition[0],
            SilentGems.MODID + ":flint"));
      }
    } else if (event.getName().equals(LootTableList.CHESTS_ABANDONED_MINESHAFT)
        || event.getName().equals(LootTableList.CHESTS_DESERT_PYRAMID)
        || event.getName().equals(LootTableList.CHESTS_JUNGLE_TEMPLE)) {
      LootPool main = event.getTable().getPool("main");
      if (main != null) {
        main.addEntry(
            new LootEntryItem(ModItems.gem, 16, 1, countAndMeta(3, 8, 0, EnumGem.values().length),
                new LootCondition[0], SilentGems.MODID + "Gems1"));
        main.addEntry(
            new LootEntryItem(ModItems.gem, 16, 1, countAndMeta(3, 8, 0, EnumGem.values().length),
                new LootCondition[0], SilentGems.MODID + "Gems2"));
      }
    }
  }

  private static LootFunction[] count(float min, float max) {

    return new LootFunction[] {
        new SetCount(new LootCondition[0], new RandomValueRange(min, max)) };
  }

  private static LootFunction[] countAndMeta(float minCount, float maxCount, int minMeta,
      int maxMeta) {

    return new LootFunction[] {
        new SetCount(new LootCondition[0], new RandomValueRange(minCount, maxCount)),
        new SetMetadata(new LootCondition[0], new RandomValueRange(minMeta, maxMeta)) };
  }
}
