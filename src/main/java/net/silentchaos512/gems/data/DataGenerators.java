package net.silentchaos512.gems.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.silentchaos512.gems.data.recipe.GemsRecipeProvider;

public final class DataGenerators {
    private DataGenerators() {}

    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        gen.addProvider(new GemsBlockTagsProvider(gen));
        gen.addProvider(new GemsItemTagsProvider(gen));
        gen.addProvider(new GemsRecipeProvider(gen));
        gen.addProvider(new GemsLootTableProvider(gen));

        gen.addProvider(new GemsMaterialsProvider(gen));
    }
}
