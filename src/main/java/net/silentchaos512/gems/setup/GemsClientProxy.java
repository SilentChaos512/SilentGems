package net.silentchaos512.gems.setup;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.item.SoulGemItem;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GemsBase.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GemsClientProxy {
    private GemsClientProxy() {}

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        GemsBlocks.registerRenderTypes(event);
        GemsContainers.registerScreens(event);
    }

    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(SoulGemItem::getColor, GemsItems.SOUL_GEM);
    }
}
