package net.silentchaos512.gems.world;

import net.minecraft.block.state.IBlockState;
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
import net.silentchaos512.lib.world.WorldGeneratorSL;

import java.util.Random;

public class GemsWorldGenerator extends WorldGeneratorSL {

  public GemsWorldGenerator() {

    super(true, SilentGems.MODID + "_retrogen");
  }

  @Override
  protected void generateSurface(World world, Random random, int posX, int posZ) {

    generateFlowers(world, random, posX, posZ);
    generateChaosNodes(world, random, posX, posZ);

    // Gems
    generateOres(world, random, posX, posZ, GemsConfig.WORLD_GEN_GEMS);

    // Chaos Ore
    generateOres(world, random, posX, posZ, GemsConfig.WORLD_GEN_CHAOS);
  }

  @Override
  protected void generateNether(World world, Random random, int posX, int posZ) {

    // Dark Gems
    generateOres(world, random, posX, posZ, GemsConfig.WORLD_GEN_GEMS_DARK);
  }

  @Override
  protected void generateEnd(World world, Random random, int posX, int posZ) {

    // Ender Ore
    generateOres(world, random, posX, posZ, GemsConfig.WORLD_GEN_ENDER);

    // Light Gems
    generateOres(world, random, posX, posZ, GemsConfig.WORLD_GEN_GEMS_LIGHT);
  }

  /**
   * Generate the ores for the given config. Uses selectState to choose different gems.
   */
  protected void generateOres(World world, Random random, int posX, int posZ, ConfigOptionOreGen config) {

    final int dimension = world.provider.getDimension();

    if (config.isEnabled() && config.canSpawnInDimension(dimension)) {
      int veinCount = config.getVeinCount(random);
      int veinSize = config.getVeinSize();

      for (int i = 0; i < veinCount; ++i) {
        BlockPos pos = config.getRandomPos(random, posX, posZ);
        IBlockState state = selectState(config, random, world, posX, posZ);
        new WorldGenMinable(state, veinSize, config.getBlockPredicate()).generate(world, random, pos);
      }
    }
  }

  /**
   * Selects an IBlockState for the config. For gems, it will normally choose one randomly each call. For essence ores,
   * it selects the appropriate ore.
   *
   * @param config
   * @param random
   * @return
   */
  protected IBlockState selectState(ConfigOptionOreGen config, Random random, World world,
      int posX, int posZ) {

    if (config == GemsConfig.WORLD_GEN_GEMS) {
      EnumGem gem;
      if (GemsConfig.GEM_REGIONS_ENABLED) {
        // Gem Regions
        long dimension = (long) world.provider.getDimension();
        long cx = (long) posX / 16 / GemsConfig.GEM_REGIONS_SIZE;
        long cz = (long) posZ / 16 / GemsConfig.GEM_REGIONS_SIZE;
        long seed = (world.getSeed() << 40L) | ((dimension & 0xFF) << 32L)
            | ((cz & 0xFFFF) << 16L) | (cx & 0xFFFF);
        Random regionRandom = new Random(seed);
        EnumGem firstGem = EnumGem.values()[regionRandom.nextInt(16)];
        EnumGem secondGem = EnumGem.values()[regionRandom.nextInt(16)];
        if (regionRandom.nextFloat() < GemsConfig.GEM_REGIONS_SECOND_GEM_CHANCE
            && random.nextBoolean()) {
          gem = secondGem;
        } else {
          gem = firstGem;
        }
      } else {
        // Classic logic
        int meta = WeightedRandom.getRandomItem(random, GemsConfig.GEM_WEIGHTS).getMeta();
        gem = EnumGem.values()[meta];
      }
      return ModBlocks.gemOre.getDefaultState().withProperty(EnumGem.VARIANT_GEM, gem);
    }
    if (config == GemsConfig.WORLD_GEN_CHAOS) {
      return ModBlocks.essenceOre.getDefaultState().withProperty(BlockEssenceOre.VARIANT, BlockEssenceOre.Type.CHAOS);
    }
    if (config == GemsConfig.WORLD_GEN_GEMS_DARK) {
      int meta = WeightedRandom.getRandomItem(random, GemsConfig.GEM_WEIGHTS_DARK).getMeta();
      EnumGem gem = EnumGem.values()[meta];
      return ModBlocks.gemOreDark.getDefaultState().withProperty(EnumGem.VARIANT_GEM, gem);
    }
    if (config == GemsConfig.WORLD_GEN_ENDER) {
      return ModBlocks.essenceOre.getDefaultState().withProperty(BlockEssenceOre.VARIANT, BlockEssenceOre.Type.ENDER);
    }
    if (config == GemsConfig.WORLD_GEN_GEMS_LIGHT) {
      int meta = WeightedRandom.getRandomItem(random, GemsConfig.GEM_WEIGHTS_LIGHT).getMeta();
      EnumGem gem = EnumGem.values()[meta];
      return ModBlocks.gemOreLight.getDefaultState().withProperty(EnumGem.VARIANT_GEM, gem);
    }

    SilentGems.logHelper.severe("GemsWorldGenerator - Unknown ore config: " + config.getName());
    return null;
  }

  private void generateFlowers(World world, Random random, int posX, int posZ) {

    if (GemsConfig.GLOW_ROSE_DIMENSION_BLACKLIST.contains(world.provider.getDimension())) return;

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

    if (GemsConfig.CHAOS_NODE_DIMENSION_BLACKLIST.contains(world.provider.getDimension())) return;

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
