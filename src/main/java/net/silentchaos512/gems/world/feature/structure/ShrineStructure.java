/*
package net.silentchaos512.gems.world.feature.structure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.silentchaos512.gems.SilentGems;

public class ShrineStructure extends ScatteredStructure<NoFeatureConfig> {
    @Override
    protected int getSeedModifier() {
        return 0x80C007;
    }

    @Override
    public IStartFactory getStartFactory() {
        return null;
    }

    @Override
    public String getStructureName() {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    public static class Start extends MarginedStructureStart {
        public Start(Structure<?> structure, int chunkX, int chunkZ, Biome biome, MutableBoundingBox boundingBox, int reference, long seed) {
            super(structure, chunkX, chunkZ, biome, boundingBox, reference, seed);
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
            BlockPos blockPos = new BlockPos(chunkX * 16, 90, chunkZ * 16);
            JigsawManager.func_214889_a(SilentGems.getId("shrine"), 7,
                    (p_create_1_, p_create_2_, p_create_3_, p_create_4_, p_create_5_, p_create_6_) -> );
        }
    }
}
*/
