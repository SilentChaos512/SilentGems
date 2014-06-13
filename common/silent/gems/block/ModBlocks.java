package silent.gems.block;

import silent.gems.core.registry.SRegistry;
import silent.gems.item.block.ItemBlockSG;
import silent.gems.lib.Names;

public class ModBlocks {

	public static void init() {
		
	    SRegistry.registerBlock(GemOre.class, Names.GEM_ORE);
		SRegistry.registerBlock(ChaosOre.class, Names.CHAOS_ORE);
		SRegistry.registerBlock(GemBlock.class, Names.GEM_BLOCK);
		SRegistry.registerBlock(GemBrick.class, Names.GEM_BRICK);
		SRegistry.registerBlock(MiscBlock.class, Names.MISC_BLOCKS);
		SRegistry.registerBlock(GlowRose.class, Names.GLOW_ROSE);
		SRegistry.registerBlock(Teleporter.class, Names.TELEPORTER);
		SRegistry.registerBlock(FluffyPlantBlock.class, Names.FLUFFY_PLANT);
	}
}
