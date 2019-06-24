package net.silentchaos512.gems;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.silentchaos512.gems.chaos.ChaosSourceCapability;
import net.silentchaos512.gems.client.gui.DebugOverlay;
import net.silentchaos512.gems.command.ChaosCommand;
import net.silentchaos512.gems.command.HungryCommand;
import net.silentchaos512.gems.compat.gear.SGearProxy;
import net.silentchaos512.gems.compat.gear.SGearStatHandler;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.crafting.altar.AltarRecipeManager;
import net.silentchaos512.gems.event.TraitEvents;
import net.silentchaos512.gems.init.*;
import net.silentchaos512.gems.item.TeleporterLinkerItem;
import net.silentchaos512.gems.lib.ColorHandlers;
import net.silentchaos512.gems.lib.chaosbuff.ChaosBuffManager;
import net.silentchaos512.gems.lib.fun.AprilFools;
import net.silentchaos512.gems.lib.soul.GearSoulPart;
import net.silentchaos512.gems.lib.soul.Soul;
import net.silentchaos512.gems.lib.urn.UpgradePlanter;
import net.silentchaos512.gems.network.Network;
import net.silentchaos512.gems.util.SoulEvents;
import net.silentchaos512.gems.world.GemsWorldFeatures;

import javax.annotation.Nullable;

class SideProxy implements IProxy {
    private MinecraftServer server = null;

    SideProxy() {
        SilentGems.LOGGER.debug("Gems SideProxy init");

        // Detect Silent Gear and load anything needed for compatibility
        SGearProxy.detectSilentGear();
        if (SGearProxy.isLoaded()) {
            SilentGems.LOGGER.info("Register part type {}", GearSoulPart.TYPE);
            GemsTraits.registerSerializers();
            MinecraftForge.EVENT_BUS.register(SoulEvents.INSTANCE);
            MinecraftForge.EVENT_BUS.register(TraitEvents.INSTANCE);
        }

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcEnqueue);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcProcess);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(GemsBlocks::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GemsContainers::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GemsEnchantments::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GemsEntities::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GemsItems::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GemsEffects::registerEffects);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GemsEffects::registerPotions);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GemsSounds::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GemsTileEntities::registerAll);

        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
        MinecraftForge.EVENT_BUS.register(Soul.Events.INSTANCE);

        GemsLoot.init();
        GemsRecipeInit.init();

        GemsConfig.init();
        Network.init();

        if (SilentGems.isDevBuild()) {
            SilentGems.LOGGER.info("Silent's Gems (version {}) detected as a dev build. If this is not a development environment, this is a bug!", SilentGems.getVersion());
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        SilentGems.LOGGER.debug("Gems commonSetup");

        ChaosSourceCapability.register();
        UpgradePlanter.init();

        if (SGearProxy.isLoaded()) {
            // Register new stats
            MinecraftForge.EVENT_BUS.register(new SGearStatHandler());
        }

        DeferredWorkQueue.runLater(GemsWorldFeatures::addFeaturesToBiomes);
    }

    private void imcEnqueue(InterModEnqueueEvent event) {
        SilentGems.LOGGER.debug("Gems imcEnqueue");
    }

    private void imcProcess(InterModProcessEvent event) {
        SilentGems.LOGGER.debug("Gems imcProcess");
    }

    private void serverAboutToStart(FMLServerAboutToStartEvent event) {
        server = event.getServer();

        IReloadableResourceManager resourceManager = event.getServer().getResourceManager();
        resourceManager.addReloadListener(ChaosBuffManager.INSTANCE);
        resourceManager.addReloadListener(AltarRecipeManager.INSTANCE);

        CommandDispatcher<CommandSource> dispatcher = event.getServer().getCommandManager().getDispatcher();
        ChaosCommand.register(dispatcher);
        if (SilentGems.isDevBuild()) {
            HungryCommand.register(dispatcher);
        }
    }

    @Override
    public MinecraftServer getServer() {
        return server;
    }

    static class Client extends SideProxy {
        Client() {
            SilentGems.LOGGER.debug("Gems SideProxy.Client init");
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

            FMLJavaModLoadingContext.get().getModEventBus().addListener(ColorHandlers::onBlockColors);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ColorHandlers::onItemColors);
            MinecraftForge.EVENT_BUS.addListener(TeleporterLinkerItem::renderGameOverlay);

            if (SGearProxy.isLoaded()) {
                MinecraftForge.EVENT_BUS.register(SoulEvents.Client.INSTANCE);
            }

            if (SilentGems.isDevBuild()) {
                MinecraftForge.EVENT_BUS.register(new DebugOverlay());
            }

            if (AprilFools.isRightDay()) {
                SilentGems.LOGGER.info("Registered April Fools Day events");
                MinecraftForge.EVENT_BUS.register(AprilFools.INSTANCE);
            }
        }

        private void clientSetup(FMLClientSetupEvent event) {
            SilentGems.LOGGER.debug("Gems clientSetup");
            GemsEntities.registerRenderers(event);
            GemsTileEntities.registerRenderers(event);
            GemsContainers.registerScreens(event);
        }

        @Nullable
        @Override
        public PlayerEntity getClientPlayer() {
            return Minecraft.getInstance().player;
        }
    }

    static class Server extends SideProxy {
        Server() {
            SilentGems.LOGGER.debug("Gems SideProxy.Server init");
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        }

        private void serverSetup(FMLDedicatedServerSetupEvent event) {
            SilentGems.LOGGER.debug("Gems serverSetup");
        }
    }
}
