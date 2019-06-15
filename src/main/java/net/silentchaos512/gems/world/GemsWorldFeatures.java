package net.silentchaos512.gems.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.FluffyPuffPlant;
import net.silentchaos512.gems.block.MiscOres;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.entity.EnderSlimeEntity;
import net.silentchaos512.gems.init.ModEntities;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.world.feature.GlowroseFeature;
import net.silentchaos512.gems.world.feature.SGOreFeature;
import net.silentchaos512.gems.world.feature.SGOreFeatureConfig;
import net.silentchaos512.lib.world.feature.PlantFeature;
import net.silentchaos512.utils.MathUtils;

import java.util.*;
import java.util.function.Predicate;

/**
 * Experimental world generation. Not sure if Forge intends to add something, but this should work
 * for now.
 */
public final class GemsWorldFeatures {
    private GemsWorldFeatures() {}

    public static void addFeaturesToBiomes() {
        EnumSet<Gems> selected = EnumSet.noneOf(Gems.class);

        for (Biome biome : ForgeRegistries.BIOMES) {
            long seed = getBiomeSeed(biome);
            Random random = new Random(seed);

            // TODO: Nether and End just uses multi-gem ore.
            if (biome.getCategory() == Biome.Category.NETHER) {
                // Nether
                addOre(biome, Gems.Set.DARK.getMultiOre(), 8, 12, 25, 95, state ->
                        state.getBlock() == Blocks.NETHERRACK);
            } else if (biome.getCategory() == Biome.Category.THEEND) {
                // The End
                addOre(biome, Gems.Set.LIGHT.getMultiOre(), 8, 12, 16, 64, state ->
                        state.getBlock() == Blocks.END_STONE);
                addEnderOre(biome, random);
                addEnderSlimeSpawns(biome);
            } else {
                // Overworld and other dimensions
                Collection<Gems> toAdd = EnumSet.noneOf(Gems.class);
                for (int i = 0; toAdd.size() < Math.abs(seed % 3) + 3 && i < 100; ++i) {
                    toAdd.add(Gems.Set.CLASSIC.selectRandom(random));
                }

                for (Gems gem : toAdd) {
                    addGemOre(biome, gem, random);
                    selected.add(gem);
                }

                // Spawn glowroses of same type
                biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(
                        new GlowroseFeature(toAdd),
                        NoFeatureConfig.NO_FEATURE_CONFIG,
                        Placement.COUNT_HEIGHTMAP_32,
                        new FrequencyConfig(2)
                ));

                addChaosOre(biome, random);
                addSilverOre(biome, random);

                if (biome.getDownfall() > 0.4f) {
                    addWildFluffyPuffs(biome);
                }
            }
        }

        Set<Gems> notSelected = EnumSet.complementOf(selected);
        notSelected.removeIf(gem -> gem.getSet() != Gems.Set.CLASSIC);

        if (!notSelected.isEmpty()) {
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
                        addGemOre(biome, gem, random);
                    }
                }
            }
        }
    }

    private static void addChaosOre(Biome biome, Random random) {
        int count = MathUtils.nextIntInclusive(random, 1, 2);
        int size = MathUtils.nextIntInclusive(random, 10, 20);
        int maxHeight = MathUtils.nextIntInclusive(random, 15, 25);
        SilentGems.LOGGER.debug("    Biome {}: add chaos ore (size {}, count {}, maxHeight {})",
                biome, size, count, maxHeight);
        addOre(biome, MiscOres.CHAOS.asBlock(), size, count, 5, maxHeight);
    }

    private static void addEnderOre(Biome biome, Random random) {
        addOre(biome, MiscOres.ENDER.asBlock(), 32, 1, 10, 70, state -> state.getBlock() == Blocks.END_STONE);
    }

    private static void addSilverOre(Biome biome, Random random) {
        addOre(biome, MiscOres.SILVER.asBlock(), 6, 2, 6, 28);
    }

    private static void addGemOre(Biome biome, Gems gem, Random random) {
        int size = MathHelper.nextInt(random, 6, 8);
        int count = MathHelper.nextInt(random, 2, 4);
        int minHeight = random.nextInt(8);
        int maxHeight = random.nextInt(40) + 30;
        SilentGems.LOGGER.debug("    Biome {}: add gem {} (size {}, count {}, height [{}, {}])",
                biome, gem, size, count, minHeight, maxHeight);
        addOre(biome, gem.getOre(), size, count, minHeight, maxHeight);
    }

    private static void addOre(Biome biome, Block block, int size, int count, int minHeight, int maxHeight) {
        addOre(biome, block, size, count, minHeight, maxHeight, s -> s.isIn(Tags.Blocks.STONE));
    }

    private static void addOre(Biome biome, Block block, int size, int count, int minHeight, int maxHeight, Predicate<BlockState> blockToReplace) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                SGOreFeature.INSTANCE,
                new SGOreFeatureConfig(
                        block.getDefaultState(),
                        size,
                        blockToReplace
                ),
                Placement.COUNT_RANGE,
                new CountRangeConfig(count, minHeight, 0, maxHeight)
        ));
    }

    private static void addWildFluffyPuffs(Biome biome) {
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(
                new PlantFeature(FluffyPuffPlant.WILD.get().getMaturePlant(), 32, 6),
                NoFeatureConfig.NO_FEATURE_CONFIG,
                Placement.COUNT_HEIGHTMAP_32,
                new FrequencyConfig(1)
        ));
    }

    @SuppressWarnings("unchecked") // cast to EntityType<EnderSlimeEntity> is valid
    private static void addEnderSlimeSpawns(Biome biome) {
        EntityType<EnderSlimeEntity> type = (EntityType<EnderSlimeEntity>) ModEntities.ENDER_SLIME.type();
        biome.getSpawns(EntityClassification.MONSTER).add(new Biome.SpawnListEntry(
                type,
                GemsConfig.COMMON.enderSlimeSpawnWeight.get(),
                GemsConfig.COMMON.enderSlimeGroupSizeMin.get(),
                GemsConfig.COMMON.enderSlimeGroupSizeMax.get()
        ));
    }

    private static long getBaseSeed() {
        // TODO: Fixed seed config?
        String username = System.getProperty("user.name");
        if (username == null) {
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
