package net.silentchaos512.gems.setup;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fmllegacy.network.IContainerFactory;
import net.silentchaos512.gems.item.container.FlowerBasketItem;
import net.silentchaos512.gems.item.container.GemBagItem;
import net.silentchaos512.gems.item.container.GemContainer;
import net.silentchaos512.gems.item.container.GemContainerScreen;

public class GemsContainers {
    public static final RegistryObject<MenuType<GemContainer>> GEM_BAG = register("gem_bag", (windowId, inv, data) ->
            new GemContainer(windowId, inv, GemsContainers.GEM_BAG.get(), GemBagItem.class));
    public static final RegistryObject<MenuType<GemContainer>> FLOWER_BASKET = register("flower_basket", (windowId, inv, data) ->
            new GemContainer(windowId, inv, GemsContainers.FLOWER_BASKET.get(), FlowerBasketItem.class));

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        MenuScreens.register(GEM_BAG.get(), GemContainerScreen::new);
        MenuScreens.register(FLOWER_BASKET.get(), GemContainerScreen::new);
    }

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return Registration.CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
    }
}
