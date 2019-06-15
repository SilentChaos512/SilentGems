package net.silentchaos512.gems.init;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.altar.AltarContainer;
import net.silentchaos512.gems.block.supercharger.SuperchargerContainer;
import net.silentchaos512.gems.block.supercharger.SuperchargerScreen;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterContainer;
import net.silentchaos512.gems.block.urn.SoulUrnContainer;
import net.silentchaos512.utils.Lazy;

import java.util.Locale;

public enum GemsContainers {
    SOUL_URN(SoulUrnContainer::new),
    SUPERCHARGER(SuperchargerContainer::new),
    TOKEN_ENCHANTER(TokenEnchanterContainer::new),
    TRANSMUTATION_ALTAR(AltarContainer::new);

    private final Lazy<ContainerType<?>> type;

    GemsContainers(ContainerType.IFactory<?> factory) {
        this.type = Lazy.of(() -> new ContainerType<>(factory));
    }

    public ContainerType<?> type() {
        return type.get();
    }
    public static void registerAll(RegistryEvent.Register<ContainerType<?>> event) {
        if (!event.getName().equals(ForgeRegistries.CONTAINERS.getRegistryName())) return;

        for (GemsContainers container : values()) {
            register(container.name().toLowerCase(Locale.ROOT), container.type());
        }
    }

    @SuppressWarnings("unchecked")
    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        ScreenManager.registerFactory((ContainerType<? extends SuperchargerContainer>) SUPERCHARGER.type(), SuperchargerScreen::new);
    }

    private static void register(String name, ContainerType<?> type) {
        ResourceLocation id = SilentGems.getId(name);
        type.setRegistryName(id);
        ForgeRegistries.CONTAINERS.register(type);
    }
}
