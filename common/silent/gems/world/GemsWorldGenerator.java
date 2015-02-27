package silent.gems.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import silent.gems.block.GemOre;
import silent.gems.block.GlowRose;
import silent.gems.block.ModBlocks;
import silent.gems.configuration.Config;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LogHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;

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

    int i, x, y, z, count;
    BlockPos pos;
    Block block;
    IBlockState state;

    // Gem ores.
    block = ModBlocks.gemOre;
    for (i = 0; i < Config.WORLD_GEM_CLUSTER_COUNT.value; ++i) {
      x = chunkX + random.nextInt(16);
      y = random.nextInt(Config.WORLD_GEM_MAX_HEIGHT.value);
      z = chunkZ + random.nextInt(16);
      pos = new BlockPos(x, y, z);
      int meta = random.nextInt(EnumGem.count());
      EnumGem gemType = EnumGem.byId(meta);
      state = block.getDefaultState().withProperty(GemOre.VARIANT, gemType);
      count = Config.WORLD_GEM_CLUSTER_SIZE.value;
      new WorldGenMinable(state, count).generate(world, random, pos);
    }

    // Chaos ores. Chance of failure.
    block = ModBlocks.chaosOre;
    for (i = 0; i < Config.WORLD_CHAOS_ORE_CLUSTER_COUNT.value; ++i) {
      if (random.nextInt(Config.WORLD_CHAOS_ORE_RARITY.value) == 0) {
        x = chunkX + random.nextInt(16);
        y = random.nextInt(Config.WORLD_CHAOS_ORE_MAX_HEIGHT.value);
        z = chunkZ + random.nextInt(16);
        pos = new BlockPos(x, y, z);
        state = block.getDefaultState();
        count = Config.WORLD_CHAOS_ORE_CLUSTER_SIZE.value;
        new WorldGenMinable(state, count).generate(world, random, pos);
      }
    }

    generateFlowers(world, random, chunkX, chunkZ);
  }

  private void generateNether() {

  }

  private void generateEnd() {

  }

  private void generateFlowers(World world, Random random, int chunkX, int chunkZ) {

    // Glow roses
    GlowRose flower = ModBlocks.glowRose;
    for (int i = 0; i < Config.WORLD_FLOWERS_PER_CHUNK.value; ++i) {
      int x = chunkX + random.nextInt(16);
      int y = random.nextInt(80) + 40;
      int z = chunkZ + random.nextInt(16);
      BlockPos pos = new BlockPos(x, y, z);
      
      int meta = random.nextInt(EnumGem.count());
      EnumGem gemType = EnumGem.byId(meta);
      IBlockState state = flower.getDefaultState().withProperty(GlowRose.VARIANT, gemType);

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
