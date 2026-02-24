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
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.data.client.GemsBlockModelGenerator;
import net.silentchaos512.gems.data.client.GemsItemModelGenerator;
import net.silentchaos512.gems.data.recipe.GemsRecipeProvider;
import net.silentchaos512.gems.data.tags.GemsBlockTagsProvider;
import net.silentchaos512.gems.data.tags.GemsDamageTypeTagsProvider;
import net.silentchaos512.gems.data.tags.GemsEntityTypeTagsProvider;
import net.silentchaos512.gems.data.tags.GemsItemTagsProvider;
import net.silentchaos512.lib.data.client.LibModelProvider;
import net.silentchaos512.lib.data.recipe.LibRecipeProvider;

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
        var lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new GemsDataMapProvider(packOutput, lookupProvider));

        GemsBlockTagsProvider blockTags = new GemsBlockTagsProvider(packOutput, lookupProvider);
        generator.addProvider(true, blockTags);
        generator.addProvider(true, new GemsItemTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new GemsEntityTypeTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new GemsDamageTypeTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, LibRecipeProvider.createRunner(packOutput, lookupProvider, "Silent's Gems Recipes", GemsRecipeProvider::new));
        generator.addProvider(true, new GemsLootTableProvider(packOutput, lookupProvider));

        generator.addProvider(true, new WorldGenGenerator(event));

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
}
