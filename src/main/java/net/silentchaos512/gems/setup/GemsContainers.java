package net.silentchaos512.gems.setup;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.item.container.FlowerBasketItem;
import net.silentchaos512.gems.item.container.GemBagItem;
import net.silentchaos512.gems.item.container.GemContainer;
import net.silentchaos512.gems.item.container.GemContainerScreen;

public class GemsContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = Registration.create(BuiltInRegistries.MENU);

    public static final DeferredHolder<MenuType<?>, MenuType<GemContainer>> GEM_BAG = register("gem_bag", (windowId, inv, data) ->
            new GemContainer(windowId, inv, GemsContainers.GEM_BAG.get(), GemBagItem.class));
    public static final DeferredHolder<MenuType<?>, MenuType<GemContainer>> FLOWER_BASKET = register("flower_basket", (windowId, inv, data) ->
            new GemContainer(windowId, inv, GemsContainers.FLOWER_BASKET.get(), FlowerBasketItem.class));

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return CONTAINERS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GemsBase.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Events {
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(GEM_BAG.get(), GemContainerScreen::new);
            event.register(FLOWER_BASKET.get(), GemContainerScreen::new);
        }
    }
}
