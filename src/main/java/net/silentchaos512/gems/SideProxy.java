package net.silentchaos512.gems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.silentchaos512.gems.client.gui.GuiTypes;
import net.silentchaos512.gems.compat.gear.SGearProxy;
import net.silentchaos512.gems.init.*;
import net.silentchaos512.gems.lib.ColorHandlers;
import net.silentchaos512.gems.util.gen.GenModels;
import net.silentchaos512.gems.util.gen.GenRecipes;
import net.silentchaos512.gems.world.GemsWorldFeatures;
import net.silentchaos512.lib.inventory.ContainerType;

import java.util.function.BiFunction;

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

        registerContainersCommon();
    }

    private static IEventBus getLifeCycleEventBus() {
        return FMLJavaModLoadingContext.get().getModEventBus();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        SilentGems.LOGGER.info("Gems commonSetup");

        DeferredWorkQueue.runLater(GemsWorldFeatures::addFeaturesToBiomes);

        if (SilentGems.RUN_GENERATORS) {
            GenModels.generateModels();
            GenRecipes.generateRecipes();
        }
    }

    private void imcEnqueue(InterModEnqueueEvent event) {
        SilentGems.LOGGER.info("Gems imcEnqueue");
    }

    private void imcProcess(InterModProcessEvent event) {
        SilentGems.LOGGER.info("Gems imcProcess");
    }

    private static void registerContainersCommon() {
        for (GuiTypes type : GuiTypes.values()) {
            //noinspection Convert2MethodRef -- compiler error
            ContainerType.register(type::getContainerType, (tileType, player) ->
                    type.getContainer(tileType, player));
        }
    }

    static class Client extends SideProxy {
        Client() {
            SilentGems.LOGGER.info("Gems SideProxy.Client init");
            SideProxy.getLifeCycleEventBus().addListener(this::clientSetup);

            MinecraftForge.EVENT_BUS.addListener(ColorHandlers::onBlockColors);
            MinecraftForge.EVENT_BUS.addListener(ColorHandlers::onItemColors);

//            OBJLoader.INSTANCE.addDomain(SilentGems.MOD_ID);

            registerContainers();
        }

        private void clientSetup(FMLClientSetupEvent event) {
            SilentGems.LOGGER.info("Gems clientSetup");
        }

        private static void registerContainers() {
            for (GuiTypes type : GuiTypes.values()) {
                //noinspection Convert2MethodRef -- compiler error
                ContainerType.registerGui(type::getContainerType, (tileType, player) ->
                        type.getGui(tileType, player));
            }

            ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> packet -> {
                ContainerType<?> type = ContainerType.factories.get(packet.getId()).get();
                if (packet.getAdditionalData() != null) type.fromBytes(packet.getAdditionalData());
                //noinspection unchecked
                return ((BiFunction<ContainerType<?>, EntityPlayer, GuiContainer>) ContainerType.guiFactories.get(packet.getId()))
                        .apply(type, Minecraft.getInstance().player);
            });
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
