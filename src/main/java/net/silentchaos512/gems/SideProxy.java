package net.silentchaos512.gems;

import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;
import net.silentchaos512.gems.compat.gear.SGearProxy;
import net.silentchaos512.gems.init.*;
import net.silentchaos512.gems.util.RecipeGen;

class SideProxy {
    SideProxy() {
        SilentGems.LOGGER.info("Gems SideProxy init");

        FMLModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLModLoadingContext.get().getModEventBus().addListener(this::imcEnqueue);
        FMLModLoadingContext.get().getModEventBus().addListener(this::imcProcess);

        FMLModLoadingContext.get().getModEventBus().addListener(ModBlocks::registerAll);
        FMLModLoadingContext.get().getModEventBus().addListener(ModEnchantments::registerAll);
        FMLModLoadingContext.get().getModEventBus().addListener(ModEntities::registerAll);
        FMLModLoadingContext.get().getModEventBus().addListener(ModItems::registerAll);
        FMLModLoadingContext.get().getModEventBus().addListener(ModPotions::registerAll);
        FMLModLoadingContext.get().getModEventBus().addListener(ModSounds::registerAll);
        FMLModLoadingContext.get().getModEventBus().addListener(ModTileEntities::registerAll);

        ModLoot.init();
        ModRecipes.init();

        // Silent Gear support?
        // TODO: Should this be converted to use IMC? Not sure that would actually work.
        SGearProxy.detectSilentGear();
        if (SGearProxy.isLoaded()) {
            SGearMaterials.init();
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        SilentGems.LOGGER.info("Gems commonSetup");

        RecipeGen.generateRecipes();
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
            FMLModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

//            OBJLoader.INSTANCE.addDomain(SilentGems.MOD_ID);
        }

        private void clientSetup(FMLClientSetupEvent event) {
            SilentGems.LOGGER.info("Gems clientSetup");
        }
    }

    static class Server extends SideProxy {
        Server() {
            SilentGems.LOGGER.info("Gems SideProxy.Server init");
            FMLModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        }

        private void serverSetup(FMLDedicatedServerSetupEvent event) {
            SilentGems.LOGGER.info("Gems serverSetup");
        }
    }
}
