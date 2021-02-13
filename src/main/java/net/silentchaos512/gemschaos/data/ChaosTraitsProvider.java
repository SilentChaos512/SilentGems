package net.silentchaos512.gemschaos.data;

import net.minecraft.data.DataGenerator;
import net.silentchaos512.gear.data.trait.TraitBuilder;
import net.silentchaos512.gear.data.trait.TraitsProvider;

import java.util.ArrayList;
import java.util.Collection;

public class ChaosTraitsProvider extends TraitsProvider {
    public ChaosTraitsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected Collection<TraitBuilder> getTraits() {
        Collection<TraitBuilder> ret = new ArrayList<>();

        // TODO

        return ret;
    }
}
