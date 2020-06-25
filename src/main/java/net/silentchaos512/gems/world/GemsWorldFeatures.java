package net.silentchaos512.gems.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.ChanceRangeConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.MiscOres;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.init.GemsEntities;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.world.feature.*;
import net.silentchaos512.utils.MathUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Experimental world generation. Not sure if Forge intends to add something, but this should work
 * for now.
 */
public final class GemsWorldFeatures {
    private static final EnumMap<Gems, Set<ResourceLocation>> GEM_BIOMES = new EnumMap<>(Gems.class);

    private GemsWorldFeatures() {}

    public static void addFeaturesToBiomes() {
        EnumSet<Gems> selected = EnumSet.noneOf(Gems.class);

        for (Biome biome : ForgeRegistries.BIOMES) {
            long seed = getBiomeSeed(biome);
            Random random = new Random(seed);

            if (biome.getCategory() == Biome.Category.NETHER) {
                // Nether
                addNetherFeatures(biome);
            } else if (biome.getCategory() == Biome.Category.THEEND) {
                // The End
                addTheEndFeatures(biome, random);
            } else {
                // Overworld and other dimensions
                Collection<Gems> toAdd = EnumSet.noneOf(Gems.class);
                long maxGemCount = selectMaxGemCount(seed);
                for (int i = 0; toAdd.size() < maxGemCount && i < 100; ++i) {
                    toAdd.add(Gems.Set.CLASSIC.selectRandom(random));
                }

                for (Gems gem : toAdd) {
                    addGemOre(biome, gem, random, DimensionType.OVERWORLD);
                    selected.add(gem);
                }

                if (!toAdd.isEmpty()) {
                    // Spawn glowroses of same type
                    addGlowroses(biome, toAdd);
                }

                addChaosOre(biome, random);
                addSilverOre(biome, random);

                if (biome.getDownfall() > 0.4f && GemsConfig.COMMON.worldGenWildFluffyPuffs.get()) {
                    addWildFluffyPuffs(biome);
                }

                for (Gems.Set gemSet : Gems.Set.values()) {
                    addGemGeode(biome, gemSet, random);
                }

                // Add regional gems for non-overworld dimensions
                int regionSize = GemsConfig.COMMON.worldGenOtherDimensionGemsRegionSize.get();
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, RegionalGemsFeature.INSTANCE
                        .withConfiguration(new RegionalGemsFeatureConfig(Gems.Set.CLASSIC, 8, regionSize, state -> state.isIn(Tags.Blocks.STONE), d -> d.getId() != DimensionType.OVERWORLD.getId()))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(regionSize > 0 ? 10 : 0, 10, 0, 50)))
                );
            }
        }

        Set<Gems> notSelected = EnumSet.complementOf(selected);
        notSelected.removeIf(gem -> gem.getSet() != Gems.Set.CLASSIC);

        if (!notSelected.isEmpty() && GemsConfig.COMMON.worldGenOverworldMaxGemsPerBiome.get() > 0) {
            SilentGems.LOGGER.debug("Some gems were not selected, adding to random biomes.");
            Random random = new Random(getBaseSeed());
            Biome[] biomes = ForgeRegistries.BIOMES.getValues().toArray(new Biome[0]);

            for (Gems gem : notSelected) {
                int count = MathHelper.nextInt(random, 2, 4);
                for (int i = 0; i < count; ++i) {
                    int biomeIndex = random.nextInt(biomes.length);
                    Biome biome = biomes[biomeIndex];
                    // Make sure it's not Nether or End
                    // Theoretically, this could leave out gems, but the chance is negligible.
                    if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
                        addGemOre(biome, gem, random, DimensionType.OVERWORLD);
                    }
                }
            }
        }

        logGemBiomes();
    }

    private static long selectMaxGemCount(long seed) {
        // Select the number of gem types for a biome based on the biome seed
        int min = GemsConfig.COMMON.worldGenOverworldMinGemsPerBiome.get();
        int max = GemsConfig.COMMON.worldGenOverworldMaxGemsPerBiome.get();
        if (max <= 0) {
            return 0;
        }
        int maxCorrected = max < min ? min : max;
        return Math.abs(seed % (maxCorrected - min + 1)) + min;
    }

    private static void addGlowroses(Biome biome, Collection<Gems> toAdd) {
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER
                .withConfiguration((new BlockClusterFeatureConfig.Builder(Util.make(() -> {
                    WeightedBlockStateProvider p = new WeightedBlockStateProvider();
                    toAdd.forEach(g -> p.addWeightedBlockstate(g.getGlowrose().getDefaultState(), 1));
                    return p;
                }), new SimpleBlockPlacer())
                        .tries(GemsConfig.COMMON.glowroseSpawnTryCount.get())
                        .build()
                ))
                .withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(2)))
        );
    }

    private static void addNetherFeatures(Biome biome) {
        int regionSize = GemsConfig.COMMON.worldGenNetherGemsRegionSize.get();
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, RegionalGemsFeature.INSTANCE
                .withConfiguration(new RegionalGemsFeatureConfig(Gems.Set.DARK, 10, regionSize, state -> state.getBlock() == Blocks.NETHERRACK, DimensionType.THE_NETHER))
                .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(regionSize > 0 ? 12 : 0, 25, 0, 95)))
        );

        //addOre(biome, Gems.Set.DARK.getMultiOre(), 8, 12, 25, 95, state -> state.getBlock() == Blocks.NETHERRACK);

        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER
                .withConfiguration((new BlockClusterFeatureConfig.Builder(Util.make(() -> {
                    WeightedBlockStateProvider p = new WeightedBlockStateProvider();
                    Gems.Set.DARK.iterator().forEachRemaining(g -> p.addWeightedBlockstate(g.getGlowrose().getDefaultState(), 1));
                    return p;
                }), new SimpleBlockPlacer())
                        .tries(GemsConfig.COMMON.glowroseSpawnTryCount.get())
                        .build()
                ))
                .withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(2)))
        );
    }

    private static void addTheEndFeatures(Biome biome, Random random) {
        int regionSize = GemsConfig.COMMON.worldGenEndGemsRegionSize.get();
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, RegionalGemsFeature.INSTANCE
                .withConfiguration(new RegionalGemsFeatureConfig(Gems.Set.LIGHT, 10, regionSize, state -> state.getBlock() == Blocks.END_STONE, DimensionType.THE_END))
                .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(regionSize > 0 ? 12 : 0, 16, 0, 72)))
        );

        //addOre(biome, Gems.Set.LIGHT.getMultiOre(), 8, 12, 16, 64, state -> state.getBlock() == Blocks.END_STONE);

        addEnderOre(biome, random);
        addEnderSlimeSpawns(biome);

        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER
                .withConfiguration((new BlockClusterFeatureConfig.Builder(Util.make(() -> {
                    WeightedBlockStateProvider p = new WeightedBlockStateProvider();
                    Gems.Set.LIGHT.iterator().forEachRemaining(g -> p.addWeightedBlockstate(g.getGlowrose().getDefaultState(), 1));
                    return p;
                }), new SimpleBlockPlacer())
                        .tries(GemsConfig.COMMON.glowroseSpawnTryCount.get())
                        .build()
                ))
                .withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(2)))
        );
    }

    private static void logGemBiomes() {
        // List which biomes each gem will spawn in, in a compact format.
        SilentGems.LOGGER.info("Your base biome seed is {}", getBaseSeed());

        for (Gems gem : Gems.values()) {
            Set<ResourceLocation> biomes = GEM_BIOMES.get(gem);
            if (biomes != null) {
                String biomeList = biomes.stream().map(ResourceLocation::toString).collect(Collectors.joining(", "));
                SilentGems.LOGGER.info("{}: {}", gem, biomeList);
            }
        }
    }

    private static void addChaosOre(Biome biome, Random random) {
        int minCount = GemsConfig.COMMON.worldGenChaosOreMinCount.get();
        int maxCount = GemsConfig.COMMON.worldGenChaosOreMaxCount.get();
        if (minCount > maxCount) {
            minCount = maxCount;
        }
        if (maxCount > 0) {
            int count = MathUtils.nextIntInclusive(random, minCount, maxCount);
            int size = MathUtils.nextIntInclusive(random, 12, 18);
            int maxHeight = MathUtils.nextIntInclusive(random, 15, 25);
            //SilentGems.LOGGER.debug("    Biome {}: add chaos ore (size {}, count {}, maxHeight {})", biome, size, count, maxHeight);
            addOre(biome, MiscOres.CHAOS.asBlock(), size, count, 5, maxHeight, d -> true);
        }
    }

    private static void addEnderOre(Biome biome, Random random) {
        addOre(biome, MiscOres.ENDER.asBlock(), 32, GemsConfig.COMMON.worldGenEnderOreCount.get(), 10, 70, state -> state.getBlock() == Blocks.END_STONE, d -> true);
    }

    private static void addSilverOre(Biome biome, Random random) {
        addOre(biome, MiscOres.SILVER.asBlock(), 6, GemsConfig.COMMON.worldGenSilverVeinCount.get(), 6, 28, d -> true);
    }

    private static void addGemOre(Biome biome, Gems gem, Random random, DimensionType dimension) {
        int size = MathHelper.nextInt(random, 6, 8);
        int count = MathHelper.nextInt(random, 2, 4);
        int minHeight = random.nextInt(8);
        int maxHeight = random.nextInt(40) + 30;
        //SilentGems.LOGGER.debug("    Biome {}: add gem {} (size {}, count {}, height [{}, {}])", biome, gem, size, count, minHeight, maxHeight);
        addOre(biome, gem.getOre(), size, count, minHeight, maxHeight, d -> d.getId() == dimension.getId());
        GEM_BIOMES.computeIfAbsent(gem, g -> new HashSet<>()).add(biome.getRegistryName());
    }

    private static void addOre(Biome biome, Block block, int size, int count, int minHeight, int maxHeight, Predicate<DimensionType> dimension) {
        addOre(biome, block, size, count, minHeight, maxHeight, s -> s.isIn(Tags.Blocks.STONE), dimension);
    }

    private static void addOre(Biome biome, Block block, int size, int count, int minHeight, int maxHeight, Predicate<BlockState> blockToReplace, Predicate<DimensionType> dimension) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, SGOreFeature.INSTANCE
                .withConfiguration(new SGOreFeatureConfig(block.getDefaultState(), size, blockToReplace, dimension))
                .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(count, minHeight, 0, maxHeight)))
        );
    }

    private static void addGemGeode(Biome biome, Gems.Set gemSet, Random random) {
        double baseChance = GemsConfig.COMMON.worldGenGeodeBaseChance.get();
        double variation = GemsConfig.COMMON.worldGenGeodeChanceVariation.get();
        float chance = (float) (baseChance + variation * random.nextGaussian());
        if (chance > 0 && baseChance > 0) {
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, GemGeodeFeature.INSTANCE
                    .withConfiguration(new GemGeodeFeatureConfig(gemSet, gemSet.getGeodeShell().asBlockState(), s -> s.isIn(Tags.Blocks.STONE)))
                    .withPlacement(Placement.CHANCE_RANGE.configure(new ChanceRangeConfig(chance, 20, 0, 40)))
            );
        }
    }

    private static void addWildFluffyPuffs(Biome biome) {
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER
                .withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(GemsBlocks.WILD_FLUFFY_PUFF_PLANT.get().getDefaultState()), new SimpleBlockPlacer())
                        .tries(12)
                        .build()
                ))
                .withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(1)))
        );
    }

    private static void addEnderSlimeSpawns(Biome biome) {
        int spawnWeight = GemsConfig.COMMON.enderSlimeSpawnWeight.get();
        if (spawnWeight > 0) {
            biome.getSpawns(EntityClassification.MONSTER).add(new Biome.SpawnListEntry(
                    GemsEntities.ENDER_SLIME.get(),
                    spawnWeight,
                    GemsConfig.COMMON.enderSlimeGroupSizeMin.get(),
                    GemsConfig.COMMON.enderSlimeGroupSizeMax.get()
            ));
        }
    }

    private static long getBaseSeed() {
        // Config override?
        String overrideValue = GemsConfig.COMMON.baseBiomeSeedOverride.get();
        if (!overrideValue.isEmpty()) {
            return overrideValue.hashCode();
        }

        // Default value is based on PC username
        String username = System.getProperty("user.name");
        if (username == null || username.isEmpty()) {
            // Fallback value
            return ModList.get().size() * 10000;
        }
        return username.hashCode();
    }

    private static long getBiomeSeed(Biome biome) {
        return getBaseSeed()
                + Objects.requireNonNull(biome.getRegistryName()).toString().hashCode()
                + biome.getCategory().ordinal() * 100
                + biome.getPrecipitation().ordinal() * 10
                + biome.getTempCategory().ordinal();
    }
}
