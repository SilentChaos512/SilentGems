package net.silentchaos512.gems.setup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.silentchaos512.gems.SilentGems;

public class GemsTags {
    public static final class Blocks {
        public static final TagKey<Block> ORES_CHAOS = common("ores/chaos");
        public static final TagKey<Block> ORES_SILVER = common("ores/silver");

        public static final TagKey<Block> GEM_ORES = gems("ores");
        public static final TagKey<Block> GLOWROSES = gems("glowroses");

        private Blocks() {}

        private static TagKey<Block> common(String path) {
            return tag("c", path);
        }

        private static TagKey<Block> gems(String path) {
            return tag(SilentGems.MOD_ID, path);
        }

        private static TagKey<Block> tag(String namespace, String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(namespace, name));
        }
    }

    public static final class Items {
        public static final TagKey<Item> INGOTS_SILVER = common("ingots/silver");
        public static final TagKey<Item> ORES_CHAOS = common("ores/chaos");
        public static final TagKey<Item> ORES_SILVER = common("ores/silver");
        public static final TagKey<Item> NUGGETS_SILVER = common("nuggets/silver");

        public static final TagKey<Item> GEM_ORES = gems("ores");
        public static final TagKey<Item> GEMS = gems("gems");
        public static final TagKey<Item> GLOWROSES = gems("glowroses");
        public static final TagKey<Item> STEW_FISH = gems("stew_fish");
        public static final TagKey<Item> STEW_MEAT = gems("stew_meat");

        public static final TagKey<Item> FLOWER_BASKET_CAN_STORE = gems("flower_basket_can_store");
        public static final TagKey<Item> GEM_BAG_CAN_STORE = gems("gem_bag_can_store");

        private Items() {}

        private static TagKey<Item> common(String path) {
            return tag("c", path);
        }

        private static TagKey<Item> gems(String path) {
            return tag(SilentGems.MOD_ID, path);
        }

        private static TagKey<Item> tag(String namespace, String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, name));
        }
    }
}
