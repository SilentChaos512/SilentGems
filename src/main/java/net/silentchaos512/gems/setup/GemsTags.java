package net.silentchaos512.gems.setup;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.silentchaos512.gems.GemsBase;

public class GemsTags {
    public static final class Blocks {
        public static final Tag.Named<Block> ORES_SILVER = forge("ores/silver");

        public static final Tag.Named<Block> GEM_ORES = gems("ores");
        public static final Tag.Named<Block> GLOWROSES = gems("glowroses");

        private Blocks() {}

        private static Tag.Named<Block> forge(String path) {
            return tag("forge", path);
        }

        private static Tag.Named<Block> gems(String path) {
            return tag(GemsBase.MOD_ID, path);
        }

        private static Tag.Named<Block> tag(String namespace, String name) {
            return BlockTags.bind(new ResourceLocation(namespace, name).toString());
        }
    }

    public static final class Items {
        public static final Tag.Named<Item> INGOTS_SILVER = forge("ingots/silver");
        public static final Tag.Named<Item> ORES_SILVER = forge("ores/silver");
        public static final Tag.Named<Item> NUGGETS_SILVER = forge("nuggets/silver");

        public static final Tag.Named<Item> GEM_ORES = gems("ores");
        public static final Tag.Named<Item> GEMS = gems("gems");
        public static final Tag.Named<Item> GLOWROSES = gems("glowroses");
        public static final Tag.Named<Item> STEW_FISH = gems("stew_fish");
        public static final Tag.Named<Item> STEW_MEAT = gems("stew_meat");

        private Items() {}

        private static Tag.Named<Item> forge(String path) {
            return tag("forge", path);
        }

        private static Tag.Named<Item> gems(String path) {
            return tag(GemsBase.MOD_ID, path);
        }

        private static Tag.Named<Item> tag(String namespace, String name) {
            return ItemTags.bind(new ResourceLocation(namespace, name).toString());
        }
    }
}
