package net.silentchaos512.gems.init;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;

public class ModTags {
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
