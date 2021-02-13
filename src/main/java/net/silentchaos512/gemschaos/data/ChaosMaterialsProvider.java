package net.silentchaos512.gemschaos.data;

import net.minecraft.data.DataGenerator;
import net.silentchaos512.gear.data.material.MaterialBuilder;
import net.silentchaos512.gear.data.material.MaterialsProvider;
import net.silentchaos512.gemschaos.ChaosMod;

import java.util.ArrayList;
import java.util.Collection;

public class ChaosMaterialsProvider extends MaterialsProvider {
    public ChaosMaterialsProvider(DataGenerator generator) {
        super(generator, ChaosMod.MOD_ID);
    }

    @Override
    protected Collection<MaterialBuilder> getMaterials() {
        Collection<MaterialBuilder> ret = new ArrayList<>();

        // TODO

        return ret;
    }
}
