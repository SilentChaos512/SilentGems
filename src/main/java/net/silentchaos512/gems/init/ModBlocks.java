package net.silentchaos512.gems.init;

import net.minecraft.item.ItemBlock;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileTeleporter;
import net.silentchaos512.lib.item.ItemBlockMetaSubtypes;
import net.silentchaos512.lib.registry.SRegistry;

import static net.silentchaos512.gems.lib.EnumGem.Set.*;

public class ModBlocks {
    public static final BlockGemOre gemOre = new BlockGemOre(CLASSIC);
    public static final BlockGemOre gemOreDark = new BlockGemOre(DARK);
    public static final BlockGemOre gemOreLight = new BlockGemOre(LIGHT);

    public static final BlockGemOreMulti multiGemOreClassic = new BlockGemOreMulti(EnumGem.Set.CLASSIC);
    public static final BlockGemOreMulti multiGemOreDark = new BlockGemOreMulti(EnumGem.Set.DARK);
    public static final BlockGemOreMulti multiGemOreLight = new BlockGemOreMulti(EnumGem.Set.LIGHT);

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
    public static final BlockTeleporterRedstone teleporterRedstone = new BlockTeleporterRedstone(CLASSIC);
    public static final BlockTeleporterRedstone teleporterRedstoneDark = new BlockTeleporterRedstone(DARK);
    public static final BlockTeleporterRedstone teleporterRedstoneLight = new BlockTeleporterRedstone(LIGHT);

    public static final BlockGlowRose glowRose = new BlockGlowRose();
    public static final BlockEssenceOre essenceOre = new BlockEssenceOre();
    public static final BlockMisc miscBlock = new BlockMisc();
    public static final BlockHardenedRock hardenedRock = new BlockHardenedRock();
    public static final BlockFluffyBlock fluffyBlock = new BlockFluffyBlock();
    public static final BlockFluffyPuffPlant fluffyPuffPlant = new BlockFluffyPuffPlant();
    public static final BlockStoneTorch stoneTorch = new BlockStoneTorch();
    public static final BlockChaosFlowerPot chaosFlowerPot = new BlockChaosFlowerPot();
    public static final BlockMaterialGrader materialGrader = new BlockMaterialGrader();
    public static final BlockChaosNode chaosNode = new BlockChaosNode();
    public static final BlockChaosAltar chaosAltar = new BlockChaosAltar();
    public static final BlockChaosPylon chaosPylon = new BlockChaosPylon();
    public static final BlockPhantomLight phantomLight = new BlockPhantomLight();

    public static void registerAll(SRegistry reg) {
        reg.registerBlock(gemOre, "gemore");
        reg.registerBlock(gemOreDark, "gemoredark");
        reg.registerBlock(gemOreLight, "gemorelight");

        reg.registerBlock(gemBlock, "gemblock");
        reg.registerBlock(gemBlockDark, "gemblockdark");
        reg.registerBlock(gemBlockLight, "gemblocklight");
        reg.registerBlock(gemBlockSuper, "gemblocksuper");
        reg.registerBlock(gemBlockSuperDark, "gemblocksuperdark");
        reg.registerBlock(gemBlockSuperLight, "gemblocksuperlight");

        reg.registerBlock(gemBrickCoated, "gembrickcoated");
        reg.registerBlock(gemBrickCoatedDark, "gembrickcoateddark");
        reg.registerBlock(gemBrickCoatedLight, "gembrickcoatedlight");
        reg.registerBlock(gemBrickSpeckled, "gembrickspeckled");
        reg.registerBlock(gemBrickSpeckledDark, "gembrickspeckleddark");
        reg.registerBlock(gemBrickSpeckledLight, "gembrickspeckledlight");

        reg.registerBlock(gemLamp, "gemlamp", new BlockGemLamp.ItemBlock(gemLamp));
        reg.registerBlock(gemLampLit, "gemlamplit", new BlockGemLamp.ItemBlock(gemLampLit)).setCreativeTab(null);
        reg.registerBlock(gemLampLitInverted, "gemlamplitinverted", new BlockGemLamp.ItemBlock(gemLampLitInverted));
        reg.registerBlock(gemLampInverted, "gemlampinverted", new BlockGemLamp.ItemBlock(gemLampInverted)).setCreativeTab(null);
        reg.registerBlock(gemLampDark, "gemlampdark", new BlockGemLamp.ItemBlock(gemLampDark));
        reg.registerBlock(gemLampLitDark, "gemlamplitdark", new BlockGemLamp.ItemBlock(gemLampLitDark)).setCreativeTab(null);
        reg.registerBlock(gemLampLitInvertedDark, "gemlamplitinverteddark", new BlockGemLamp.ItemBlock(gemLampLitInvertedDark));
        reg.registerBlock(gemLampInvertedDark, "gemlampinverteddark", new BlockGemLamp.ItemBlock(gemLampInvertedDark)).setCreativeTab(null);
        reg.registerBlock(gemLampLight, "gemlamplight", new BlockGemLamp.ItemBlock(gemLampLight));
        reg.registerBlock(gemLampLitLight, "gemlamplitlight", new BlockGemLamp.ItemBlock(gemLampLitLight)).setCreativeTab(null);
        reg.registerBlock(gemLampLitInvertedLight, "gemlamplitinvertedlight", new BlockGemLamp.ItemBlock(gemLampLitInvertedLight));
        reg.registerBlock(gemLampInvertedLight, "gemlampinvertedlight", new BlockGemLamp.ItemBlock(gemLampInvertedLight)).setCreativeTab(null);

        reg.registerBlock(gemGlass, "gemglass");
        reg.registerBlock(gemGlassDark, "gemglassdark");
        reg.registerBlock(gemGlassLight, "gemglasslight");

        reg.registerBlock(teleporterAnchor, "teleporteranchor", new ItemBlock(teleporterAnchor));
        reg.registerBlock(teleporter, "teleporter");
        reg.registerBlock(teleporterDark, "teleporterdark");
        reg.registerBlock(teleporterLight, "teleporterlight");
        reg.registerBlock(teleporterRedstone, "teleporterredstone");
        reg.registerBlock(teleporterRedstoneDark, "teleporterredstonedark");
        reg.registerBlock(teleporterRedstoneLight, "teleporterredstonelight");
        // Need to register teleporter tile entity manually
        reg.registerTileEntity(TileTeleporter.class, "teleporter");

        reg.registerBlock(multiGemOreClassic, "multi_gem_ore_classic");
        reg.registerBlock(multiGemOreDark, "multi_gem_ore_dark");
        reg.registerBlock(multiGemOreLight, "multi_gem_ore_light");

        reg.registerBlock(glowRose, Names.GLOW_ROSE, new ItemBlockMetaSubtypes(glowRose, 16));
        reg.registerBlock(essenceOre, Names.ESSENCE_ORE);
        reg.registerBlock(miscBlock, Names.MISC_BLOCK, new BlockMisc.ItemBlock(miscBlock));
        reg.registerBlock(hardenedRock, Names.HARDENED_ROCK);
        reg.registerBlock(fluffyBlock, Names.FLUFFY_BLOCK);
        reg.registerBlock(fluffyPuffPlant, Names.FLUFFY_PUFF_PLANT).setCreativeTab(null);
        reg.registerBlock(stoneTorch, Names.STONE_TORCH);
        reg.registerBlock(chaosFlowerPot, Names.CHAOS_FLOWER_POT);
        reg.registerBlock(materialGrader, Names.MATERIAL_GRADER);
        reg.registerBlock(chaosNode, Names.CHAOS_NODE);
        reg.registerBlock(chaosAltar, Names.CHAOS_ALTAR);
        reg.registerBlock(chaosPylon, Names.CHAOS_PYLON, new ItemBlockMetaSubtypes(chaosPylon, BlockChaosPylon.VariantType.values().length));
        reg.registerBlock(phantomLight, Names.PHANTOM_LIGHT);
    }
}
