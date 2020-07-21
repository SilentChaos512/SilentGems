package net.silentchaos512.gems.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;

public class GemsTags {
    public static final class Blocks {
        // Forge tags
        public static final ITag.INamedTag<Block> ORES_SILVER = forge("ores/silver");
        public static final ITag.INamedTag<Block> STORAGE_BLOCKS_CHAOS = forge("storage_blocks/chaos");
        public static final ITag.INamedTag<Block> STORAGE_BLOCKS_CHAOS_COAL = forge("storage_blocks/chaos_coal");
        public static final ITag.INamedTag<Block> STORAGE_BLOCKS_CHAOS_IRON = forge("storage_blocks/chaos_iron");
        public static final ITag.INamedTag<Block> STORAGE_BLOCKS_SILVER = forge("storage_blocks/silver");

        // Mod tags
        public static final ITag.INamedTag<Block> CORRUPTABLE_DIRT = gems("corruptables/dirt");
        public static final ITag.INamedTag<Block> CORRUPTABLE_STONE = gems("corruptables/stone");
        public static final ITag.INamedTag<Block> FLUFFY_BLOCKS = gems("fluffy_blocks");
        public static final ITag.INamedTag<Block> GEM_BLOCKS = gems("gem_blocks");
        public static final ITag.INamedTag<Block> GLOWROSES = gems("glowroses");
        public static final ITag.INamedTag<Block> HARDENED_ROCKS = gems("hardened_rocks");
        public static final ITag.INamedTag<Block> PEDESTALS = gems("pedestals");
        public static final ITag.INamedTag<Block> SUPERCHARGER_PILLAR_CAP = gems("supercharger_pillar/cap");
        public static final ITag.INamedTag<Block> SUPERCHARGER_PILLAR_LEVEL1 = gems("supercharger_pillar/level1");
        public static final ITag.INamedTag<Block> SUPERCHARGER_PILLAR_LEVEL2 = gems("supercharger_pillar/level2");
        public static final ITag.INamedTag<Block> SUPERCHARGER_PILLAR_LEVEL3 = gems("supercharger_pillar/level3");

        private Blocks() {}

        private static ITag.INamedTag<Block> forge(String path) {
            return tag("forge", path);
        }

        private static ITag.INamedTag<Block> gems(String path) {
            return tag(SilentGems.MOD_ID, path);
        }

        private static ITag.INamedTag<Block> tag(String namespace, String name) {
            return BlockTags.makeWrapperTag(new ResourceLocation(namespace, name).toString());
        }
    }

    public static final class Items {
        // Forge tags
        public static final ITag.INamedTag<Item> GEMS_CHAOS = forge("gems/chaos");
        public static final ITag.INamedTag<Item> INGOTS_SILVER = forge("ingots/silver");
        public static final ITag.INamedTag<Item> NUGGETS_SILVER = forge("nuggets/silver");
        public static final ITag.INamedTag<Item> ORES_SILVER = forge("ores/silver");
        public static final ITag.INamedTag<Item> STORAGE_BLOCKS_CHAOS = forge("storage_blocks/chaos");
        public static final ITag.INamedTag<Item> STORAGE_BLOCKS_CHAOS_COAL = forge("storage_blocks/chaos_coal");
        public static final ITag.INamedTag<Item> STORAGE_BLOCKS_CHAOS_IRON = forge("storage_blocks/chaos_iron");
        public static final ITag.INamedTag<Item> STORAGE_BLOCKS_SILVER = forge("storage_blocks/silver");

        // Mod tags
        public static final ITag.INamedTag<Item> CHAOS_GEMS = gems("chaos_gems");
        public static final ITag.INamedTag<Item> CHAOS_ORBS = gems("chaos_orbs");
        public static final ITag.INamedTag<Item> CHARGING_AGENT_TIER1 = gems("charging_agents/tier1");
        public static final ITag.INamedTag<Item> CHARGING_AGENT_TIER2 = gems("charging_agents/tier2");
        public static final ITag.INamedTag<Item> CHARGING_AGENT_TIER3 = gems("charging_agents/tier3");
        public static final ITag.INamedTag<Item> CHARGING_AGENTS = gems("charging_agents");
        public static final ITag.INamedTag<Item> CORRUPTED_DUSTS = gems("corrupted_dusts");
        public static final ITag.INamedTag<Item> FLUFFY_BLOCKS = gems("fluffy_blocks");
        public static final ITag.INamedTag<Item> GEM_BLOCKS = gems("gem_blocks");
        public static final ITag.INamedTag<Item> GEMS = gems("gems");
        public static final ITag.INamedTag<Item> GEMS_CLASSIC = gems("gems/classic");
        public static final ITag.INamedTag<Item> GEMS_DARK = gems("gems/dark");
        public static final ITag.INamedTag<Item> GEMS_LIGHT = gems("gems/light");
        public static final ITag.INamedTag<Item> GLOWROSES = gems("glowroses");
        public static final ITag.INamedTag<Item> HARDENED_ROCKS = gems("hardened_rocks");
        public static final ITag.INamedTag<Item> PEDESTALS = gems("pedestals");
        public static final ITag.INamedTag<Item> RETURN_HOME_CHARMS = gems("return_home_charms");
        public static final ITag.INamedTag<Item> SHARDS = gems("shards");
        public static final ITag.INamedTag<Item> STEW_FISH = gems("stew_fish");
        public static final ITag.INamedTag<Item> STEW_MEAT = gems("stew_meat");
        public static final ITag.INamedTag<Item> SUPERCHARGER_PILLAR_CAP = gems("supercharger_pillar/cap");
        public static final ITag.INamedTag<Item> SUPERCHARGER_PILLAR_LEVEL1 = gems("supercharger_pillar/level1");
        public static final ITag.INamedTag<Item> SUPERCHARGER_PILLAR_LEVEL2 = gems("supercharger_pillar/level2");
        public static final ITag.INamedTag<Item> SUPERCHARGER_PILLAR_LEVEL3 = gems("supercharger_pillar/level3");
        public static final ITag.INamedTag<Item> TELEPORTER_CATALYST = gems("teleporter_catalyst");
        public static final ITag.INamedTag<Item> TELEPORTERS = gems("teleporters");
        public static final ITag.INamedTag<Item> WISP_ESSENCES = gems("wisp_essences");

        private Items() {}

        private static ITag.INamedTag<Item> forge(String path) {
            return tag("forge", path);
        }

        private static ITag.INamedTag<Item> gems(String path) {
            return tag(SilentGems.MOD_ID, path);
        }

        private static ITag.INamedTag<Item> tag(String namespace, String name) {
            return ItemTags.makeWrapperTag(new ResourceLocation(namespace, name).toString());
        }
    }
}
