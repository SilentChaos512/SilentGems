package net.silentchaos512.gems;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;
import net.silentchaos512.gems.compat.gear.SGearProxy;
import net.silentchaos512.gems.init.*;

class SideProxy {
    SideProxy() {
        FMLModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLModLoadingContext.get().getModEventBus().addListener(this::imcEnqueue);
        FMLModLoadingContext.get().getModEventBus().addListener(this::imcProcess);

        MinecraftForge.EVENT_BUS.addListener(ModBlocks::registerAll);
        MinecraftForge.EVENT_BUS.addListener(ModEntities::registerAll);
        MinecraftForge.EVENT_BUS.addListener(ModItems::registerAll);
        MinecraftForge.EVENT_BUS.addListener(ModPotions::registerAll);
        MinecraftForge.EVENT_BUS.addListener(ModSounds::registerAll);
        MinecraftForge.EVENT_BUS.addListener(ModTileEntities::registerAll);

        ModLoot.init();

        // Silent Gear support?
        // TODO: Should this be converted to use IMC? Not sure that would actually work.
        SGearProxy.detectSilentGear();
        if (SGearProxy.isLoaded()) {
            SGearMaterials.init();
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void imcEnqueue(InterModEnqueueEvent event) {
    }

    private void imcProcess(InterModProcessEvent event) {
    }

    static class Client extends SideProxy {
        Client() {
            FMLModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

//            OBJLoader.INSTANCE.addDomain(SilentGems.MOD_ID);
        }

        private void clientSetup(FMLClientSetupEvent event) {
        }
    }

    static class Server extends SideProxy {
        Server() {
            FMLModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        }

        private void serverSetup(FMLDedicatedServerSetupEvent event) {
        }
    }
}
