package net.silentchaos512.gems.setup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.silentchaos512.gems.GemsBase;

public class GemsTags {
    public static final class Blocks {
        public static final TagKey<Block> ORES_SILVER = forge("ores/silver");

        public static final TagKey<Block> GEM_ORES = gems("ores");
        public static final TagKey<Block> GLOWROSES = gems("glowroses");

        private Blocks() {}

        private static TagKey<Block> forge(String path) {
            return tag("forge", path);
        }

        private static TagKey<Block> gems(String path) {
            return tag(GemsBase.MOD_ID, path);
        }

        private static TagKey<Block> tag(String namespace, String name) {
            return BlockTags.create(new ResourceLocation(namespace, name));
        }
    }

    public static final class Items {
        public static final TagKey<Item> INGOTS_SILVER = forge("ingots/silver");
        public static final TagKey<Item> ORES_SILVER = forge("ores/silver");
        public static final TagKey<Item> NUGGETS_SILVER = forge("nuggets/silver");

        public static final TagKey<Item> GEM_ORES = gems("ores");
        public static final TagKey<Item> GEMS = gems("gems");
        public static final TagKey<Item> GLOWROSES = gems("glowroses");
        public static final TagKey<Item> STEW_FISH = gems("stew_fish");
        public static final TagKey<Item> STEW_MEAT = gems("stew_meat");

        private Items() {}

        private static TagKey<Item> forge(String path) {
            return tag("forge", path);
        }

        private static TagKey<Item> gems(String path) {
            return tag(GemsBase.MOD_ID, path);
        }

        private static TagKey<Item> tag(String namespace, String name) {
            return ItemTags.create(new ResourceLocation(namespace, name));
        }
    }
}
