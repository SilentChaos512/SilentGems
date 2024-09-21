package net.silentchaos512.gems.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.setup.GemsTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GemsEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public GemsEntityTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, SilentGems.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(GemsTags.EntityTypes.COFFEE_PRODUCERS)
                .add(EntityType.RABBIT);
    }
}
