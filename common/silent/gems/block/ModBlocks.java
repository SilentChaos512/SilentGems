package silent.gems.block;

import silent.gems.core.registry.SRegistry;
import silent.gems.item.block.ItemBlockSG;
import silent.gems.lib.Names;

public class ModBlocks {

    public static void init() {

        SRegistry.registerBlock(GemOre.class, Names.GEM_ORE);
        SRegistry.registerBlock(ChaosOre.class, Names.CHAOS_ORE);
        SRegistry.registerBlock(GemBlock.class, Names.GEM_BLOCK);
        SRegistry.registerBlock(GemBrick.class, Names.GEM_BRICK_COATED, ItemBlockSG.class, new Object[] { Names.GEM_BRICK_COATED });
        SRegistry.registerBlock(GemBrick.class, Names.GEM_BRICK_SPECKLED, ItemBlockSG.class, new Object[] { Names.GEM_BRICK_SPECKLED });
        SRegistry.registerBlock(MiscBlock.class, Names.MISC_BLOCKS);
        SRegistry.registerBlock(GlowRose.class, Names.GLOW_ROSE);
        SRegistry.registerBlock(Teleporter.class, Names.TELEPORTER);
        SRegistry.registerBlock(GemLamp.class, Names.GEM_LAMP, ItemBlockSG.class, new Object[] { false, false });
        SRegistry.registerBlock(GemLamp.class, Names.GEM_LAMP_LIT, ItemBlockSG.class, new Object[] { true, false });
        SRegistry.registerBlock(GemLamp.class, Names.GEM_LAMP_INV_LIT, ItemBlockSG.class, new Object[] { true, true });
        SRegistry.registerBlock(GemLamp.class, Names.GEM_LAMP_INV, ItemBlockSG.class, new Object[] { false, true });
        SRegistry.registerBlock(FluffyPlantBlock.class, Names.FLUFFY_PLANT);
    }
}
