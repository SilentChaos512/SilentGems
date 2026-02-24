package net.silentchaos512.gems.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.silentchaos512.gems.setup.Gems;

import java.util.concurrent.CompletableFuture;

public class GemsDataMapProvider extends DataMapProvider {
    protected GemsDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        final var compostables = builder(NeoForgeDataMaps.COMPOSTABLES);
        for (Gems gem : Gems.values()) {
            compostables.add(gem.getGlowroseItemTag(), new Compostable(0.65f), false);
        }
    }
}
