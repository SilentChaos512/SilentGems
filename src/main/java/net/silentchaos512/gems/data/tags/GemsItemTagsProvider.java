package net.silentchaos512.gems.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.setup.GemsTags;
import net.silentchaos512.gems.setup.Gems;
import net.silentchaos512.lib.data.tag.LibItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class GemsItemTagsProvider extends LibItemTagsProvider {
    public GemsItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, SilentGems.MOD_ID);
    }

    @Override
    protected DirectTagAppender<Item> tag(TagKey<Item> tag) {
        return new DirectTagAppender<>(super.tag(tag), item -> BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        (new GemsBlockItemTagsProvider() {
            @Override
            protected DirectTagAppender<Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
                return new DirectTagAppender<>(
                        new LibItemTagsProvider.BlockToItemConverter(GemsItemTagsProvider.this.tag(itemTag)),
                        block -> BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow()
                );
            }
        }).run();

        for (Gems gem : Gems.values()) {
            // Items
            tag(gem.getItemTag()).add(gem.getItem());

            // Groups
            tag(GemsTags.Items.GEMS).addTag(gem.getItemTag());
            tag(Tags.Items.GEMS).add(gem.getItem());

            // Others
            tag(ItemTags.BEACON_PAYMENT_ITEMS).add(gem.getItem());
        }

        // Gem bag and flower basket accepted items
        tag(GemsTags.Items.FLOWER_BASKET_CAN_STORE).addTag(Tags.Items.FLOWERS);
        tag(GemsTags.Items.GEM_BAG_CAN_STORE).addTag(Tags.Items.GEMS);

        tag(GemsTags.Items.INGOTS_SILVER).add(GemsItems.SILVER_INGOT.get());
        tag(GemsTags.Items.NUGGETS_SILVER).add(GemsItems.SILVER_NUGGET.get());
        tag(GemsTags.Items.RODS_SILVER).add(GemsItems.SILVER_ROD.get());

        tag(GemsTags.Items.STEW_FISH).add(Items.COD, Items.SALMON);
        tag(GemsTags.Items.STEW_MEAT).add(Items.BEEF, Items.CHICKEN, Items.MUTTON, Items.PORKCHOP, Items.RABBIT);
    }
}
