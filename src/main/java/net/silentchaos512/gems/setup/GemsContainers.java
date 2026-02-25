package net.silentchaos512.gems.setup;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GemsContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = Registration.create(BuiltInRegistries.MENU);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return CONTAINERS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    @EventBusSubscriber(value = Dist.CLIENT)
    public static class Events {
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
        }
    }
}
