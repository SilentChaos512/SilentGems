package net.silentchaos512.gems.setup;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.IContainerFactory;
import net.silentchaos512.gems.item.container.*;

public class GemsContainers {
    public static final RegistryObject<ContainerType<GemContainer>> GEM_BAG = register("gem_bag", (windowId, inv, data) ->
            new GemContainer(windowId, inv, GemsContainers.GEM_BAG.get(), GemBagItem.class));
    public static final RegistryObject<ContainerType<GemContainer>> FLOWER_BASKET = register("flower_basket", (windowId, inv, data) ->
            new GemContainer(windowId, inv, GemsContainers.FLOWER_BASKET.get(), FlowerBasketItem.class));

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        ScreenManager.register(GEM_BAG.get(), GemContainerScreen::new);
        ScreenManager.register(FLOWER_BASKET.get(), GemContainerScreen::new);
    }

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String name, IContainerFactory<T> factory) {
        return Registration.CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
    }
}
