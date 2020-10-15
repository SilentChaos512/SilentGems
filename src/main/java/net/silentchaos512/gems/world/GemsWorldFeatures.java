package net.silentchaos512.gems.world;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.MiscOres;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.init.GemsEntities;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.world.feature.*;
import net.silentchaos512.utils.MathUtils;

import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = SilentGems.MOD_ID)
public final class GemsWorldFeatures {
    private static final EnumMap<Gems, Set<ResourceLocation>> GEM_BIOMES = new EnumMap<>(Gems.class);

    private GemsWorldFeatures() {}

    @SubscribeEvent
    public static void addFeaturesToBiomes(BiomeLoadingEvent biome) {
        EnumSet<Gems> selected = EnumSet.noneOf(Gems.class);

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
                addGemOre(biome, gem, random);
                selected.add(gem);
            }

            if (!toAdd.isEmpty()) {
                // Spawn glowroses of same type
                addGlowroses(biome, toAdd);
            }

            addChaosOre(biome, random);
            addSilverOre(biome, random);

            if (biome.getClimate().downfall > 0.4f && GemsConfig.COMMON.worldGenWildFluffyPuffs.get()) {
                addWildFluffyPuffs(biome);
            }

            for (Gems.Set gemSet : Gems.Set.values()) {
                addGemGeode(biome, gemSet, random, Tags.Blocks.STONE);
            }

            // Add regional gems for non-overworld dimensions
            int regionSize = GemsConfig.COMMON.worldGenOtherDimensionGemsRegionSize.get();
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, RegionalGemsFeature.INSTANCE
                    .withConfiguration(new RegionalGemsFeatureConfig(Gems.Set.CLASSIC, 8, regionSize))
                    .withPlacement(Placement.field_242907_l.configure(new TopSolidRangeConfig(10, 0, 50)))
                    .func_242731_b(regionSize > 0 ? 10 : 0)
            );
        }

        /*Set<Gems> notSelected = EnumSet.complementOf(selected);
        notSelected.removeIf(gem -> gem.getSet() != Gems.Set.CLASSIC);

        if (!notSelected.isEmpty() && GemsConfig.COMMON.worldGenOverworldMaxGemsPerBiome.get() > 0) {
            SilentGems.LOGGER.debug("Some gems were not selected, adding to random biomes.");
            Random random2 = new Random(getBaseSeed());
            Biome[] biomes = ForgeRegistries.BIOMES.getValues().toArray(new Biome[0]);

            for (Gems gem : notSelected) {
                int count = MathHelper.nextInt(random2, 2, 4);
                for (int i = 0; i < count; ++i) {
                    int biomeIndex = random2.nextInt(biomes.length);
                    Biome biome = biomes[biomeIndex];
                    // Make sure it's not Nether or End
                    // Theoretically, this could leave out gems, but the chance is negligible.
                    if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
                        addGemOre(biome, gem, random2);
                    }
                }
            }
        }*/
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

    private static void addGlowroses(BiomeLoadingEvent biome, Collection<Gems> toAdd) {
        biome.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER
                .withConfiguration((new BlockClusterFeatureConfig.Builder(Util.make(() -> {
                    WeightedBlockStateProvider p = new WeightedBlockStateProvider();
                    toAdd.forEach(g -> p.addWeightedBlockstate(g.getGlowrose().getDefaultState(), 1));
                    return p;
                }), new SimpleBlockPlacer())
                        .tries(GemsConfig.COMMON.glowroseSpawnTryCount.get())
                        .build()
                ))
                .withPlacement(Placement.field_242897_C.configure(new FeatureSpreadConfig(1)))
                .withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig(4)))
        );
    }

    private static void addNetherFeatures(BiomeLoadingEvent biome) {
        int regionSize = GemsConfig.COMMON.worldGenNetherGemsRegionSize.get();
        biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, RegionalGemsFeature.INSTANCE
                .withConfiguration(new RegionalGemsFeatureConfig(Gems.Set.DARK, 10, regionSize))
                .withPlacement(Placement.field_242907_l.configure(new TopSolidRangeConfig(25, 0, 95)))
                .func_242731_b(regionSize > 0 ? 12 : 0)
        );

        //addOre(biome, Gems.Set.DARK.getMultiOre(), 8, 12, 25, 95, state -> state.getBlock() == Blocks.NETHERRACK);

        biome.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER
                .withConfiguration((new BlockClusterFeatureConfig.Builder(Util.make(() -> {
                    WeightedBlockStateProvider p = new WeightedBlockStateProvider();
                    Gems.Set.DARK.iterator().forEachRemaining(g -> p.addWeightedBlockstate(g.getGlowrose().getDefaultState(), 1));
                    return p;
                }), new SimpleBlockPlacer())
                        .tries(GemsConfig.COMMON.glowroseSpawnTryCount.get())
                        .build()
                ))
                .withPlacement(Features.Placements.VEGETATION_PLACEMENT)
                .withPlacement(Placement.field_242897_C.configure(new FeatureSpreadConfig(2)))
                .func_242733_d(128)
                .func_242729_a(4)
        );
    }

    private static void addTheEndFeatures(BiomeLoadingEvent biome, Random random) {
        int regionSize = GemsConfig.COMMON.worldGenEndGemsRegionSize.get();
        biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, RegionalGemsFeature.INSTANCE
                .withConfiguration(new RegionalGemsFeatureConfig(Gems.Set.LIGHT, 10, regionSize))
                .withPlacement(Placement.field_242907_l.configure(new TopSolidRangeConfig(16, 0, 72)))
                .func_242731_b(regionSize > 0 ? 12 : 0)
        );

        //addOre(biome, Gems.Set.LIGHT.getMultiOre(), 8, 12, 16, 64, state -> state.getBlock() == Blocks.END_STONE);

        addEnderOre(biome, random);
        addEnderSlimeSpawns(biome);

        biome.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER
                .withConfiguration((new BlockClusterFeatureConfig.Builder(Util.make(() -> {
                    WeightedBlockStateProvider p = new WeightedBlockStateProvider();
                    Gems.Set.LIGHT.iterator().forEachRemaining(g -> p.addWeightedBlockstate(g.getGlowrose().getDefaultState(), 1));
                    return p;
                }), new SimpleBlockPlacer())
                        .tries(GemsConfig.COMMON.glowroseSpawnTryCount.get())
                        .build()
                ))
                .withPlacement(Features.Placements.VEGETATION_PLACEMENT)
                .withPlacement(Placement.field_242897_C.configure(new FeatureSpreadConfig(2)))
                .func_242733_d(128)
                .func_242729_a(4)
        );
    }

    public static void logGemBiomes() {
        // List which biomes each gem will spawn in, in a compact format.
        SilentGems.LOGGER.info("Your base biome seed is {}", getBaseSeed());

        for (Gems gem : Gems.values()) {
            Set<ResourceLocation> biomes = GEM_BIOMES.get(gem);
            if (biomes != null) {
                String biomeList = biomes.stream().map(ResourceLocation::toString).collect(Collectors.joining(", "));
                SilentGems.LOGGER.info("{}: {}", gem, biomeList);
            } else if (gem.getSet() == Gems.Set.CLASSIC) {
                SilentGems.LOGGER.error("Classic gem '{}' was not added to any biomes! Try editing 'baseBiomeSeedOverride' in the config.", gem.getName());
            }
        }
    }

    private static void addChaosOre(BiomeLoadingEvent biome, Random random) {
        int minCount = GemsConfig.COMMON.worldGenChaosOreMinCount.get();
        int maxCount = GemsConfig.COMMON.worldGenChaosOreMaxCount.get();
        if (minCount > maxCount) {
            minCount = maxCount;
        }
        if (maxCount > 0) {
            int count = MathUtils.nextIntInclusive(random, minCount, maxCount);
            int size = MathUtils.nextIntInclusive(random, 6, 9);
            int maxHeight = MathUtils.nextIntInclusive(random, 15, 25);
            //SilentGems.LOGGER.debug("    Biome {}: add chaos ore (size {}, count {}, maxHeight {})", biome, size, count, maxHeight);
            addOre(biome, MiscOres.CHAOS.asBlock(), size, count, 5, maxHeight, Tags.Blocks.STONE);
        }
    }

    private static void addEnderOre(BiomeLoadingEvent biome, Random random) {
        addOre(biome, MiscOres.ENDER.asBlock(), 16, GemsConfig.COMMON.worldGenEnderOreCount.get(), 10, 70, Tags.Blocks.END_STONES);
    }

    private static void addSilverOre(BiomeLoadingEvent biome, Random random) {
        addOre(biome, MiscOres.SILVER.asBlock(), 6, GemsConfig.COMMON.worldGenSilverVeinCount.get(), 6, 28, Tags.Blocks.STONE);
    }

    private static void addGemOre(BiomeLoadingEvent biome, Gems gem, Random random) {
        int size = MathHelper.nextInt(random, 6, 8);
        int count = MathHelper.nextInt(random, 2, 4);
        int minHeight = random.nextInt(8);
        int maxHeight = random.nextInt(40) + 30;
        //SilentGems.LOGGER.debug("    Biome {}: add gem {} (size {}, count {}, height [{}, {}])", biome, gem, size, count, minHeight, maxHeight);
        addOre(biome, gem.getOre(), size, count, minHeight, maxHeight, getOreGenTargetBlock(gem.getSet()));
        GEM_BIOMES.computeIfAbsent(gem, g -> new HashSet<>()).add(biome.getName());
    }

    public static ITag<Block> getOreGenTargetBlock(Gems.Set gemSet) {
        if (gemSet == Gems.Set.DARK)
            return Tags.Blocks.NETHERRACK;
        if (gemSet == Gems.Set.LIGHT)
            return Tags.Blocks.END_STONES;
        return Tags.Blocks.STONE;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    private static void addOre(BiomeLoadingEvent biome, Block block, int size, int count, int minHeight, int maxHeight, ITag<Block> blockToReplace) {
        biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, SGOreFeature.INSTANCE
                .withConfiguration(new SGOreFeatureConfig(block.getDefaultState(), size, blockToReplace))
                .withPlacement(Placement.field_242907_l.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
                .func_242728_a()
                .func_242731_b(count)
        );
    }

    private static void addGemGeode(BiomeLoadingEvent biome, Gems.Set gemSet, Random random, ITag.INamedTag<Block> target) {
        double baseChance = GemsConfig.COMMON.worldGenGeodeBaseChance.get();
        double variation = GemsConfig.COMMON.worldGenGeodeChanceVariation.get();
        float chance = (float) (baseChance + variation * random.nextGaussian());
        if (chance > 0 && baseChance > 0) {
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, GemGeodeFeature.INSTANCE
                    .withConfiguration(new GemGeodeFeatureConfig(gemSet, gemSet.getGeodeShell().asBlockState(), target))
                    .withPlacement(Placement.field_242908_m.configure(new TopSolidRangeConfig(20, 0, 40)))
                    .withPlacement(Placement.field_242898_b.configure(new ChanceConfig((int) (1f / chance))))
            );
        }
    }

    private static void addWildFluffyPuffs(BiomeLoadingEvent biome) {
        biome.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER
                .withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(GemsBlocks.WILD_FLUFFY_PUFF_PLANT.get().getDefaultState()), new SimpleBlockPlacer())
                        .tries(12)
                        .build()
                ))
                .withPlacement(Placement.field_242897_C.configure(new FeatureSpreadConfig(1)))
        );
    }

    private static void addEnderSlimeSpawns(BiomeLoadingEvent biome) {
        int spawnWeight = GemsConfig.COMMON.enderSlimeSpawnWeight.get();
        if (spawnWeight > 0) {
            biome.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(
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

    private static long getBiomeSeed(BiomeLoadingEvent biome) {
        return getBaseSeed()
                + Objects.requireNonNull(biome.getName()).toString().hashCode()
                + biome.getCategory().ordinal() * 100
                + biome.getClimate().precipitation.ordinal() * 10
                + biome.getClimate().temperatureModifier.ordinal();
    }
}
