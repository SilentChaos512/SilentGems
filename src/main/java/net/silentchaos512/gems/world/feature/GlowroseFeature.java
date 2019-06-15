package net.silentchaos512.gems.world.feature;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.lib.Gems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class GlowroseFeature extends FlowersFeature {
    private final List<Gems> gems = new ArrayList<>();

    public GlowroseFeature(Collection<Gems> gems) {
        super(NoFeatureConfig::deserialize);
        this.gems.addAll(gems);
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random, BlockPos pos, NoFeatureConfig config) {
        BlockState flower = this.getRandomFlower(random, pos);
        int numberPlaced = 0;
        int maxCount = GemsConfig.COMMON.glowroseMaxPlaceCount.get();
        int tryCount = GemsConfig.COMMON.glowroseSpawnTryCount.get();

        // Same as super, but fewer iterations
        for(int j = 0; j < tryCount && numberPlaced < maxCount; ++j) {
            BlockPos pos1 = pos.add(
                    random.nextInt(8) - random.nextInt(8),
                    random.nextInt(4) - random.nextInt(4),
                    random.nextInt(8) - random.nextInt(8)
            );
            if (world.isAirBlock(pos1) && pos1.getY() < 255 && flower.isValidPosition(world, pos1)) {
                world.setBlockState(pos1, flower, 2);
                ++numberPlaced;
            }
        }

        return numberPlaced > 0;
    }

    @Override
    public BlockState getRandomFlower(Random random, BlockPos pos) {
        int index = random.nextInt(this.gems.size());
        Gems gem = this.gems.get(index);
        return gem.getGlowrose().getDefaultState();
    }
}
