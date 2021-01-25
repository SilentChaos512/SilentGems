package net.silentchaos512.gems.setup;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class GemsClientProxy {
    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GemsClientProxy::clientSetup);
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        GemsBlocks.registerRenderTypes(event);
//        GemsEntities.registerRenderers(event);
//        GemsTileEntities.registerRenderers(event);
        GemsContainers.registerScreens(event);
//        GemsModelProperties.register(event);
    }
}
