package net.silentchaos512.gems.world.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.silentchaos512.gems.lib.Gems;

import java.util.Random;

public interface IRegionalGemsConfig {
    default Gems selectGem(ISeedReader world, BlockPos pos, Random random, Gems.Set gemSet, int regionSize) {
        int regionSizeNonZero = regionSize > 0 ? regionSize : 1;
        int regionX = pos.getX() / (16 * regionSizeNonZero);
        int regionZ = pos.getZ() / (16 * regionSizeNonZero);
        long regionId = ((long) regionX << 32) + regionZ;
        Random regionRand = new Random(regionId + world.getSeed());

        int gemCount = 4 + regionRand.nextInt(3);
        int index = random.nextInt(gemCount);
        Gems gem = gemSet.selectRandom(regionRand);
        for (int i = 0; i < index; ++i) {
            gem = gemSet.selectRandom(regionRand);
        }
        return gem;
    }
}
