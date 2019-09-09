package net.silentchaos512.gems.chaos;

import com.google.common.collect.ImmutableList;
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
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.chaos.ChaosEvent;
import net.silentchaos512.gems.block.CorruptedBlocks;
import net.silentchaos512.gems.entity.ChaosLightningBoltEntity;
import net.silentchaos512.gems.world.spawner.CorruptedSlimeSpawner;
import net.silentchaos512.gems.world.spawner.WispSpawner;
import net.silentchaos512.lib.util.TimeUtils;
import net.silentchaos512.utils.MathUtils;
import net.silentchaos512.utils.config.BooleanValue;
import net.silentchaos512.utils.config.ConfigSpecWrapper;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = SilentGems.MOD_ID)
public final class ChaosEvents {
    private static final Map<ResourceLocation, ChaosEvent> EVENTS = new HashMap<>();
    private static final Map<ResourceLocation, BooleanValue> EVENT_CONFIGS = new HashMap<>();
    private static final Map<UUID, Map<ResourceLocation, Integer>> COOLDOWN_TIMERS = new HashMap<>();

    private static final Queue<Supplier<LightningBoltEntity>> LIGHTNING_QUEUE = new ArrayDeque<>();
    private static final int LIGHTNING_TICK_FREQUENCY = 10;
    private static int tickTimer = 0;

    private static final Marker MARKER = MarkerManager.getMarker("ChaosEvents");

    // Not an actual cap on the value, just used for event probability
    private static final int MAX_CHAOS = 5_000_000;

    static {
        addChaosEvent(SilentGems.getId("lightning"), new ChaosEvent(0.1f, 60, 500_000, MAX_CHAOS, 50_000, "Spawn a regular lightning bolt (can cause fire)", (player, chaos) ->
                spawnLightningBolt(player, player.world)
        ));
        addChaosEvent(SilentGems.getId("chaos_lightning"), new ChaosEvent(0.3f, 30, 300_000, MAX_CHAOS, 30_000, "Spawn several lightning bolts that do not cause fire", (player, chaos) -> {
            int boltCount = 5 + SilentGems.random.nextInt(6);
            return spawnChaosLightningBolts(player, player.world, boltCount);
        }));
        addChaosEvent(SilentGems.getId("corrupt_blocks"), new ChaosEvent(0.2f, 600, 750_000, MAX_CHAOS, 100_000, "Create a patch of corrupted blocks", (player, chaos) ->
                corruptBlocks(player, player.world)
        ));
        addChaosEvent(SilentGems.getId("corrupted_slimes"), new ChaosEvent(0.1f, 600, 180_000, MAX_CHAOS / 3, 15_000, "Spawn a group of corrupted slimes",
                CorruptedSlimeSpawner::spawnSlimes
        ));
        addChaosEvent(SilentGems.getId("spawn_wisps"), new ChaosEvent(0.1f, 900, 220_000, MAX_CHAOS / 4, 25_000, "Spawn a group of wisps (random element)",
                WispSpawner::spawnWisps
        ));
        addChaosEvent(SilentGems.getId("thunderstorm"), new ChaosEvent(0.05f, 1200, 1_000_000, MAX_CHAOS, 200_000, "Changes the weather to a thunderstorm", (player, chaos) -> {
            int time = TimeUtils.ticksFromMinutes(MathUtils.nextIntInclusive(7, 15));
            return setThunderstorm(player.world, time);
        }));
    }

    private ChaosEvents() {throw new IllegalAccessError("Utility class");}

    /**
     * Register a new chaos event. Mods should use their own mod ID for the namespace.
     *
     * @param id    The event ID
     * @param event The event
     * @throws IllegalStateException if an event with the same ID already exists
     */
    public static void addChaosEvent(ResourceLocation id, ChaosEvent event) {
        if (EVENTS.containsKey(id))
            throw new IllegalStateException("Duplicate chaos event ID: " + id);
        EVENTS.put(id, event);
    }

    static void tryChaosEvents(PlayerEntity player, World world, int chaos) {
        if (player == null || !player.isAlive())
            return;

        Map<ResourceLocation, Integer> cooldownTimers = COOLDOWN_TIMERS.computeIfAbsent(player.getUniqueID(), uuid -> new HashMap<>());

        //noinspection OverlyLongLambda
        EVENTS.forEach((id, event) -> {
            int cooldown = getAndDecrementTimer(id, cooldownTimers);
            if (cooldown <= 0 && checkConfig(id) && event.tryActivate(player, chaos)) {
                SilentGems.LOGGER.info(MARKER, "Activate {} @ {}", id, player.getScoreboardName());
                cooldownTimers.put(id, event.getCooldownTime());
            }
        });
    }

    private static int getAndDecrementTimer(ResourceLocation id, Map<ResourceLocation, Integer> timers) {
        if (!timers.containsKey(id))
            return 0;

        int time = timers.get(id) - 1;
        if (time <= 0)
            timers.remove(id);
        else
            timers.put(id, time);

        return time;
    }

    public static Collection<ResourceLocation> getEventIds() {
        return EVENTS.keySet();
    }

    /**
     * Trigger a chaos event, regardless of chaos levels or config settings.
     *
     * @param eventId The event ID
     * @param player  The player to target
     */
    public static void triggerEvent(ResourceLocation eventId, PlayerEntity player) {
        player.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source -> {
            ChaosEvent event = EVENTS.get(eventId);
            if (event != null) {
                event.activate(player, source.getChaos());
            }
        });
    }

    /**
     * Checks whether or not the event is allowed in the config file. If the config is missing for
     * some reason, this will return {@code true}.
     *
     * @param eventId The ID of the chaos event
     * @return True if the event is allowed to trigger randomly
     */
    public static boolean checkConfig(ResourceLocation eventId) {
        if (EVENT_CONFIGS.containsKey(eventId))
            return EVENT_CONFIGS.get(eventId).get();
        return true;
    }

    /**
     * Load configs for chaos events, DO NOT CALL.
     *
     * @param wrapper Config wrapper
     */
    public static void loadConfigs(ConfigSpecWrapper wrapper) {
        wrapper.comment("chaos.events",
                "Allows individual events to be disabled.",
                "Note that disabling events will likely increase the frequency of other events.");
        EVENTS.forEach((id, event) -> EVENT_CONFIGS.put(id, wrapper.builder("chaos.events." + id).comment(event.getConfigComment()).define(true)));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        ++tickTimer;
        if (!LIGHTNING_QUEUE.isEmpty() && tickTimer % LIGHTNING_TICK_FREQUENCY == 0) {
            LightningBoltEntity bolt = LIGHTNING_QUEUE.remove().get();
            ((ServerWorld) bolt.world).addLightningBolt(bolt);
        }
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

    private static boolean spawnChaosLightningBolts(Entity entity, World world, int count) {
        if (world instanceof ServerWorld && canSpawnLightningIn(world.dimension.getType())) {
            for (int i = 0; i < count; ++i) {
                double posX = entity.posX + MathUtils.nextIntInclusive(-64, 64);
                double posZ = entity.posZ + MathUtils.nextIntInclusive(-64, 64);
                int height = world.getHeight(Heightmap.Type.MOTION_BLOCKING, (int) posX, (int) posZ);
                LIGHTNING_QUEUE.add(() -> new ChaosLightningBoltEntity(world, posX, height, posZ));
            }
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
        int posY = 64 + MathUtils.nextIntInclusive(-32, 64);

        BlockPos pos = new BlockPos(posX, posY, posZ);
        boolean done = false;
        while (pos.getY() > 1 && !done) {
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

    public static List<String> getCooldownTimersDebugText(PlayerEntity player) {
        if (player == null || !COOLDOWN_TIMERS.containsKey(player.getUniqueID()))
            return ImmutableList.of();

        return COOLDOWN_TIMERS.get(player.getUniqueID()).entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.toList());
    }
}
