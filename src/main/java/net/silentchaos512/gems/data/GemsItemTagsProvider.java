package net.silentchaos512.gems.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.setup.GemsTags;
import net.silentchaos512.gems.util.Gems;

public class GemsItemTagsProvider extends ItemTagsProvider {
    public GemsItemTagsProvider(GatherDataEvent event, BlockTagsProvider blocks) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), blocks.contentsGetter(), SilentGems.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (Gems gem : Gems.values()) {
            // Block copies
            copy(gem.getModOresTag(), gem.getModOresItemTag());
            copy(gem.getOreTag(), gem.getOreItemTag());
            copy(gem.getBlockTag(), gem.getBlockItemTag());
            copy(gem.getGlowroseTag(), gem.getGlowroseItemTag());

            // Items
            tag(gem.getItemTag()).add(gem.getItem());

            // Gem bag and flower basket accepted items
            tag(GemsTags.Items.FLOWER_BASKET_CAN_STORE).addTag(ItemTags.FLOWERS);
            tag(GemsTags.Items.GEM_BAG_CAN_STORE).addTag(Tags.Items.GEMS);

            // Groups
            tag(GemsTags.Items.GEM_ORES).addTag(gem.getModOresItemTag());
            tag(GemsTags.Items.GEMS).addTag(gem.getItemTag());
            tag(GemsTags.Items.GLOWROSES).addTag(gem.getGlowroseItemTag());
            tag(Tags.Items.GEMS).add(gem.getItem());

            // Others
            tag(ItemTags.BEACON_PAYMENT_ITEMS).add(gem.getItem());
        }

        copy(Tags.Blocks.ORES, Tags.Items.ORES);
        copy(GemsTags.Blocks.ORES_CHAOS, GemsTags.Items.ORES_CHAOS);
        tag(GemsTags.Items.ORES_CHAOS).add(GemsBlocks.CHAOS_ORE.asItem(), GemsBlocks.DEEPSLATE_CHAOS_ORE.asItem());
        copy(GemsTags.Blocks.ORES_SILVER, GemsTags.Items.ORES_SILVER);
        tag(GemsTags.Items.ORES_SILVER).add(GemsBlocks.SILVER_ORE.asItem(), GemsBlocks.DEEPSLATE_SILVER_ORE.asItem());

        tag(GemsTags.Items.INGOTS_SILVER).add(GemsItems.SILVER_INGOT.get());
        tag(GemsTags.Items.NUGGETS_SILVER).add(GemsItems.SILVER_NUGGET.get());
        tag(GemsTags.Items.RODS_SILVER).add(GemsItems.SILVER_ROD.get());

        tag(GemsTags.Items.STEW_FISH).add(Items.COD, Items.SALMON);
        tag(GemsTags.Items.STEW_MEAT).add(Items.BEEF, Items.CHICKEN, Items.MUTTON, Items.PORKCHOP, Items.RABBIT);

        copy(BlockTags.FLOWERS, ItemTags.FLOWERS);
    }
}
