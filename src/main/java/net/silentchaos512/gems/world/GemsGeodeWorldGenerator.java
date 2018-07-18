package net.silentchaos512.gems.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.BlockHardenedRock;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.lib.world.WorldGeneratorSL;

import java.util.Random;

public class GemsGeodeWorldGenerator extends WorldGeneratorSL {

  public GemsGeodeWorldGenerator() {

    super(true, SilentGems.MODID + "_geode_retrogen");
  }

  @Override
  protected void generateSurface(World world, Random random, int posX, int posZ) {

    if (random.nextFloat() < GemsConfig.GEODE_DARK_FREQUENCY)
      generateGeode(world, random, posX, posZ, EnumGem.Set.DARK,
          ModBlocks.hardenedRock.getDefaultState().withProperty(BlockHardenedRock.VARIANT,
              BlockHardenedRock.Type.NETHERRACK));
    if (random.nextFloat() < GemsConfig.GEODE_LIGHT_FREQUENCY)
      generateGeode(world, random, posX, posZ, EnumGem.Set.LIGHT,
          ModBlocks.hardenedRock.getDefaultState().withProperty(BlockHardenedRock.VARIANT,
              BlockHardenedRock.Type.END_STONE));
  }

  protected void generateGeode(World world, Random random, int posX, int posZ, EnumGem.Set gemSet,
      IBlockState shellBlock) {

    BlockPos pos = new BlockPos(posX + random.nextInt(16) + 8,
        GemsConfig.GEODE_MIN_Y + random.nextInt(GemsConfig.GEODE_MAX_Y - GemsConfig.GEODE_MIN_Y),
        posZ + random.nextInt(16) + 8);
    new GeodeGenerator(shellBlock, gemSet).generate(world, random, pos);
  }
}
