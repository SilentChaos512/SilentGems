package net.silentchaos512.gems.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityTypeIds;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.setup.GemsTags;

import java.util.concurrent.CompletableFuture;

public class GemsEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public GemsEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, SilentGems.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GemsTags.EntityTypes.COFFEE_PRODUCERS)
                .add(EntityTypeIds.RABBIT);
        tag(GemsTags.EntityTypes.END_MONSTERS)
                .add(EntityTypeIds.ENDER_DRAGON)
                .add(EntityTypeIds.ENDERMAN)
                .add(EntityTypeIds.ENDERMITE)
                .add(EntityTypeIds.SHULKER);
    }
}
