package net.silentchaos512.gems;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.silentchaos512.gear.api.stats.ItemStat;
import net.silentchaos512.gems.chaos.ChaosSourceCapability;
import net.silentchaos512.gems.client.GemsModelProperties;
import net.silentchaos512.gems.client.gui.DebugOverlay;
import net.silentchaos512.gems.command.ChaosCommand;
import net.silentchaos512.gems.command.HungryCommand;
import net.silentchaos512.gems.command.SoulCommand;
import net.silentchaos512.gems.compat.gear.SGearProxy;
import net.silentchaos512.gems.compat.gear.SGearStatHandler;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.data.DataGenerators;
import net.silentchaos512.gems.event.TraitEvents;
import net.silentchaos512.gems.init.*;
import net.silentchaos512.gems.item.CraftingItems;
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
import net.silentchaos512.lib.event.Greetings;
import net.silentchaos512.lib.util.LibHooks;

import javax.annotation.Nullable;

class SideProxy implements IProxy {
    private final MinecraftServer server = null;

    SideProxy() {
        Registration.register();
        GemsConfig.init();
        Network.init();

        // Detect Silent Gear and load anything needed for compatibility
        SGearProxy.detectSilentGear();
        if (SGearProxy.isLoaded()) {
            SilentGems.LOGGER.info("Register part type {}", GearSoulPart.TYPE);
            GemsTraits.registerSerializers();
            MinecraftForge.EVENT_BUS.register(SoulEvents.INSTANCE);
            MinecraftForge.EVENT_BUS.register(TraitEvents.INSTANCE);
            FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ItemStat.class, SGearStatHandler::registerStats);
        }

        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataGenerators::gatherData);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcEnqueue);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcProcess);

        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(EntityType.class, GemsEntities::registerTypes);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Feature.class, GemsWorldFeatures::registerFeatures);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Placement.class, GemsWorldFeatures::registerPlacements);
        MinecraftForge.EVENT_BUS.addListener(this::addReloadListener);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.register(Soul.Events.INSTANCE);

        Greetings.addMessage(ChaosBuffManager::getGreetingErrorMessage);

        if (SilentGems.isDevBuild()) {
            SilentGems.LOGGER.info("Silent's Gems (version {}) detected as a dev build. If this is not a development environment, this is a bug!", SilentGems.getVersion());
            Greetings.addMessage(GemsBlocks::checkForMissingLootTables);
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        ChaosSourceCapability.register();
        UpgradePlanter.init();

        LibHooks.registerCompostable(0.3f, GemsItems.FLUFFY_PUFF_SEEDS);
        LibHooks.registerCompostable(0.5f, CraftingItems.FLUFFY_PUFF);

        if (SGearProxy.isLoaded()) {
            // Register new stats
            MinecraftForge.EVENT_BUS.register(new SGearStatHandler());
        }
    }

    private void imcEnqueue(InterModEnqueueEvent event) {
    }

    private void imcProcess(InterModProcessEvent event) {
    }

    private void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(ChaosBuffManager.INSTANCE);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        ChaosCommand.register(dispatcher);
        SoulCommand.register(dispatcher);
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
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

            FMLJavaModLoadingContext.get().getModEventBus().addListener(ColorHandlers::onBlockColors);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ColorHandlers::onItemColors);
            MinecraftForge.EVENT_BUS.addListener(TeleporterLinkerItem::renderGameOverlay);

            if (SGearProxy.isLoaded()) {
                MinecraftForge.EVENT_BUS.register(SoulEvents.Client.INSTANCE);
            }

            MinecraftForge.EVENT_BUS.register(new DebugOverlay());

            if (AprilFools.isRightDay()) {
                SilentGems.LOGGER.info("Registered April Fools Day events");
                MinecraftForge.EVENT_BUS.register(AprilFools.INSTANCE);
            }
        }

        private void clientSetup(FMLClientSetupEvent event) {
            GemsBlocks.registerRenderTypes(event);
            GemsEntities.registerRenderers(event);
            GemsTileEntities.registerRenderers(event);
            GemsContainers.registerScreens(event);
            GemsModelProperties.register(event);
        }

        @Nullable
        @Override
        public PlayerEntity getClientPlayer() {
            return Minecraft.getInstance().player;
        }
    }

    static class Server extends SideProxy {
        Server() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        }

        private void serverSetup(FMLDedicatedServerSetupEvent event) {
        }
    }
}
