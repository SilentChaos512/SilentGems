package net.silentchaos512.gems.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.gems.world.OreConfigDefaults;

import java.util.*;
import java.util.function.Function;

public class WorldGenGenerator extends DatapackBuiltinEntriesProvider {
    private static final RuleTest replaceStone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    private static final RuleTest replaceDeepslate = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
    private static final RuleTest replaceNetherrack = new TagMatchTest(Tags.Blocks.NETHERRACK);
    private static final RuleTest replaceEndStone = new TagMatchTest(Tags.Blocks.END_STONES);

    private static final ConfiguredFeature<?, ?> silverOre = new ConfiguredFeature<>(Feature.ORE,
            new OreConfiguration(
                    ImmutableList.of(
                            OreConfiguration.target(replaceStone, GemsBlocks.SILVER_ORE.asBlockState()),
                            OreConfiguration.target(replaceDeepslate, GemsBlocks.DEEPSLATE_SILVER_ORE.asBlockState())
                    ),
                    8,
                    0.2f
            )
    );

    private static final Map<Gems, ConfiguredFeature<?, ?>> overworldOreFeatures = makeMap(g -> {
        OreConfigDefaults config = g.getOreConfigDefaults(Level.OVERWORLD);
        return new ConfiguredFeature<>(Feature.ORE,
                new OreConfiguration(
                        ImmutableList.of(
                                OreConfiguration.target(replaceStone, g.getOre().defaultBlockState()),
                                OreConfiguration.target(replaceDeepslate, g.getDeepslateOre().defaultBlockState())
                        ),
                        config.size(),
                        config.discardChanceOnAirExposure()
                )
        );
    });

    private static final Map<Gems, ConfiguredFeature<?, ?>> netherOreFeatures = makeMap(g -> {
        OreConfigDefaults config = g.getOreConfigDefaults(Level.NETHER);
        return new ConfiguredFeature<>(Feature.ORE,
                new OreConfiguration(
                        replaceNetherrack,
                        g.getNetherOre().defaultBlockState(),
                        config.size(),
                        config.discardChanceOnAirExposure()
                )
        );
    });

    private static final Map<Gems, ConfiguredFeature<?, ?>> endOreFeatures = makeMap(g -> {
        OreConfigDefaults config = g.getOreConfigDefaults(Level.END);
        return new ConfiguredFeature<>(Feature.ORE,
                new OreConfiguration(
                        replaceEndStone,
                        g.getEndOre().defaultBlockState(),
                        config.size(),
                        config.discardChanceOnAirExposure()
                )
        );
    });

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ctx -> {
                ctx.register(configuredFeature(GemsBase.getId("overworld/silver_ore")), silverOre);

                overworldOreFeatures.forEach((gem, feature) -> ctx.register(getOreFeatureKey(gem, Level.OVERWORLD), feature));
                netherOreFeatures.forEach((gem, feature) -> ctx.register(getOreFeatureKey(gem, Level.NETHER), feature));
                endOreFeatures.forEach((gem, feature) -> ctx.register(getOreFeatureKey(gem, Level.END), feature));
            })
            .add(Registries.PLACED_FEATURE, ctx -> {
                ResourceKey<ConfiguredFeature<?, ?>> silverOreKey = configuredFeature(GemsBase.getId("overworld/silver_ore"));
                ctx.register(placedFeature(silverOreKey.location()), placed(holderFeature(ctx, silverOreKey), -60, 40, 16));

                overworldOreFeatures.forEach((gem, feature) -> makePlacedFeature(ctx, gem, Level.OVERWORLD));
                netherOreFeatures.forEach((gem, feature) -> makePlacedFeature(ctx, gem, Level.NETHER));
                endOreFeatures.forEach((gem, feature) -> makePlacedFeature(ctx, gem, Level.END));
            })
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ctx -> {
                ResourceKey<ConfiguredFeature<?, ?>> silverOreKey = configuredFeature(GemsBase.getId("overworld/silver_ore"));
                registerOreBiomeModifiers(ctx, Level.OVERWORLD, BiomeTags.IS_OVERWORLD, overworldOreFeatures, "overworld_ores", Lists.newArrayList(silverOreKey));
                registerOreBiomeModifiers(ctx, Level.NETHER, BiomeTags.IS_NETHER, netherOreFeatures, "nether_ores", Collections.emptyList());
                registerOreBiomeModifiers(ctx, Level.END, BiomeTags.IS_END, endOreFeatures, "end_ores", Collections.emptyList());
            });

    public WorldGenGenerator(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), BUILDER, Collections.singleton(GemsBase.MOD_ID));
    }

    public static <T> Map<Gems, T> makeMap(Function<Gems, T> getter) {
        LinkedHashMap<Gems, T> map = new LinkedHashMap<>();
        for (Gems gem : Gems.values()) {
            map.put(gem, getter.apply(gem));
        }
        return map;
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> getOreFeatureKey(Gems gem, ResourceKey<Level> level) {
        return configuredFeature(GemsBase.getId(level.location().getPath() + "/" + gem.getName() + "_ore"));
    }

    private static void makePlacedFeature(BootstapContext<PlacedFeature> ctx, Gems gem, ResourceKey<Level> level) {
        ResourceKey<ConfiguredFeature<?, ?>> key = getOreFeatureKey(gem, level);
        OreConfigDefaults config = gem.getOreConfigDefaults(level);
        PlacedFeature placed = placed(holderFeature(ctx, key), config.minHeight(), config.maxHeight(), config.count());
        ctx.register(placedFeature(key.location()), placed);
    }

    private static void registerOreBiomeModifiers(BootstapContext<BiomeModifier> ctx, ResourceKey<Level> level, TagKey<Biome> biomes, Map<Gems, ConfiguredFeature<?, ?>> ores, String modifierName, Collection<ResourceKey<ConfiguredFeature<?, ?>>> others) {
        HolderSet.Named<Biome> biomeTagSet = ctx.lookup(Registries.BIOME).getOrThrow(biomes);
        ArrayList<Holder<PlacedFeature>> list = new ArrayList<>();

        ores.forEach((gem, feature) -> {
            ResourceKey<ConfiguredFeature<?, ?>> key = getOreFeatureKey(gem, level);
            list.add(holderPlaced(ctx, key.location()));
        });

        others.forEach(o -> list.add(holderPlaced(ctx, o.location())));

        BiomeModifier oresMod = new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomeTagSet,
                HolderSet.direct(list),
                GenerationStep.Decoration.UNDERGROUND_ORES
        );

        ctx.register(biomeModifier(GemsBase.getId(modifierName)), oresMod);

    }

    public static ConfiguredFeature<?, ?> ore(Block block, RuleTest replacing, int size) {
        return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(replacing, block.defaultBlockState(), size));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> configuredFeature(ResourceLocation name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, name);
    }

    protected static ResourceKey<PlacedFeature> placedFeature(ResourceLocation name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, name);
    }

    protected static ResourceKey<BiomeModifier> biomeModifier(ResourceLocation name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, name);
    }

    public static PlacedFeature placed(Holder<ConfiguredFeature<?, ?>> feature, int minHeight, int maxHeight, int count) {
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

    public static Holder<ConfiguredFeature<?, ?>> holderFeature(BootstapContext<PlacedFeature> ctx, ResourceKey<ConfiguredFeature<?, ?>> location) {
        return ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(location);
    }

    public static Holder<PlacedFeature> holderPlaced(BootstapContext<BiomeModifier> ctx, ResourceLocation location) {
        return ctx.lookup(Registries.PLACED_FEATURE).getOrThrow(placedFeature(location));
    }
}
