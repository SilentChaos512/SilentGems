package net.silentchaos512.gems.chaos;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.chaos.ChaosEvent;
import net.silentchaos512.gems.block.CorruptedBlocks;
import net.silentchaos512.gems.world.spawner.WispSpawner;
import net.silentchaos512.lib.util.TimeUtils;
import net.silentchaos512.utils.MathUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class ChaosEvents {
    private static final Map<ResourceLocation, ChaosEvent> EVENTS = new HashMap<>();
    private static final Marker MARKER = MarkerManager.getMarker("ChaosEvents");

    // Not an actual cap on the value, just used for event probability
    private static final int MAX_CHAOS = 5_000_000;

    static {
        addChaosEvent(SilentGems.getId("chaos_lightning"), new ChaosEvent(0.25f, 250_000, MAX_CHAOS, 25_000, (entity, chaos) ->
                spawnLightningBolt(entity, entity.world)
        ));
        addChaosEvent(SilentGems.getId("corrupt_blocks"), new ChaosEvent(0.25f, 800_000, MAX_CHAOS, 150_000, (entity, chaos) ->
                corruptBlocks(entity, entity.world)
        ));
        addChaosEvent(SilentGems.getId("spawn_chaos_wisps"), new ChaosEvent(0.4f, 100_000, MAX_CHAOS / 5, 5_000,
                WispSpawner::spawnWisps
        ));
        addChaosEvent(SilentGems.getId("thunderstorm"), new ChaosEvent(0.05f, 1_000_000, MAX_CHAOS, 200_000, (entity, chaos) -> {
            int time = TimeUtils.ticksFromMinutes(MathUtils.nextIntInclusive(7, 15));
            return setThunderstorm(entity.world, time);
        }));
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

    public static Collection<ResourceLocation> getEventIds() {
        return EVENTS.keySet();
    }

    public static void triggerEvent(ResourceLocation eventId, PlayerEntity player) {
        player.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source -> {
            ChaosEvent event = EVENTS.get(eventId);
            if (event != null) {
                event.activate(player, source.getChaos());
            }
        });
    }

    private static boolean spawnLightningBolt(Entity entity, World world) {
        if (world instanceof ServerWorld && canSpawnLightningIn(world.dimension.getType())) {
            double posX = entity.posX + MathUtils.nextIntInclusive(-64, 64);
            double posZ = entity.posZ + MathUtils.nextIntInclusive(-64, 64);
            int height = world.getHeight(Heightmap.Type.MOTION_BLOCKING, (int) posX, (int) posZ);
            LightningBoltEntity bolt = new LightningBoltEntity(world, posX, height, posZ, false);
            ((ServerWorld) world).addLightningBolt(bolt);
            return true;
        }
        return false;
    }

    private static boolean canSpawnLightningIn(DimensionType dimensionType) {
        return dimensionType.getId() != DimensionType.THE_NETHER.getId() && dimensionType.getId() != DimensionType.THE_END.getId();
    }

    private static boolean corruptBlocks(Entity entity, World world) {
        int posX = (int) entity.posX + MathUtils.nextIntInclusive(-128, 128);
        int posZ = (int) entity.posZ + MathUtils.nextIntInclusive(-128, 128);
        int posY = 64 + MathUtils.nextIntInclusive(-32, 32);

        BlockPos pos = new BlockPos(posX, posY, posZ);
        boolean done = false;
        while (pos.getY() > 5 && !done) {
            BlockState state = world.getBlockState(pos);
            for (CorruptedBlocks corruptedBlocks : CorruptedBlocks.values()) {
                if (corruptedBlocks.canReplace(state.getBlock())) {
                    makeCorruptedBlockCluster(world, pos, corruptedBlocks);
                    done = true;
                }
            }
            pos = pos.down();
        }

        return done;
    }

    private static void makeCorruptedBlockCluster(World world, BlockPos pos, CorruptedBlocks corruptedBlock) {
        SilentGems.LOGGER.debug(MARKER, "corrupted cluster @ {}", pos);
        BlockState newState = corruptedBlock.asBlockState();
        world.setBlockState(pos, newState, 3);
        for (int i = 0; i < 40; ++i) {
            BlockPos pos1 = new BlockPos(
                    pos.getX() + (int) (1.5 * SilentGems.random.nextGaussian()),
                    pos.getY() + (int) (1.5 * SilentGems.random.nextGaussian()),
                    pos.getZ() + (int) (1.5 * SilentGems.random.nextGaussian())
            );
            BlockState target = world.getBlockState(pos1);
            if (corruptedBlock.canReplace(target.getBlock())) {
                world.setBlockState(pos1, newState, 3);
            }
        }
    }

    private static boolean setThunderstorm(World world, int time) {
        if (!world.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE) || world.getWorldInfo().isThundering())
            return false;

        world.getWorldInfo().setClearWeatherTime(0);
        world.getWorldInfo().setRainTime(time);
        world.getWorldInfo().setThunderTime(time);
        world.getWorldInfo().setRaining(true);
        world.getWorldInfo().setThundering(true);
        return true;
    }
}
