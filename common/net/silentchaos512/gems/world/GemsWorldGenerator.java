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
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.BlockEssenceOre;
import net.silentchaos512.gems.config.ConfigOptionOreGen;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.util.WeightedRandomItemSG;
import net.silentchaos512.lib.world.WorldGeneratorSL;

public class GemsWorldGenerator extends WorldGeneratorSL {

  public GemsWorldGenerator() {

    super(true, SilentGems.MODID + "_retrogen");
  }

  @Override
  protected void generateSurface(World world, Random random, int posX, int posZ) {

    int i, meta, veinCount, veinSize;
    BlockPos pos;
    Block block;
    IBlockState state;
    ConfigOptionOreGen config;

    generateFlowers(world, random, posX, posZ);
    generateChaosNodes(world, random, posX, posZ);

    // Gems
    config = GemsConfig.WORLD_GEN_GEMS;
    if (config.isEnabled()) {
      block = ModBlocks.gemOre;
      veinCount = config.getVeinCount(random);
      veinSize = config.veinSize;
      SilentGems.logHelper.debug(veinCount);
      for (i = 0; i < veinCount; ++i) {
        pos = config.getRandomPos(random, posX, posZ);
        meta = ((WeightedRandomItemSG) WeightedRandom.getRandomItem(random, GemsConfig.GEM_WEIGHTS))
            .getMeta();
        EnumGem gem = EnumGem.values()[meta];
        state = block.getDefaultState().withProperty(EnumGem.VARIANT_GEM, gem);
        new WorldGenMinable(state, veinSize).generate(world, random, pos);
      }
    }

    // Chaos Ore
    config = GemsConfig.WORLD_GEN_CHAOS;
    if (config.isEnabled()) {
      block = ModBlocks.essenceOre;
      veinCount = config.getVeinCount(random);
      veinSize = config.veinSize;
      for (i = 0; i < veinCount; ++i) {
        pos = config.getRandomPos(random, posX, posZ);
        state = block.getDefaultState().withProperty(BlockEssenceOre.VARIANT,
            BlockEssenceOre.Type.CHAOS);
        new WorldGenMinable(state, veinSize).generate(world, random, pos);
      }
    }
  }

  @Override
  protected void generateNether(World world, Random random, int posX, int posZ) {

    int i, meta, veinCount, veinSize;
    BlockPos pos;
    Block block;
    IBlockState state;
    ConfigOptionOreGen config;
    Predicate predicate = BlockMatcher.forBlock(Blocks.NETHERRACK);

    // Dark Gems
    config = GemsConfig.WORLD_GEN_GEMS_DARK;
    if (config.isEnabled()) {
      block = ModBlocks.gemOreDark;
      veinCount = config.getVeinCount(random);
      veinSize = config.veinSize;
      for (i = 0; i < veinCount; ++i) {
        pos = config.getRandomPos(random, posX, posZ);
        meta = ((WeightedRandomItemSG) WeightedRandom.getRandomItem(random, GemsConfig.GEM_WEIGHTS))
            .getMeta();
        EnumGem gem = EnumGem.values()[meta];
        state = block.getDefaultState().withProperty(EnumGem.VARIANT_GEM, gem);
        new WorldGenMinable(state, veinSize, predicate).generate(world, random, pos);
      }
    }
  }

  @Override
  protected void generateEnd(World world, Random random, int posX, int posZ) {

    int i, meta, veinCount, veinSize;
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
    for (i = 0; i < veinCount; ++i) {
      pos = config.getRandomPos(random, posX, posZ);
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
      x = posX + 8 + random.nextInt(16);
      y = random.nextInt(120) + 50;
      z = posZ + 8 + random.nextInt(16);
      pos = new BlockPos(x, y, z);

      meta = random.nextInt(16);
      EnumGem gem = EnumGem.values()[meta];
      state = ModBlocks.glowRose.getDefaultState().withProperty(EnumGem.VARIANT_GEM, gem);

      // Find top-most valid block
      for (; y > 50; --y) {
        if (world.isAirBlock(pos) && pos.getY() < 255
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
      x = posX + 8 + random.nextInt(16);
      y = random.nextInt(120) + 120;
      z = posZ + 8 + random.nextInt(16);
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
