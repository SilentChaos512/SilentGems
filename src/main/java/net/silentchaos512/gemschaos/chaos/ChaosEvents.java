package net.silentchaos512.gemschaos.chaos;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.api.ChaosEvent;
import net.silentchaos512.gemschaos.config.ChaosConfig;
import net.silentchaos512.lib.util.TimeUtils;
import net.silentchaos512.utils.MathUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ChaosMod.MOD_ID)
public final class ChaosEvents {
    private static final Map<ResourceLocation, ChaosEvent> EVENTS = new HashMap<>();
    private static final Map<ResourceLocation, ForgeConfigSpec.BooleanValue> EVENT_CONFIGS = new HashMap<>();
    private static final Map<UUID, Map<ResourceLocation, Integer>> COOLDOWN_TIMERS = new HashMap<>();

    private static final Queue<Supplier<LightningBoltEntity>> LIGHTNING_QUEUE = new ArrayDeque<>();
    private static final int LIGHTNING_TICK_FREQUENCY = 10;
    private static int tickTimer = 0;

    private static final Marker MARKER = MarkerManager.getMarker("ChaosEvents");

    // Not an actual cap on the value, just used for event probability
    private static final int MAX_CHAOS = 5_000_000;

    static {
        addChaosEvent(ChaosMod.getId("lightning"), new ChaosEvent(0.1f, 120, 500_000, MAX_CHAOS, 50_000, "Spawn a regular lightning bolt (can cause fire)", (player, chaos) ->
                spawnLightningBolt(player, player.world)
        ));
        addChaosEvent(ChaosMod.getId("chaos_lightning"), new ChaosEvent(0.3f, 60, 300_000, MAX_CHAOS, 30_000, "Spawn several lightning bolts that do not cause fire", (player, chaos) -> {
            int boltCount = 5 + ChaosMod.RANDOM.nextInt(6);
            return spawnChaosLightningBolts(player, player.world, boltCount);
        }));
//        addChaosEvent(ChaosMod.getId("corrupt_blocks"), new ChaosEvent(0.2f, 300, 750_000, MAX_CHAOS, 100_000, "Create a patch of corrupted blocks", (player, chaos) ->
//                corruptBlocks(player, player.world)
//        ));
//        addChaosEvent(ChaosMod.getId("corrupted_slimes"), new ChaosEvent(0.1f, 1200, 180_000, MAX_CHAOS / 3, 15_000, "Spawn a group of corrupted slimes",
//                CorruptedSlimeSpawner::spawnSlimes
//        ));
//        addChaosEvent(ChaosMod.getId("spawn_wisps"), new ChaosEvent(0.1f, 1800, 220_000, MAX_CHAOS / 4, 25_000, "Spawn a group of wisps (random element)",
//                WispSpawner::spawnWisps
//        ));
//        addChaosEvent(ChaosMod.getId("thunderstorm"), new ChaosEvent(0.05f, 2400, 1_000_000, MAX_CHAOS, 200_000, "Changes the weather to a thunderstorm", (player, chaos) -> {
//            int time = TimeUtils.ticksFromMinutes(MathUtils.nextIntInclusive(7, 15));
//            return setThunderstorm(player.world, time);
//        }));
        addChaosEvent(ChaosMod.getId("chaos_sickness"), new ChaosEvent(0.15f, 1200, 900_000, MAX_CHAOS, 200_000, "Applies negative potion effects to the player",
                ChaosEvents::applyChaosSickness
        ));
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
        if (player == null || !player.isAlive() || (ChaosConfig.Common.chaosNoEventsUntilHasBed.get() && !hasBed(player)))
            return;

        Map<ResourceLocation, Integer> cooldownTimers = COOLDOWN_TIMERS.computeIfAbsent(player.getUniqueID(), uuid -> new HashMap<>());

        //noinspection OverlyLongLambda
        EVENTS.forEach((id, event) -> {
            int cooldown = getAndDecrementTimer(id, cooldownTimers);
            if (cooldown <= 0 && checkConfig(id) && event.tryActivate(player, chaos)) {
                ChaosMod.LOGGER.info(MARKER, "Activate {} @ {}", id, player.getScoreboardName());
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

    private static boolean hasBed(PlayerEntity player) {
        return player.getBedPosition().isPresent();
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
        ChaosMod.LOGGER.debug("Triggering event {}", eventId);
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
     * @param builder Config wrapper
     */
    public static void loadConfigs(ForgeConfigSpec.Builder builder) {
        builder.comment("chaos.events",
                "Allows individual events to be disabled.",
                "Note that disabling events will likely increase the frequency of other events.");
        EVENTS.forEach((id, event) -> EVENT_CONFIGS.put(id, builder.comment(event.getConfigComment()).define("chaos.events." + id, true)));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        ++tickTimer;
        if (!LIGHTNING_QUEUE.isEmpty() && tickTimer % LIGHTNING_TICK_FREQUENCY == 0) {
            LightningBoltEntity bolt = LIGHTNING_QUEUE.remove().get();
            bolt.world.addEntity(bolt);
        }
    }

    private static boolean spawnLightningBolt(Entity entity, World world) {
        if (world instanceof ServerWorld && canSpawnLightningIn(world.getDimensionKey())) {
            double posX = entity.getPosX() + MathUtils.nextIntInclusive(-64, 64);
            double posZ = entity.getPosZ() + MathUtils.nextIntInclusive(-64, 64);
            int height = world.getHeight(Heightmap.Type.MOTION_BLOCKING, (int) posX, (int) posZ);
            LightningBoltEntity bolt = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
            bolt.moveForced(new Vector3d(posX, height, posZ));
            world.addEntity(bolt);
            return true;
        }
        return false;
    }

    private static boolean spawnChaosLightningBolts(Entity entity, World world, int count) {
        if (world instanceof ServerWorld && canSpawnLightningIn(world.getDimensionKey())) {
            for (int i = 0; i < count; ++i) {
                double posX = entity.getPosX() + MathUtils.nextIntInclusive(-64, 64);
                double posZ = entity.getPosZ() + MathUtils.nextIntInclusive(-64, 64);
                int height = world.getHeight(Heightmap.Type.MOTION_BLOCKING, (int) posX, (int) posZ);
//                LIGHTNING_QUEUE.add(() -> new ChaosLightningBoltEntity(world, posX, height, posZ)); // FIXME: re-add entity
            }
            return true;
        }
        return false;
    }

    private static boolean canSpawnLightningIn(RegistryKey<World> dimension) {
        return dimension != World.THE_NETHER && dimension != World.THE_END;
    }

    /*private static boolean corruptBlocks(Entity entity, World world) {
        int posX = (int) entity.getPosX() + MathUtils.nextIntInclusive(-128, 128);
        int posZ = (int) entity.getPosZ() + MathUtils.nextIntInclusive(-128, 128);
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
    }*/

    /*private static void makeCorruptedBlockCluster(World world, BlockPos pos, CorruptedBlocks corruptedBlock) {
        ChaosMod.LOGGER.debug(MARKER, "corrupted cluster @ {}", pos);
        BlockState newState = corruptedBlock.asBlockState();
        world.setBlockState(pos, newState, 3);
        for (int i = 0; i < 40; ++i) {
            BlockPos pos1 = new BlockPos(
                    pos.getX() + (int) (1.5 * ChaosMod.random.nextGaussian()),
                    pos.getY() + (int) (1.5 * ChaosMod.random.nextGaussian()),
                    pos.getZ() + (int) (1.5 * ChaosMod.random.nextGaussian())
            );
            BlockState target = world.getBlockState(pos1);
            if (corruptedBlock.canReplace(target.getBlock())) {
                world.setBlockState(pos1, newState, 3);
            }
        }
    }*/

    private static boolean setThunderstorm(World world, int time) {
        if (!world.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE) || world.getWorldInfo().isThundering() || !(world instanceof ServerWorld))
            return false;

        IServerWorldInfo info = ((ServerWorld) world).getServer().getServerConfiguration().getServerWorldInfo();
        //info.setClearWeatherTime(0);
        info.setRainTime(time);
        info.setThunderTime(time);
        info.setRaining(true);
        info.setThundering(true);
        return true;
    }

    private static boolean applyChaosSickness(PlayerEntity player, Integer chaos) {
        //player.addPotionEffect(new EffectInstance(GemsEffects.CHAOS_SICKNESS.get(), TimeUtils.ticksFromMinutes(10))); // FIXME: re-add effect
        player.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, TimeUtils.ticksFromMinutes(5)));
        if (chaos > 2_000_000)
            player.addPotionEffect(new EffectInstance(Effects.HUNGER, TimeUtils.ticksFromMinutes(5)));
        if (chaos > 4_000_000)
            player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, TimeUtils.ticksFromMinutes(5)));
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
