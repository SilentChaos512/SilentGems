package net.silentchaos512.gems.init;

import static net.silentchaos512.gems.lib.EnumGem.Set.CLASSIC;
import static net.silentchaos512.gems.lib.EnumGem.Set.DARK;
import static net.silentchaos512.gems.lib.EnumGem.Set.LIGHT;

import net.minecraft.block.Block;
import net.silentchaos512.gems.block.BlockChaosAltar;
import net.silentchaos512.gems.block.BlockChaosFlowerPot;
import net.silentchaos512.gems.block.BlockChaosNode;
import net.silentchaos512.gems.block.BlockChaosPylon;
import net.silentchaos512.gems.block.BlockEssenceOre;
import net.silentchaos512.gems.block.BlockFluffyBlock;
import net.silentchaos512.gems.block.BlockFluffyPuffPlant;
import net.silentchaos512.gems.block.BlockGem;
import net.silentchaos512.gems.block.BlockGemBrick;
import net.silentchaos512.gems.block.BlockGemGlass;
import net.silentchaos512.gems.block.BlockGemLamp;
import net.silentchaos512.gems.block.BlockGemOre;
import net.silentchaos512.gems.block.BlockGlowRose;
import net.silentchaos512.gems.block.BlockMaterialGrader;
import net.silentchaos512.gems.block.BlockMisc;
import net.silentchaos512.gems.block.BlockPhantomLight;
import net.silentchaos512.gems.block.BlockTeleporter;
import net.silentchaos512.gems.block.BlockTeleporterAnchor;
import net.silentchaos512.gems.block.BlockTeleporterRedstone;
import net.silentchaos512.gems.item.block.ItemBlockGemLamp;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.gems.tile.TileChaosFlowerPot;
import net.silentchaos512.gems.tile.TileChaosNode;
import net.silentchaos512.gems.tile.TileChaosPylon;
import net.silentchaos512.gems.tile.TileMaterialGrader;
import net.silentchaos512.gems.tile.TilePhantomLight;
import net.silentchaos512.gems.tile.TileTeleporter;
import net.silentchaos512.lib.registry.IRegistrationHandler;
import net.silentchaos512.lib.registry.SRegistry;

public class ModBlocks implements IRegistrationHandler<Block> {

  public static final BlockGemOre gemOre = new BlockGemOre(CLASSIC);
  public static final BlockGemOre gemOreDark = new BlockGemOre(DARK);
  public static final BlockGemOre gemOreLight = new BlockGemOre(LIGHT);

  public static final BlockGem gemBlock = new BlockGem(CLASSIC, false);
  public static final BlockGem gemBlockDark = new BlockGem(DARK, false);
  public static final BlockGem gemBlockLight = new BlockGem(LIGHT, false);
  public static final BlockGem gemBlockSuper = new BlockGem(CLASSIC, true);
  public static final BlockGem gemBlockSuperDark = new BlockGem(DARK, true);
  public static final BlockGem gemBlockSuperLight = new BlockGem(LIGHT, true);

  public static final BlockGemBrick gemBrickCoated = new BlockGemBrick(CLASSIC, true);
  public static final BlockGemBrick gemBrickCoatedDark = new BlockGemBrick(DARK, true);
  public static final BlockGemBrick gemBrickCoatedLight = new BlockGemBrick(LIGHT, true);
  public static final BlockGemBrick gemBrickSpeckled = new BlockGemBrick(CLASSIC, false);
  public static final BlockGemBrick gemBrickSpeckledDark = new BlockGemBrick(DARK, false);
  public static final BlockGemBrick gemBrickSpeckledLight = new BlockGemBrick(LIGHT, false);

  public static final BlockGemLamp gemLamp = new BlockGemLamp(CLASSIC, false, false);
  public static final BlockGemLamp gemLampLit = new BlockGemLamp(CLASSIC, true, false);
  public static final BlockGemLamp gemLampLitInverted = new BlockGemLamp(CLASSIC, true, true);
  public static final BlockGemLamp gemLampInverted = new BlockGemLamp(CLASSIC, false, true);
  public static final BlockGemLamp gemLampDark = new BlockGemLamp(DARK, false, false);
  public static final BlockGemLamp gemLampLitDark = new BlockGemLamp(DARK, true, false);
  public static final BlockGemLamp gemLampLitInvertedDark = new BlockGemLamp(DARK, true, true);
  public static final BlockGemLamp gemLampInvertedDark = new BlockGemLamp(DARK, false, true);
  public static final BlockGemLamp gemLampLight = new BlockGemLamp(LIGHT, false, false);
  public static final BlockGemLamp gemLampLitLight = new BlockGemLamp(LIGHT, true, false);
  public static final BlockGemLamp gemLampLitInvertedLight = new BlockGemLamp(LIGHT, true, true);
  public static final BlockGemLamp gemLampInvertedLight = new BlockGemLamp(LIGHT, false, true);

  public static final BlockGemGlass gemGlass = new BlockGemGlass(CLASSIC);
  public static final BlockGemGlass gemGlassDark = new BlockGemGlass(DARK);
  public static final BlockGemGlass gemGlassLight = new BlockGemGlass(LIGHT);

  public static final BlockTeleporterAnchor teleporterAnchor = new BlockTeleporterAnchor();
  public static final BlockTeleporter teleporter = new BlockTeleporter(CLASSIC, false);
  public static final BlockTeleporter teleporterDark = new BlockTeleporter(DARK, false);
  public static final BlockTeleporter teleporterLight = new BlockTeleporter(LIGHT, false);
  public static final BlockTeleporterRedstone teleporterRedstone = new BlockTeleporterRedstone(
      CLASSIC);
  public static final BlockTeleporterRedstone teleporterRedstoneDark = new BlockTeleporterRedstone(
      DARK);
  public static final BlockTeleporterRedstone teleporterRedstoneLight = new BlockTeleporterRedstone(
      LIGHT);

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

  @Override
  public void registerAll(SRegistry reg) {

    reg.registerBlock(gemOre);
    reg.registerBlock(gemOreDark);
    reg.registerBlock(gemOreLight);

    reg.registerBlock(gemBlock);
    reg.registerBlock(gemBlockDark);
    reg.registerBlock(gemBlockLight);
    reg.registerBlock(gemBlockSuper);
    reg.registerBlock(gemBlockSuperDark);
    reg.registerBlock(gemBlockSuperLight);

    reg.registerBlock(gemBrickCoated);
    reg.registerBlock(gemBrickCoatedDark);
    reg.registerBlock(gemBrickCoatedLight);
    reg.registerBlock(gemBrickSpeckled);
    reg.registerBlock(gemBrickSpeckledDark);
    reg.registerBlock(gemBrickSpeckledLight);

    reg.registerBlock(gemLamp, new ItemBlockGemLamp(gemLamp));
    reg.registerBlock(gemLampLit, new ItemBlockGemLamp(gemLampLit)).setCreativeTab(null);
    reg.registerBlock(gemLampLitInverted, new ItemBlockGemLamp(gemLampLitInverted));
    reg.registerBlock(gemLampInverted, new ItemBlockGemLamp(gemLampInverted)).setCreativeTab(null);
    reg.registerBlock(gemLampDark, new ItemBlockGemLamp(gemLampDark));
    reg.registerBlock(gemLampLitDark, new ItemBlockGemLamp(gemLampLitDark)).setCreativeTab(null);
    reg.registerBlock(gemLampLitInvertedDark, new ItemBlockGemLamp(gemLampLitInvertedDark));
    reg.registerBlock(gemLampInvertedDark, new ItemBlockGemLamp(gemLampInvertedDark))
        .setCreativeTab(null);
    reg.registerBlock(gemLampLight, new ItemBlockGemLamp(gemLampLight));
    reg.registerBlock(gemLampLitLight, new ItemBlockGemLamp(gemLampLitLight)).setCreativeTab(null);
    reg.registerBlock(gemLampLitInvertedLight, new ItemBlockGemLamp(gemLampLitInvertedLight));
    reg.registerBlock(gemLampInvertedLight, new ItemBlockGemLamp(gemLampInvertedLight))
        .setCreativeTab(null);

    reg.registerBlock(gemGlass);
    reg.registerBlock(gemGlassDark);
    reg.registerBlock(gemGlassLight);

    reg.registerBlock(teleporterAnchor);
    reg.registerBlock(teleporter);
    reg.registerBlock(teleporterDark);
    reg.registerBlock(teleporterLight);
    reg.registerBlock(teleporterRedstone);
    reg.registerBlock(teleporterRedstoneDark);
    reg.registerBlock(teleporterRedstoneLight);

    reg.registerBlock(glowRose, Names.GLOW_ROSE);
    reg.registerBlock(essenceOre);
    reg.registerBlock(miscBlock);
    reg.registerBlock(fluffyBlock);
    reg.registerBlock(fluffyPuffPlant, Names.FLUFFY_PUFF_PLANT).setCreativeTab(null);
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
