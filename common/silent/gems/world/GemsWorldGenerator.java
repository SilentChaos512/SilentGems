package silent.gems.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import silent.gems.block.GlowRose;
import silent.gems.configuration.Config;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import cpw.mods.fml.common.IWorldGenerator;

public class GemsWorldGenerator implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

        int d = world.provider.dimensionId;

        if (d == 0) {
            generateSurface(world, random, chunkX * 16, chunkZ * 16);
        }
        else if (d == -1) {
            generateNether();
        }
        else if (d == 1) {
            generateEnd();
        }
        else {
            generateSurface(world, random, chunkX * 16, chunkZ * 16);
        }
    }

    private void generateSurface(World world, Random random, int chunkX, int chunkZ) {

        int i, x, y, z;

        // Gem ores.
        for (i = 0; i < Config.WORLD_GEM_CLUSTER_COUNT.value; ++i) {
            x = chunkX + random.nextInt(16);
            y = random.nextInt(Config.WORLD_GEM_MAX_HEIGHT.value);
            z = chunkZ + random.nextInt(16);
            new WorldGenMinable(SRegistry.getBlock(Names.GEM_ORE).blockID, random.nextInt(EnumGem.all().length),
                    Config.WORLD_GEM_CLUSTER_SIZE.value, Block.stone.blockID).generate(world, random, x, y, z);
        }

        // Chaos ores. Chance of failure.
        for (i = 0; i < Config.WORLD_CHAOS_ORE_CLUSTER_COUNT.value; ++i) {
            if (random.nextInt(Config.WORLD_CHAOS_ORE_RARITY.value) == 0) {
                x = chunkX + random.nextInt(16);
                y = random.nextInt(Config.WORLD_CHAOS_ORE_MAX_HEIGHT.value);
                z = chunkZ + random.nextInt(16);
                new WorldGenMinable(SRegistry.getBlock(Names.CHAOS_ORE).blockID, 0, Config.WORLD_CHAOS_ORE_CLUSTER_SIZE.value,
                        Block.stone.blockID).generate(world, random, x, y, z);
            }
        }
        
        generateFlowers(world, random, chunkX, chunkZ);
    }

    private void generateNether() {

        // TODO
    }

    private void generateEnd() {

        // TODO
    }
    
    private void generateFlowers(World world, Random random, int chunkX, int chunkZ) {
        
        int i, x, y, z, m;
        
        // Glow roses
        GlowRose flower = (GlowRose) SRegistry.getBlock(Names.GLOW_ROSE);
        for (i = 0; i < Config.WORLD_FLOWERS_PER_CHUNK.value; ++i) {
            x = chunkX + random.nextInt(16);
            y = random.nextInt(80) + 40;
            z = chunkZ + random.nextInt(16);
            m = random.nextInt(EnumGem.all().length);
            
            // Find top-most valid block.
            for (; y > 40 && world.isAirBlock(x, y - 1, z); --y);

            if (flower.canBlockStay(world, x, y, z)) {
                world.setBlock(x, y, z, flower.blockID, m, 2);
            }
        }
    }
}
