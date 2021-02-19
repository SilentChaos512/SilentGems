package net.silentchaos512.gems.setup;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.GemsBase;

public class GemsTags {
    public static final class Blocks {
        public static final ITag.INamedTag<Block> GEM_ORES = gems("ores");
        public static final ITag.INamedTag<Block> GLOWROSES = gems("glowroses");

        private Blocks() {}

        private static ITag.INamedTag<Block> forge(String path) {
            return tag("forge", path);
        }

        private static ITag.INamedTag<Block> gems(String path) {
            return tag(GemsBase.MOD_ID, path);
        }

        private static ITag.INamedTag<Block> tag(String namespace, String name) {
            return BlockTags.makeWrapperTag(new ResourceLocation(namespace, name).toString());
        }
    }

    public static final class Items {
        public static final ITag.INamedTag<Item> GEM_ORES = gems("ores");
        public static final ITag.INamedTag<Item> GEMS = gems("gems");
        public static final ITag.INamedTag<Item> GLOWROSES = gems("glowroses");
        public static final ITag.INamedTag<Item> STEW_FISH = gems("stew_fish");
        public static final ITag.INamedTag<Item> STEW_MEAT = gems("stew_meat");

        private Items() {}

        private static ITag.INamedTag<Item> forge(String path) {
            return tag("forge", path);
        }

        private static ITag.INamedTag<Item> gems(String path) {
            return tag(GemsBase.MOD_ID, path);
        }

        private static ITag.INamedTag<Item> tag(String namespace, String name) {
            return ItemTags.makeWrapperTag(new ResourceLocation(namespace, name).toString());
        }
    }
}
