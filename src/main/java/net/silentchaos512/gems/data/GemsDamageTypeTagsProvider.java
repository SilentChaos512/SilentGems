package net.silentchaos512.gems.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.setup.GemsTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GemsDamageTypeTagsProvider extends TagsProvider<DamageType> {
    protected GemsDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, SilentGems.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GemsTags.DamageTypes.NEPTUNES_BLESSING_PROTECTS)
                .add(DamageTypes.TRIDENT);
    }
}
