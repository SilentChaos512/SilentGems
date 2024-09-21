package net.silentchaos512.gems.setup;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.silentchaos512.gems.SilentGems;

@EventBusSubscriber(value = Dist.CLIENT, modid = SilentGems.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class GemsClientProxy {
    private GemsClientProxy() {}

    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
    }
}
