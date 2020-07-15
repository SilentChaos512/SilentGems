package net.silentchaos512.gems.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;

public class GemsTags {
    public static final class Blocks {
        // Forge tags
        public static final Tag<Block> ORES_SILVER = forge("ores/silver");
        public static final Tag<Block> STORAGE_BLOCKS_CHAOS = forge("storage_blocks/chaos");
        public static final Tag<Block> STORAGE_BLOCKS_CHAOS_COAL = forge("storage_blocks/chaos_coal");
        public static final Tag<Block> STORAGE_BLOCKS_CHAOS_IRON = forge("storage_blocks/chaos_iron");
        public static final Tag<Block> STORAGE_BLOCKS_SILVER = forge("storage_blocks/silver");

        // Mod tags
        public static final Tag<Block> CORRUPTABLE_DIRT = gems("corruptables/dirt");
        public static final Tag<Block> CORRUPTABLE_STONE = gems("corruptables/stone");
        public static final Tag<Block> FLUFFY_BLOCKS = gems("fluffy_blocks");
        public static final Tag<Block> GEM_BLOCKS = gems("gem_blocks");
        public static final Tag<Block> GLOWROSES = gems("glowroses");
        public static final Tag<Block> HARDENED_ROCKS = gems("hardened_rocks");
        public static final Tag<Block> PEDESTALS = gems("pedestals");
        public static final Tag<Block> SUPERCHARGER_PILLAR_CAP = gems("supercharger_pillar/cap");
        public static final Tag<Block> SUPERCHARGER_PILLAR_LEVEL1 = gems("supercharger_pillar/level1");
        public static final Tag<Block> SUPERCHARGER_PILLAR_LEVEL2 = gems("supercharger_pillar/level2");
        public static final Tag<Block> SUPERCHARGER_PILLAR_LEVEL3 = gems("supercharger_pillar/level3");

        private Blocks() {}

        private static Tag<Block> forge(String path) {
            return tag("forge", path);
        }

        private static Tag<Block> gems(String path) {
            return tag(SilentGems.MOD_ID, path);
        }

        private static Tag<Block> tag(String namespace, String name) {
            return new BlockTags.Wrapper(new ResourceLocation(namespace, name));
        }
    }

    public static final class Items {
        // Forge tags
        public static final Tag<Item> GEMS_CHAOS = forge("gems/chaos");
        public static final Tag<Item> INGOTS_SILVER = forge("ingots/silver");
        public static final Tag<Item> NUGGETS_SILVER = forge("nuggets/silver");
        public static final Tag<Item> ORES_SILVER = forge("ores/silver");
        public static final Tag<Item> RODS_ORNATE_GOLD = forge("rods/ornate_gold");
        public static final Tag<Item> RODS_ORNATE_SILVER = forge("rods/ornate_silver");
        public static final Tag<Item> STORAGE_BLOCKS_CHAOS = forge("storage_blocks/chaos");
        public static final Tag<Item> STORAGE_BLOCKS_CHAOS_COAL = forge("storage_blocks/chaos_coal");
        public static final Tag<Item> STORAGE_BLOCKS_CHAOS_IRON = forge("storage_blocks/chaos_iron");
        public static final Tag<Item> STORAGE_BLOCKS_SILVER = forge("storage_blocks/silver");

        // Mod tags
        public static final Tag<Item> CHAOS_GEMS = gems("chaos_gems");
        public static final Tag<Item> CHAOS_ORBS = gems("chaos_orbs");
        public static final Tag<Item> CHARGING_AGENT_TIER1 = gems("charging_agents/tier1");
        public static final Tag<Item> CHARGING_AGENT_TIER2 = gems("charging_agents/tier2");
        public static final Tag<Item> CHARGING_AGENT_TIER3 = gems("charging_agents/tier3");
        public static final Tag<Item> CHARGING_AGENTS = gems("charging_agents");
        public static final Tag<Item> CORRUPTED_DUSTS = gems("corrupted_dusts");
        public static final Tag<Item> FLUFFY_BLOCKS = gems("fluffy_blocks");
        public static final Tag<Item> GEM_BLOCKS = gems("gem_blocks");
        public static final Tag<Item> GEMS = gems("gems");
        public static final Tag<Item> GEMS_CLASSIC = gems("gems/classic");
        public static final Tag<Item> GEMS_DARK = gems("gems/dark");
        public static final Tag<Item> GEMS_LIGHT = gems("gems/light");
        public static final Tag<Item> GLOWROSES = gems("glowroses");
        public static final Tag<Item> HARDENED_ROCKS = gems("hardened_rocks");
        public static final Tag<Item> PEDESTALS = gems("pedestals");
        public static final Tag<Item> RETURN_HOME_CHARMS = gems("return_home_charms");
        public static final Tag<Item> SHARDS = gems("shards");
        public static final Tag<Item> STEW_FISH = gems("stew_fish");
        public static final Tag<Item> STEW_MEAT = gems("stew_meat");
        public static final Tag<Item> SUPERCHARGER_PILLAR_CAP = gems("supercharger_pillar/cap");
        public static final Tag<Item> SUPERCHARGER_PILLAR_LEVEL1 = gems("supercharger_pillar/level1");
        public static final Tag<Item> SUPERCHARGER_PILLAR_LEVEL2 = gems("supercharger_pillar/level2");
        public static final Tag<Item> SUPERCHARGER_PILLAR_LEVEL3 = gems("supercharger_pillar/level3");
        public static final Tag<Item> TELEPORTER_CATALYST = gems("teleporter_catalyst");
        public static final Tag<Item> TELEPORTERS = gems("teleporters");
        public static final Tag<Item> WISP_ESSENCES = gems("wisp_essences");

        private Items() {}

        private static Tag<Item> forge(String path) {
            return tag("forge", path);
        }

        private static Tag<Item> gems(String path) {
            return tag(SilentGems.MOD_ID, path);
        }

        private static Tag<Item> tag(String namespace, String name) {
            return new ItemTags.Wrapper(new ResourceLocation(namespace, name));
        }
    }
}
