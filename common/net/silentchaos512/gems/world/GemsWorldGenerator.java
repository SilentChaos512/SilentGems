package net.silentchaos512.gems.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.silentchaos512.gems.block.GemOre;
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

    int d = world.provider.getDimensionId();

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

    int i, x, y, z, meta, count;
    BlockPos pos;
    Block block;
    IBlockState state;

    // Gem ores.
    block = ModBlocks.gemOre;
    for (i = 0; i < Config.WORLD_GEM_CLUSTER_COUNT; ++i) {
      x = chunkX + random.nextInt(16);
      y = random.nextInt(Config.WORLD_GEM_MAX_HEIGHT);
      z = chunkZ + random.nextInt(16);
      pos = new BlockPos(x, y, z);
      meta = ((WeightedRandomItemSG) WeightedRandom.getRandomItem(random, Config.GEM_WEIGHTS))
          .getMeta();
      EnumGem gem = EnumGem.get(meta);
      state = block.getDefaultState().withProperty(GemOre.VARIANT, gem);
      count = Config.WORLD_GEM_CLUSTER_SIZE;
      new WorldGenMinable(state, count).generate(world, random, pos);
    }

    // Chaos ores. Chance of failure.
    block = ModBlocks.chaosOre;
    for (i = 0; i < Config.WORLD_CHAOS_ORE_CLUSTER_COUNT; ++i) {
      if (random.nextInt(Config.WORLD_CHAOS_ORE_RARITY) == 0) {
        x = chunkX + random.nextInt(16);
        y = random.nextInt(Config.WORLD_CHAOS_ORE_MAX_HEIGHT);
        z = chunkZ + random.nextInt(16);
        pos = new BlockPos(x, y, z);
        state = block.getDefaultState();
        count = Config.WORLD_CHAOS_ORE_CLUSTER_SIZE;
        new WorldGenMinable(state, count).generate(world, random, pos);
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

    int i, x, y, z, meta;
    BlockPos pos;
    IBlockState state;

    // Glow roses
    GlowRose flower = (GlowRose) SRegistry.getBlock(Names.GLOW_ROSE);
    for (i = 0; i < Config.WORLD_FLOWERS_PER_CHUNK; ++i) {
      x = chunkX + random.nextInt(16);
      y = random.nextInt(80) + 40;
      z = chunkZ + random.nextInt(16);
      pos = new BlockPos(x, y, z);

      meta = random.nextInt(EnumGem.values().length);
      EnumGem gem = EnumGem.get(meta);
      state = flower.getDefaultState().withProperty(GlowRose.VARIANT, gem);

      // Find top-most valid block.
      for (; y > 40 && world.isAirBlock(pos.down()); --y) {
        pos = pos.down();
      }

      if (flower.canBlockStay(world, pos, state)) {
        world.setBlockState(pos, state);
      }
    }
  }
}
