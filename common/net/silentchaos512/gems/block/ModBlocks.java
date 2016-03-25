package net.silentchaos512.gems.block;

import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.SRegistry;

public class ModBlocks {

  public static BlockGemOre gemOre, gemOreDark;
  public static BlockGem gemBlock, gemBlockDark;
  public static BlockEssenceOre essenceOre;
  public static BlockGlowRose glowRose;
  public static BlockFluffyBlock fluffyBlock;
  public static BlockFluffyPuffPlant fluffyPuffPlant;
  public static BlockChaosNode chaosNode;

  public static void init() {

    SRegistry reg = SilentGems.instance.registry;
    gemOre = (BlockGemOre) reg.registerBlock(new BlockGemOre(false));
    gemOreDark = (BlockGemOre) reg.registerBlock(new BlockGemOre(true));
    gemBlock = (BlockGem) reg.registerBlock(new BlockGem(false));
    gemBlockDark = (BlockGem) reg.registerBlock(new BlockGem(true));
    essenceOre = (BlockEssenceOre) reg.registerBlock(new BlockEssenceOre());
    glowRose = (BlockGlowRose) reg.registerBlock(new BlockGlowRose(), Names.GLOW_ROSE);
    fluffyBlock = (BlockFluffyBlock) reg.registerBlock(new BlockFluffyBlock(), Names.FLUFFY_BLOCK);
    fluffyPuffPlant = (BlockFluffyPuffPlant) reg.registerBlock(new BlockFluffyPuffPlant(),
        Names.FLUFFY_PUFF_PLANT);
    chaosNode = (BlockChaosNode) reg.registerBlock(new BlockChaosNode());
  }
}
