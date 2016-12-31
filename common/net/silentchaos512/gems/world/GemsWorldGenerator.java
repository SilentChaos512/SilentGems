package net.silentchaos512.gems.world;

import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.silentchaos512.gems.block.BlockEssenceOre;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.config.ConfigOptionOreGen;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.util.WeightedRandomItemSG;

public class GemsWorldGenerator implements IWorldGenerator {

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world,
      IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

    switch (world.provider.getDimension()) {
      case 0:
        generateSurface(world, random, chunkX * 16, chunkZ * 16);
        break;
      case -1:
        generateNether(world, random, chunkX * 16, chunkZ * 16);
        break;
      case 1:
        generateEnd(world, random, chunkX * 16, chunkZ * 16);
        break;
      default:
        generateSurface(world, random, chunkX * 16, chunkZ * 16);
    }
  }

  private void generateSurface(World world, Random random, int posX, int posZ) {

    int i, x, y, z, meta, veinCount, veinSize;
    BlockPos pos;
    Block block;
    IBlockState state;
    ConfigOptionOreGen config;

    generateFlowers(world, random, posX, posZ);
    generateChaosNodes(world, random, posX, posZ);

    // Gems
    config = GemsConfig.WORLD_GEN_GEMS;
    block = ModBlocks.gemOre;
    veinCount = config.getVeinCount(random);
    veinSize = config.veinSize;
    for (i = 0; i < veinCount; ++i) {
      x = posX + random.nextInt(16);
      y = random.nextInt(config.maxY - config.minY) + config.minY;
      z = posZ + random.nextInt(16);
      pos = new BlockPos(x, y, z);
      // GemTest.instance.logHelper.debug(pos);
      meta = ((WeightedRandomItemSG) WeightedRandom.getRandomItem(random, GemsConfig.GEM_WEIGHTS))
          .getMeta();
      EnumGem gem = EnumGem.values()[meta];
      state = block.getDefaultState().withProperty(EnumGem.VARIANT_GEM, gem);
      new WorldGenMinable(state, veinSize).generate(world, random, pos);
    }

    // Chaos Ore
    config = GemsConfig.WORLD_GEN_CHAOS;
    block = ModBlocks.essenceOre;
    veinCount = config.getVeinCount(random);
    veinSize = config.veinSize;
    for (i = 0; i < veinCount; ++i) {
      x = posX + random.nextInt(16);
      y = random.nextInt(config.maxY - config.minY) + config.minY;
      z = posZ + random.nextInt(16);
      pos = new BlockPos(x, y, z);
      state = block.getDefaultState().withProperty(BlockEssenceOre.VARIANT,
          BlockEssenceOre.Type.CHAOS);
      new WorldGenMinable(state, veinSize).generate(world, random, pos);
    }
  }

  private void generateNether(World world, Random random, int posX, int posZ) {

    int i, x, y, z, meta, veinCount, veinSize;
    BlockPos pos;
    Block block;
    IBlockState state;
    ConfigOptionOreGen config;
    Predicate predicate = BlockMatcher.forBlock(Blocks.NETHERRACK);

    // Dark Gems
    config = GemsConfig.WORLD_GEN_GEMS_DARK;
    block = ModBlocks.gemOreDark;
    veinCount = config.getVeinCount(random);
    veinSize = config.veinSize;
    for (i = 0; i < config.veinCount; ++i) {
      x = posX + random.nextInt(16);
      y = random.nextInt(config.maxY - config.minY) + config.minY;
      z = posZ + random.nextInt(16);
      pos = new BlockPos(x, y, z);
      // GemTest.instance.logHelper.debug(pos);
      meta = ((WeightedRandomItemSG) WeightedRandom.getRandomItem(random, GemsConfig.GEM_WEIGHTS))
          .getMeta();
      EnumGem gem = EnumGem.values()[meta];
      state = block.getDefaultState().withProperty(EnumGem.VARIANT_GEM, gem);
      new WorldGenMinable(state, veinSize, predicate).generate(world, random, pos);
    }
  }

  private void generateEnd(World world, Random random, int posX, int posZ) {

    int i, x, y, z, meta, veinCount, veinSize;
    BlockPos pos;
    Block block;
    IBlockState state;
    ConfigOptionOreGen config;
    Predicate predicate = BlockMatcher.forBlock(Blocks.END_STONE);

    // Ender Ore
    config = GemsConfig.WORLD_GEN_ENDER;
    block = ModBlocks.essenceOre;
    veinCount = config.getVeinCount(random);
    veinSize = config.veinSize;
    for (i = 0; i < config.veinCount; ++i) {
      x = posX + random.nextInt(16);
      y = random.nextInt(config.maxY - config.minY) + config.minY;
      z = posZ + random.nextInt(16);
      pos = new BlockPos(x, y, z);
      state = block.getDefaultState().withProperty(BlockEssenceOre.VARIANT,
          BlockEssenceOre.Type.ENDER);
      new WorldGenMinable(state, veinSize, predicate).generate(world, random, pos);
    }
  }

  private void generateFlowers(World world, Random random, int posX, int posZ) {

    int i, x, y, z, meta;
    IBlockState state;
    BlockPos pos;

    for (i = 0; i < GemsConfig.GLOW_ROSE_PER_CHUNK; ++i) {
      x = posX + random.nextInt(16);
      y = random.nextInt(120) + 50;
      z = posZ + random.nextInt(16);
      pos = new BlockPos(x, y, z);

      meta = random.nextInt(16);
      EnumGem gem = EnumGem.values()[meta];
      state = ModBlocks.glowRose.getDefaultState().withProperty(EnumGem.VARIANT_GEM, gem);

      // Find top-most valid block
      for (; y > 50; --y) {
        if (world.isAirBlock(pos) && (!world.provider.hasNoSky() || pos.getY() < 255)
            && ModBlocks.glowRose.canBlockStay(world, pos, state)) {
          world.setBlockState(pos, state, 2);
          break;
        }
        pos = pos.down();
      }
    }
  }

  private void generateChaosNodes(World world, Random random, int posX, int posZ) {

    int i, x, y, z;
    IBlockState state;
    BlockPos pos;

    int count = (int) GemsConfig.CHAOS_NODES_PER_CHUNK;
    float diff = GemsConfig.CHAOS_NODES_PER_CHUNK - count;
    count += random.nextFloat() < diff ? 1 : 0;

    for (i = 0; i < count; ++i) {
      x = posX + random.nextInt(16);
      y = random.nextInt(120) + 120;
      z = posZ + random.nextInt(16);
      pos = new BlockPos(x, y, z);
      state = ModBlocks.chaosNode.getDefaultState();

      // Find top-most solid block
      for (; y > 50 && world.isAirBlock(pos.down()); --y) {
        pos = pos.down();
      }

      // Move up a couple blocks.
      pos = pos.up(random.nextInt(3) + 2);

      // Spawn if possible.
      if (world.isAirBlock(pos)) {
        world.setBlockState(pos, state, 2);
      }
    }
  }
}
