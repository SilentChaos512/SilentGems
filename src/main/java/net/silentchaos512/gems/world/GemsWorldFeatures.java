package net.silentchaos512.gems.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.config.OreConfig;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.util.Gems;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = GemsBase.MOD_ID)
public final class GemsWorldFeatures {
    public static final RuleTest BASE_STONE_END = new TagMatchTest(Tags.Blocks.END_STONES);

    private static boolean configuredFeaturesRegistered = false;

    private GemsWorldFeatures() {}

    @SubscribeEvent
    public static void biomeLoading(BiomeLoadingEvent biome) {
        registerConfiguredFeatures();

        if (biome.getCategory() == Biome.BiomeCategory.NETHER) {
            addGemOreFeatures(biome, Level.NETHER);
        } else if (biome.getCategory() == Biome.BiomeCategory.THEEND) {
            addGemOreFeatures(biome, Level.END);
        } else {
            addGemOreFeatures(biome, Level.OVERWORLD);
            biome.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                    GemsConfig.Common.silverOres.getPlacedFeature());
        }
    }

    private static void registerConfiguredFeatures() {
        if (configuredFeaturesRegistered) return;
        configuredFeaturesRegistered = true;

        for (Gems gem : Gems.values()) {
            registerConfiguredFeature(gem.getName(), gem.getOreConfiguredFeature(Level.OVERWORLD));
            registerConfiguredFeature(gem.getName() + "_nether", gem.getOreConfiguredFeature(Level.NETHER));
            registerConfiguredFeature(gem.getName() + "_end", gem.getOreConfiguredFeature(Level.END));

            registerConfiguredFeature(gem.getName() + "_glowrose", gem.getGlowroseConfiguredFeature(Level.OVERWORLD));
            registerConfiguredFeature(gem.getName() + "_nether_glowrose", gem.getGlowroseConfiguredFeature(Level.NETHER));
            registerConfiguredFeature(gem.getName() + "_end_glowrose", gem.getGlowroseConfiguredFeature(Level.END));
        }

        registerConfiguredFeature("silver", GemsConfig.Common.silverOres.createConfiguredFeature(
                config -> {
                    ImmutableList<OreConfiguration.TargetBlockState> targetList = ImmutableList.of(
                            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, GemsBlocks.SILVER_ORE.get().defaultBlockState()),
                            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, GemsBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));
                    return Feature.ORE.configured(new OreConfiguration(targetList, config.getSize(), config.getDiscardChanceOnAirExposure()));
                },
                (config, feature) -> {
                    return feature.placed(List.of(
                            CountPlacement.of(config.getCount()),
                            RarityFilter.onAverageOnceEvery(config.getRarity()),
                            HeightRangePlacement.triangle(VerticalAnchor.absolute(config.getMinHeight()), VerticalAnchor.absolute(config.getMaxHeight())),
                            InSquarePlacement.spread(),
                            BiomeFilter.biome()
                    ));
                }
        ));

        logOreConfigs();
    }

    private static void registerConfiguredFeature(String name, ConfiguredFeature<?, ?> configuredFeature) {
        GemsBase.LOGGER.debug("register configured feature '{}'", name);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, GemsBase.getId(name), configuredFeature);
    }

    private static void addGemOreFeatures(BiomeLoadingEvent biome, ResourceKey<Level> level) {
        for (Gems gem : Gems.values()) {
            biome.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, gem.getOrePlacedFeature(level));
        }
        addGlowroseFeatures(biome, level);
    }

    private static void addGlowroseFeatures(BiomeLoadingEvent biome, ResourceKey<Level> level) {
        for (Gems gem : Gems.values()) {
            biome.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, gem.getGlowrosePlacedFeature(level));
        }
    }

    private static final boolean LOG_ORE_CONFIGS = true;

    private static void logOreConfigs() {
        if (LOG_ORE_CONFIGS) {
            GemsBase.LOGGER.info("# Ore Configs");

            Map<Gems, OreConfig> overworldConfigs = new LinkedHashMap<>();
            Map<Gems, OreConfig> netherConfigs = new LinkedHashMap<>();
            Map<Gems, OreConfig> endConfigs = new LinkedHashMap<>();

            for (Gems gem : Gems.values()) {
                OreConfig overworld = gem.getOreConfig(Level.OVERWORLD);
                OreConfig nether = gem.getOreConfig(Level.NETHER);
                OreConfig end = gem.getOreConfig(Level.END);

                if (overworld.isEnabled()) {
                    overworldConfigs.put(gem, overworld);
                }
                if (nether.isEnabled()) {
                    netherConfigs.put(gem, nether);
                }
                if (end.isEnabled()) {
                    endConfigs.put(gem, end);
                }
            }

            logOreConfigsForDim("Overworld", overworldConfigs);
            logOreConfigsForDim("The Nether", netherConfigs);
            logOreConfigsForDim("The End", endConfigs);
        }
    }

    private static void logOreConfigsForDim(String name, Map<Gems, OreConfig> configs) {
        GemsBase.LOGGER.info("## {}", name);

        double totalVeinsPerChunk = configs.values().stream().mapToDouble(c -> (double) c.getCount() / c.getRarity()).sum();
        int totalVeinSize = configs.values().stream().mapToInt(OreConfig::getSize).sum();
        GemsBase.LOGGER.info("Total veins per chunk: {}  ", totalVeinsPerChunk);
        GemsBase.LOGGER.info("Average vein size: {}  ", (double) totalVeinSize / configs.size());

        //noinspection OverlyLongLambda
        configs.forEach((gem, config) -> {
            float avgCount = (float) config.getCount() / config.getRarity();
            String rarity = config.getRarity() > 1
                    ? String.format(" in 1/%d of chunks (avg %.3f per chunk)", config.getRarity(), avgCount)
                    : "";
            GemsBase.LOGGER.info("- {}", gem.getDisplayName().getString());
            GemsBase.LOGGER.info("  - {} veins of size {}{}",
                    config.getCount(),
                    config.getSize(),
                    rarity);
            GemsBase.LOGGER.info("  - at heights {} to {}",
                    config.getMinHeight(),
                    config.getMaxHeight());
        });
    }
}
