package net.silentchaos512.gems.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class GemsWorldGen {
    public static final RuleTest BASE_STONE_END = new TagMatchTest(Tags.Blocks.END_STONES);

    public static void init(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        /*RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());

        Map<ResourceLocation, ConfiguredFeature<?, ?>> configuredFeatures = Maps.newLinkedHashMap();
        Map<ResourceLocation, PlacedFeature> placedFeatures = Maps.newLinkedHashMap();

        List<Holder<PlacedFeature>> overworldFeatureList = Lists.newArrayList();
        List<Holder<PlacedFeature>> netherFeatureList = Lists.newArrayList();
        List<Holder<PlacedFeature>> endFeatureList = Lists.newArrayList();

        HolderSet<Biome> overworldBiomes = new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_OVERWORLD);
        HolderSet<Biome> netherBiomes = new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_NETHER);
        HolderSet<Biome> endBiomes = new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_END);

        // Silver
        ResourceLocation silverName = GemsBase.getId("silver_ore");
        ImmutableList<OreConfiguration.TargetBlockState> silverTargetBlocks = ImmutableList.of(
                OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, GemsBlocks.SILVER_ORE.get().defaultBlockState()),
                OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, GemsBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));
        ConfiguredFeature<?, ?> silverOreFeature = new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(silverTargetBlocks, 7));
        PlacedFeature silverOrePlaced = new PlacedFeature(holder(silverOreFeature, ops, silverName), List.of(
                CountPlacement.of(2),
                RarityFilter.onAverageOnceEvery(1),
                HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(36)),
                InSquarePlacement.spread(),
                BiomeFilter.biome()
        ));
        configuredFeatures.put(silverName, silverOreFeature);
        placedFeatures.put(silverName, silverOrePlaced);
        overworldFeatureList.add(holderPlaced(silverOrePlaced, ops, silverName));

        // Gems
        for (Gems gem : Gems.values()) {
            // Ores
            ResourceLocation overworldOreName = GemsBase.getId(gem.getName() + "_overworld_ore");
            ResourceLocation netherOreName = GemsBase.getId(gem.getName() + "_nether_ore");
            ResourceLocation endOreName = GemsBase.getId(gem.getName() + "_end_ore");

            ConfiguredFeature<?, ?> oreOverworld = gem.createOreConfiguredFeature(Level.OVERWORLD);
            ConfiguredFeature<?, ?> oreNether = gem.createOreConfiguredFeature(Level.NETHER);
            ConfiguredFeature<?, ?> oreEnd = gem.createOreConfiguredFeature(Level.END);

            PlacedFeature placedOreOverworld = gem.createOrePlacedFeature(Level.OVERWORLD, holder(oreOverworld, ops, overworldOreName));
            PlacedFeature placedOreNether = gem.createOrePlacedFeature(Level.NETHER, holder(oreNether, ops, netherOreName));
            PlacedFeature placedOreEnd = gem.createOrePlacedFeature(Level.END, holder(oreEnd, ops, endOreName));

            configuredFeatures.put(overworldOreName, oreOverworld);
            configuredFeatures.put(netherOreName, oreNether);
            configuredFeatures.put(endOreName, oreEnd);
            placedFeatures.put(overworldOreName, placedOreOverworld);
            placedFeatures.put(netherOreName, placedOreNether);
            placedFeatures.put(endOreName, placedOreEnd);
            overworldFeatureList.add(holderPlaced(placedOreOverworld, ops, overworldOreName));
            netherFeatureList.add(holderPlaced(placedOreNether, ops, netherOreName));
            endFeatureList.add(holderPlaced(placedOreEnd, ops, endOreName));

            // Glowroses
            ResourceLocation overworldGlowroseName = GemsBase.getId(gem.getName() + "_overworld_glowrose");
            ResourceLocation netherGlowroseName = GemsBase.getId(gem.getName() + "_nether_glowrose");
            ResourceLocation endGlowroseName = GemsBase.getId(gem.getName() + "_end_glowrose");

            ConfiguredFeature<?, ?> glowroseOverworld = gem.createGlowroseConfiguredFeature(Level.OVERWORLD);
            ConfiguredFeature<?, ?> glowroseNether = gem.createGlowroseConfiguredFeature(Level.NETHER);
            ConfiguredFeature<?, ?> glowroseEnd = gem.createGlowroseConfiguredFeature(Level.END);

            PlacedFeature placedGlowroseOverworld = gem.createGlowrosePlacedFeature(Level.OVERWORLD, holder(glowroseOverworld, ops, overworldGlowroseName));
            PlacedFeature placedGlowroseNether = gem.createGlowrosePlacedFeature(Level.NETHER, holder(glowroseNether, ops, netherGlowroseName));
            PlacedFeature placedGlowroseEnd = gem.createGlowrosePlacedFeature(Level.END, holder(glowroseEnd, ops, endGlowroseName));

            configuredFeatures.put(overworldGlowroseName, glowroseOverworld);
            configuredFeatures.put(netherGlowroseName, glowroseNether);
            configuredFeatures.put(endGlowroseName, glowroseEnd);
            placedFeatures.put(overworldGlowroseName, placedGlowroseOverworld);
            placedFeatures.put(netherGlowroseName, placedGlowroseNether);
            placedFeatures.put(endGlowroseName, placedGlowroseEnd);
            overworldFeatureList.add(holderPlaced(placedGlowroseOverworld, ops, overworldGlowroseName));
            netherFeatureList.add(holderPlaced(placedGlowroseNether, ops, netherGlowroseName));
            endFeatureList.add(holderPlaced(placedGlowroseEnd, ops, endGlowroseName));
        }

        // Biome modifiers
        BiomeModifier overworldOres = new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                overworldBiomes,
                HolderSet.direct(overworldFeatureList),
                GenerationStep.Decoration.UNDERGROUND_ORES
        );
        BiomeModifier netherOres = new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                netherBiomes,
                HolderSet.direct(netherFeatureList),
                GenerationStep.Decoration.UNDERGROUND_ORES
        );
        BiomeModifier endOres = new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                endBiomes,
                HolderSet.direct(endFeatureList),
                GenerationStep.Decoration.UNDERGROUND_ORES
        );

        DataProvider configuredFeatureProvider = JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, GemsBase.MOD_ID, ops, Registry.CONFIGURED_FEATURE_REGISTRY,
                configuredFeatures);
        DataProvider placedFeatureProvider = JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, GemsBase.MOD_ID, ops, Registry.PLACED_FEATURE_REGISTRY,
                placedFeatures);
        DataProvider biomeModifierProvider = JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, GemsBase.MOD_ID, ops, ForgeRegistries.Keys.BIOME_MODIFIERS,
                ImmutableMap.of(
                        GemsBase.getId("overworld_ores"), overworldOres,
                        GemsBase.getId("nether_ores"), netherOres,
                        GemsBase.getId("end_ores"), endOres
                )
        );

        generator.addProvider(true, configuredFeatureProvider);
        generator.addProvider(true, placedFeatureProvider);
        generator.addProvider(true, biomeModifierProvider);*/
    }

    /*public static Holder<ConfiguredFeature<?, ?>> holder(ConfiguredFeature<?, ?> feature, RegistryOps<JsonElement> ops, ResourceLocation location) {
        return ops.registryAccess.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).getOrCreateHolderOrThrow(ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, location));
    }

    public static Holder<PlacedFeature> holderPlaced(PlacedFeature feature, RegistryOps<JsonElement> ops, ResourceLocation location) {
        return ops.registryAccess.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY).getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, location));
    }*/
}
