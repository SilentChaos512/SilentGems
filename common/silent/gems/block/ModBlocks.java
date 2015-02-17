package silent.gems.block;

import silent.gems.core.registry.SRegistry;
import silent.gems.item.block.ItemBlockSG;
import silent.gems.lib.Names;

public class ModBlocks {

  public static ChaosEssenceBlock chaosEssenceBlock;
  public static ChaosOre chaosOre;
  public static FluffyPlantBlock fluffyPlant;
  public static GemBlock gemBlock;
  public static GemBrick gemBrickCoated;
  public static GemBrick gemBrickSpeckled;
  public static GemLamp gemLamp;
  public static GemLamp gemLampLit;
  public static GemLamp gemLampInv;
  public static GemLamp gemLampInvLit;
  public static GemOre gemOre;
  public static GlowRose glowRose;
  public static MiscBlock miscBlocks;
  public static Teleporter teleporter;

  public static void init() {

    Class clazz = ItemBlockSG.class;

    gemOre = (GemOre) SRegistry.registerBlock(GemOre.class, Names.GEM_ORE);

    chaosOre = (ChaosOre) SRegistry.registerBlock(ChaosOre.class, Names.CHAOS_ORE);

    gemBlock = (GemBlock) SRegistry.registerBlock(GemBlock.class, Names.GEM_BLOCK);

    gemBrickCoated = (GemBrick) SRegistry.registerBlock(GemBrick.class, Names.GEM_BRICK_COATED,
        clazz, new Object[] { Names.GEM_BRICK_COATED });

    gemBrickSpeckled = (GemBrick) SRegistry.registerBlock(GemBrick.class, Names.GEM_BRICK_SPECKLED,
        clazz, new Object[] { Names.GEM_BRICK_SPECKLED });

    miscBlocks = (MiscBlock) SRegistry.registerBlock(MiscBlock.class, Names.MISC_BLOCKS);

    chaosEssenceBlock = (ChaosEssenceBlock) SRegistry.registerBlock(ChaosEssenceBlock.class,
        Names.CHAOS_ESSENCE_BLOCK);

    glowRose = (GlowRose) SRegistry.registerBlock(GlowRose.class, Names.GLOW_ROSE);

    teleporter = (Teleporter) SRegistry.registerBlock(Teleporter.class, Names.TELEPORTER);

    gemLamp = (GemLamp) SRegistry.registerBlock(GemLamp.class, Names.GEM_LAMP, clazz,
        new Object[] { false, false });

    gemLampLit = (GemLamp) SRegistry.registerBlock(GemLamp.class, Names.GEM_LAMP_LIT, clazz,
        new Object[] { true, false });

    gemLampInvLit = (GemLamp) SRegistry.registerBlock(GemLamp.class, Names.GEM_LAMP_INV_LIT, clazz,
        new Object[] { true, true });

    gemLampInv = (GemLamp) SRegistry.registerBlock(GemLamp.class, Names.GEM_LAMP_INV, clazz,
        new Object[] { false, true });

    fluffyPlant = (FluffyPlantBlock) SRegistry.registerBlock(FluffyPlantBlock.class,
        Names.FLUFFY_PLANT);
  }
}
