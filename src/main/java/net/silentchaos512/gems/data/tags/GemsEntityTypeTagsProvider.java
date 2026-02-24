package net.silentchaos512.gems.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.setup.GemsTags;

import java.util.concurrent.CompletableFuture;

public class GemsEntityTypeTagsProvider extends IntrinsicHolderTagsProvider<EntityType<?>> {
    public GemsEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        //noinspection deprecation
        super(output, Registries.ENTITY_TYPE, lookupProvider, et -> et.builtInRegistryHolder().key(), SilentGems.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GemsTags.EntityTypes.COFFEE_PRODUCERS)
                .add(EntityType.RABBIT);
        tag(GemsTags.EntityTypes.END_MONSTERS)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.ENDERMAN)
                .add(EntityType.ENDERMITE)
                .add(EntityType.SHULKER);
    }
}
