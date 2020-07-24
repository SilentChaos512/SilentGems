package net.silentchaos512.gems.data;

import net.minecraft.data.DataGenerator;
import net.silentchaos512.gear.data.trait.TraitBuilder;
import net.silentchaos512.gear.data.trait.TraitsProvider;
import net.silentchaos512.gems.init.GemsTraits;

import java.util.ArrayList;
import java.util.Collection;

public class GemsTraitsProvider extends TraitsProvider {
    public GemsTraitsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected Collection<TraitBuilder> getTraits() {
        Collection<TraitBuilder> ret = new ArrayList<>();

        ret.add(TraitBuilder.simple(GemsTraits.AERIAL, 5));

        return ret;
    }
}
