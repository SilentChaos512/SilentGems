package net.silentchaos512.gems.world.spawner;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.AbstractWispEntity;
import net.silentchaos512.gems.lib.WispTypes;
import net.silentchaos512.utils.MathUtils;

public final class WispSpawner {
    private static final int MIN_GROUP_COUNT = 2;
    private static final int MAX_GROUP_COUNT = 4;

    private WispSpawner() {}

    public static boolean spawnWisps(Entity player, int chaos) {
        if (player.world.getDifficulty() == Difficulty.PEACEFUL) return false;

        WispTypes type = WispTypes.selectRandom(SilentGems.random);
        int count = MathUtils.nextIntInclusive(SilentGems.random, MIN_GROUP_COUNT, MAX_GROUP_COUNT);
        BlockPos pos = getRandomHeight(player.world, player.world.getChunk(player.getPosition()));
        return spawnWispGroup(type.getEntityType(), count, player.world, player.getPosition());
    }

    private static <T extends AbstractWispEntity> boolean spawnWispGroup(EntityType<T> type, int count, World world, BlockPos pos) {
        int areaX = pos.getX() + 16 * MathUtils.nextIntInclusive(-2, 2);
        int areaZ = pos.getZ() + 16 * MathUtils.nextIntInclusive(-2, 2);

        if (areaX == pos.getX() || areaZ == pos.getZ()) {
            // Don't spawn right on top of the player...
            areaX = pos.getX() + 24;
            areaZ = pos.getZ() + 24;
        }

        int spawned = 0;

        for (int i = 0; i < count; ++i) {
            int posX = areaX + MathUtils.nextIntInclusive(-8, 8);
            int posZ = areaZ + MathUtils.nextIntInclusive(-8, 8);
            int posY = world.getChunk(posX, posZ).getTopBlockY(Heightmap.Type.WORLD_SURFACE, posX, posZ) + 1;
            BlockPos blockPos = new BlockPos(posX, posY, posZ);
            SilentGems.LOGGER.debug("{}: {}", type.getRegistryName(), blockPos);

            DifficultyInstance difficultyInstance = world.getDifficultyForLocation(blockPos);

            if (WorldEntitySpawner.isSpawnableSpace(world, blockPos, world.getBlockState(blockPos), world.getFluidState(blockPos))) {
                T wisp = type.create(world);
                if (wisp != null) {
                    wisp.moveToBlockPosAndAngles(blockPos, 0f, 0f);
                    wisp.onInitialSpawn(world, difficultyInstance, SpawnReason.NATURAL, null, null);
                    world.addEntity(wisp);
                    ++spawned;
                }
            }
        }

        return spawned > 0;
    }

    private static BlockPos getRandomHeight(World worldIn, IChunk chunkIn) {
        ChunkPos chunkpos = chunkIn.getPos();
        int x = chunkpos.getXStart() + worldIn.rand.nextInt(16);
        int z = chunkpos.getZStart() + worldIn.rand.nextInt(16);
        int top = chunkIn.getTopBlockY(Heightmap.Type.WORLD_SURFACE, x, z) + 1;
        int y = worldIn.rand.nextInt(top + 1);
        return new BlockPos(x, y, z);
    }
}
