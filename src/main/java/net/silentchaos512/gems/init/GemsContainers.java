package net.silentchaos512.gems.init;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.IContainerFactory;
import net.silentchaos512.gems.block.altar.AltarContainer;
import net.silentchaos512.gems.block.altar.AltarScreen;
import net.silentchaos512.gems.block.purifier.PurifierContainer;
import net.silentchaos512.gems.block.purifier.PurifierScreen;
import net.silentchaos512.gems.block.supercharger.SuperchargerContainer;
import net.silentchaos512.gems.block.supercharger.SuperchargerScreen;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterContainer;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterScreen;
import net.silentchaos512.gems.block.urn.SoulUrnContainer;
import net.silentchaos512.gems.block.urn.SoulUrnScreen;
import net.silentchaos512.gems.item.container.GemBagContainer;
import net.silentchaos512.gems.item.container.GemContainerScreen;
import net.silentchaos512.gems.item.container.GlowroseBasketContainer;

public final class GemsContainers {
    public static final RegistryObject<ContainerType<GemBagContainer>> GEM_BAG = register("gem_bag", GemBagContainer::new);
    public static final RegistryObject<ContainerType<GlowroseBasketContainer>> GLOWROSE_BASKET = register("glowrose_basket", GlowroseBasketContainer::new);
    public static final RegistryObject<ContainerType<PurifierContainer>> PURIFIER = register("purifier", PurifierContainer::new);
    public static final RegistryObject<ContainerType<SoulUrnContainer>> SOUL_URN = register("soul_urn", SoulUrnContainer::new);
    public static final RegistryObject<ContainerType<SuperchargerContainer>> SUPERCHARGER = register("supercharger", SuperchargerContainer::new);
    public static final RegistryObject<ContainerType<TokenEnchanterContainer>> TOKEN_ENCHANTER = register("token_enchanter", TokenEnchanterContainer::new);
    public static final RegistryObject<ContainerType<AltarContainer>> TRANSMUTATION_ALTAR = register("transmutation_altar", AltarContainer::new);

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(GEM_BAG.get(), GemContainerScreen::new);
        ScreenManager.registerFactory(GLOWROSE_BASKET.get(), GemContainerScreen::new);
        ScreenManager.registerFactory(PURIFIER.get(), PurifierScreen::new);
        ScreenManager.registerFactory(SOUL_URN.get(), SoulUrnScreen::new);
        ScreenManager.registerFactory(SUPERCHARGER.get(), SuperchargerScreen::new);
        ScreenManager.registerFactory(TOKEN_ENCHANTER.get(), TokenEnchanterScreen::new);
        ScreenManager.registerFactory(TRANSMUTATION_ALTAR.get(), AltarScreen::new);
    }

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String name, IContainerFactory<T> factory) {
        return Registration.CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
    }
}
