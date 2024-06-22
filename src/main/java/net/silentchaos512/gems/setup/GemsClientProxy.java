package net.silentchaos512.gems.setup;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.item.SoulGemItem;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GemsBase.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GemsClientProxy {
    private GemsClientProxy() {}

    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(SoulGemItem::getColor, () -> GemsItems.SOUL_GEM.get());
    }
}
