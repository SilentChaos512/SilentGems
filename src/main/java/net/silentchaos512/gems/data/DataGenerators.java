package net.silentchaos512.gems.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.data.client.GemsBlockStateProvider;
import net.silentchaos512.gems.data.client.GemsItemModelProvider;

@Mod.EventBusSubscriber(modid = GemsBase.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators {
    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        GemsBlockTagsProvider blockTags = new GemsBlockTagsProvider(gen, existingFileHelper);
        gen.addProvider(blockTags);
        gen.addProvider(new GemsItemTagsProvider(gen, blockTags, existingFileHelper));
//        gen.addProvider(new GemsRecipeProvider(gen));
        gen.addProvider(new GemsLootTableProvider(gen));
//        gen.addProvider(new GemsAdvancementProvider(gen));

        gen.addProvider(new GemsMaterialsProvider(gen));

        gen.addProvider(new GemsBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(new GemsItemModelProvider(gen, existingFileHelper));
    }
}
