package net.silentchaos512.gems.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.GemOreBlock;
import net.silentchaos512.gems.data.client.GemsBlockStateProvider;
import net.silentchaos512.gems.data.client.GemsItemModelProvider;
import net.silentchaos512.gems.data.recipe.GemsRecipeProvider;
import net.silentchaos512.gems.setup.GemsBlocks;

import java.util.stream.Collectors;

@EventBusSubscriber(modid = SilentGems.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class DataGenerators {
    private DataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();
        var packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        GemsBlockTagsProvider blockTags = new GemsBlockTagsProvider(event);
        generator.addProvider(true, blockTags);
        generator.addProvider(true, new GemsItemTagsProvider(event, blockTags));
        generator.addProvider(true, new GemsEntityTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(true, new GemsRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(true, new GemsLootTableProvider(packOutput, lookupProvider));
//        generator.addProvider(new GemsAdvancementProvider(generator));

        boolean gearIsLoaded = ModList.get().isLoaded("silentgear");
        if (gearIsLoaded) {
            generator.addProvider(true, new GemsTraitsProvider(generator));
            generator.addProvider(true, new GemsMaterialsProvider(generator));
        }

        generator.addProvider(true, new GemsBlockStateProvider(generator, existingFileHelper));
        generator.addProvider(true, new GemsItemModelProvider(generator, existingFileHelper));

        generator.addProvider(true, new WorldGenGenerator(event));

        SilentGems.LOGGER.info(
                GemsBlocks.BLOCKS.getEntries().stream()
                        .map(DeferredHolder::get)
                        .filter(block -> block instanceof GemOreBlock)
                        .map(BuiltInRegistries.BLOCK::getKey)
                        .map(ResourceLocation::toString)
                        .collect(Collectors.joining(" "))
        );
    }
}
