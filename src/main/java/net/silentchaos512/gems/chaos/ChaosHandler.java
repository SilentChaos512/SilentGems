package net.silentchaos512.gems.chaos;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.chaos.IChaosSource;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod.EventBusSubscriber(modid = SilentGems.MOD_ID)
public final class ChaosHandler {
    private static final Marker MARKER = MarkerManager.getMarker("ChaosHandler");

    private ChaosHandler() {}

    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (ChaosSourceCapability.canAttachTo(entity)) {
            event.addCapability(ChaosSourceCapability.NAME, new ChaosSourceCapability());
        }
    }

    @SubscribeEvent
    public static void onAttachWorldCapabilities(AttachCapabilitiesEvent<World> event) {
        World world = event.getObject();
        if (ChaosSourceCapability.canAttachTo(world)) {
            event.addCapability(ChaosSourceCapability.NAME, new ChaosSourceCapability());
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.world;
        if (world.isRemote) return;

        if (world.getGameTime() % 20 == 0) {
            entity.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source ->
                    entitySourceTick(entity, world, source));
        }
    }

    private static void entitySourceTick(Entity entity, World world, IChaosSource source) {
        // Transfer chaos to world
        world.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(worldSource -> {
            int amount = Math.min(source.getChaos(), Chaos.ENTITY_TO_WORLD_RATE);
            worldSource.addChaos(amount);
            source.addChaos(-amount);
        });

        // Try chaos events
        if (world.getGameTime() % 20 == 0 && entity instanceof PlayerEntity) {
            final int chaos = getEffectiveChaosForEvents(source, world);
            ChaosEvents.tryChaosEvents((PlayerEntity) entity, world, chaos);
        }
    }

    private static int getEffectiveChaosForEvents(IChaosSource player, World world) {
        IChaosSource worldSource = world.getCapability(ChaosSourceCapability.INSTANCE).orElseThrow(IllegalStateException::new);
        int equilibriumPoint = Chaos.getEquilibriumPoint(world);
        return getEffectiveChaosForEvents(player.getChaos(), worldSource.getChaos(), equilibriumPoint);
    }

    public static int getEffectiveChaosForEvents(int playerChaos, int worldChaos, int equilibrium) {
        // If player's chaos is low and world is high, do not subject the player to all the world's
        // chaos. Just base them on the equilibrium point instead.
        if (playerChaos < 50_000 && worldChaos > 2 * equilibrium)
            return playerChaos + equilibrium;
        // Player gets all the chaos!
        return playerChaos + worldChaos;
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (world.isRemote) return;

        if (world.getGameTime() % 20 == 0) {
            final int equilibrium = Chaos.getEquilibriumPoint(world);
            final int rate = Chaos.getDissipationRate(world);

            // Add/subtract chaos to get closer to equilibrium point
            world.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source -> {
                source.addChaos(source.getChaos() > equilibrium ? -rate : rate);
            });
        }
    }
}
