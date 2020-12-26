package net.silentchaos512.gems.setup;

import net.minecraft.item.Item;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.registry.ItemRegistryObject;

import java.util.function.Supplier;

public class GemsItems {
    static {
        Gems.registerItems();
    }

    private GemsItems() {}

    static void register() {}

    private static <T extends Item> ItemRegistryObject<T> register(String name, Supplier<T> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }
}
