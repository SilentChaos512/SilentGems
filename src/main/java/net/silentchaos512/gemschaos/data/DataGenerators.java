package net.silentchaos512.gemschaos.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.data.client.ChaosBlockStateProvider;
import net.silentchaos512.gemschaos.data.client.ChaosItemModelProvider;
import net.silentchaos512.gemschaos.data.recipe.ChaosRecipeProvider;

@Mod.EventBusSubscriber(modid = ChaosMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators {
    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        ChaosBlockTagsProvider blockTags = new ChaosBlockTagsProvider(gen, existingFileHelper);
        gen.addProvider(blockTags);
        gen.addProvider(new ChaosItemTagsProvider(gen, blockTags, existingFileHelper));
        gen.addProvider(new ChaosRecipeProvider(gen));
        gen.addProvider(new ChaosLootTableProvider(gen));
//        gen.addProvider(new GemsAdvancementProvider(gen));

        gen.addProvider(new ChaosTraitsProvider(gen));
        gen.addProvider(new ChaosMaterialsProvider(gen));

        gen.addProvider(new ChaosBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(new ChaosItemModelProvider(gen, existingFileHelper));
    }
}
