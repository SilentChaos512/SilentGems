package net.silentchaos512.gems.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.world.damagesource.DamageTypes;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.setup.GemsTags;

import java.util.concurrent.CompletableFuture;

public class GemsDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public GemsDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, SilentGems.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GemsTags.DamageTypes.NEPTUNES_BLESSING_PROTECTS)
                .add(DamageTypes.TRIDENT);
    }
}
