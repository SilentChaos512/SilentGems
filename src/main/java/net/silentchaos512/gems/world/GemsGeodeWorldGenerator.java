package net.silentchaos512.gems.world;

public class GemsGeodeWorldGenerator /*extends WorldGeneratorSL*/ {

//  public GemsGeodeWorldGenerator() {
//
//    super(true, SilentGems.MOD_ID + "_geode_retrogen");
//  }
//
//  @Override
//  protected void generateSurface(World world, Random random, int posX, int posZ) {
//
//    if (random.nextFloat() < GemsConfig.GEODE_DARK_FREQUENCY)
//      generateGeode(world, random, posX, posZ, Gems.Set.DARK,
//          ModBlocks.hardenedRock.getDefaultState().withProperty(HardenedRock.VARIANT,
//              HardenedRock.Type.NETHERRACK));
//    if (random.nextFloat() < GemsConfig.GEODE_LIGHT_FREQUENCY)
//      generateGeode(world, random, posX, posZ, Gems.Set.LIGHT,
//          ModBlocks.hardenedRock.getDefaultState().withProperty(HardenedRock.VARIANT,
//              HardenedRock.Type.END_STONE));
//  }
//
//  protected void generateGeode(World world, Random random, int posX, int posZ, Gems.Set gemSet,
//      IBlockState shellBlock) {
//
//    BlockPos pos = new BlockPos(posX + random.nextInt(16) + 8,
//        GemsConfig.GEODE_MIN_Y + random.nextInt(GemsConfig.GEODE_MAX_Y - GemsConfig.GEODE_MIN_Y),
//        posZ + random.nextInt(16) + 8);
//    new GeodeGenerator(shellBlock, gemSet).generate(world, random, pos);
//  }
}
