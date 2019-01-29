package net.silentchaos512.gems.world;

import net.minecraft.block.Block;
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
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.world.feature.GlowroseFeature;

import java.util.*;

/**
 * Experimental world generation. Not sure if Forge intends to add something, but this should work
 * for now.
 */
public class GemsWorldFeatures {
    public static void addFeaturesToBiomes() {
        EnumSet<Gems> selected = EnumSet.noneOf(Gems.class);

        for (Biome biome : ForgeRegistries.BIOMES) {
            // TODO: Nether and End just uses multi-gem ore.
            if (biome.getCategory() == Biome.Category.NETHER) {
                addOre(biome, Gems.Set.DARK.getMultiOre(), 8, 12, 25, 95);
            } else if (biome.getCategory() == Biome.Category.THEEND) {
                addOre(biome, Gems.Set.LIGHT.getMultiOre(), 8, 12, 16, 64);
            } else {
                long seed = getBaseSeed()
                        + Objects.requireNonNull(biome.getRegistryName()).toString().hashCode()
                        + biome.getCategory().ordinal() * 100
                        + biome.getPrecipitation().ordinal() * 10
                        + biome.getTempCategory().ordinal();
                SilentGems.LOGGER.info("get biome feature seed {} -> {}", getBaseSeed(), seed);

                Random random = new Random(seed);

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
            }
        }

        Set<Gems> notSelected = EnumSet.complementOf(selected);
        notSelected.removeIf(gem -> gem.getSet() != Gems.Set.CLASSIC);

        if (!notSelected.isEmpty()) {
            SilentGems.LOGGER.warn("Some gems were not selected, adding to random biomes.");
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

    private static void addGemOre(Biome biome, Gems gem, Random random) {
        int size = MathHelper.nextInt(random, 6, 8);
        int count = MathHelper.nextInt(random, 2, 4);
        SilentGems.LOGGER.info("    Biome {}: add gem {} (size {}, count {})", biome.getRegistryName(), gem, size, count);
        addOre(biome, gem.getOre(), size, count, 5, 45);
    }

    private static void addOre(Biome biome, Block block, int size, int count, int minHeight, int maxHeight) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(
                Feature.MINABLE, new MinableConfig(
                        MinableConfig.IS_ROCK,
                        block.getDefaultState(),
                        size
                ),
                Biome.COUNT_RANGE,
                new CountRangeConfig(count, minHeight, 0, maxHeight)
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
}
