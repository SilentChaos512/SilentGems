package net.silentchaos512.gems.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.data.client.GemsBlockStateProvider;
import net.silentchaos512.gems.data.client.GemsItemModelProvider;
import net.silentchaos512.gems.data.recipe.GemsRecipeProvider;

@Mod.EventBusSubscriber(modid = GemsBase.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators {
    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        GemsBlockTagsProvider blockTags = new GemsBlockTagsProvider(event);
        gen.addProvider(true, blockTags);
        gen.addProvider(true, new GemsItemTagsProvider(event, blockTags));
        gen.addProvider(true, new GemsRecipeProvider(gen));
        gen.addProvider(true, new GemsLootTableProvider(gen));
//        gen.addProvider(new GemsAdvancementProvider(gen));

        boolean gearIsLoaded = ModList.get().isLoaded("silentgear");
        if (gearIsLoaded) {
            gen.addProvider(true, new GemsTraitsProvider(gen));
            gen.addProvider(true, new GemsMaterialsProvider(gen));
        }

        gen.addProvider(true, new GemsBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(true, new GemsItemModelProvider(gen, existingFileHelper));

        gen.addProvider(true, new WorldGenGenerator(event));
    }
}
