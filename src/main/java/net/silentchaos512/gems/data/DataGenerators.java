package net.silentchaos512.gems.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.silentchaos512.gems.data.client.GemsBlockStateProvider;
import net.silentchaos512.gems.data.client.GemsItemModelProvider;
import net.silentchaos512.gems.data.recipe.GemsRecipeProvider;

public final class DataGenerators {
    private DataGenerators() {}

    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        GemsBlockTagsProvider blockTags = new GemsBlockTagsProvider(gen);
        gen.addProvider(blockTags);
        gen.addProvider(new GemsItemTagsProvider(gen, blockTags));
        gen.addProvider(new GemsRecipeProvider(gen));
        gen.addProvider(new GemsLootTableProvider(gen));

        gen.addProvider(new GemsMaterialsProvider(gen));

        gen.addProvider(new GemsBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(new GemsItemModelProvider(gen, existingFileHelper));
    }
}
