package net.silentchaos512.gemschaos.chaos;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.api.ChaosApi;
import net.silentchaos512.gemschaos.api.IChaosSource;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod.EventBusSubscriber(modid = ChaosMod.MOD_ID)
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
        if (world.getGameTime() % 20 == 0 && entity instanceof PlayerEntity) {
            // Add/subtract chaos to get closer to equilibrium point
            final int equilibrium = ChaosApi.Chaos.getEquilibriumPoint(world);
            final int rate = ChaosApi.Chaos.getDissipationRate(world);
            source.addChaos(source.getChaos() > equilibrium ? -rate : rate);

            // Try chaos events
            ChaosEvents.tryChaosEvents((PlayerEntity) entity, world, source.getChaos());
        }
    }
}
