package net.silentchaos512.gemschaos.setup;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.silentchaos512.gemschaos.ChaosMod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ChaosMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ChaosClientProxy {
    private ChaosClientProxy() {}

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ChaosBlocks.registerRenderTypes(event);
//        ChaosContainers.registerScreens(event);
    }
}
