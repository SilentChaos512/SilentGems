package net.silentchaos512.gems.block;

import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.block.ItemBlockGemLamp;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.gems.tile.TileChaosFlowerPot;
import net.silentchaos512.gems.tile.TileChaosNode;
import net.silentchaos512.gems.tile.TileChaosPylon;
import net.silentchaos512.gems.tile.TileMaterialGrader;
import net.silentchaos512.gems.tile.TilePhantomLight;
import net.silentchaos512.gems.tile.TileTeleporter;
import net.silentchaos512.lib.registry.SRegistry;

public class ModBlocks {

  public static final BlockGemOre gemOre = new BlockGemOre(false);
  public static final BlockGemOre gemOreDark = new BlockGemOre(true);
  public static final BlockGem gemBlock = new BlockGem(false, false);
  public static final BlockGem gemBlockDark = new BlockGem(true, false);
  public static final BlockGem gemBlockSuper = new BlockGem(false, true);
  public static final BlockGem gemBlockSuperDark = new BlockGem(true, true);
  public static final BlockGemBrick gemBrickCoated = new BlockGemBrick(false, true);
  public static final BlockGemBrick gemBrickCoatedDark = new BlockGemBrick(true, true);
  public static final BlockGemBrick gemBrickSpeckled = new BlockGemBrick(false, false);
  public static final BlockGemBrick gemBrickSpeckledDark = new BlockGemBrick(true, false);
  public static final BlockGemLamp gemLamp = new BlockGemLamp(false, false, false);
  public static final BlockGemLamp gemLampLit = new BlockGemLamp(false, true, false);
  public static final BlockGemLamp gemLampLitInverted = new BlockGemLamp(false, true, true);
  public static final BlockGemLamp gemLampInverted = new BlockGemLamp(false, false, true);
  public static final BlockGemLamp gemLampDark = new BlockGemLamp(true, false, false);
  public static final BlockGemLamp gemLampLitDark = new BlockGemLamp(true, true, false);
  public static final BlockGemLamp gemLampLitInvertedDark = new BlockGemLamp(true, true, true);
  public static final BlockGemLamp gemLampInvertedDark = new BlockGemLamp(true, false, true);
  public static final BlockTeleporterAnchor teleporterAnchor = new BlockTeleporterAnchor();
  public static final BlockTeleporter teleporter = new BlockTeleporter(false, false);
  public static final BlockTeleporter teleporterDark = new BlockTeleporter(true, false);
  public static final BlockTeleporterRedstone teleporterRedstone = new BlockTeleporterRedstone(false);
  public static final BlockTeleporterRedstone teleporterRedstoneDark = new BlockTeleporterRedstone(true);
  public static final BlockGlowRose glowRose = new BlockGlowRose();
  public static final BlockEssenceOre essenceOre = new BlockEssenceOre();
  public static final BlockMisc miscBlock = new BlockMisc();
  public static final BlockFluffyBlock fluffyBlock = new BlockFluffyBlock();
  public static final BlockFluffyPuffPlant fluffyPuffPlant = new BlockFluffyPuffPlant();
  public static final BlockChaosFlowerPot chaosFlowerPot = new BlockChaosFlowerPot();
  public static final BlockMaterialGrader materialGrader = new BlockMaterialGrader();
  public static final BlockChaosNode chaosNode = new BlockChaosNode();
  public static final BlockChaosAltar chaosAltar = new BlockChaosAltar();
  public static final BlockChaosPylon chaosPylon = new BlockChaosPylon();
  public static final BlockPhantomLight phantomLight = new BlockPhantomLight();

  public static void init() {

    SRegistry reg = SilentGems.instance.registry;
    reg.registerBlock(gemOre);
    reg.registerBlock(gemOreDark);
    reg.registerBlock(gemBlock);
    reg.registerBlock(gemBlockDark);
    reg.registerBlock(gemBlockSuper);
    reg.registerBlock(gemBlockSuperDark);
    reg.registerBlock(gemBrickCoated);
    reg.registerBlock(gemBrickCoatedDark);
    reg.registerBlock(gemBrickSpeckled);
    reg.registerBlock(gemBrickSpeckledDark);
    reg.registerBlock(gemLamp, new ItemBlockGemLamp(gemLamp));
    reg.registerBlock(gemLampLit, new ItemBlockGemLamp(gemLampLit));
    reg.registerBlock(gemLampLitInverted, new ItemBlockGemLamp(gemLampLitInverted));
    reg.registerBlock(gemLampInverted, new ItemBlockGemLamp(gemLampInverted));
    reg.registerBlock(gemLampDark, new ItemBlockGemLamp(gemLampDark));
    reg.registerBlock(gemLampLitDark, new ItemBlockGemLamp(gemLampLitDark));
    reg.registerBlock(gemLampLitInvertedDark, new ItemBlockGemLamp(gemLampLitInvertedDark));
    reg.registerBlock(gemLampInvertedDark, new ItemBlockGemLamp(gemLampInvertedDark));
    reg.registerBlock(teleporterAnchor);
    reg.registerBlock(teleporter);
    reg.registerBlock(teleporterDark);
    reg.registerBlock(teleporterRedstone);
    reg.registerBlock(teleporterRedstoneDark);
    reg.registerBlock(glowRose, Names.GLOW_ROSE);
    reg.registerBlock(essenceOre);
    reg.registerBlock(miscBlock);
    reg.registerBlock(fluffyBlock);
    reg.registerBlock(fluffyPuffPlant, Names.FLUFFY_PUFF_PLANT);
    reg.registerBlock(chaosFlowerPot, Names.CHAOS_FLOWER_POT);
    reg.registerBlock(materialGrader);
    reg.registerBlock(chaosNode);
    reg.registerBlock(chaosAltar);
    reg.registerBlock(chaosPylon, Names.CHAOS_PYLON);
    reg.registerBlock(phantomLight);

    reg.registerTileEntity(TileTeleporter.class, Names.TELEPORTER);
    reg.registerTileEntity(TileChaosFlowerPot.class, Names.CHAOS_FLOWER_POT);
    reg.registerTileEntity(TileMaterialGrader.class, Names.MATERIAL_GRADER);
    reg.registerTileEntity(TileChaosNode.class, Names.CHAOS_NODE);
    reg.registerTileEntity(TileChaosAltar.class, Names.CHAOS_ALTAR);
    reg.registerTileEntity(TileChaosPylon.class, Names.CHAOS_PYLON);
    reg.registerTileEntity(TilePhantomLight.class, Names.PHANTOM_LIGHT);
  }
}
