package silent.gems.block;

import net.minecraftforge.common.MinecraftForge;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.Names;


public class ModBlocks {

    // Default ids
    private final static int CHAOS_ORE_ID = 3201;
    private final static int GEM_BLOCK_ID = 3202;
    private final static int GEM_BRICK_ID = 3203;
    private final static int GEM_ORE_ID = 3200;
    private final static int GLOW_ROSE_ID = 3204;
    
    public static void init() {
        
        SRegistry.registerBlock(ChaosOre.class, Names.CHAOS_ORE, CHAOS_ORE_ID);
        SRegistry.registerBlock(GemBlock.class, Names.GEM_BLOCK, GEM_BLOCK_ID, GemBlockItemBlock.class);
        SRegistry.registerBlock(GemBrick.class, Names.GEM_BRICK, GEM_BRICK_ID, GemBrickItemBlock.class);
        SRegistry.registerBlock(GemOre.class, Names.GEM_ORE, GEM_ORE_ID, GemOreItemBlock.class);
        SRegistry.registerBlock(GlowRose.class, Names.GLOW_ROSE, GLOW_ROSE_ID, GlowRoseItemBlock.class);
        
        /*
         * Set harvest levels
         */
        MinecraftForge.setBlockHarvestLevel(SRegistry.getBlock(Names.CHAOS_ORE), "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(SRegistry.getBlock(Names.GEM_ORE), "pickaxe", 2);
    }
}
