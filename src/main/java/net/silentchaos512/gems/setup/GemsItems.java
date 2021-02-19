package net.silentchaos512.gems.setup;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.IItemProvider;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.item.GemsFoodItem;
import net.silentchaos512.gems.item.PetSummonerItem;
import net.silentchaos512.gems.item.SoulGemItem;
import net.silentchaos512.gems.item.container.FlowerBasketItem;
import net.silentchaos512.gems.item.container.GemBagItem;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.registry.ItemRegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public final class GemsItems {
    private static final Collection<ItemRegistryObject<? extends Item>> SIMPLE_MODEL_ITEMS = new ArrayList<>();

    static {
        Gems.registerItems();
    }

    public static final ItemRegistryObject<Item> SILVER_INGOT = registerCraftingItem("silver_ingot");
    public static final ItemRegistryObject<Item> SILVER_NUGGET = registerCraftingItem("silver_nugget");
    public static final ItemRegistryObject<Item> FLUFFY_PUFF = registerCraftingItem("fluffy_puff");
    public static final ItemRegistryObject<Item> FLUFFY_FABRIC = registerCraftingItem("fluffy_fabric");
    // fluffy puff seeds here
    public static final ItemRegistryObject<Item> LOLINOMICON = registerCraftingItem("lolinomicon");

    public static final ItemRegistryObject<SoulGemItem> SOUL_GEM = register("soul_gem", () ->
            new SoulGemItem(unstackableProps()));

    public static final ItemRegistryObject<GemBagItem> GEM_BAG = register("gem_bag", () ->
            new GemBagItem(unstackableProps()));
    public static final ItemRegistryObject<FlowerBasketItem> FLOWER_BASKET = register("flower_basket", () ->
            new FlowerBasketItem(unstackableProps()));

    public static final ItemRegistryObject<PetSummonerItem> SUMMON_KITTY = registerSimpleModel("summon_kitty", () ->
            new PetSummonerItem(PetSummonerItem::getCat, baseProps()));
    public static final ItemRegistryObject<PetSummonerItem> SUMMON_PUPPY = registerSimpleModel("summon_puppy", () ->
            new PetSummonerItem(PetSummonerItem::getDog, baseProps()));

    public static final ItemRegistryObject<Item> POTATO_ON_A_STICK = registerFood("potato_on_a_stick", Items.STICK, new Food.Builder()
            .hunger(6)
            .saturation(0.7f));
    public static final ItemRegistryObject<Item> SUGAR_COOKIE = registerFood("sugar_cookie", null, new Food.Builder()
            .hunger(2)
            .saturation(0.4f)
            .setAlwaysEdible()
            .effect(() -> new EffectInstance(Effects.SPEED, 600), 1f)
            .effect(() -> new EffectInstance(Effects.HASTE, 400), 1f));
    public static final ItemRegistryObject<Item> UNCOOKED_MEATY_STEW = registerFood("uncooked_meaty_stew", Items.BOWL, new Food.Builder()
            .hunger(4)
            .saturation(0.6f));
    public static final ItemRegistryObject<Item> MEATY_STEW = registerFood("meaty_stew", Items.BOWL, new Food.Builder()
            .hunger(12)
            .saturation(1.6f));
    public static final ItemRegistryObject<Item> UNCOOKED_FISHY_STEW = registerFood("uncooked_fishy_stew", Items.BOWL, new Food.Builder()
            .hunger(4)
            .saturation(0.5f));
    public static final ItemRegistryObject<Item> FISHY_STEW = registerFood("fishy_stew", Items.BOWL, new Food.Builder()
            .hunger(10)
            .saturation(1.2f));
    public static final ItemRegistryObject<Item> IRON_POTATO = registerFood("iron_potato", null, new Food.Builder()
            .hunger(9)
            .saturation(0.9f)
            .setAlwaysEdible()
            .effect(() -> new EffectInstance(Effects.ABSORPTION, 10 * 60 * 20, 4), 1f)
            .effect(() -> new EffectInstance(Effects.RESISTANCE, 5 * 60 * 20, 0), 1f)
            .effect(() -> new EffectInstance(Effects.STRENGTH, 5 * 60 * 20, 1), 1f)
    );

    private GemsItems() {}

    static void register() {}

    public static Collection<ItemRegistryObject<? extends Item>> getSimpleModelItems() {
        return Collections.unmodifiableCollection(SIMPLE_MODEL_ITEMS);
    }

    private static <T extends Item> ItemRegistryObject<T> register(String name, Supplier<T> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }

    private static <T extends Item> ItemRegistryObject<T> registerSimpleModel(String name, Supplier<T> item) {
        ItemRegistryObject<T> ret = register(name, item);
        SIMPLE_MODEL_ITEMS.add(ret);
        return ret;
    }

    private static ItemRegistryObject<Item> registerCraftingItem(String name) {
        // Registers a generic, basic item with no special properties. Useful for items that are
        // used primarily for crafting recipes.
        return registerSimpleModel(name, () -> new Item(baseProps()));
    }

    private static ItemRegistryObject<Item> registerFood(String name, @Nullable IItemProvider returnItem, Food.Builder foodBuilder) {
        ItemRegistryObject<Item> ret = register(name, () -> new GemsFoodItem(foodBuilder, returnItem, baseProps()));
        SIMPLE_MODEL_ITEMS.add(ret);
        return ret;
    }

    private static Item.Properties baseProps() {
        return new Item.Properties().group(GemsBase.ITEM_GROUP);
    }

    private static Item.Properties unstackableProps() {
        return baseProps().maxStackSize(1);
    }
}
