package net.silentchaos512.gems.world;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.silentchaos512.gems.block.GlowRose;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.WeightedRandomItemSG;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

public class GemsWorldGenerator implements IWorldGenerator {

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world,
      IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

    int d = world.provider.dimensionId;

    if (d == 0) {
      generateSurface(world, random, chunkX * 16, chunkZ * 16);
    } else if (d == -1) {
      generateNether();
    } else if (d == 1) {
      generateEnd();
    } else {
      generateSurface(world, random, chunkX * 16, chunkZ * 16);
    }
  }

  private void generateSurface(World world, Random random, int chunkX, int chunkZ) {

    int i, x, y, z, meta;

    // Gem ores.
    for (i = 0; i < Config.WORLD_GEM_CLUSTER_COUNT; ++i) {
      x = chunkX + random.nextInt(16);
      y = random.nextInt(Config.WORLD_GEM_MAX_HEIGHT);
      z = chunkZ + random.nextInt(16);
      // WeightedRandomItemSG w = (WeightedRandomItemSG) WeightedRandom.getRandomItem(random, Config.GEM_WEIGHTS);
      meta = ((WeightedRandomItemSG) WeightedRandom.getRandomItem(random, Config.GEM_WEIGHTS))
          .getMeta();
      new WorldGenMinable(ModBlocks.gemOre, meta, Config.WORLD_GEM_CLUSTER_SIZE, Blocks.stone)
          .generate(world, random, x, y, z);
    }

    // Chaos ores. Chance of failure.
    for (i = 0; i < Config.WORLD_CHAOS_ORE_CLUSTER_COUNT; ++i) {
      if (random.nextInt(Config.WORLD_CHAOS_ORE_RARITY) == 0) {
        x = chunkX + random.nextInt(16);
        y = random.nextInt(Config.WORLD_CHAOS_ORE_MAX_HEIGHT);
        z = chunkZ + random.nextInt(16);
        new WorldGenMinable(ModBlocks.chaosOre, 0, Config.WORLD_CHAOS_ORE_CLUSTER_SIZE,
            Blocks.stone).generate(world, random, x, y, z);
      }
    }

    generateFlowers(world, random, chunkX, chunkZ);
  }

  private void generateNether() {

    // TODO
  }

  private void generateEnd() {

    // TODO
  }

  private void generateFlowers(World world, Random random, int chunkX, int chunkZ) {

    int i, x, y, z, m;

    // Glow roses
    GlowRose flower = (GlowRose) SRegistry.getBlock(Names.GLOW_ROSE);
    for (i = 0; i < Config.WORLD_FLOWERS_PER_CHUNK; ++i) {
      x = chunkX + random.nextInt(16);
      y = random.nextInt(80) + 40;
      z = chunkZ + random.nextInt(16);
      m = random.nextInt(EnumGem.all().length);

      // Find top-most valid block.
      for (; y > 40 && world.isAirBlock(x, y - 1, z); --y)
        ;

      if (flower.canBlockStay(world, x, y, z)) {
        world.setBlock(x, y, z, flower, m, 2);
      }
    }
  }
}
