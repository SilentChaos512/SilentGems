package net.silentchaos512.gems.setup;

import net.minecraft.item.Item;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.registry.ItemRegistryObject;

import java.util.function.Supplier;

public class GemsItems {
    static {
        Gems.registerItems();
    }

    public static final ItemRegistryObject<GemBagItem> GEM_BAG = register("gem_bag", () ->
            new GemBagItem(unstackableProps()));
    public static final ItemRegistryObject<FlowerBasketItem> FLOWER_BASKET = register("flower_basket", () ->
            new FlowerBasketItem(unstackableProps()));

    private GemsItems() {}

    static void register() {}

    private static <T extends Item> ItemRegistryObject<T> register(String name, Supplier<T> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }
    private static Item.Properties baseProps() {
        return new Item.Properties().group(GemsBase.ITEM_GROUP);
    }

    private static Item.Properties unstackableProps() {
        return baseProps().maxStackSize(1);
    }
}
