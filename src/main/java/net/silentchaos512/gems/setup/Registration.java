package net.silentchaos512.gems.setup;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.gems.SilentGems;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Registration {
    private Registration() {}

    public static void register(IEventBus modEventBus) {
        GemsBlocks.BLOCKS.register(modEventBus);
        GemsContainers.CONTAINERS.register(modEventBus);
        GemsCreativeTabs.CREATIVE_TABS.register(modEventBus);
        GemsDataComponents.REGISTRAR.register(modEventBus);
        GemsItems.ITEMS.register(modEventBus);

        if (ModList.get().isLoaded("silentgear")) {
//            GemsTraits.registerSerializers();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Block> Collection<T> getBlocks(Class<T> clazz) {
        return GemsBlocks.BLOCKS.getEntries().stream()
                .map(DeferredHolder::get)
                .filter(clazz::isInstance)
                .map(b -> (T) b)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Item> Collection<T> getItems(Class<T> clazz) {
        return GemsItems.ITEMS.getEntries().stream()
                .map(DeferredHolder::get)
                .filter(clazz::isInstance)
                .map(i -> (T) i)
                .collect(Collectors.toList());
    }

    public static Collection<Item> getItems(Predicate<Item> predicate) {
        return GemsItems.ITEMS.getEntries().stream()
                .map(DeferredHolder::get)
                .filter(predicate)
                .collect(Collectors.toList());
    }

    static <T> DeferredRegister<T> create(Registry<T> registry) {
        return DeferredRegister.create(registry, SilentGems.MOD_ID);
    }
}
