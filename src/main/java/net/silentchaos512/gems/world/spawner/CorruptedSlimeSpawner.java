package net.silentchaos512.gems.world.spawner;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.*;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.CorruptedSlimeEntity;
import net.silentchaos512.gems.init.GemsEntities;
import net.silentchaos512.utils.MathUtils;

public final class CorruptedSlimeSpawner {
    private static final int MIN_GROUP_COUNT = 3;
    private static final int MAX_GROUP_COUNT = 6;

    private CorruptedSlimeSpawner() {}

    public static boolean spawnSlimes(Entity player, int chaos) {
        if (player.world.getDifficulty() == Difficulty.PEACEFUL || !player.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING))
            return false;

        int count = MathUtils.nextIntInclusive(SilentGems.random, MIN_GROUP_COUNT, MAX_GROUP_COUNT);
        BlockPos pos = getRandomHeight(player.world, player.world.getChunk(player.getPosition()));

        for (int i = 0; i < 4; ++i) {
            if (spawnGroup(GemsEntities.CORRUPTED_SLIME.get(), count, player.world, player.getPosition())) {
                return true;
            }
        }
        return false;
    }

    private static boolean spawnGroup(EntityType<?> type, int count, World world, BlockPos pos) {
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
            int posY = world.getChunk(posX / 16, posZ / 16).getTopBlockY(Heightmap.Type.WORLD_SURFACE, posX, posZ) + 1;
            BlockPos blockPos = new BlockPos(posX, posY, posZ);
            SilentGems.LOGGER.debug("{}: {}", type.getRegistryName(), blockPos);

            DifficultyInstance difficultyInstance = world.getDifficultyForLocation(blockPos);

            if (world instanceof IServerWorld && canSpawnAt(world, blockPos, type)) {
                Entity entity = type.create(world);
                if (entity != null && entity instanceof CorruptedSlimeEntity) {
                    CorruptedSlimeEntity slime = (CorruptedSlimeEntity) entity;
                    if (ForgeEventFactory.canEntitySpawn(slime, world, posX, posY, posZ, null, SpawnReason.NATURAL) != Event.Result.DENY) {
                        slime.moveToBlockPosAndAngles(blockPos, 0f, 0f);
                        slime.onInitialSpawn((IServerWorld) world, difficultyInstance, SpawnReason.NATURAL, null, null);
                        world.addEntity(slime);
                        ++spawned;
                    } else {
                        entity.remove();
                    }
                } else {
                    throw new IllegalArgumentException("Entity type is not CorruptedSlimeEntity? " + type);
                }
            }
        }

        return spawned > 0;
    }

    private static boolean canSpawnAt(World world, BlockPos pos, EntityType<?> type) {
        final boolean isSpawnableSpace = WorldEntitySpawner.func_234968_a_(world, pos, world.getBlockState(pos), world.getFluidState(pos), type);
        return isSpawnableSpace && world.getLightFor(LightType.BLOCK, pos) < 7;
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
