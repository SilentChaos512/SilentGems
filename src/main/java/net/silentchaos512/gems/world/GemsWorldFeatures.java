package net.silentchaos512.gems.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.FluffyPuffPlant;
import net.silentchaos512.gems.block.MiscOres;
import net.silentchaos512.gems.entity.EntityEnderSlime;
import net.silentchaos512.gems.init.ModEntities;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.world.feature.GlowroseFeature;
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
                biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createCompositeFlowerFeature(
                        new GlowroseFeature(toAdd),
                        Biome.SURFACE_PLUS_32,
                        new FrequencyConfig(2)
                ));

                addChaosOre(biome, random);

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
        addOre(biome, MiscOres.CHAOS.getBlock(), size, count, 5, maxHeight);
    }

    private static void addEnderOre(Biome biome, Random random) {
        addOre(biome, MiscOres.ENDER.getBlock(), 32, 1, 10, 70, state ->
                state.getBlock() == Blocks.END_STONE);
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
        addOre(biome, block, size, count, minHeight, maxHeight, MinableConfig.IS_ROCK);
    }

    private static void addOre(Biome biome, Block block, int size, int count, int minHeight, int maxHeight, Predicate<IBlockState> blockToReplace) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(
                Feature.MINABLE, new MinableConfig(
                        blockToReplace,
                        block.getDefaultState(),
                        size
                ),
                Biome.COUNT_RANGE,
                new CountRangeConfig(count, minHeight, 0, maxHeight)
        ));
    }

    private static void addWildFluffyPuffs(Biome biome) {
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createCompositeFlowerFeature(
                new PlantFeature(FluffyPuffPlant.WILD.get().getMaturePlant(), 32, 6),
                Biome.SURFACE_PLUS_32,
                new FrequencyConfig(1)
        ));
    }

    @SuppressWarnings("unchecked") // cast to EntityType<EntityEnderSlime> is valid
    private static void addEnderSlimeSpawns(Biome biome) {
        EntityType<EntityEnderSlime> type = (EntityType<EntityEnderSlime>) ModEntities.ENDER_SLIME.type();
        biome.getSpawns(EnumCreatureType.MONSTER).add(new Biome.SpawnListEntry(type, 3, 1, 2));
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
