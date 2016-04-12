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

  public static BlockGemOre gemOre = new BlockGemOre(false);
  public static BlockGemOre gemOreDark = new BlockGemOre(true);
  public static BlockGem gemBlock = new BlockGem(false, false);
  public static BlockGem gemBlockDark = new BlockGem(true, false);
  public static BlockGem gemBlockSuper = new BlockGem(false, true);
  public static BlockGem gemBlockSuperDark = new BlockGem(true, true);
  public static BlockGemBrick gemBrickCoated = new BlockGemBrick(false, true);
  public static BlockGemBrick gemBrickCoatedDark = new BlockGemBrick(true, true);
  public static BlockGemBrick gemBrickSpeckled = new BlockGemBrick(false, false);
  public static BlockGemBrick gemBrickSpeckledDark = new BlockGemBrick(true, false);
  public static BlockTeleporter gemTeleporter = new BlockTeleporter(false, false);
  public static BlockTeleporter gemTeleporterDark = new BlockTeleporter(true, false);
  public static BlockEssenceOre essenceOre = new BlockEssenceOre();
  public static BlockGlowRose glowRose = new BlockGlowRose();
  public static BlockFluffyBlock fluffyBlock = new BlockFluffyBlock();
  public static BlockFluffyPuffPlant fluffyPuffPlant = new BlockFluffyPuffPlant();
  public static BlockMaterialGrader materialGrader = new BlockMaterialGrader();
  public static BlockChaosNode chaosNode = new BlockChaosNode();
  public static BlockSL chaosAltar = new BlockSL(1, SilentGems.MOD_ID, Names.CHAOS_ALTAR,
      Material.iron); // TODO
  public static BlockChaosPylon chaosPylon = new BlockChaosPylon();

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
    reg.registerBlock(gemTeleporter);
    reg.registerBlock(gemTeleporterDark);
    reg.registerTileEntity(TileTeleporter.class, Names.TELEPORTER);
    reg.registerBlock(essenceOre);
    reg.registerBlock(glowRose, Names.GLOW_ROSE);
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
