package net.silentchaos512.gems.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;

public class ModTags {
    public static class Blocks {
        public static final Tag<Block> SUPERCHARGER_PILLAR_CAP = tag("supercharger_pillar/cap");
        public static final Tag<Block> SUPERCHARGER_PILLAR_LEVEL1 = tag("supercharger_pillar/level1");
        public static final Tag<Block> SUPERCHARGER_PILLAR_LEVEL2 = tag("supercharger_pillar/level2");
        public static final Tag<Block> SUPERCHARGER_PILLAR_LEVEL3 = tag("supercharger_pillar/level3");

        private static Tag<Block> tag(String name) {
            return new BlockTags.Wrapper(new ResourceLocation(SilentGems.MOD_ID, name));
        }

        private static Tag<Block> tag(String namespace, String name) {
            return new BlockTags.Wrapper(new ResourceLocation(namespace, name));
        }
    }

    public static class Items {
        public static final Tag<Item> GEMS_CHAOS = tag("forge", "gems/chaos");
        public static final Tag<Item> MOD_GEMS = tag("gems");
        public static final Tag<Item> MOD_SHARDS = tag("shards");
        public static final Tag<Item> RODS_ORNATE_GOLD = tag("forge", "rods/ornate_gold");
        public static final Tag<Item> RODS_ORNATE_SILVER = tag("forge", "rods/ornate_silver");
        public static final Tag<Item> STEW_FISH = tag("stew_fish");
        public static final Tag<Item> STEW_MEAT = tag("stew_meat");

        private static Tag<Item> tag(String name) {
            return new ItemTags.Wrapper(new ResourceLocation(SilentGems.MOD_ID, name));
        }

        private static Tag<Item> tag(String namespace, String name) {
            return new ItemTags.Wrapper(new ResourceLocation(namespace, name));
        }
    }
}
