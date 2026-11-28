package net.silentchaos512.gems.data;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.data.client.GemsBlockModelGenerator;
import net.silentchaos512.gems.data.client.GemsItemModelGenerator;
import net.silentchaos512.gems.data.recipe.GemsRecipeProvider;
import net.silentchaos512.gems.data.tags.GemsBlockTagsProvider;
import net.silentchaos512.gems.data.tags.GemsDamageTypeTagsProvider;
import net.silentchaos512.gems.data.tags.GemsEntityTypeTagsProvider;
import net.silentchaos512.gems.data.tags.GemsItemTagsProvider;
import net.silentchaos512.lib.data.client.LibModelProvider;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@EventBusSubscriber()
public final class DataGenerators {
    private DataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        // Tags and other data providers use the world registries, like vanilla
        var lookupProvider = event.getWorldLookupProvider();

        GemsBlockTagsProvider blockTags = new GemsBlockTagsProvider(packOutput, lookupProvider);
        generator.addProvider(true, blockTags);
        generator.addProvider(true, new GemsItemTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new GemsEntityTypeTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new GemsDamageTypeTagsProvider(packOutput, lookupProvider));
        // Recipes, loot tables, and worldgen are datapack registries, see gatherRegistryEntries

        boolean gearIsLoaded = ModList.get().isLoaded("silentgear");
        if (gearIsLoaded) {
            generator.addProvider(true, new GemsTraitsProvider(lookupProvider, generator));
            generator.addProvider(true, new GemsMaterialsProvider(lookupProvider, generator));
        }

        generator.addProvider(true, new LibModelProvider(packOutput, SilentGems.MOD_ID) {
            @Override
            protected BlockModelGenerators createBlockModelGenerators(Consumer<BlockModelDefinitionGenerator> blockStateOutput, ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
                return new GemsBlockModelGenerator(blockStateOutput, itemModelOutput, modelOutput);
            }

            @Override
            protected ItemModelGenerators createItemModelGenerators(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
                return new GemsItemModelGenerator(itemModelOutput, modelOutput);
            }
        });
    }

    /**
     * Recipes and loot tables became reloadable datapack registries in 26.3, so they are generated from registry
     * bootstraps instead of standalone data providers, along with the worldgen registries.
     */
    @SubscribeEvent
    public static void gatherRegistryEntries(GatherDataRegistryEntriesEvent event) {
        event.recipe(GemsRecipeProvider::new);
        GemsLootTableProvider.gather(event);
        WorldGenGenerator.gather(event);
    }
}
