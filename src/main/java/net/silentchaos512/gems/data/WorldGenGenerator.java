package net.silentchaos512.gems.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.data.event.DatapackRegistryGatherer;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.setup.Gems;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.world.OreConfigDefaults;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Generates features, placed features, and biome modifiers. As of 26.3, configured features were merged into
 * {@link Feature} (registered in {@link Registries#FEATURE}), and datapack registry entries are gathered in
 * {@link net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent}.
 */
public final class WorldGenGenerator {
    private static final RuleTest replaceStone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    private static final RuleTest replaceDeepslate = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
    private static final RuleTest replaceNetherrack = new TagMatchTest(Tags.BlockItems.NETHERRACKS.block());
    private static final RuleTest replaceEndStone = new TagMatchTest(Tags.BlockItems.END_STONES.block());

    private static final List<ResourceKey<Level>> DIMENSIONS = List.of(Level.OVERWORLD, Level.NETHER, Level.END);

    private WorldGenGenerator() {
    }

    public static DatapackRegistryGatherer gather(DatapackRegistryGatherer gatherer) {
        return gatherer
                .add(Registries.FEATURE, WorldGenGenerator::bootstrapFeatures)
                .add(Registries.PLACED_FEATURE, WorldGenGenerator::bootstrapPlacedFeatures)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, WorldGenGenerator::bootstrapBiomeModifiers);
    }

    private static void bootstrapFeatures(BootstrapContext<Feature> ctx) {
        ctx.register(feature(SilentGems.getId("overworld/silver_ore")), new OreFeature(
                List.of(
                        BlockReplacement.replace(replaceStone, GemsBlocks.SILVER_ORE.get().defaultBlockState()),
                        BlockReplacement.replace(replaceDeepslate, GemsBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState())
                ),
                8,
                0.2f
        ));

        ctx.register(feature(SilentGems.getId("overworld/chaos_ore")), new OreFeature(
                List.of(
                        BlockReplacement.replace(replaceStone, GemsBlocks.CHAOS_ORE.get().defaultBlockState()),
                        BlockReplacement.replace(replaceDeepslate, GemsBlocks.DEEPSLATE_CHAOS_ORE.get().defaultBlockState())
                ),
                12,
                0.5f
        ));

        for (Gems gem : Gems.values()) {
            OreConfigDefaults overworld = gem.getOreConfigDefaults(Level.OVERWORLD);
            ctx.register(getOreFeatureKey(gem, Level.OVERWORLD), new OreFeature(
                    List.of(
                            BlockReplacement.replace(replaceStone, gem.getOre().defaultBlockState()),
                            BlockReplacement.replace(replaceDeepslate, gem.getDeepslateOre().defaultBlockState())
                    ),
                    overworld.size(),
                    overworld.discardChanceOnAirExposure()
            ));

            OreConfigDefaults nether = gem.getOreConfigDefaults(Level.NETHER);
            ctx.register(getOreFeatureKey(gem, Level.NETHER), ore(gem.getNetherOre(), replaceNetherrack, nether.size(), nether.discardChanceOnAirExposure()));

            OreConfigDefaults end = gem.getOreConfigDefaults(Level.END);
            ctx.register(getOreFeatureKey(gem, Level.END), ore(gem.getEndOre(), replaceEndStone, end.size(), end.discardChanceOnAirExposure()));

            for (ResourceKey<Level> level : DIMENSIONS) {
                ctx.register(getGlowroseFeatureKey(gem, level), glowroses(gem));
            }
        }
    }

    private static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> ctx) {
        ResourceKey<Feature> silverOreKey = feature(SilentGems.getId("overworld/silver_ore"));
        ctx.register(placedFeature(silverOreKey.identifier()), placed(holderFeature(ctx, silverOreKey), -60, 40, 16));

        ResourceKey<Feature> chaosOreKey = feature(SilentGems.getId("overworld/chaos_ore"));
        ctx.register(placedFeature(chaosOreKey.identifier()), placed(holderFeature(ctx, chaosOreKey), -60, 20, 6));

        for (ResourceKey<Level> level : DIMENSIONS) {
            for (Gems gem : Gems.values()) {
                makePlacedFeature(ctx, gem, level);
                makePlacedGlowroseFeature(ctx, gem, level);
            }
        }
    }

    private static void bootstrapBiomeModifiers(BootstrapContext<BiomeModifier> ctx) {
        List<ResourceKey<Feature>> overworldOthers = new ArrayList<>();
        overworldOthers.add(feature(SilentGems.getId("overworld/silver_ore")));
        overworldOthers.add(feature(SilentGems.getId("overworld/chaos_ore")));
        for (Gems gem : Gems.values()) overworldOthers.add(getGlowroseFeatureKey(gem, Level.OVERWORLD));
        registerOreBiomeModifiers(ctx, Level.OVERWORLD, BiomeTags.IS_OVERWORLD, "overworld_features", overworldOthers);

        List<ResourceKey<Feature>> netherOthers = new ArrayList<>();
        for (Gems gem : Gems.values()) netherOthers.add(getGlowroseFeatureKey(gem, Level.NETHER));
        registerOreBiomeModifiers(ctx, Level.NETHER, BiomeTags.IS_NETHER, "nether_features", netherOthers);

        List<ResourceKey<Feature>> endOthers = new ArrayList<>();
        for (Gems gem : Gems.values()) endOthers.add(getGlowroseFeatureKey(gem, Level.END));
        registerOreBiomeModifiers(ctx, Level.END, BiomeTags.IS_END, "end_features", endOthers);
    }

    private static ResourceKey<Feature> getOreFeatureKey(Gems gem, ResourceKey<Level> level) {
        return feature(SilentGems.getId(level.identifier().getPath() + "/" + gem.getName() + "_ore"));
    }

    private static ResourceKey<Feature> getGlowroseFeatureKey(Gems gem, ResourceKey<Level> level) {
        return feature(SilentGems.getId(level.identifier().getPath() + "/" + gem.getName() + "_glowrose"));
    }

    private static void makePlacedFeature(BootstrapContext<PlacedFeature> ctx, Gems gem, ResourceKey<Level> level) {
        ResourceKey<Feature> key = getOreFeatureKey(gem, level);
        OreConfigDefaults config = gem.getOreConfigDefaults(level);
        PlacedFeature placed = placed(holderFeature(ctx, key), config.minHeight(), config.maxHeight(), config.count());
        ctx.register(placedFeature(key.identifier()), placed);
    }

    private static void makePlacedGlowroseFeature(BootstrapContext<PlacedFeature> ctx, Gems gem, ResourceKey<Level> level) {
        ResourceKey<Feature> key = getGlowroseFeatureKey(gem, level);
        PlacedFeature placed = new PlacedFeature(holderFeature(ctx, key),
                ImmutableList.of(
                        RarityFilter.onAverageOnceEvery(360),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome(),
                        CountPlacement.of(32),
                        OffsetPlacement.ofTriangle(7, 3),
                        BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)
                )
        );
        ctx.register(placedFeature(key.identifier()), placed);
    }

    private static void registerOreBiomeModifiers(BootstrapContext<BiomeModifier> ctx, ResourceKey<Level> level, TagKey<Biome> biomes, String modifierName, Collection<ResourceKey<Feature>> others) {
        HolderSet.Named<Biome> biomeTagSet = ctx.lookup(Registries.BIOME).getOrThrow(biomes);
        ArrayList<Holder<PlacedFeature>> list = new ArrayList<>();

        for (Gems gem : Gems.values()) {
            list.add(holderPlaced(ctx, getOreFeatureKey(gem, level).identifier()));
        }

        others.forEach(o -> list.add(holderPlaced(ctx, o.identifier())));

        BiomeModifier oresMod = new BiomeModifiers.AddFeaturesBiomeModifier(
                biomeTagSet,
                HolderSet.direct(list),
                GenerationStep.Decoration.UNDERGROUND_ORES
        );

        ctx.register(biomeModifier(SilentGems.getId(modifierName)), oresMod);
    }

    private static SimpleBlockFeature glowroses(Gems g) {
        return new SimpleBlockFeature(BlockStateProvider.of(g.getGlowrose()));
    }

    public static OreFeature ore(Block block, RuleTest replacing, int size, float discardChanceOnAirExposure) {
        return new OreFeature(List.of(BlockReplacement.replace(replacing, block.defaultBlockState())), size, discardChanceOnAirExposure);
    }

    public static ResourceKey<Feature> feature(Identifier name) {
        return ResourceKey.create(Registries.FEATURE, name);
    }

    private static ResourceKey<PlacedFeature> placedFeature(Identifier name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, name);
    }

    private static ResourceKey<BiomeModifier> biomeModifier(Identifier name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, name);
    }

    public static PlacedFeature placed(Holder<Feature> feature, int minHeight, int maxHeight, int count) {
        return new PlacedFeature(feature, placements(minHeight, maxHeight, count));
    }

    public static List<PlacementModifier> placements(int minHeight, int maxHeight, int count) {
        return ImmutableList.of(
                HeightRangePlacement.uniform(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)),
                InSquarePlacement.spread(),
                CountPlacement.of(count),
                BiomeFilter.biome()
        );
    }

    private static Holder<Feature> holderFeature(BootstrapContext<PlacedFeature> ctx, ResourceKey<Feature> location) {
        return ctx.lookup(Registries.FEATURE).getOrThrow(location);
    }

    private static Holder<PlacedFeature> holderPlaced(BootstrapContext<BiomeModifier> ctx, Identifier location) {
        return ctx.lookup(Registries.PLACED_FEATURE).getOrThrow(placedFeature(location));
    }
}
