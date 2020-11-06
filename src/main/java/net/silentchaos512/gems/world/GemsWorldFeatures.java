package net.silentchaos512.gems.world;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.MiscOres;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.init.GemsEntities;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.world.feature.*;
import net.silentchaos512.lib.world.placement.DimensionFilterConfig;
import net.silentchaos512.lib.world.placement.LibPlacements;
import net.silentchaos512.utils.Lazy;

@Mod.EventBusSubscriber(modid = SilentGems.MOD_ID)
public final class GemsWorldFeatures {
    public static final Feature<RegionalGlowrosesFeatureConfig> REGIONAL_GLOWROSES = new RegionalGlowrosesFeature(RegionalGlowrosesFeatureConfig.CODEC);

    //region ConfiguredFeatures

    // Regional Gems
    private static final Lazy<RegionalGemsFeatureConfig> OVERWORLD_REGIONAL_GEMS_CONFIG = Lazy.of(() -> new RegionalGemsFeatureConfig(Gems.Set.CLASSIC,
            8,
            GemsConfig.Common.regionSizeOverworld.get()));
    private static final Lazy<RegionalGemsFeatureConfig> NETHER_REGIONAL_GEMS_CONFIG = Lazy.of(() -> new RegionalGemsFeatureConfig(Gems.Set.DARK,
            8,
            GemsConfig.Common.regionSizeNether.get()));
    private static final Lazy<RegionalGemsFeatureConfig> END_REGIONAL_GEMS_CONFIG = Lazy.of(() -> new RegionalGemsFeatureConfig(Gems.Set.LIGHT,
            8,
            GemsConfig.Common.regionSizeEnd.get()));
    private static final Lazy<RegionalGemsFeatureConfig> MOD_DIM_REGIONAL_GEMS_CONFIG = Lazy.of(() -> new RegionalGemsFeatureConfig(Gems.Set.CLASSIC,
            8,
            GemsConfig.Common.regionSizeOthers.get()));
    private static final Lazy<ConfiguredFeature<?, ?>> OVERWORLD_GEMS = Lazy.of(() -> RegionalGemsFeature.INSTANCE
            .withConfiguration(OVERWORLD_REGIONAL_GEMS_CONFIG.get())
            .withPlacement(Placement.field_242907_l.configure(new TopSolidRangeConfig(8, 0, 48)))
            .withPlacement(LibPlacements.DIMENSION_FILTER.configure(DimensionFilterConfig.whitelist(World.OVERWORLD)))
            .func_242728_a()
            .func_242731_b(10));
    private static final Lazy<ConfiguredFeature<?, ?>> NETHER_GEMS = Lazy.of(() -> RegionalGemsFeature.INSTANCE
            .withConfiguration(NETHER_REGIONAL_GEMS_CONFIG.get())
            .withPlacement(Placement.field_242907_l.configure(new TopSolidRangeConfig(25, 0, 95)))
            .withPlacement(LibPlacements.DIMENSION_FILTER.configure(DimensionFilterConfig.whitelist(World.THE_NETHER)))
            .func_242728_a()
            .func_242731_b(10));
    private static final Lazy<ConfiguredFeature<?, ?>> END_GEMS = Lazy.of(() -> RegionalGemsFeature.INSTANCE
            .withConfiguration(END_REGIONAL_GEMS_CONFIG.get())
            .withPlacement(Placement.field_242907_l.configure(new TopSolidRangeConfig(16, 0, 72)))
            .withPlacement(LibPlacements.DIMENSION_FILTER.configure(DimensionFilterConfig.whitelist(World.THE_END)))
            .func_242728_a()
            .func_242731_b(10));
    private static final Lazy<ConfiguredFeature<?, ?>> MOD_DIM_GEMS = Lazy.of(() -> RegionalGemsFeature.INSTANCE
            .withConfiguration(MOD_DIM_REGIONAL_GEMS_CONFIG.get())
            .withPlacement(Placement.field_242907_l.configure(new TopSolidRangeConfig(8, 0, 48)))
            .withPlacement(LibPlacements.DIMENSION_FILTER.configure(DimensionFilterConfig.blacklist(World.OVERWORLD, World.THE_NETHER, World.THE_END)))
            .func_242728_a()
            .func_242731_b(10));

    // Regional glowroses
    private static final Lazy<ConfiguredFeature<?, ?>> OVERWORLD_GLOWROSES = Lazy.of(() -> REGIONAL_GLOWROSES
            .withConfiguration(new RegionalGlowrosesFeatureConfig(Gems.Set.CLASSIC, GemsConfig.Common.regionSizeOverworld.get()))
            .withPlacement(Placement.field_242897_C.configure(new FeatureSpreadConfig(2)))
            .withPlacement(LibPlacements.DIMENSION_FILTER.configure(DimensionFilterConfig.whitelist(World.OVERWORLD)))
            .func_242733_d(128)
            .func_242729_a(16));
    private static final Lazy<ConfiguredFeature<?, ?>> NETHER_GLOWROSES = Lazy.of(() -> REGIONAL_GLOWROSES
            .withConfiguration(new RegionalGlowrosesFeatureConfig(Gems.Set.DARK, GemsConfig.Common.regionSizeNether.get(), 48))
            .withPlacement(Features.Placements.VEGETATION_PLACEMENT)
            .withPlacement(Placement.field_242897_C.configure(new FeatureSpreadConfig(2)))
            .withPlacement(LibPlacements.DIMENSION_FILTER.configure(DimensionFilterConfig.whitelist(World.THE_NETHER)))
            .func_242733_d(128)
            .func_242729_a(4));
    private static final Lazy<ConfiguredFeature<?, ?>> END_GLOWROSES = Lazy.of(() -> REGIONAL_GLOWROSES
            .withConfiguration(new RegionalGlowrosesFeatureConfig(Gems.Set.LIGHT, GemsConfig.Common.regionSizeEnd.get()))
            .withPlacement(Placement.field_242897_C.configure(new FeatureSpreadConfig(2)))
            .withPlacement(LibPlacements.DIMENSION_FILTER.configure(DimensionFilterConfig.whitelist(World.THE_END)))
            .func_242733_d(128)
            .func_242729_a(8));
    private static final Lazy<ConfiguredFeature<?, ?>> MOD_DIM_GLOWROSES = Lazy.of(() -> REGIONAL_GLOWROSES
            .withConfiguration(new RegionalGlowrosesFeatureConfig(Gems.Set.CLASSIC, GemsConfig.Common.regionSizeOthers.get(), 24))
            .withPlacement(Placement.field_242897_C.configure(new FeatureSpreadConfig(2)))
            .withPlacement(LibPlacements.DIMENSION_FILTER.configure(DimensionFilterConfig.blacklist(World.OVERWORLD, World.THE_NETHER, World.THE_END)))
            .func_242733_d(128)
            .func_242729_a(8));

    // Other Ores
    private static final Lazy<ConfiguredFeature<?, ?>> CHAOS_ORE = Lazy.of(() -> createOre(MiscOres.CHAOS.asBlock(),
            9,
            GemsConfig.Common.worldGenChaosOreVeinCount.get(),
            0,
            20,
            Tags.Blocks.STONE));
    private static final Lazy<ConfiguredFeature<?, ?>> SILVER_ORE = Lazy.of(() -> createOre(MiscOres.SILVER.asBlock(),
            6,
            GemsConfig.Common.worldGenSilverVeinCount.get(),
            6,
            28,
            Tags.Blocks.STONE));
    private static final Lazy<ConfiguredFeature<?, ?>> ENDER_ORE = Lazy.of(() -> createOre(MiscOres.ENDER.asBlock(),
            16,
            GemsConfig.Common.worldGenEnderOreCount.get(),
            10,
            70,
            Tags.Blocks.END_STONES));

    // Geodes
    private static final Lazy<ConfiguredFeature<?, ?>> OVERWORLD_CLASSIC_GEODE = Lazy.of(() -> createGeodeFeature(Gems.Set.CLASSIC,
            1f,
            BlockTags.BASE_STONE_OVERWORLD,
            DimensionFilterConfig.whitelist(World.OVERWORLD)));
    private static final Lazy<ConfiguredFeature<?, ?>> OVERWORLD_DARK_GEODE = Lazy.of(() -> createGeodeFeature(Gems.Set.DARK,
            2f,
            BlockTags.BASE_STONE_OVERWORLD,
            DimensionFilterConfig.whitelist(World.OVERWORLD)));
    private static final Lazy<ConfiguredFeature<?, ?>> OVERWORLD_LIGHT_GEODE = Lazy.of(() -> createGeodeFeature(Gems.Set.LIGHT,
            3f,
            BlockTags.BASE_STONE_OVERWORLD,
            DimensionFilterConfig.whitelist(World.OVERWORLD)));
    private static final Lazy<ConfiguredFeature<?, ?>> NETHER_DARK_GEODE = Lazy.of(() -> createGeodeFeature(Gems.Set.DARK,
            1f,
            BlockTags.BASE_STONE_NETHER,
            DimensionFilterConfig.whitelist(World.THE_NETHER)));
    private static final Lazy<ConfiguredFeature<?, ?>> END_LIGHT_GEODE = Lazy.of(() -> createGeodeFeature(Gems.Set.LIGHT,
            1f,
            Tags.Blocks.END_STONES,
            DimensionFilterConfig.whitelist(World.THE_END)));
    private static final Lazy<ConfiguredFeature<?, ?>> MOD_DIM_CLASSIC_GEODE = Lazy.of(() -> createGeodeFeature(Gems.Set.CLASSIC,
            1f,
            BlockTags.BASE_STONE_OVERWORLD,
            DimensionFilterConfig.blacklist(World.OVERWORLD, World.THE_NETHER, World.THE_END)));
    private static final Lazy<ConfiguredFeature<?, ?>> MOD_DIM_DARK_GEODE = Lazy.of(() -> createGeodeFeature(Gems.Set.DARK,
            2f,
            BlockTags.BASE_STONE_OVERWORLD,
            DimensionFilterConfig.blacklist(World.OVERWORLD, World.THE_NETHER, World.THE_END)));
    private static final Lazy<ConfiguredFeature<?, ?>> MOD_DIM_LIGHT_GEODE = Lazy.of(() -> createGeodeFeature(Gems.Set.LIGHT,
            3f,
            BlockTags.BASE_STONE_OVERWORLD,
            DimensionFilterConfig.blacklist(World.OVERWORLD, World.THE_NETHER, World.THE_END)));

    // Other plants
    private static final Lazy<ConfiguredFeature<?, ?>> WILD_FLUFFY_PUFFS = Lazy.of(() -> Feature.FLOWER
            .withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(GemsBlocks.WILD_FLUFFY_PUFF_PLANT.get().getDefaultState()), new SimpleBlockPlacer())
                    .tries(12)
                    .build()
            ))
            .withPlacement(Placement.field_242897_C.configure(new FeatureSpreadConfig(1)))
            .func_242729_a(4));

    //endregion

    private GemsWorldFeatures() {}

    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        registerFeature(event, GemGeodeFeature.INSTANCE, "geode");
        registerFeature(event, RegionalGemsFeature.INSTANCE, "regional_gems");
        registerFeature(event, REGIONAL_GLOWROSES, "regional_glowroses");
        registerFeature(event, SGOreFeature.INSTANCE, "ore");
    }

    public static void registerPlacements(RegistryEvent.Register<Placement<?>> event) {
    }

    private static void registerFeature(RegistryEvent.Register<Feature<?>> event, Feature<?> feature, String name) {
        event.getRegistry().register(feature.setRegistryName(SilentGems.getId(name)));
    }

    private static void registerPlacement(RegistryEvent.Register<Placement<?>> event, Placement<?> placement, String name) {
        event.getRegistry().register(placement.setRegistryName(SilentGems.getId(name)));
    }

    private static boolean configuredFeaturesRegistered = false;

    private static void registerConfiguredFeatures() {
        if (configuredFeaturesRegistered) return;

        configuredFeaturesRegistered = true;

        // Regional Gems
        registerConfiguredFeature("overworld_gems", OVERWORLD_GEMS.get());
        registerConfiguredFeature("nether_gems", NETHER_GEMS.get());
        registerConfiguredFeature("end_gems", END_GEMS.get());
        registerConfiguredFeature("mod_dim_gems", MOD_DIM_GEMS.get());
        // Regional Glowroses
        registerConfiguredFeature("overworld_glowroses", OVERWORLD_GLOWROSES.get());
        registerConfiguredFeature("nether_glowroses", NETHER_GLOWROSES.get());
        registerConfiguredFeature("end_glowroses", END_GLOWROSES.get());
        registerConfiguredFeature("mod_dim_glowroses", MOD_DIM_GLOWROSES.get());
        // Other Ores
        registerConfiguredFeature("chaos_ore", CHAOS_ORE.get());
        registerConfiguredFeature("silver_ore", SILVER_ORE.get());
        registerConfiguredFeature("ender_ore", ENDER_ORE.get());
        //Geodes
        registerConfiguredFeature("overworld_classic_geode", OVERWORLD_CLASSIC_GEODE.get());
        registerConfiguredFeature("overworld_dark_geode", OVERWORLD_DARK_GEODE.get());
        registerConfiguredFeature("overworld_light_geode", OVERWORLD_LIGHT_GEODE.get());
        registerConfiguredFeature("nether_dark_geode", NETHER_DARK_GEODE.get());
        registerConfiguredFeature("end_light_geode", END_LIGHT_GEODE.get());
        registerConfiguredFeature("mod_dim_classic_geode", MOD_DIM_CLASSIC_GEODE.get());
        registerConfiguredFeature("mod_dim_dark_geode", MOD_DIM_DARK_GEODE.get());
        registerConfiguredFeature("mod_dim_light_geode", MOD_DIM_LIGHT_GEODE.get());
    }

    private static void registerConfiguredFeature(String name, ConfiguredFeature<?, ?> configuredFeature) {
        ResourceLocation id = SilentGems.getId(name);
        SilentGems.LOGGER.debug("Register configured feature '{}'", id);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, id, configuredFeature);
    }

    @SubscribeEvent
    public static void addFeaturesToBiomes(BiomeLoadingEvent biome) {
        // Need to load these as late as possible, or configs won't be loaded
        registerConfiguredFeatures();

        if (biome.getCategory() == Biome.Category.NETHER) {
            // Nether
            addNetherFeatures(biome);
        } else if (biome.getCategory() == Biome.Category.THEEND) {
            // The End
            addTheEndFeatures(biome);
        } else {
            // Ores, gems
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, OVERWORLD_GEMS.get());
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MOD_DIM_GEMS.get());
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CHAOS_ORE.get());
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, SILVER_ORE.get());

            // Glowroses
            biome.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, OVERWORLD_GLOWROSES.get());
            biome.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MOD_DIM_GLOWROSES.get());

            // Geodes
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, OVERWORLD_CLASSIC_GEODE.get());
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, OVERWORLD_DARK_GEODE.get());
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, OVERWORLD_LIGHT_GEODE.get());
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MOD_DIM_CLASSIC_GEODE.get());
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MOD_DIM_DARK_GEODE.get());
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MOD_DIM_LIGHT_GEODE.get());

            if (biome.getClimate().downfall > 0.4f && GemsConfig.Common.worldGenWildFluffyPuffs.get()) {
                biome.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, WILD_FLUFFY_PUFFS.get());
            }
        }
    }

    private static void addNetherFeatures(BiomeLoadingEvent biome) {
        biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, NETHER_GEMS.get());
        biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, NETHER_DARK_GEODE.get());

        biome.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, NETHER_GLOWROSES.get());

        biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, NETHER_DARK_GEODE.get());
    }

    private static void addTheEndFeatures(BiomeLoadingEvent biome) {
        biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, END_GEMS.get());
        biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, END_LIGHT_GEODE.get());
        biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ENDER_ORE.get());

        biome.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, END_GLOWROSES.get());

        biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, END_LIGHT_GEODE.get());

        addEnderSlimeSpawns(biome);
    }

    public static ITag<Block> getOreGenTargetBlock(Gems.Set gemSet) {
        if (gemSet == Gems.Set.DARK)
            return Tags.Blocks.NETHERRACK;
        if (gemSet == Gems.Set.LIGHT)
            return Tags.Blocks.END_STONES;
        return Tags.Blocks.STONE;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    private static ConfiguredFeature<?, ?> createOre(Block block, int size, int count, int minHeight, int maxHeight, ITag<Block> blockToReplace) {
        return Feature.ORE
                .withConfiguration(new OreFeatureConfig(new TagMatchRuleTest(blockToReplace), block.getDefaultState(), size))
                .withPlacement(Placement.field_242907_l.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
                .func_242728_a()
                .func_242731_b(count);
    }

    private static ConfiguredFeature<?, ?> createGeodeFeature(Gems.Set gemSet, float baseChance, ITag.INamedTag<Block> target, DimensionFilterConfig dimensionFilter) {
        return GemGeodeFeature.INSTANCE
                .withConfiguration(new GemGeodeFeatureConfig(gemSet, gemSet.getGeodeShell().asBlockState(), target))
                .withPlacement(Placement.field_242908_m.configure(new TopSolidRangeConfig(20, 0, 40)))
                .withPlacement(Placement.field_242898_b.configure(new ChanceConfig((int) (baseChance / GemsConfig.Common.worldGenGeodeBaseChance.get()))))
                .withPlacement(LibPlacements.DIMENSION_FILTER.configure(dimensionFilter));
    }

    private static void addEnderSlimeSpawns(BiomeLoadingEvent biome) {
        int spawnWeight = GemsConfig.Common.enderSlimeSpawnWeight.get();
        if (spawnWeight > 0) {
            biome.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(
                    GemsEntities.ENDER_SLIME.get(),
                    spawnWeight,
                    GemsConfig.Common.enderSlimeGroupSizeMin.get(),
                    GemsConfig.Common.enderSlimeGroupSizeMax.get()
            ));
        }
    }
}
