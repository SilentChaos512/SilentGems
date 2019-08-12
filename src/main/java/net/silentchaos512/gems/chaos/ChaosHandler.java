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
    // TODO: Add configs!
    private static final int ENTITY_TO_WORLD_RATE = 200;
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
            int amount = Math.min(source.getChaos(), ENTITY_TO_WORLD_RATE);
            worldSource.addChaos(amount);
            source.addChaos(-amount);
        });

        // Try chaos events
        if (world.getGameTime() % 20 == 0 && entity instanceof PlayerEntity) {
            ChaosEvents.tryChaosEvents((PlayerEntity) entity, world, source.getChaos());
        }
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
