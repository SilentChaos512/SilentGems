package silent.gems.block;

import net.minecraftforge.common.MinecraftForge;
import silent.gems.core.registry.SRegistry;
import silent.gems.item.block.GemBlockItemBlock;
import silent.gems.item.block.GemBrickItemBlock;
import silent.gems.item.block.GemOreItemBlock;
import silent.gems.item.block.GlowRoseItemBlock;
import silent.gems.item.block.MiscBlockItemBlock;
import silent.gems.item.block.MushroomBlockItemBlock;
import silent.gems.item.block.TeleporterItemBlock;
import silent.gems.lib.Names;


public class ModBlocks {

    // Default ids
    private final static int CHAOS_ORE_ID = 3201;
    private final static int GEM_BLOCK_ID = 3202;
    private final static int GEM_BRICK_ID = 3203;
    private final static int GEM_ORE_ID = 3200;
    private final static int GLOW_ROSE_ID = 3204;
    private final static int MISC_BLOCKS_ID = 3206;
    private final static int MUSHROOM_BLOCK_ID = 3208;
    private final static int SHINY_CRAFTER_ID = 3207;
    private final static int TELEPORTER_ID = 3205;
    
    public static void init() {
        
        SRegistry.registerBlock(ChaosOre.class, Names.CHAOS_ORE, CHAOS_ORE_ID);
        SRegistry.registerBlock(GemBlock.class, Names.GEM_BLOCK, GEM_BLOCK_ID, GemBlockItemBlock.class);
        SRegistry.registerBlock(GemBrick.class, Names.GEM_BRICK, GEM_BRICK_ID, GemBrickItemBlock.class);
        SRegistry.registerBlock(GemOre.class, Names.GEM_ORE, GEM_ORE_ID, GemOreItemBlock.class);
        SRegistry.registerBlock(GlowRose.class, Names.GLOW_ROSE, GLOW_ROSE_ID, GlowRoseItemBlock.class);
        SRegistry.registerBlock(MiscBlock.class, Names.MISC_BLOCKS, MISC_BLOCKS_ID, MiscBlockItemBlock.class);
        SRegistry.registerBlock(MushroomBlock.class, Names.MUSHROOM_BLOCK, MUSHROOM_BLOCK_ID, MushroomBlockItemBlock.class);
        SRegistry.registerBlock(ShinyCrafter.class, Names.SHINY_CRAFTER, SHINY_CRAFTER_ID);
        SRegistry.registerBlock(Teleporter.class, Names.TELEPORTER, TELEPORTER_ID, TeleporterItemBlock.class);
        
        /*
         * Set harvest levels
         */
        MinecraftForge.setBlockHarvestLevel(SRegistry.getBlock(Names.CHAOS_ORE), "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(SRegistry.getBlock(Names.GEM_ORE), "pickaxe", 2);
    }
}
