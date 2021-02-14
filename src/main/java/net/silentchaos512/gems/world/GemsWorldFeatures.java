package net.silentchaos512.gems.world;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
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
import java.util.Map;

@Mod.EventBusSubscriber(modid = GemsBase.MOD_ID)
public final class GemsWorldFeatures {
    public static final RuleTest BASE_STONE_END = new TagMatchRuleTest(Tags.Blocks.END_STONES);

    private static boolean configuredFeaturesRegistered = false;

    private GemsWorldFeatures() {}

    @SubscribeEvent
    public static void biomeLoading(BiomeLoadingEvent biome) {
        registerConfiguredFeatures();

        if (biome.getCategory() == Biome.Category.NETHER) {
            addGemOreFeatures(biome, World.THE_NETHER);
        } else if (biome.getCategory() == Biome.Category.THEEND) {
            addGemOreFeatures(biome, World.THE_END);
        } else {
            addGemOreFeatures(biome, World.OVERWORLD);
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                    GemsConfig.Common.silverOres.getConfiguredFeature());
        }
    }

    private static void registerConfiguredFeatures() {
        if (configuredFeaturesRegistered) return;
        configuredFeaturesRegistered = true;

        for (Gems gem : Gems.values()) {
            registerConfiguredFeature(gem.getName(), gem.getOreConfiguredFeature(World.OVERWORLD));
            registerConfiguredFeature(gem.getName() + "_nether", gem.getOreConfiguredFeature(World.THE_NETHER));
            registerConfiguredFeature(gem.getName() + "_end", gem.getOreConfiguredFeature(World.THE_END));
        }

        registerConfiguredFeature("silver", GemsConfig.Common.silverOres.createConfiguredFeature(
                OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
                GemsBlocks.SILVER_ORE.asBlockState()));

        logOreConfigs();
    }

    private static void registerConfiguredFeature(String name, ConfiguredFeature<?, ?> configuredFeature) {
        GemsBase.LOGGER.debug("register configured feature '{}'", name);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, GemsBase.getId(name), configuredFeature);
    }

    private static void addGemOreFeatures(BiomeLoadingEvent biome, RegistryKey<World> world) {
        for (Gems gem : Gems.values()) {
            ConfiguredFeature<?, ?> feature = gem.getOreConfiguredFeature(world);
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
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
                OreConfig overworld = gem.getOreConfig(World.OVERWORLD);
                OreConfig nether = gem.getOreConfig(World.THE_NETHER);
                OreConfig end = gem.getOreConfig(World.THE_END);

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
