package net.silentchaos512.gems.block;

import net.minecraft.block.material.Material;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosNode;
import net.silentchaos512.gems.tile.TileChaosPylon;
import net.silentchaos512.gems.tile.TileMaterialGrader;
import net.silentchaos512.gems.tile.TileTeleporter;
import net.silentchaos512.lib.block.BlockSL;
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
  public static final BlockMaterialGrader materialGrader = new BlockMaterialGrader();
  public static final BlockChaosNode chaosNode = new BlockChaosNode();
  public static final BlockSL chaosAltar = new BlockSL(1, SilentGems.MOD_ID, Names.CHAOS_ALTAR,
      Material.IRON); // TODO
  public static final BlockChaosPylon chaosPylon = new BlockChaosPylon();

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
    reg.registerBlock(teleporterAnchor);
    reg.registerBlock(teleporter);
    reg.registerBlock(teleporterDark);
    reg.registerBlock(teleporterRedstone);
    reg.registerBlock(teleporterRedstoneDark);
    reg.registerTileEntity(TileTeleporter.class, Names.TELEPORTER);
    reg.registerBlock(glowRose, Names.GLOW_ROSE);
    reg.registerBlock(essenceOre);
    reg.registerBlock(miscBlock);
    reg.registerBlock(fluffyBlock);
    reg.registerBlock(fluffyPuffPlant, Names.FLUFFY_PUFF_PLANT);
    reg.registerBlock(materialGrader);
    reg.registerTileEntity(TileMaterialGrader.class, Names.MATERIAL_GRADER);
    reg.registerBlock(chaosNode);
    reg.registerTileEntity(TileChaosNode.class, Names.CHAOS_NODE);
    reg.registerBlock(chaosAltar);
    reg.registerBlock(chaosPylon, Names.CHAOS_PYLON);
    reg.registerTileEntity(TileChaosPylon.class, Names.CHAOS_PYLON);
  }
}
