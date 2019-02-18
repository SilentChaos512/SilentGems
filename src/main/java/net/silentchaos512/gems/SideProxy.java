package net.silentchaos512.gems;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.silentchaos512.gems.compat.gear.SGearProxy;
import net.silentchaos512.gems.init.*;
import net.silentchaos512.gems.lib.ColorHandlers;
import net.silentchaos512.gems.util.ModelGen;
import net.silentchaos512.gems.util.RecipeGen;
import net.silentchaos512.gems.world.GemsWorldFeatures;
import net.silentchaos512.lib.util.GameUtil;

class SideProxy {
    SideProxy() {
        SilentGems.LOGGER.info("Gems SideProxy init");

        getLifeCycleEventBus().addListener(this::commonSetup);
        getLifeCycleEventBus().addListener(this::imcEnqueue);
        getLifeCycleEventBus().addListener(this::imcProcess);

        getLifeCycleEventBus().addListener(ModBlocks::registerAll);
        getLifeCycleEventBus().addListener(ModEnchantments::registerAll);
        getLifeCycleEventBus().addListener(ModEntities::registerAll);
        getLifeCycleEventBus().addListener(ModItems::registerAll);
        getLifeCycleEventBus().addListener(ModPotions::registerAll);
        getLifeCycleEventBus().addListener(ModSounds::registerAll);
        getLifeCycleEventBus().addListener(ModTileEntities::registerAll);

        ModLoot.init();
        ModRecipes.init();

        // Silent Gear support?
        // TODO: Should this be converted to use IMC? Not sure that would actually work.
        SGearProxy.detectSilentGear();
        if (SGearProxy.isLoaded()) {
            SGearMaterials.init();
        }
    }

    private static IEventBus getLifeCycleEventBus() {
        return FMLJavaModLoadingContext.get().getModEventBus();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        SilentGems.LOGGER.info("Gems commonSetup");

        DeferredWorkQueue.runLater(GemsWorldFeatures::addFeaturesToBiomes);

        if (GameUtil.isDeobfuscated()) {
            ModelGen.generateModels();
            RecipeGen.generateRecipes();
        }
    }

    private void imcEnqueue(InterModEnqueueEvent event) {
        SilentGems.LOGGER.info("Gems imcEnqueue");
    }

    private void imcProcess(InterModProcessEvent event) {
        SilentGems.LOGGER.info("Gems imcProcess");
    }

    static class Client extends SideProxy {
        Client() {
            SilentGems.LOGGER.info("Gems SideProxy.Client init");
            SideProxy.getLifeCycleEventBus().addListener(this::clientSetup);

            MinecraftForge.EVENT_BUS.addListener(ColorHandlers::onBlockColors);
            MinecraftForge.EVENT_BUS.addListener(ColorHandlers::onItemColors);

//            OBJLoader.INSTANCE.addDomain(SilentGems.MOD_ID);
        }

        private void clientSetup(FMLClientSetupEvent event) {
            SilentGems.LOGGER.info("Gems clientSetup");
        }
    }

    static class Server extends SideProxy {
        Server() {
            SilentGems.LOGGER.info("Gems SideProxy.Server init");
            SideProxy.getLifeCycleEventBus().addListener(this::serverSetup);
        }

        private void serverSetup(FMLDedicatedServerSetupEvent event) {
            SilentGems.LOGGER.info("Gems serverSetup");
        }
    }
}
