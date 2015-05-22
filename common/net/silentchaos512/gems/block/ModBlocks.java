package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.item.HoldingGem;
import net.silentchaos512.gems.item.block.ItemBlockSG;
import net.silentchaos512.gems.lib.Names;

public class ModBlocks {

  public static GemOre gemOre;
  public static ChaosOre chaosOre;
  public static GemBlock gemBlock;
  public static GemBrick gemBrickCoated;
  public static GemBrick gemBrickSpeckled;
  public static MiscBlock miscBlock;
  public static GlowRose glowRose;
  public static Teleporter teleporter;
  public static GemLamp gemLamp;
  public static GemLamp gemLampLit;
  public static GemLamp gemLampInvertedLit;
  public static GemLamp gemLampInverted;
  public static FluffyPlantBlock fluffyPlant;
  public static HoldingGemBlock holdingGemBlock;

  public static void init() {

    gemOre = (GemOre) SRegistry.registerBlock(GemOre.class, Names.GEM_ORE);
    chaosOre = (ChaosOre) SRegistry.registerBlock(ChaosOre.class, Names.CHAOS_ORE);
    gemBlock = (GemBlock) SRegistry.registerBlock(GemBlock.class, Names.GEM_BLOCK);
    gemBrickCoated = (GemBrick) SRegistry.registerBlock(GemBrick.class, Names.GEM_BRICK_COATED,
        ItemBlockSG.class, Names.GEM_BRICK_COATED);
    gemBrickSpeckled = (GemBrick) SRegistry.registerBlock(GemBrick.class, Names.GEM_BRICK_SPECKLED,
        ItemBlockSG.class, Names.GEM_BRICK_SPECKLED);
    miscBlock = (MiscBlock) SRegistry.registerBlock(MiscBlock.class, Names.MISC_BLOCKS);
    glowRose = (GlowRose) SRegistry.registerBlock(GlowRose.class, Names.GLOW_ROSE);
    teleporter = (Teleporter) SRegistry.registerBlock(Teleporter.class, Names.TELEPORTER);
    gemLamp = (GemLamp) SRegistry.registerBlock(GemLamp.class, Names.GEM_LAMP, ItemBlockSG.class,
        false, false);
    gemLampLit = (GemLamp) SRegistry.registerBlock(GemLamp.class, Names.GEM_LAMP_LIT,
        ItemBlockSG.class, true, false);
    gemLampInvertedLit = (GemLamp) SRegistry.registerBlock(GemLamp.class, Names.GEM_LAMP_INV_LIT,
        ItemBlockSG.class, true, true);
    gemLampInverted = (GemLamp) SRegistry.registerBlock(GemLamp.class, Names.GEM_LAMP_INV,
        ItemBlockSG.class, false, true);
    fluffyPlant = (FluffyPlantBlock) SRegistry.registerBlock(FluffyPlantBlock.class,
        Names.FLUFFY_PLANT);
    holdingGemBlock = (HoldingGemBlock) SRegistry.registerBlock(HoldingGemBlock.class,
        Names.HOLDING_GEM, HoldingGem.class);
  }
}
