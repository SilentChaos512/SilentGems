package net.silentchaos512.gems.chaos;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.chaos.ChaosEvent;
import net.silentchaos512.gems.block.CorruptedBlocks;
import net.silentchaos512.utils.MathUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;
import java.util.Map;

public final class ChaosEvents {
    private static final Map<ResourceLocation, ChaosEvent> EVENTS = new HashMap<>();
    private static final Marker MARKER = MarkerManager.getMarker("ChaosEvents");

    // Not an actual cap on the value, just used for event probability
    private static final int MAX_CHAOS = 5_000_000;

    static {
        addChaosEvent(SilentGems.getId("chaos_lightning"), new ChaosEvent(
                0.25f, 150_000, MAX_CHAOS, 10_000, (entity, chaos) ->
                spawnLightningBolt(entity, entity.world)
        ));
        addChaosEvent(SilentGems.getId("corrupt_blocks"), new ChaosEvent(
                0.25f, 1_000_000, MAX_CHAOS, 100_000, (entity, chaos) ->
                corruptBlocks(entity, entity.world)
        ));
    }

    private ChaosEvents() {throw new IllegalAccessError("Utility class");}

    public static void addChaosEvent(ResourceLocation id, ChaosEvent event) {
        EVENTS.put(id, event);
    }

    static void tryChaosEvents(Entity entity, World world, int chaos) {
        EVENTS.forEach((id, event) -> {
            if (event.tryActivate(entity, chaos)) {
                SilentGems.LOGGER.info(MARKER, "Activate {} @ {}", id, entity.getScoreboardName());
            }
        });
    }

    private static boolean tryChance(float max, int chaos, int maxChaos) {
        float chance = Math.min(max * chaos / maxChaos, max);
        return MathUtils.tryPercentage(chance);
    }

    private static void spawnLightningBolt(Entity entity, World world) {
        double posX = entity.posX + MathUtils.nextIntInclusive(-64, 64);
        double posZ = entity.posZ + MathUtils.nextIntInclusive(-64, 64);
        int height = world.getHeight(Heightmap.Type.MOTION_BLOCKING, (int) posX, (int) posZ);
        EntityLightningBolt bolt = new EntityLightningBolt(world, posX, height, posZ, false);
        world.addWeatherEffect(bolt);
    }

    private static void corruptBlocks(Entity entity, World world) {
        int posX = (int) entity.posX + MathUtils.nextIntInclusive(-128, 128);
        int posZ = (int) entity.posZ + MathUtils.nextIntInclusive(-128, 128);
        int posY = 64 + MathUtils.nextIntInclusive(-32, 32);

        BlockPos pos = new BlockPos(posX, posY, posZ);
        boolean done = false;
        while (pos.getY() > 5 && !done) {
            IBlockState state = world.getBlockState(pos);
            for (CorruptedBlocks corruptedBlocks : CorruptedBlocks.values()) {
                if (corruptedBlocks.canReplace(state.getBlock())) {
                    makeCorruptedBlockCluster(world, pos, corruptedBlocks);
                    done = true;
                }
            }
            pos = pos.down();
        }
    }

    private static void makeCorruptedBlockCluster(World world, BlockPos pos, CorruptedBlocks corruptedBlock) {
        SilentGems.LOGGER.debug(MARKER, "corrupted cluster @ {}", pos);
        IBlockState newState = corruptedBlock.asBlockState();
        world.setBlockState(pos, newState, 3);
        for (int i = 0; i < 40; ++i) {
            BlockPos pos1 = new BlockPos(
                    pos.getX() + (int) (1.5 * SilentGems.random.nextGaussian()),
                    pos.getY() + (int) (1.5 * SilentGems.random.nextGaussian()),
                    pos.getZ() + (int) (1.5 * SilentGems.random.nextGaussian())
            );
            IBlockState target = world.getBlockState(pos1);
            if (corruptedBlock.canReplace(target.getBlock())) {
                world.setBlockState(pos1, newState, 3);
            }
        }
    }
}
