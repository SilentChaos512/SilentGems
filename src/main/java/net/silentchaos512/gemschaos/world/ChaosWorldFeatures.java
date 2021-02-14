package net.silentchaos512.gemschaos.world;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.config.OreConfig;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.config.ChaosConfig;
import net.silentchaos512.gemschaos.setup.ChaosBlocks;

@Mod.EventBusSubscriber(modid = ChaosMod.MOD_ID)
public final class ChaosWorldFeatures {
    private static boolean configuredFeaturesRegistered = false;

    private ChaosWorldFeatures() {}

    @SubscribeEvent
    public static void biomeLoading(BiomeLoadingEvent biome) {
        registerConfiguredFeatures();

        addOreFeature(biome, ChaosConfig.Common.chaosOres);
    }

    private static void registerConfiguredFeatures() {
        if (configuredFeaturesRegistered) return;
        configuredFeaturesRegistered = true;

        registerConfiguredFeature("chaos", ChaosConfig.Common.chaosOres.createConfiguredFeature(
                OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
                ChaosBlocks.CHAOS_ORE.asBlockState()));
    }

    private static void registerConfiguredFeature(String name, ConfiguredFeature<?, ?> configuredFeature) {
        ChaosMod.LOGGER.debug("register configured feature '{}'", name);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, ChaosMod.getId(name), configuredFeature);
    }

    private static void addOreFeature(BiomeLoadingEvent biome, OreConfig config) {
        if (config.isEnabled()) {
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, config.getConfiguredFeature());
        }
    }
}
